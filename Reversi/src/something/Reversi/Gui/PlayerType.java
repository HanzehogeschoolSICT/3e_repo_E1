package something.Reversi.Gui;


import something.Core.player.ManualPlayer;
import something.Core.player.Player;
import something.Reversi.ReversiBoard;
import something.Reversi.player.MiniMaxReversiAIPlayer;
import something.Reversi.player.RandomReversiAIPlayer;
import something.Reversi.player.ScoreGreedyReversiAIPlayer;
import something.Reversi.player.TileGreedyReversiAIPlayer;

import java.util.function.Supplier;

public enum PlayerType {
    Human(ManualPlayer::new),
    TileGreedyAI(TileGreedyReversiAIPlayer::new),
    ScoreGreedyAI(ScoreGreedyReversiAIPlayer::new),
    RandomAI(RandomReversiAIPlayer::new),
    MinimaxAI_200(() -> new MiniMaxReversiAIPlayer(200)),
    MinimaxAI_2000(() -> new MiniMaxReversiAIPlayer(2000)),
    MinimaxAI_4000(() -> new MiniMaxReversiAIPlayer(4000)),
    MinimaxAI_5500(() -> new MiniMaxReversiAIPlayer(5500));

    private Supplier<Player<ReversiBoard>> supplier;

    PlayerType(Supplier<Player<ReversiBoard>> supplier) {
        this.supplier = supplier;
    }

    public Player<ReversiBoard> getPlayer() {
        return supplier.get();
    }
}
