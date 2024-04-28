package gobblet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a tree structure to explore possible moves on a game board.
 */
public class BoardTree {

    /**
     * The root node of the board tree.
     */
    public BoardNode rootNode;

    /**
     * The destination X coordinate for a move.
     */
    private int destX;

    /**
     * The destination Y coordinate for a move.
     */
    private int destY;

    /**
     * The piece to be moved in a particular move.
     */
    private Piece pieceToMove;

    /**
     * Initializes a new instance of the BoardTree class.
     *
     * @param rootNode The root node of the board tree.
     */
    public BoardTree(BoardNode rootNode) {
        this.rootNode = rootNode;
    }

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
    public void generateTree(int depth, BoardNode originalRootNode) throws CloneNotSupportedException {
        if (depth == 0) {
            Easy easy = new Easy();
            originalRootNode.setScore(easy.evaluateBoard(originalRootNode.getBoard(), originalRootNode.isMaxPlayer()));
            return;
        }

        Board board = rootNode.getBoard();
        List<Piece> movables = board.getMovables();

        for (int i = 0; i < movables.size(); i++) {
            Piece piece = movables.get(i);
            if ((rootNode.isMaxPlayer() && piece.isBlack()) || (!rootNode.isMaxPlayer() && !piece.isBlack())) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        if (board.isLegal(piece, j, k)) {
                            Board cloneBoard = board.clone();
                            cloneBoard.move(cloneBoard.getPiece(piece.getX(), piece.getY()), j, k);
                            BoardNode boardNode = new BoardNode(cloneBoard, piece, j, k);
                            if (rootNode.isMaxPlayer()) boardNode.setMinimizer();
                            else boardNode.setMaximizer();
                            rootNode.addChild(boardNode);

                            this.rootNode = boardNode;
                            this.generateTree(depth - 1, boardNode);
                            this.rootNode = originalRootNode;
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs MinMax algorithm on a list of board nodes and sets the scores accordingly.
     *
     * @param childrenNodes The list of child nodes to apply MinMax on.
     * @return The final score after applying MinMax.
     */
    public int MinMax(List<BoardNode> childrenNodes) {
        int score = 0;
        BoardNode checkNode = childrenNodes.get(0);
        if (checkNode.isMaxPlayer()) {
            for (int i = 0; i < childrenNodes.size(); i++) {
                BoardNode currentNode = childrenNodes.get(i);
                if (currentNode.getChildren().get(0).getChildren().isEmpty()) {
                    score = getMaxScoreNode(currentNode.getChildren()).getScore();
                } else {
                    MinMax(currentNode.getChildren());
                    score = getMaxScoreNode(currentNode.getChildren()).getScore();
                }
                currentNode.setScore(score);
            }
            return score;
        } else {
            for (int i = 0; i < childrenNodes.size(); i++) {
                BoardNode currentNode = childrenNodes.get(i);
                if (currentNode.getChildren().get(0).getChildren().isEmpty()) {
                    score = getMinScoreNode(currentNode.getChildren()).getScore();
                } else {
                    MinMax(currentNode.getChildren());
                    score = getMinScoreNode(currentNode.getChildren()).getScore();
                }
                currentNode.setScore(score);
            }
            return score;
        }
    }

    // Helper functions

    /**
     * Gets the board node with the minimum score from a list of board nodes.
     *
     * @param childrenList The list of board nodes.
     * @return The board node with the minimum score.
     */
    public BoardNode getMinScoreNode(List<BoardNode> childrenList) {
        return childrenList.stream().min(Comparator.comparingInt(BoardNode::getScore)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Gets the board node with the maximum score from a list of board nodes.
     *
     * @param childrenList The list of board nodes.
     * @return The board node with the maximum score.
     */
    public BoardNode getMaxScoreNode(List<BoardNode> childrenList) {
        return childrenList.stream().max(Comparator.comparingInt(BoardNode::getScore)).orElseThrow(NoSuchElementException::new);
    }
}
