package something.Client.player;

import something.Client.Board;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OnlinePlayer<GameType extends Board> extends Player<GameType> {

	public OnlinePlayer(String username, PlayerType<GameType> playerType) {
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
