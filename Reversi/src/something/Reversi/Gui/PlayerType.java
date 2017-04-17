package something.Reversi.Gui;


import something.Core.player.ManualPlayer;
import something.Core.player.Player;
import something.Reversi.ReversiBoard;
import something.Reversi.player.MiniMaxReversiAIPlayer;
import something.Reversi.player.RandomReversiAIPlayer;
import something.Reversi.player.TileGreedyReversiAIPlayer;

import java.util.function.Supplier;

public enum PlayerType {
    Human(ManualPlayer::new),
    TileGreedyAI(TileGreedyReversiAIPlayer::new),
    RandomAI(RandomReversiAIPlayer::new),
    MinimaxAI(MiniMaxReversiAIPlayer::new);

    private Supplier<Player<ReversiBoard>> supplier;

    PlayerType(Supplier<Player<ReversiBoard>> supplier) {
        this.supplier = supplier;
    }

    public Player<ReversiBoard> getPlayer() {
        return supplier.get();
    }
}
