package something.TicTacToe;

public enum Mark {
    EMPTY(" "),
    NOUGHT("O"),
    CROSS("X");

    private String str;

    Mark(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
