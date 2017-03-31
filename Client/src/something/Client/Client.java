package something.Client;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.ChallengeCancelledEvent;
import something.Client.event.events.ChallengeReceiveEvent;
import something.Client.event.events.MatchFinishEvent;
import something.Client.event.events.MatchStartEvent;
import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;
import something.Client.utils.StringUtils;

/**
 * Created by samikroon on 3/27/17.
 */
public class Client implements GameClient {
    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;

    private CommandType ct;
    private boolean success = false;
    private String[] gameList;
    private String[] playerList;

    private LinkedList<GameEventListener> registeredListeners;
    
    public Client () {
    	registeredListeners = new LinkedList<>();
    	
    	try {
			connect(InetAddress.getByName("localhost"), 7789);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    public synchronized void guardedLock() {
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
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	startListening();

            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean login(String username) {
        if (waitForUnlock()) {
            try {
                sendCommand("login " + username);
                setCommandType(CommandType.LOGIN);
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
        if (waitForUnlock()) {
            try {
                sendCommand("bye");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getGameList() {
        if (waitForUnlock()) {
            try {
                sendCommand("get gamelist");
                setCommandType(CommandType.GETGAMELIST);
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
        if (waitForUnlock()) {
            try {
                sendCommand("get playerlist");
                setCommandType(CommandType.GETPLAYERS);
                guardedLock();
                return playerList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return playerList;
    }

    @Override
    public boolean subscribe(String game) {
        if (waitForUnlock()) {
            try {
                sendCommand("subscribe " + game);
                setCommandType(CommandType.SUBSCRIBE);
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
        if (waitForUnlock()) {
            try {
                sendCommand("move " + move);
                setCommandType(CommandType.MOVE);
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
        if (waitForUnlock()) {
            try {
                sendCommand("forfeit");
                setCommandType(CommandType.FORFEIT);
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
        if (waitForUnlock()) {
            try {
                sendCommand("challenge \"" + player + "\" \"" + game + "\"");
                setCommandType(CommandType.CHALLENGE);
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
        if (waitForUnlock()) {
            try {
                sendCommand("challenge accept " + challengeNum);
                setCommandType(CommandType.ACCEPTCHALLENGE);
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
                    while (true) {
                        String line = br.readLine();
                                                
                        if (line != null && (line.startsWith("OK") || line.startsWith("ERR") || line.startsWith("SVR"))) {   
                        	System.out.println(line);
                        	
                        	//Received event
                        	if(line.startsWith("SVR GAME ")) {
                        		String eventStr = line.substring("SVR GAME ".length());
                        		String eventType = eventStr.split(" \\{")[0];
                        		HashMap<String, String> data = StringUtils.stringToMap("{" + eventStr.split(" \\{")[1]);
                        		
                        		switch(eventType) {
                        			case "MATCH":
                        				callEvent(new MatchStartEvent(client, data.get("GAMETYPE"), data.get("PLAYERTOMOVE"), data.get("OPPONENT")));
                        				break;
                        			case "YOURTURN":
                        				callEvent(new YourTurnEvent(client, data.get("TURNMESSAGE")));
                        				break;
                        			case "MOVE":
                        				callEvent(new MoveEvent(client, data.get("PLAYER"), data.get("DETAILS"), data.get("MOVE")));
                        				break;
                        			case "WIN":
                        			case "LOSS":
                        			case "DRAW":
                        				callEvent(new MatchFinishEvent(client, data.get("RESULT"), data.get("PLAYERONESCORE"), data.get("PLAYERTWOSCORE"), data.get("COMMENT")));
                        				break;
                        			case "CHALLENGE":
                        				callEvent(new ChallengeReceiveEvent(client, data.get("CHALLENGER"), data.get("GAMETYPE"), data.get("CHALLENGENUMBER")));
                        				break;
                        			case "CHALLENGE CANCELLED":
                        				callEvent(new ChallengeCancelledEvent(client, data.get("CHALLENGENUMBER")));
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
                                        line = br.readLine();
                                        gameList = StringUtils.stringToArray(line.substring("SVR GAMELIST ".length()));
                                        break;
                                    case GETPLAYERS:
                                        line = br.readLine();
                                                                                
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
    
    private synchronized void setCommandType(CommandType type) {
    	ct = type;
    }
    
    private synchronized boolean isLocked() {
    	return ct != null;
    }
    
    private boolean waitForUnlock() { 
    	while(isLocked()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	return true;
    }
}
