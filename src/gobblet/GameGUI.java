package gobblet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameGUI {

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
            System.out.println("GAME OVER");//will do it later
        }

    }
    private static final int WIDTH = 512;
    private static final int HIEGHT = 512;
    private static final int POSX = 50;
    private static final int POSY = 50;
    private static GameSystem gs;
    private static boolean isBlackTurn = false;
    private static boolean picked = false;

    public static void main(String[] args) {
        gs = new GameSystem(new player(false), new player(true));
        JFrame frame = new JFrame();
        frame.setBounds(POSX, POSY, WIDTH, HIEGHT);
        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.black);
                g.fillRect(80, 80, 300, 300);
                g.setColor(Color.getHSBColor(144, 144, 144));
                g.fillRect(100, 100, 260, 260);
                g.setColor(Color.RED);
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        drawCircleByCenter(g, x * 64 + 133, y * 64 + 133, 30);
                    }
                }
                paintPieces(g);
            }
        };
        frame.add(pn);

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Not needed
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
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

        });

        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

    }
    //end of main

    //Helper Functions
    static void drawCircleByCenter(Graphics g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    static void paintPieces(Graphics g) {
        List<Piece> pieces = gs.getBoardCopy().getMovables();
        for (Piece p : pieces) {
            int x_pos = p.getX() * 64 + 133;
            int y_pos = p.getY() * 64 + 133;
            if (p.getY() < 0) {
                y_pos = 40;
            }
            if (p.getY() == 4) {
                y_pos = 410;
            }

            if (p.isPicked()) {
                g.setColor(Color.green);

                drawCircleByCenter(g, x_pos, y_pos, 30);
                if (p.getSize() == 1) {
                    drawCircleByCenter(g, x_pos, y_pos, 20);
                }
            }
            if (p.isBlack()) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.magenta);
            }
            if (p.getSize() == 4) {
                drawCircleByCenter(g, x_pos, y_pos, 28);
            }
            if (p.getSize() == 3) {
                drawCircleByCenter(g, x_pos, y_pos, 25);
            }
            if (p.getSize() == 2) {
                drawCircleByCenter(g, x_pos, y_pos, 22);
            }
            if (p.getSize() == 1) {
                drawCircleByCenter(g, x_pos, y_pos, 19);
            }

        }
    }

}
