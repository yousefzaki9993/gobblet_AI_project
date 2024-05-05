package gobblet;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardNode implements Comparable<BoardNode> {

    private boolean maxPlayer;
    private int score;
    private List<BoardNode> children = null;
    private Board board;  // Board class used as an attribute
    private int PrevX;
    private int PrevY;
    private Piece PrevPiece;

    //constuctor
    public BoardNode(Board board, Piece PrevPiece, int PrevX, int PrevY) {
        this.board = board;
        this.PrevPiece = PrevPiece;
        this.PrevX = PrevX;
        this.PrevY = PrevY;
    }

    public void deleteBoard() {
        board = null;
    }
    // Getters and setters for other attributes

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    // methods 
    public List<BoardNode> getChildren() {
//		List<BoardNode> childrenCopy = new ArrayList<>();
//    	if(children!= null) {
//    	    childrenCopy = new ArrayList <> (children); // To prevent external modification 
//    	}

        return children;
    }

    public void addChild(BoardNode child) {
        //check if there is no children and create list of children 
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public void evaluate(int depth, ScoreEval ev) {
        if (ev == null) {
            throw new IllegalArgumentException("invalide evaloator");
        }
        if (depth < 0) {
            throw new IllegalArgumentException("depth must be >= 0");
        }
        if (score == 10000 || score == -10000) {
            return;
        }
        if (depth == 0) {
            this.score = ev.evaluateBoard(board);
            return;
        }
        if (board == null) {
            throw new IllegalStateException("board missing!");
        }
        if (children == null) {
            generateChildren();
        }
        for (BoardNode child : children) {
            child.evaluate(depth - 1, ev);
        }
        if (!maxPlayer) {
            children.sort((o1, o2) -> {
                return o1.compareTo(o2);
            });
        } else {
            children.sort((o1, o2) -> {
                return -(o1.compareTo(o2));
            });
        }
        this.score = children.get(0).getScore();
    }

    public int getScore() {
        return score;
    }

    public void setMaximizer() {
        maxPlayer = true;

        if (children != null) {
            for (BoardNode child : children) {
                child.setMinimizer();
            }
        }
    }

    public void setMinimizer() {
        maxPlayer = false;

        if (children != null) {
            for (BoardNode child : children) {
                child.setMaximizer();
            }
        }
    }

    public boolean isMaxPlayer() {
        return maxPlayer;
    }

    public void setScore(int score) {      //Temporary function for testing
        this.score = score;
    }

    @Override
    public int compareTo(BoardNode node) {
        return Integer.compare(this.score, node.score);
    }

    private void generateChildren() {
        if (children != null) {
            throw new IllegalStateException("node already has children");
        }
        if (board == null) {
            throw new IllegalStateException("board not found");
        }
        try {
            List<Piece> movables = board.getMovables(); //gets list of pieces that can be moved legally
            for (int i = 0; i < movables.size(); i++) {
                Piece piece = movables.get(i);
                if ((!maxPlayer && piece.isBlack()) || (maxPlayer && !piece.isBlack())) { //if the piece fits the right turn
                    for (int j = 0; j < 4; j++) { //iterate through every slot in the board
                        for (int k = 0; k < 4; k++) {

                            if (board.isLegal(piece, j, k)) {
                                Board cloneBoard;

                                cloneBoard = board.clone();

                                //Make a move in the cloned board
                                cloneBoard.move(cloneBoard.getPiece(piece.getX(), piece.getY()), j, k);

                                //Add the cloned board to a board node
                                BoardNode boardnode = new BoardNode(cloneBoard, piece, j, k);
                                //set maximizer or minimizer
                                if (maxPlayer) {
                                    boardnode.setMinimizer();
                                } else {
                                    boardnode.setMaximizer();
                                }
                                //add the board node to the root node
                                this.addChild(boardnode);
                                //if a board node satisfy winning condition, make it a leaf node and give it max/min score
                                Piece[] line = boardnode.getBoard().getWinningLine();
                                if (line != null) {

                                    int score = line[0].isBlack() ? -10000 : 10000;
                                    boardnode.setScore(score);
                                    boardnode.deleteBoard();
                                }

                            }
                        }
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BoardNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.deleteBoard(); //setting board to null after generating children to save memory
    }

    public int getPrevX() {
        return PrevX;
    }

    public int getPrevY() {
        return PrevY;
    }

    public Piece getPrevPiece() {
        return PrevPiece;
    }

}