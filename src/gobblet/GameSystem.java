package gobblet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSystem {

    private final Board board;
    private boolean turn;
    private boolean running;
    private Boolean winner = null; //null if running
    private Piece picked = null;
    private Notifier notifier;

    public GameSystem(Observer player1, Observer player2) {//player1 isBlack = false & player2 isBlack=true
        board = new Board();
        turn = false;
        running = true;
        notifier = new Notifier();
        notifier.add(player1);
        notifier.add(player2);
    }

    public boolean move(boolean isBlackTurn, int toX, int toY) { //returns false if move is invalide
        if (picked.getX() == toX && picked.getY() == toY) {//unpick
            if (toY > -1 && toY < 4) {
                return false;
            } else {
                picked.unpick();
                picked = null;
                return true;
            }
        }
        if (!running) {
            return false;
        }
        if (picked.isBlack() != isBlackTurn) {
            return false;
        }
        if (board.isLegal(picked, toX, toY)) {
            board.move(picked, toX, toY);
            picked.unpick();
            picked = null;
            check_for_winner();
            if (running) {
                turn = !turn;
            }
            notifier.notifyPlayer(!isBlackTurn);//notify end of turn
            return true;
        }
        return false;
    }

    public boolean pick(boolean isBlackTurn, int initX, int initY) {

        if (!running) {
            return false;
        }
        Piece p = board.getPiece(initX, initY);
        if (p == null) {
            return false;
        }

        if (isBlackTurn == p.isBlack() && picked == null) {
            p.pick();
            picked = p;
            if (p.isOnBoard()) {
                Piece[] line = board.getWinningLine();
                if (line != null) {
                    boolean movable = false;
                    for (Piece lp : line) {
                        if (p.getX() != lp.getX() && p.getY() != lp.getY() && p.getSize() > lp.getSize()) {
                            movable = true;
                            break;
                        }
                    }
                    if (!movable) {
                        winner = line[0].isBlack();
                        running = false;
                        notifier.notifyPlayersEnd();//notify end of the game
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void check_for_winner() {
        Piece[] line = board.getWinningLine();
        if (line != null) {
            winner = line[0].isBlack();
            running = false;
            notifier.notifyPlayersEnd();//notify end of the game
        }

    }

    public Board getBoardCopy() {
        try {
            return board.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(GameSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean isOver() {
        return !running;
    }

    public boolean getWinner() { // returns true if black is winner false if white is winner
        return winner;
    }

    public boolean getTurn() { // true is blck turn
        return turn;
    }

}
