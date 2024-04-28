package gobblet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JPanel;

public class GameGUI extends JPanel {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int POSX = WIDTH / 4;
    private static final int POSY = HEIGHT / 4;
    private static final int R = WIDTH / 16 - 4;
    private static GameSystem gs;
    private static boolean isBlackTurn = false;
    private static boolean picked = false;
    private static boolean run = false;
    private static String feedback = null;
    private static boolean GO;

    public static class player implements Observer {

        private final boolean isBlack;

        public player(boolean isBlack) {
            this.isBlack = isBlack;
        }

        @Override
        public boolean isBlack() {
            return isBlack;
        }

        @Override
        public void startRole() {
            isBlackTurn = isBlack;
        }

        @Override
        public void endGame() {
            if (picked) {
                feedback = "GAME OVER! A PLAYER MOVED AN UNMOVABLE PIECE!!";
            } else {
                feedback = "GAME OVER! A PLAYER MADE A LINE OF 4!!";
            }

            picked = false;
            GO = true;

        }

    }

    @Override
    public void paint(Graphics g) {

        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.black);
        g.fillRect(POSX - 20, POSY - 20, GameGUI.WIDTH / 2 + 40, GameGUI.HEIGHT / 2 + 40);
        try {
            g.drawImage(ImageIO.read(new File("assets/board.png")), POSX, POSY, GameGUI.WIDTH / 2, GameGUI.HEIGHT / 2, null);
            paintPieces(g);
        } catch (IOException ex) {
            Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public enum playerType {
        USER, CPU_EASY, CPU_MID, CPU_HARD
    }

    public static void start(playerType pt1, playerType pt2) {
        picked = false;
        GO = false;
        run = true;
        isBlackTurn = false;
        Observer p1, p2;
        switch (pt1) {
            case USER:
                p1 = new player(false);
                break;
            case CPU_EASY:
                p1 = new player(false);
                break;
            case CPU_MID:
                p1 = new player(false);
                break;

            case CPU_HARD:
                p1 = new player(false);
                break;
            default:
                throw new AssertionError();
        }
        switch (pt2) {
            case USER:
                p2 = new player(true);
                break;
            case CPU_EASY:
                p2 = new player(true);
                break;
            case CPU_MID:
                p2 = new player(true);
                break;

            case CPU_HARD:
                p2 = new player(true);
                break;
            default:
                throw new AssertionError();
        }
        gs = new GameSystem(p1, p2);

    }

    public static void pause() {
        run = false;
    }

    public static void resume() {
        run = true;
    }

    public static void stop() {
        gs = null;
        run = false;
        isBlackTurn = false;
        picked = false;
    }

    public static boolean isBlackTurn() {
        return GO ? gs.getWinner() : isBlackTurn;
    }

    public GameGUI() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Not needed
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!run) {
                    return;
                }
                Point p = e.getPoint();
                int x = p.x;
                int y = p.y;
                int x_index = (int) Math.floor((x - POSX) / (WIDTH / 8.0));
                int y_index = (int) Math.floor((y - POSY) / (HEIGHT / 8.0)); //to account for integer approximation

                if ((x_index > 3 || x_index < 0 || y_index > 4 || y_index < -1) && picked) {//unpick
                    picked = !gs.unpick(isBlackTurn);
                    if (!picked) {
                        feedback = "Piece Unpicked!";
                    } else {
                        feedback = gs.getBoardLastIllegalNote();
                    }
                } else if ((y_index == 4 || y_index == -1) && picked) {//unpick & repick
                    picked = !gs.unpick(isBlackTurn);
                    picked = gs.pick(isBlackTurn, x_index, y_index);
                    if (picked) {
                        feedback = "Piece Repicked!";
                    } else {
                        feedback = gs.getBoardLastIllegalNote();
                    }
                } else if (!picked) {//pick
                    picked = gs.pick(isBlackTurn, x_index, y_index);
                    if (picked) {
                        feedback = "Piece picked!";
                    } else {
                        feedback = "Cannot pick other player piece!";
                    }
                } else {
                    picked = !gs.move(isBlackTurn, x_index, y_index);
                    if (!picked) {
                        feedback = "Move Accepted!";
                    } else {
                        feedback = gs.getBoardLastIllegalNote();
                    }
                }

                if (run) {
                    repaint();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Not needed
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Not needed
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Not needed
            }

        }));

    }
    //end of main

    //Helper Functions
    static void drawCircleByCenter(Graphics g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public static void paintPieces(Graphics g) throws IOException {
        if (!run) {
            return;
        }
        Board board = gs.getBoardCopy();
        List<Piece> pieces = board.getMovables();
        for (Piece p : pieces) {

            if (p.isPicked()) {
                Piece under = board.getPiece(p.getX(), p.getY());
                if (under != null) {
                    paintPiece(g, under, p.getX(), p.getY());

                }
                paintPiece(g, p, 5, -2);
                continue;

            }
            paintPiece(g, p, p.getX(), p.getY());

        }
    }

    public static void paintPiece(Graphics g, Piece p, int x, int y) throws IOException {
        int x_pos = x * (WIDTH / 8) + POSX + 4;
        int y_pos = y * (HEIGHT / 8) + POSY + 4;
        if (y == -1) {
            y_pos = HEIGHT / 10;
        }
        if (y == 4) {
            y_pos = 9 * HEIGHT / 10 - 2 * R;
        }
        String color;
        if (p.isBlack()) {
            color = "black";
        } else {
            color = "white";
        }
        switch (p.getSize()) {
            case 4 -> {

                g.drawImage(ImageIO.read(new File("assets/extra large " + color + ".png")), x_pos, y_pos, 2 * R, 2 * R, null);
            }
            case 3 -> {

                g.drawImage(ImageIO.read(new File("assets/large " + color + ".png")), x_pos, y_pos, 2 * R, 2 * R, null);
            }
            case 2 -> {

                g.drawImage(ImageIO.read(new File("assets/medium " + color + ".png")), x_pos, y_pos, 2 * R, 2 * R, null);
            }
            case 1 -> {

                g.drawImage(ImageIO.read(new File("assets/small " + color + ".png")), x_pos, y_pos, 2 * R, 2 * R, null);
            }
            default -> {
            }
        }

    }

    public static String getFeedback() {
        return feedback;
    }

    public static void addNeutralObserver(Observer o) {
        gs.addNeutralObserver(o);
    }
}
