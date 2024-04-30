package gobblet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoveEvaloator {

    private int destX;
    private int destY;
    private Piece PieceToMove;
    private Board board;

    public MoveEvaloator(Board board) {
        this.board = board;
    }



    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    public Piece getPieceToMove() {
        return PieceToMove;
    }
}