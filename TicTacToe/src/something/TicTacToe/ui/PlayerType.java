package something.TicTacToe.ui;


import something.Core.player.ManualPlayer;
import something.Core.player.Player;
import something.TicTacToe.TicTacToeBoard;
import something.TicTacToe.player.TicTacToeAIPlayer;

import java.util.function.Supplier;

public enum PlayerType {
    Human(ManualPlayer::new), SimpleAI(TicTacToeAIPlayer::new);

    private Supplier<Player<TicTacToeBoard>> supplier;

    PlayerType(Supplier<Player<TicTacToeBoard>> supplier) {
        this.supplier = supplier;
    }

    public Player<TicTacToeBoard> getPlayer() {
        return supplier.get();
    }
}
