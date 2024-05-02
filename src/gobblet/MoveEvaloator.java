/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gobblet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.Timer;
import java.util.TimerTask;

public class MoveEvaloator {

  /**
 * Class responsible for evaluating moves on the chess board.
 */
public class MoveEvaluator {
    
    // Private instance variables
    private int destX;
    private int destY;
    private Piece pieceToMove;
    private Board board;

    /**
     * Constructor for MoveEvaluator.
     *
     * @param board The chess board on which moves are evaluated.
     */
    public MoveEvaluator(Board board) {
        this.board = board;
    }

    /**
     * Evaluate a move based on the given parameters.
     *
     * @param isBlack Indicates whether the player making the move is playing with black pieces.
     * @param time The time available for evaluating the move.
     * @param scoreEval The object used for scoring the evaluated move.
     */
    public void evaluateMove(boolean isBlack, float time, ScoreEval scoreEval) {
        Thread t = new Thread(() -> {
            try {

                BoardNode root = new BoardNode(board, null, 0, 0);
                BoardTree tree = new BoardTree(root);
                tree.deleteBoards = false;
                if (isBlack) {
                    root.setMinimizer();
                } else {
                    root.setMaximizer();
                }
                tree.generateTree(Math.round(1), root);

                List<BoardTree> subTrees = new ArrayList<>();

                List<BoardNode> children = root.getChildren();
                int i = 0; // i = depth - 1
                while(true) { 
                    i++;
                    for (BoardNode bn : children) {

                        BoardTree subtree = new BoardTree(bn);

                        subTrees.add(subtree);
                        if (!isBlack) {
                            bn.setMinimizer();
                        } else {
                            bn.setMaximizer();
                        }

                        subtree.generateTree(Math.round(i), bn);
                        subtree.AlphaBetaPruning();
                    }

                    Integer[] indices = IntStream.range(0, children.size()).boxed().toArray(Integer[]::new);
                    Arrays.sort(indices, Comparator.comparingInt(k -> subTrees.get(k).moveScore));
                    List<BoardNode> sortedChildren = new ArrayList<>();

                    for (int j = 0; j < children.size(); j++) {
                        sortedChildren.add(children.get(indices[j]));
                    }

                    children = sortedChildren;
                    subTrees.sort(Comparator.comparingInt(bd -> bd.moveScore));
                    int from, to;
                    if (children.size() > 10) {
                        if (!isBlack) {
                            from = 0;
                            to = children.size() * 2 / 3;

                        } else {
                            from = children.size() * 1 / 3;
                            to = children.size();
                        }
                        children.subList(from, to).clear();
                        subTrees.subList(from, to).clear();
                    }
                    
                    destX = children.get(0).PrevX;
                    destY = children.get(0).PrevY;
                    PieceToMove = children.get(0).PrevPiece;
                    System.out.println("max depth = "+ (i+1));
                }
                
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MoveEvaloator.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        t.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                t.stop();
                System.out.println("-----");
            }
        }, (long) (time*1000));  // Stop after 5 seconds
       
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MoveEvaloator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the X-coordinate of the destination for the move.
     *
     * @return The X-coordinate of the destination.
     */
    public int getDestX() {
        return destX;
    }

    /**
     * Get the Y-coordinate of the destination for the move.
     *
     * @return The Y-coordinate of the destination.
     */
    public int getDestY() {
        return destY;
    }

    /**
     * Get the chess piece that is to be moved.
     *
     * @return The chess piece to be moved.
     */
    public Piece getPieceToMove() {
        return pieceToMove;
    }

}
