package something.Core;
import something.Core.event.events.client.ChallengeCancelledEvent;
import something.Core.event.events.client.ChallengeReceiveEvent;
import something.Core.event.events.client.MatchFinishEvent;
import something.Core.event.events.client.MatchStartEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.YourTurnEvent;
import something.Core.utils.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Client extends Listenable {
		
    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;

    private CommandType ct;
    private boolean success = false;
    private String[] gameList;
    private String[] playerList;
    
    public void guardedLock() {
        while (ct != null) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {}
        }
    }

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

    public boolean login(String username) {
        if (!isLocked()) {
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

    public void logout() {
        if (!isLocked()) {
            try {
                sendCommand("bye");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getGameList() {
        if (!isLocked()) {
            try {
                sendCommand("get gamelist");
                setCommandType(CommandType.GETGAMELIST);
                guardedLock();
                return gameList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gameList;
    }

    public String[] getPlayers() {
        if (!isLocked()) {
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

    public boolean subscribe(String game) {
        if (!isLocked()) {
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

    public boolean makeMove(int move) {
        if (!isLocked()) {
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

    public boolean forfeit() {
        if (!isLocked()) {
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

    public boolean challenge(String player, String game) {
        if (!isLocked()) {
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

    public boolean acceptChallenge(String challengeNum) {
        if (!isLocked()) {
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
    
    public void startListening() {
        Client client = this;

        Thread listen = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        String line = br.readLine();
                                                            
                        if (line != null && (line.startsWith("OK") || line.startsWith("ERR") || line.startsWith("SVR"))) {
                        	//Received event
                        	if(line.startsWith("SVR GAME ")) {
                        		String eventStr = line.substring("SVR GAME ".length());
                        		String eventType = eventStr.split(" \\{")[0];
                        		HashMap<String, String> data = StringUtils.stringToMap("{" + eventStr.split(" \\{")[1]);
                        		
                        		switch(eventType) {
                        			case "MATCH":
                        				fireEvent(new MatchStartEvent(client, data.get("GAMETYPE"), data.get("PLAYERTOMOVE"), data.get("OPPONENT")));
                        				break;
                        			case "YOURTURN":
                        				fireEvent(new YourTurnEvent());
                        				break;
                        			case "MOVE":
                        				fireEvent(new MoveEvent(Integer.valueOf(data.get("MOVE"))));
                        				break;
                        			case "WIN":
                        			case "LOSS":
                        			case "DRAW":
                        				fireEvent(new MatchFinishEvent(client, eventType, data.get("PLAYERONESCORE"), data.get("PLAYERTWOSCORE"), data.get("COMMENT")));
                        				break;
                        			case "CHALLENGE":
                        				fireEvent(new ChallengeReceiveEvent(client, data.get("CHALLENGER"), data.get("GAMETYPE"), data.get("CHALLENGENUMBER")));
                        				break;
                        			case "CHALLENGE CANCELLED":
                        				fireEvent(new ChallengeCancelledEvent(client, data.get("CHALLENGENUMBER")));
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

    public void sendCommand(String writable) throws IOException {      	
    	if(bw == null) {
    		throw new NullPointerException("Client not connected");
    	}
    	
    	bw.write(writable);
        bw.newLine();
        bw.flush();
    }
    
    private void setCommandType(CommandType type) {
    	ct = type;
    }
    
    private boolean isLocked() {
    	return ct != null;
    }
}
