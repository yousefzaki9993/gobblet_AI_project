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
    
    List<Piece> getOffBoard() {
        List<Piece> op = new ArrayList<>();
        for (Piece p : pieces) {
            if (!p.isOnBoard()) {
                op.add(p);
            }
        }
        return op;
    }

    public Piece[] getWinningLine() {
        Piece[] line = new Piece[4];
        //check all rows
        for (int row = 0; row < length; row++) {
            for (int i = 0; i < length; i++) {
                line[i] = getPiece(row, i);
            }
            if (checkLine(line)) {
                return line;
            }
        }

        //check all columns
        for (int col = 0; col < length; col++) {
            for (int i = 0; i < length; i++) {
                line[i] = getPiece(i, col);
            }
            if (checkLine(line)) {
                return line;
            }
        }

        //check one diagonal
        for (int i = 0; i < length; i++) {
            line[i] = getPiece(i, i);
        }
        if (checkLine(line)) {
            return line;
        }

        //check the other diagonal
        for (int i = 0; i < length; i++) {
            line[i] = getPiece(i, length - i - 1);
        }
        if (checkLine(line)) {
            return line;
        }

        return null;
    }
    
    private boolean checkLine(Piece[] line) {
        for (Piece p : line) {
            if (p == null) {
                return false;
            }
        }
        return line[0].isBlack() == line[1].isBlack()
                && line[1].isBlack() == line[2].isBlack()
                && line[2].isBlack() == line[3].isBlack();
    }

    public boolean checkThreeInLine(int col,int row,  boolean isBlack) {
        Piece[] line = new Piece[4];
        //check row

        for (int i = 0; i < length; i++) {
            line[i] = getPiece(row, i);
        }
        if (check3(line, isBlack)) {
            return true;
        }

        //check column
        for (int i = 0; i < length; i++) {
            line[i] = getPiece(i, col);
        }
        if (check3(line, isBlack)) {
            return true;
        }

        //check one diagonal
        if (col == row) {
            for (int i = 0; i < length; i++) {
                line[i] = getPiece(i, i);
            }
            if (check3(line, isBlack)) {
                return true;
            }
        }

        //check the other diagonal
        if (col == length - row - 1) {
            for (int i = 0; i < length; i++) {
                line[i] = getPiece(i, length - i - 1);
            }
            if (check3(line, isBlack)) {
                return true;
            }
        }
        return false;
    }

    private boolean check3(Piece[] line, boolean isBlack) {
        int c = 0;
        for (Piece p : line) {
            if (p != null && p.isBlack() == isBlack) {
                c++;
            }
            
        }
        
        return c >= 3;
        
    }

    public String getLastIllegalNote() {
        return illegalNote;
    }

    @Override
    public Board clone() throws CloneNotSupportedException {
        super.clone();
        List<Piece> ps = new ArrayList<>();
        for (Piece p : pieces) {
            ps.add(p.clone());
        }
        return new Board(ps);
    }
}