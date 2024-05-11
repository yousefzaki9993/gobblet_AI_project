package gobblet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSystem {
    // The game board
    private final Board board;
    // Indicates the current turn (true for black, false for white)
    private boolean turn;
    // Flag to determine if the game is currently running
    private boolean running;
    // Holds the winner of the game
    private Boolean winner = null; //null if running
    // Represents the currently picked piece
    private Piece picked = null;
    // Notifier for game events
    private Notifier notifier;

     /**
     * Pauses the game.
     */
    public void pause() {
        if (running) {
            notifier.pause();
        }
    }
    /**
     * Resumes the paused game.
     */
    public void resume() {
        notifier.resume();
    }
    /**
     * Constructor for GameSystem. 
     */
    public GameSystem() {//player1 isBlack = false & player2 isBlack=true
        board = new Board();
        turn = false;

        notifier = new Notifier();

    }
    /**
     * Sets the players for the game.
     * @param player1 The first player
     * @param player2 The second player
     */
    public void setPlayers(Observer player1, Observer player2) {
        if (running) {
            throw new IllegalStateException();
        }
        notifier.add(player1);
        notifier.add(player2);
        running = true;
        notifier.notifyPlayer(false);
    }

    
    /**
     * Adds a neutral observer to the game.
     * @param o The observer to be added
     */
    public void addNeutralObserver(Observer o) {
        notifier.addNeutralObserver(o);
    }

    /**
     * Unpicks the currently picked piece.
     * @param isBlackTurn Indicates whether it is black's turn
     * @return True if successful, false otherwise
     */
    public boolean unpick(boolean isBlackTurn) {
        if (!picked.isOnBoard()) {
            picked.unpick();
            picked = null;
            return true;
        }
        return false;
    }
    /**
     * Moves a piece on the board.
     * @param isBlackTurn Indicates whether it is black's turn
     * @param toX The destination X coordinate
     * @param toY The destination Y coordinate
     * @return True if the move is valid, false otherwise
     */
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
                notifier.notifyPlayer(!isBlackTurn);//notify end of turn
            }

            return true;
        }

        return false;
    }
    /**
     * Picks a piece from the board.
     * @param isBlackTurn Indicates whether it is black's turn
     * @param initX The initial X coordinate of the piece
     * @param initY The initial Y coordinate of the piece
     * @return True if successful, false otherwise
     */
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
                // check for line underneath
                Piece[] line = board.getWinningLine();
                if (line != null) {
                    boolean movable = false;
                    for (Piece lp : line) {
                        if ((p.getX() != lp.getX() || p.getY() != lp.getY()) && p.getSize() > lp.getSize()) {
                            movable = true;
                            break;
                        }
                    }
                    if (!movable) {
                        winner = line[0].isBlack();
                        running = false;
                        notifier.notifyPlayersEnd();//notify end of the game
                        return true;
                    }
                }
                //check for a place to move to
                boolean[][] notMovableTo = new boolean[4][4];
                List<Piece> movables = board.getMovables();
                for (Piece mp : movables) {
                    if (mp.isOnBoard() && mp.getSize() >= picked.getSize()) {
                        notMovableTo[mp.getX()][mp.getY()] = true;
                    }
                }
                boolean movable = false;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (!notMovableTo[i][j]) {
                            movable = true;
                            break;
                        }
                        if (movable) {
                            break;
                        }
                    }
                }
                if (!movable) {
                    winner = !isBlackTurn;
                    running = false;
                    notifier.notifyPlayersEnd();//notify end of the game
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    /**
     * Checks for a winner in the game.
     */
    public void check_for_winner() {
        Piece[] line = board.getWinningLine();
        if (line != null) {
            winner = line[0].isBlack();
            running = false;
            notifier.notifyPlayersEnd();//notify end of the game

        } else if (board.isDraw()) {
            running = false;
            notifier.notifyPlayersEnd();//notify end of the game
        }

    }
    /**
     * Returns a copy of the current game board.
     * @return A copy of the game board
     */
    public Board getBoardCopy() {
        try {
            return board.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(GameSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * Checks if the game is over.
     * @return True if the game is over, false otherwise
     */
    public boolean isOver() {
        return !running;
    }
    /**
     * Gets the winner of the game.
     * @return True if black is the winner, false if white is the winner
     */
    public Boolean getWinner() { // returns true if black is winner false if white is winner
        return winner;
    }
    /**
     * Gets the current turn in the game.
     * @return True if it is black's turn, false otherwise
     */
    public boolean getTurn() { // true is blck turn
        return turn;
    }
    /**
     * Gets the last note related to an illegal move on the board.
     * @return The last illegal note on the board
     */
    public String getBoardLastIllegalNote() {
        return board.getLastIllegalNote();
    }

}
