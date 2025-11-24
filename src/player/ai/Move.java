package player.ai;

public class Move {
    private int row;
    private int col;
    private int value;

    Move() {
        this.row = -1;
        this.col = -1;
        this.value = 0;
    }

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = -1;
    }

    public Move(Move move) {
        this.row = move.row;
        this.col = move.col;
        this.value = move.value;
    }

    Move(int value) {
        this.row = -1;
        this.col = -1;
        this.value = value;
    }

    Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    int getValue() {
        return this.value;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }

    void setValue(int value) {
        this.value = value;
    }

    Move max(Move move) {
        return (this.getValue() > move.getValue()) ? this : move;
    }

    Move min(Move move) {
        return (this.getValue() < move.getValue()) ? this : move;
    }
}
