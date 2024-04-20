package gobblet;

public class Piece implements Cloneable{

    private int x;
    private int y;

    private final int size;
    private final boolean isBlack;
    private boolean picked;

    public Piece(int x, int y, boolean isBlack, int size) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.size = size;
        this.picked = false;
    }
    private Piece(int x, int y, boolean isBlack, int size,boolean picked) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.size = size;
        this.picked = picked;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    boolean isOnBoard() {
        return (y > -1 && y < 4);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public boolean isPicked() {
        return picked;
    }

    public void pick() {
        picked = true;
    }

    public void unpick() {
        picked = false;
    }

    @Override
    public Piece clone() throws CloneNotSupportedException {
        super.clone();
        return new Piece(x, y, isBlack, size,picked);
    }

}
