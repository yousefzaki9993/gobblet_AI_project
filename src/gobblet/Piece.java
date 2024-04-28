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

    public boolean isPartOfWinningSequence(Board board, int deltaX, int deltaY, boolean isBlack) {
        int x = getX();
        int y = getY();
        int count = 0;

        // Check in the positive direction
        while (board.getPiece(x, y) != null && board.getPiece(x, y).isBlack() == isBlack) {
            count++;
            x += deltaX;
            y += deltaY;
        }

        x = getX() - deltaX;
        y = getY() - deltaY;

        // Check in the negative direction
        while (board.getPiece(x, y) != null && board.getPiece(x, y).isBlack() == isBlack) {
            count++;
            x -= deltaX;
            y -= deltaY;
        }

        return count >= 3; // Adjust the threshold based on your game's winning conditions
    }

}
