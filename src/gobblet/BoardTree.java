package gobblet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * Represents a tree structure to explore possible moves on a game board with Alpha-Beta Pruning.
 */
public class BoardTree {
// Fields

    /**
     * The root node of the board tree.
     */
    public BoardNode rootNode;
        /**
     * The destination X coordinate for a move.
     */
    public int destX;
       /**
     * The destination Y coordinate for a move.
     */
    public int destY;

    /**
     * The piece to be moved in a particular move.
     */
    public Piece PieceToMove;
       /**
     * The score of the move.
     */
    public int moveScore;
        /**
     * Flag indicating whether to delete boards after generating the tree.
     */
    public boolean deleteBoards = true;
    /**
     * score evaluator object.
     */
    public ScoreEval ev;
    // Constructor

    /**
     * Initializes a new instance of the BoardTree class.
     *
     * @param rootNode The root node of the board tree.
     */
    public BoardTree(BoardNode rootNode, ScoreEval ev) {
        this.rootNode = rootNode;
        this.ev = ev;
    }
        // Public Methods

    /**
     * Gets the root node of the board tree.
     *
     * @return The root node of the board tree.
     */
    public BoardNode getValue() {
        return rootNode;
    }
        /**
     * Generates a tree structure exploring possible moves on the board up to a specified depth.
     *
     * @param depth             The depth to explore in the tree.
     * @param originalRootNode  The original root node before any modifications.
     * @throws CloneNotSupportedException Thrown if cloning of the board is not supported.
     */
    public void generateTree(int depth, BoardNode originalRootNode) throws CloneNotSupportedException { //function for generating tree of possible moves
        if (depth == 0) {
            originalRootNode.setScore(ev.evaluateBoard(originalRootNode.getBoard()));
            if(deleteBoards)rootNode.deleteBoard();
            return;
        }

        Board board = rootNode.getBoard();
        List<Piece> movables = board.getMovables(); //gets list of pieces that can be moved legally

        for (int i = 0; i < movables.size(); i++) {
            Piece piece = movables.get(i);
            if ((!rootNode.isMaxPlayer() && piece.isBlack()) || (rootNode.isMaxPlayer() && !piece.isBlack())) { //if the piece fits the right turn
                for (int j = 0; j < 4; j++) { //iterate through every slot in the board
                    for (int k = 0; k < 4; k++) {
                        if (board.isLegal(piece, j, k)) {
                            Board cloneBoard = board.clone();
                            //Make a move in the cloned board
                            cloneBoard.move(cloneBoard.getPiece(piece.getX(), piece.getY()), j, k);
                            //Add the cloned board to a board node
                            BoardNode boardnode = new BoardNode(cloneBoard, piece, j, k);
                            //set maximizer or minimizer
                            if (rootNode.isMaxPlayer()) {
                                boardnode.setMinimizer();
                            } else {
                                boardnode.setMaximizer();
                            }
                            //add the board node to the root node
                            rootNode.addChild(boardnode);
                            
                            //if a board node satisfy winning condition, make it a leaf node and give it max/min score
                            Piece[] line = boardnode.getBoard().getWinningLine();
                            if(line != null) {
                            	int score = line[0].isBlack() ? -10000:10000;
                            	boardnode.setScore(score);
                            	continue;
                            }
                            
                            //change the rootNode for tree generation
                            this.rootNode = boardnode;
                            this.generateTree(depth - 1, boardnode);
                            
                            if(deleteBoards)rootNode.deleteBoard(); //setting board to null after generating children to save memory
                            this.rootNode = originalRootNode;
                        }
                    }
                }
            }
        }
    }
        /**
     * Applies Alpha-Beta Pruning to find the best move and updates destination coordinates and piece to move.
     */
    public void AlphaBetaPruning() {
    	
        moveScore = alphaBeta(rootNode,-1000000,1000000,rootNode.isMaxPlayer()); 
        BoardNode MinMaxNode;
        if (rootNode.isMaxPlayer()) { //returns the node that represents the best next move
            MinMaxNode = getMaxScoreNode(rootNode.getChildren());
        } else {
            MinMaxNode = getMinScoreNode(rootNode.getChildren());
        }
        //sets variables for what piece should be moved to what x and y
        this.destX = MinMaxNode.PrevX;
        this.destY = MinMaxNode.PrevY;
        this.PieceToMove = MinMaxNode.PrevPiece;
    }
        // Static Methods

    /**
     * Applies Alpha-Beta Pruning algorithm to evaluate the best move score.
     *
     * @param node              The current node in the tree.
     * @param alpha             The best score that the maximizing player is assured of.
     * @param beta              The best score that the minimizing player is assured of.
     * @param maximizingPlayer True if the current player is maximizing, false otherwise.
     * @return The best move score.
     */
    static int alphaBeta(BoardNode node,int alpha, int beta, boolean maximizingPlayer) {
    	
        if (node.getChildren().isEmpty()) { // base condition that checks if node is a leaf node
        	 return node.getScore();
        }
           
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (BoardNode child : node.getChildren()) {
                int eval = alphaBeta(child, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                
                alpha = Math.max(alpha, eval);
                
                if (beta <= alpha) {
                    break;
                }
                
            }
            node.setScore(maxEval);
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (BoardNode child : node.getChildren()) {
                int eval = alphaBeta(child, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            node.setScore(minEval);
            return minEval;
        }
    }
 
    // Helper methods

    /**
     * Gets the board node with the minimum score from a list of board nodes.
     *
     * @param childrenList The list of board nodes.
     * @return The board node with the minimum score.
     */
    public BoardNode getMinScoreNode(List<BoardNode> childrenList) { //return the node with minimum score from a list of children nodes
        BoardNode tempNode = childrenList.stream().min(Comparator.comparingInt(BoardNode::getScore)).orElseThrow(NoSuchElementException::new);
        return tempNode;
    }
        /**
     * Gets the board node with the maximum score from a list of board nodes.
     *
     * @param childrenList The list of board nodes.
     * @return The board node with the maximum score.
     */
    public BoardNode getMaxScoreNode(List<BoardNode> childrenList) {//return the node with maximum score from a list of children nodes
        BoardNode tempNode = childrenList.stream().max(Comparator.comparingInt(BoardNode::getScore)).orElseThrow(NoSuchElementException::new);
        return tempNode;
    }
    

}
