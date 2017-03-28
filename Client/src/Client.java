import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import event.GameEvent;
import event.GameEventListener;
import event.events.ChallengeCancelledEvent;
import event.events.ChallengeReceiveEvent;
import event.events.MatchFinishEvent;
import event.events.MatchStartEvent;
import event.events.MoveEvent;
import event.events.YourTurnEvent;
import utils.StringUtils;

/**
 * Created by samikroon on 3/27/17.
 */
public class Client implements GameClient {
    private Socket socket;
    private BufferedWriter bw;
    
    private CommandType ct;
    private boolean success = false;
    private String[] gameList;
    private String[] playerList;

    private LinkedList<GameEventListener> registeredListeners;
    
    public Client () {
    	registeredListeners = new LinkedList<>();
    }

    public void guardedLock() {
        while (ct != null) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public boolean connect(InetAddress inetAddress, int port) {
        try {
            this.socket = new Socket(inetAddress, port);
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean login(String username) {
        if (ct == null) {
            try {
                sendCommand("login " + username);
                ct = CommandType.LOGIN;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void logout() {
        if (ct == null) {
            try {
                sendCommand("bye");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getGameList() {
        if (ct == null) {
            try {
                sendCommand("get gamelist");
                ct = CommandType.GETGAMELIST;
                guardedLock();
                return gameList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String[] getPlayers() {
        if (ct == null) {
            try {
                sendCommand("get playerlist");
                ct = CommandType.GETPLAYERS;
                guardedLock();
                return playerList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean subscribe(String game) {
        if (ct == null) {
            try {
                sendCommand("subscribe " + game);
                ct = CommandType.SUBSCRIBE;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean move(String move) {
        if (ct == null) {
            try {
                sendCommand("move " + move);
                ct = CommandType.MOVE;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean forfeit() {
        if (ct == null) {
            try {
                sendCommand("forfeit");
                ct = CommandType.FORFEIT;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean challenge(String player, String game) {
        if (ct == null) {
            try {
                sendCommand("challenge \"" + player + "\" \"" + game + "\"");
                ct = CommandType.CHALLENGE;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean acceptChallenge(int challengeNum) {
        if (ct == null) {
            try {
                sendCommand("challenge accept " + challengeNum);
                ct = CommandType.ACCEPTCHALLENGE;
                guardedLock();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void registerEventListener(GameEventListener gameEventListener) {
    	registeredListeners.add(gameEventListener);
    }
    
    @Override
    public void callEvent(GameEvent event) {
    	System.out.println(event.getClass());
    	
    	Iterator<GameEventListener> listenerIterator = registeredListeners.iterator();
    	
    	while(listenerIterator.hasNext()) {
    		GameEventListener listener = listenerIterator.next();
    		
    		listener.handleEvent(event);
    	}
    }
    
    public void startListening() {
        Client client = this;
        
        Thread listen = new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (true) {
                        String line = in.readLine();
                        
                        if (line != null && (line.startsWith("OK") || line.startsWith("ERR") || line.startsWith("SVR"))) {   
                        	//Received event
                        	if(line.startsWith("SVR GAME ")) {
                        		String eventStr = line.substring("SVR GAME ".length());
                        		String eventType = eventStr.split(" \\{")[0];
                        		HashMap<String, String> data = StringUtils.stringToMap("{" + eventStr.split(" \\{")[1]);
                        		
                        		switch(eventType) {
                        			case "MATCH":
                        				callEvent(new MatchStartEvent(data.get("GAMETYPE"), data.get("PLAYERTOMOVE"), data.get("OPPONENT")));
                        				break;
                        			case "YOURTURN":
                        				callEvent(new YourTurnEvent(data.get("TURNMESSAGE")));
                        				break;
                        			case "MOVE":
                        				callEvent(new MoveEvent(data.get("PLAYER"), data.get("DETAILS"), data.get("MOVE")));
                        				break;
                        			case "WIN":
                        			case "LOSS":
                        			case "DRAW":
                        				callEvent(new MatchFinishEvent(data.get("RESULT"), data.get("PLAYERONESCORE"), data.get("PLAYERTWOSCORE"), data.get("COMMENT")));
                        				break;
                        			case "CHALLENGE":
                        				callEvent(new ChallengeReceiveEvent(data.get("CHALLENGER"), data.get("GAMETYPE"), data.get("CHALLENGENUMBER")));
                        				break;
                        			case "CHALLENGE CANCELLED":
                        				callEvent(new ChallengeCancelledEvent(data.get("CHALLENGENUMBER")));
                        				break;
                        		}
                        		
                        	//Received command response
                        	} else if (ct != null) {
                                switch (ct) {
                                    case LOGIN:
                                    case SUBSCRIBE:
                                    case MOVE:
                                    case FORFEIT:
                                    case CHALLENGE:
                                    case ACCEPTCHALLENGE:
                                        success = line.startsWith("OK");
                                        break;
                                    case GETGAMELIST:
                                        line = in.readLine();
                                        gameList = StringUtils.stringToArray(line.substring("SVR GAMELIST ".length()));
                                        break;
                                    case GETPLAYERS:
                                        line = in.readLine();
                                        playerList = StringUtils.stringToArray(line.substring("SVR PLAYERLIST ".length()));
                                        break;
                                }
                                ct = null;
                                synchronized (client) {
                                    client.notify();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listen.start();
    }

    private void sendCommand(String writable) throws IOException {
        bw.write(writable);
        bw.newLine();
        bw.flush();
    }

    public static void main(String[] args) {
        Client client = new Client();
        
        boolean connected = false;
        try {
            connected = client.connect(InetAddress.getByName("localhost"), 7789);
            client.startListening();
            System.out.println("connected: " + connected);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        if (connected) {
            boolean login = client.login("#" + new Random().nextInt(100));
            System.out.println("logging in: " + login);
            
            String[] players = client.getPlayers();
            System.out.println("players: " + Arrays.asList(players));
            
            String[] games = client.getGameList();
            System.out.println("games: " + Arrays.asList(games));
            
            client.subscribe("Tic-tac-toe");
        }
    }
}
