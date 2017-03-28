import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    public Client () {

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
                System.out.println("login " + username);
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
    public boolean logout() {
        if (ct == null) {
            try {
                sendCommand("bye");
                ct = CommandType.LOGOUT;
                guardedLock();
                return success;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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

    }

    public void listener() {
        Client client = this;
        Thread listen = new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (true) {
                        String line = in.readLine();
                        System.out.println(line);
                        if (line != null &&
                                (line.startsWith("OK") || line.startsWith("ERR") || line.startsWith("SVR"))) {
                            if (ct != null) {
                                System.out.println(ct);
                                switch (ct) {
                                    case LOGIN:
                                    case SUBSCRIBE:
                                    case MOVE:
                                    case FORFEIT:
                                    case CHALLENGE:
                                    case ACCEPTCHALLENGE:
                                        System.out.println("in switch case");
                                        success = true;
                                        break;
                                    case GETGAMELIST:
                                        line = in.readLine();
                                        gameList = StringUtils.parseString(line.substring("SVR GAMELIST ".length()));
                                        break;
                                    case GETPLAYERS:
                                        line = in.readLine();
                                        System.out.println(line);
                                        playerList = StringUtils.parseString(line.substring("SVR PLAYERLIST ".length()));
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
            client.listener();
            System.out.println("connected: " + connected);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        if (connected) {
            boolean login = client.login("samikroon");
            System.out.println("logging in: " + login);
            String[] test = client.getPlayers();
            System.out.println("players: " + Arrays.asList(test));
        }


    }

}
