package something.Client.player;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OnlinePlayer extends Player {

	public OnlinePlayer(String username, PlayerType playerType) {
		super(username, playerType);
		
		//Gameserver IP: 145.33.225.170
		try {
			connect(InetAddress.getByName("localhost"), 7789);
			login(username);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void makeMove(int index) {
		move(index + "");
		setHasTurn(false);
	}
}
