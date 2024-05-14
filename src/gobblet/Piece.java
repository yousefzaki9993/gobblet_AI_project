package gobblet;

public class Piece implements Cloneable{

    private int x;
    private int y;

    private final int size;
    private final boolean isBlack;
    private boolean picked;
    
    /**
     * Constructor for Piece
     * 
     * @param x The X-coordinate of the piece
     * @param y The Y-coordinate of the piece
     * @param isBlack The color of the piece
     * @param size The size of the piece
     */
    public Piece(int x, int y, boolean isBlack, int size) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.size = size;
        this.picked = false;
    }
    
    /**
     * Constructor for Piece
     * 
     * @param x The X-coordinate of the piece
     * @param y The Y-coordinate of the piece
     * @param isBlack The color of the piece
     * @param size The size of the piece
     * @picked Is the piece picked or not
     */
    private Piece(int x, int y, boolean isBlack, int size,boolean picked) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.size = size;
        this.picked = picked;
    }
    
    /**
     * 
     * @param x The X-coordinate of the move
     * @param y The Y-coordinate of the move
     */
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * @return The size of the piece
     */
    public int getSize() {
        return size;
    }
    
    /**
     * 
     * @return is the piece on board or outside the board
     */
    boolean isOnBoard() {
        return (y > -1 && y < 4);
    }
    
    /**
     * 
     * @return The X-coordinate of the piece
     */
    public int getX() {
        return x;
    }
    
    /**
     * 
     * @return The Y-coordinate of the piece
     */
    public int getY() {
        return y;
    }
    
    /**
     * 
     * @return true if piece is black or false when piece is white
     */
    public boolean isBlack() {
        return isBlack;
    }
    
    /**
     * 
     * @return true if piece is picked or false when piece is not picked
     */
    public boolean isPicked() { //checks if a piece is selected
        return picked;
    }
    /**
     * 
     * mark piece as picked 
     */
    public void pick() {
        picked = true;
    }
    
    /**
     * 
     * mark piece as not picked 
     */
    public void unpick() {
        picked = false;
    }
    
    /**
     * 
     * @Override 
     */
    public Piece clone() throws CloneNotSupportedException {
        super.clone();
        return new Piece(x, y, isBlack, size,picked);
    }
    
    /**
     * 
     * @param board The board and its state
     * @param deltaX The difference on X-coordinate
     * @param deltaY The difference on Y-coordinate
     * @param isBlack The color of the piece
     * @return The adjusted threshold based on your game's winning conditions
     */
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
