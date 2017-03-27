import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
            this.socket = new Socket(inetAddress, 7789);
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
                bw.write(username + "\n");
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
                bw.write("bye\n");
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
                bw.write("get gamelist\n");
                ct = CommandType.GETGAMELIST;
                System.out.println("before lock");
                guardedLock();
                System.out.println("after lock");
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
                bw.write("get playerlist\n");
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
                bw.write("subscribe " + game + "\n");
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
                bw.write("move " + move + "\n");
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
                bw.write("forfeit\n");
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
                bw.write("challenge \"" + player + "\" \"" + game + "\"\n");
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
                bw.write("challenge accept " + challengeNum + "\n");
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

    public void listener () {
        Client client = this;
        Thread listen = new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (true) {
                        String line;
                        if ((line = in.readLine()) != null && ct != null) {
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
                                    line = line.substring(1, line.length()-1);
                                    gameList = line.split(",");
                                    break;
                                case GETPLAYERS:
                                    line = line.substring(1, line.length()-1);
                                    playerList = line.split(",");
                                    break;


                            }
                            ct = null;
                            synchronized (client) {
                                client.notify();
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

    public static void main(String[] args) {
        Client client = new Client();
        boolean connected = false;
        try {
            connected = client.connect(InetAddress.getByName("145.33.225.170"), 7789);
            System.out.println(connected);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.listener();

        if (connected) {
            System.out.println("hoi");
            boolean login = client.login("samikroon");
            System.out.println("logging in: " + login);
            String[] test = client.getGameList();
            System.out.println("gamelist: " +test);
        }


    }

}
