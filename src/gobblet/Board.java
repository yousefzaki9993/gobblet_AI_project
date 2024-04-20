package gobblet;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable{

    public static final int length = 4;
    private List<Piece> pieces = new ArrayList<>();
    private String illegalNote = null;

    public Board() {
        intialize_pieces();
    }

    public void move(Piece p, int toX, int toY) {
        if (!isLegal(p, toX, toY)) {
            throw new IllegalArgumentException("Illegal move attempt! " + illegalNote);
        }
        p.move(toX, toY);
        
    }

    public boolean isLegal(Piece p, int toX, int toY) {
        if (toX > 3 || toX < 0 || toY > 3 || toY < 0) {
            illegalNote = "Illegal Move (can't move piece back to offboard)";
            return false;
        }
        if (p.getX() == toX && p.getY() == toY) {// if it was the exact same piece
            illegalNote = "Illegal Move (move must change piece coordinates)";
            return false;
        }
        Piece toP = getPiece(toX, toY);
        if (toP == null) {
            return true;
        }

        if (toP.getSize() > p.getSize()) {// if that piece was smaller (overtake it)
            illegalNote = "Illegal Move (can't overtake bigger or equal piece)";
            return false;
        }

        if (toP.isBlack() == p.isBlack()) {
            return true;
        }
        //if player tries to overtake an opponent smaller piece
        if (!p.isOnBoard() && !checkThreeInLine(toY, toX, !p.isBlack())) {// if that piece was off board
            illegalNote = "Illegal Move (can't overtake opponent from board unless he has three connected pieces)";
            return false;
        }

        //if that piece was on board or target is over three in row
        return true;
    }

    private Board(List<Piece> pieces) {
        this.pieces = pieces;
    }

    public final void intialize_pieces() {
        for (int i = 0; i < 3; i++) {
            for (int j = length; j > 0; j--) {
                pieces.add(new Piece(i, -1, true, j));
                pieces.add(new Piece(i, length, false, j));
            }
        }
    }

    public Piece getPiece(int x, int y) {
        Piece s = null;
        for (Piece p : pieces) {
            if ((p.getX() == x && p.getY() == y) && (s == null || p.getSize() > s.getSize()) && !p.isPicked()) {
                s = p;
            }
        }
        return s;
    }

    List<Piece> getPieces() {
        return pieces;
    }

    List<Piece> getMovables() {
        List<Piece> surPieces = new ArrayList<>();
        Piece[][] surface = new Piece[6][4];
        for (Piece p : pieces) {
            int y = p.getY()+1;
            int x = p.getX();
            if (surface[y][x] == null || surface[y][x].getSize() < p.getSize()) {
                surface[y][x] = p;
            }
        }
        for(int i = 0;i<6;i++){
            for(int j = 0; j < 4;j++){
                if(surface[i][j] != null)surPieces.add(surface[i][j]);
            }
        }
        return surPieces;
    }

    List<Piece> getOnBoard() {
        List<Piece> op = new ArrayList<>();
        for (Piece p : pieces) {
            if (p.isOnBoard()) {
                op.add(p);
            }
        }
        return op;
    }
}