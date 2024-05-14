/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gobblet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.Timer;
import java.util.TimerTask;

public class MoveEvaloator {

    private int destX;
    private int destY;
    private Piece PieceToMove;
    private Board board;

    public MoveEvaloator(Board board) {
        this.board = board;
    }

    public void Eval(boolean isBlack, float time, ScoreEval ev,int limit) {
        Thread t = new Thread(() -> {
            BoardTree tree = new BoardTree(board, ev, isBlack);
            
            for(int i = 1; i <= limit ; i++){
                System.out.println(isBlack ? "BLACK:" : "WHITE:");

                tree.evaluate(i);

                System.out.println(tree.getBestScore());
                destX = tree.getDestX();
                destY = tree.getDestY();
                PieceToMove = tree.getPieceToMove();
                System.out.println("max depth = " + i);
                if (tree.getBestScore() == 10000 || tree.getBestScore() == -10000) {
                    break;
                }
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
        }, (long) (time * 1000));  // Stop after 5 seconds

        try {
            t.join();
        } catch (InterruptedException ex) {
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
