package gobblet;
//necessary imports
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
    private boolean draw = false;

    /**
     * Constructor for BoardNode.
     *
     * @param board The current state of the board.
     * @param PrevPiece The piece played in the previous move.
     * @param PrevX The X-coordinate of the previous move.
     * @param PrevY The Y-coordinate of the previous move.
     */
    public BoardNode(Board board, Piece PrevPiece, int PrevX, int PrevY) {
        this.board = board;
        this.PrevPiece = PrevPiece;
        this.PrevX = PrevX;
        this.PrevY = PrevY;
    }

   
    /**
     * Deletes the board associated with this node.
     */
    public void deleteBoard() {
        board = null;
    }
    /**
     * Gets the board associated with this node.
     *
     * @return The board associated with this node.
     */

    public Board getBoard() {
        return board;
    }
    /**
     * Sets the board associated with this node.
     *
     * @param board The board to be set.
     */
    public void setBoard(Board board) {
        this.board = board;
    }
    /**
     * Gets the list of child nodes.
     *
     * @return List of child nodes.
     */
    public List<BoardNode> getChildren() {
        return children;
    }
    /**
     * Adds a child node to the list of children.
     *
     * @param child The child node to be added.
     */
    public void addChild(BoardNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
    /**
     * Evaluates the node based on the current depth, evaluation method, alpha, beta, and a parameter 'b'.
     *
     * @param depth The depth of the evaluation in the game tree.
     * @param ev The evaluation method to be used.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @param b A parameter used in the evaluation.
     */
    public void evaluate(int depth, ScoreEval ev, Integer alpha, Integer beta, int b) {
        if (ev == null) {
            throw new IllegalArgumentException("invalide evaloator");
        }
        if (depth < 0) {
            throw new IllegalArgumentException("depth must be >= 0");
        }
        //if the move causes a win 
        if (score == 10000 || score == -10000) {
            return;
        }
        //if the move causes a draw
        if(draw){
            System.out.println("drawnode");
            score = 0;
            return;
        }
        // if depth = 0 evaluate node using evaluator function
        if (depth == 0) {
            this.score = ev.evaluateBoard(board);
            return;
        }
        // if it is leaf node which is evaluated recently return
        if (board == null && children.isEmpty()) {
            return;
        }
        // if the node is not leaf node but children are not generated generate them
        if (board != null) {
            generateChildren();
        }
        
        // evaluate each child recursivly in aloop
        //  b(chil) =  b(parent)-rank of child
        if (b > children.size()) {
            b = children.size();
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).evaluate(depth - 1, ev, alpha, beta, (b - i - 1 > 0) ? b - i - 1 : 1);
            if (maxPlayer) {
                if (alpha == null || children.get(i).getScore() > alpha) {
                    alpha = children.get(i).getScore();
                    if (beta != null && beta <= alpha) {
                        this.score = alpha;
                        return;
                    }
                }
            } else {
                if (beta == null || children.get(i).getScore() < beta) {
                    beta = children.get(i).getScore();
                    if (alpha != null && beta <= alpha) {
                        this.score = beta;
                        return;
                    }
                }
            }
        }
        // sort children for next iterations
        if (!maxPlayer) {
            children.sort((o1, o2) -> {
                return o1.compareTo(o2);
            });
        } else {
            children.sort((o1, o2) -> {
                return -(o1.compareTo(o2));
            });
        }
        // if maximizer alpha will be used by parent and if not beta will be
        if (maxPlayer) {
            this.score = alpha;
        } else {
            this.score = beta;
        }

        if (b < children.size()) {
            children.subList(b, children.size()).clear();
        }
    }
    //returns node score last evaluated
    public int getScore() {
        return score;
    }
    /**
     * Gets the score associated with this node.
     *
     * @return The score associated with this node.
     */
    public int getScore() {
        return score;
    }
    /**
     * Sets the node as a maximizer.
     */
    public void setMaximizer() {
        maxPlayer = true;

        if (children != null) {
            for (BoardNode child : children) {
                child.setMinimizer();
            }
        }
    }
    /**
     * Sets the node as a minimizer.
     */
    public void setMinimizer() {
        maxPlayer = false;

        if (children != null) {
            for (BoardNode child : children) {
                child.setMaximizer();
            }
        }
    }
    /**
     * Checks if the node is associated with the maximizing player.
     *
     * @return True if the node is associated with the maximizing player, false otherwise.
     */
    public boolean isMaxPlayer() {
        return maxPlayer;
    }
    /**
     * Sets the score associated with this node.
     *
     * @param score The score to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * Compares this node with another node for sorting purposes.
     *
     * @param node The node to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(BoardNode node) {
        return Integer.compare(this.score, node.score);
    }
    /**
     * Generates child nodes for the current node.
     */
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
                                } else if (boardnode.getBoard().isDraw()) {
                                    boardnode.setDraw();
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

    /**
     * Gets the X-coordinate of the previous move.
     *
     * @return The X-coordinate of the previous move.
     */
    public int getPrevX() {
        return PrevX;
    }
    /**
     * Gets the Y-coordinate of the previous move.
     *
     * @return The Y-coordinate of the previous move.
     */
    public int getPrevY() {
        return PrevY;
    }
    /**
     * Gets the piece played in the previous move.
     *
     * @return The piece played in the previous move.
     */
    public Piece getPrevPiece() {
        return PrevPiece;
    }
    /**
     * Sets the draw flag to indicate a draw in the current node.
     */
    private void setDraw() {
        draw = true;
    }
}
