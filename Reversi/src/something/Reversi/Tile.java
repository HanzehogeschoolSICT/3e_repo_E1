package something.Reversi;

public enum Tile {
    EMPTY(" "),
    BLACK("B"),
    WHITE("W");

    private String str;

    Tile(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
