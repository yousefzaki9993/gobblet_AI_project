package gobblet;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {

    public static final int length = 4;
    //moves record
    private int whiteLastFromX = 0;
    private int whiteLastFromY = 0;
    private int whiteLastToX = 0;
    private int whiteLastToY = 0;
    private int blackLastFromX = 0;
    private int blackLastFromY = 0;
    private int blackLastToX = 0;
    private int blackLastToY = 0;
    //repeate counter
    private int whiteRepeats = 0;
    private int BlackRepeats = 0;
    // records a draw
    private boolean draw = false;

    private List<Piece> pieces = new ArrayList<>();
    private String illegalNote = null;

    public Board() {
        intialize_pieces();
    }

    public void move(Piece p, int toX, int toY) {
        if (!isLegal(p, toX, toY)) {
            throw new IllegalArgumentException("Illegal move attempt! " + illegalNote);
        }

        if (p.isBlack()) {
            //check if moves are repeated
            if (p.getX() == blackLastToX && p.getY() == blackLastToY && toX == blackLastFromX && toY == blackLastFromY) {
                BlackRepeats++;
            } else {
                BlackRepeats = 0;
            }
            //record new moves
            blackLastToX = toX;
            blackLastToY = toY;
            blackLastFromX = p.getX();
            blackLastFromY = p.getY();
        } else {
            //check if moves are repeated
            if (p.getX() == whiteLastToX && p.getY() == whiteLastToY && toX == whiteLastFromX && toY == whiteLastFromY) {
                whiteRepeats++;
            } else {
                whiteRepeats = 0;
            }
            //record new moves
            whiteLastToX = toX;
            whiteLastToY = toY;
            whiteLastFromX = p.getX();
            whiteLastFromY = p.getY();
        }
        //check counter
        if (whiteRepeats >= 6 && BlackRepeats >= 6) {
            draw = true;
        }
        //move piece
        p.move(toX, toY);

    }

    public boolean isDraw() {
        return draw;
    }

    //check if move matches game rules
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

        if (toP.getSize() >= p.getSize()) {// if that piece was smaller (overtake it)
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

    // create new board using pieces from another board (for clonning)
    private Board(List<Piece> pieces,
            int whiteLastFromX,
            int whiteLastFromY,
            int whiteLastToX,
            int whiteLastToY,
            int blackLastFromX,
            int blackLastFromY,
            int blackLastToX,
            int blackLastToY,
            int whiteRepeats,
            int BlackRepeats,
            boolean draw) {
        this.pieces = pieces;
        //moves record
        this.whiteLastFromX = whiteLastFromX;
        this.whiteLastFromY = whiteLastFromY;
        this.whiteLastToX = whiteLastToX;
        this.whiteLastToY = whiteLastToY;
        this.blackLastFromX = blackLastFromX;
        this.blackLastFromY = blackLastFromY;
        this.blackLastToX = blackLastToX;
        this.blackLastToY = blackLastToY;
        //repeate counter
        this.whiteRepeats = whiteRepeats;
        this.BlackRepeats = BlackRepeats;
        // records a draw
        this.draw = draw;
    }

// create pieces and add them to side of board
    public final void intialize_pieces() {
        for (int i = 0; i < 3; i++) {
            for (int j = length; j > 0; j--) {
                pieces.add(new Piece(i, -1, true, j));
                pieces.add(new Piece(i, length, false, j));
            }
        }
    }

    // returns the largest piece at (x,y)
    public Piece getPiece(int x, int y) { //get surfacing piece in a specific location
        Piece s = null;
        for (Piece p : pieces) {
            if ((p.getX() == x && p.getY() == y) && (s == null || p.getSize() > s.getSize()) && !p.isPicked()) {
                s = p;
            }
        }
        return s;
    }

    // return all pieces
    List<Piece> getPieces() {
        return pieces;
    }

    List<Piece> getMovables() { //get list of pieces that can be legally moved
        List<Piece> surPieces = new ArrayList<>();
        Piece[][] surface = new Piece[6][4];
        for (Piece p : pieces) {
            int y = p.getY() + 1;
            int x = p.getX();
            if (surface[y][x] == null || surface[y][x].getSize() < p.getSize()) {
                surface[y][x] = p;
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (surface[i][j] != null) {
                    surPieces.add(surface[i][j]);
                }
            }
        }
        return surPieces;
    }

    // returns pieces that are on the board (movable or not)
    List<Piece> getOnBoard() {
        List<Piece> op = new ArrayList<>();
        for (Piece p : pieces) {
            if (p.isOnBoard()) {
                op.add(p);
            }
        }
        return op;
    }

    // returns the pieces at the side of the board
    List<Piece> getOffBoard() {
        List<Piece> op = new ArrayList<>();
        for (Piece p : pieces) {
            if (!p.isOnBoard()) {
                op.add(p);
            }
        }
        return op;
    }

    // check for winning line and returns it if any
    public Piece[] getWinningLine() { // return list of pieces that form a winning line
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

    public boolean checkThreeInLine(int col, int row, boolean isBlack) { //helper function for checking if three pieces are connected together in a line
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
        return new Board(ps,whiteLastFromX, whiteLastFromY, whiteLastToX, whiteLastToY, blackLastFromX, blackLastFromY, blackLastToX, blackLastToY, whiteRepeats, BlackRepeats, draw);
    }
}
