/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gobblet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoveEvaloator {

    private int destX;
    private int destY;
    private Piece PieceToMove;
    private Board board;

    public MoveEvaloator(Board board) {
        this.board = board;
    }

    public void Eval(boolean isBlack, float time, ScoreEval ev) {
        try {
            BoardNode root = new BoardNode(board, null, 0, 0);
            BoardTree tree = new BoardTree(root);
            if (isBlack) {
                root.setMinimizer();
            } else {
                root.setMaximizer();
            }
            tree.generateTree(Math.round(2 * time), root);
            
            tree.AlphaBetaPruning();
            destY = tree.destY;
            destX = tree.destX;
            PieceToMove = tree.PieceToMove;
           
            
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MoveEvaloator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    public Piece getPieceToMove() {
        return PieceToMove;
    }
}