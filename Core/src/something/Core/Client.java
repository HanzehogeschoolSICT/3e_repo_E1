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
            } catch (InterruptedException e) {
            }
        }
    }

    public void connect(InetAddress inetAddress, int port) throws IOException {
        this.socket = new Socket(inetAddress, port);
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        startListening();
    }

    public boolean login(String username) throws IOException {
        if (!isLocked()) {
            sendCommand("login " + username);
            setCommandType(CommandType.LOGIN);
            guardedLock();
            return success;
        }
        return false;
    }

    public void logout() throws IOException {
        if (!isLocked()) {
            sendCommand("bye");
            socket.close();
        }
    }

    public String[] getGameList() throws IOException {
        if (!isLocked()) {
            sendCommand("get gamelist");
            setCommandType(CommandType.GETGAMELIST);
            guardedLock();
            return gameList;
        }
        return gameList;
    }

    public String[] getPlayers() throws IOException {
        if (!isLocked()) {
            sendCommand("get playerlist");
            setCommandType(CommandType.GETPLAYERS);
            guardedLock();
            return playerList;
        }
        return playerList;
    }

    public boolean subscribe(String game) throws IOException {
        if (!isLocked()) {
            sendCommand("subscribe " + game);
            setCommandType(CommandType.SUBSCRIBE);
            guardedLock();
            return success;
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

    public boolean forfeit() throws IOException {
        if (!isLocked()) {
            sendCommand("forfeit");
            setCommandType(CommandType.FORFEIT);
            guardedLock();
            return success;
        }
        return false;
    }

    public boolean challenge(String player, String game) throws IOException {
        if (!isLocked()) {
            sendCommand("challenge \"" + player + "\" \"" + game + "\"");
            setCommandType(CommandType.CHALLENGE);
            guardedLock();
            return success;
        }
        return false;
    }

    public boolean acceptChallenge(String challengeNum) throws IOException {
        if (!isLocked()) {
            sendCommand("challenge accept " + challengeNum);
            setCommandType(CommandType.ACCEPTCHALLENGE);
            guardedLock();
            return success;
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
                            if (line.startsWith("SVR GAME ")) {
                                String eventStr = line.substring("SVR GAME ".length());
                                String eventType = eventStr.split(" \\{")[0];
                                HashMap<String, String> data = StringUtils.stringToMap("{" + eventStr.split(" \\{")[1]);
                                switch (eventType) {
                                    case "MATCH":
                                        fireEvent(new MatchStartEvent(client, data.get("GAMETYPE"), data.get("PLAYERTOMOVE"), data.get("OPPONENT")));
                                        break;
                                    case "YOURTURN":
                                        fireEvent(new YourTurnEvent());
                                        break;
                                    case "MOVE":
                                        fireEvent(new MoveEvent(data.get("PLAYER"), Integer.valueOf(data.get("MOVE"))));
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
                    // Exit immediately & cleanly
                }
            }
        });
        listen.start();
    }

    public void sendCommand(String writable) throws IOException {
        if (bw == null) {
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

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
