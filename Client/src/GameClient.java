import event.GameEventListener;

import java.net.InetAddress;
import event.GameEvent;
import event.GameEventListener;

public interface GameClient {
    boolean connect(InetAddress inetAddress, int port);
    boolean login(String username);
    boolean logout();
    String[] getGameList();
    String[] getPlayers();
    boolean subscribe(String game);
    boolean move(String move);
    boolean forfeit();
    boolean challenge(String player, String game);
    boolean acceptChallenge(int challengeNum);

    void registerEventListener(GameEventListener gameEventListener);
    void callEvent(GameEvent event);
}
