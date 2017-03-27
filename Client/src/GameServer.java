import java.net.InetAddress;
import java.util.Collection;

public interface GameServer {
    boolean connect(InetAddress inetAddress, int port);
    boolean login(String username);
    boolean logout();
    Collection<String> getGameList();
    Collection<String> getPlayers();
    boolean subscribe(String game);
    boolean move(String move);
    boolean forfeit();
    boolean challenge(String player, String game);
    boolean acceptChallenge(int challengeNum);

    void registerEventListener(GameEventListener gameEventListener);
}
