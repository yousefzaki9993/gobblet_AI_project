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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameGUI {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int POSX = WIDTH / 4;
    private static final int POSY = HEIGHT / 4;
    private static final int R = WIDTH / 16;
    private static GameSystem gs;
    private static boolean isBlackTurn = false;
    private static boolean picked = false;

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

    public static void main(String[] args) {
        gs = new GameSystem(new player(false), new player(true));
        JFrame frame = new JFrame();
        
        frame.setResizable(false);
        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.black);
                g.fillRect(POSX - 20, POSY - 20, GameGUI.WIDTH / 2 + 40, GameGUI.HEIGHT / 2 + 40);
                try {
                    g.drawImage(ImageIO.read(new File("assets/board.png")), POSX, POSY, GameGUI.WIDTH / 2, GameGUI.HEIGHT / 2, null);
                    paintPieces(g);
                } catch (IOException ex) {
                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        pn.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane().add(pn);

        frame.pack();
        frame.setLocationRelativeTo(null);
        pn.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Not needed
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int x_index = (p.x - POSX) / (WIDTH / 8);
                int y_index = (p.y - POSY) < 0 ? -1 : ((p.y - POSY) / (HEIGHT / 8) > 3 ? 4 : (e.getY() - POSY) / (HEIGHT / 8)); //to account for integer approximation
                System.out.println(x_index + "," + y_index);

                if (!picked) {
                    picked = gs.pick(isBlackTurn, x_index, y_index);

                } else {
                    picked = !gs.move(isBlackTurn, x_index, y_index);

                }

                frame.repaint();
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


        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
    //end of main

    //Helper Functions
    static void drawCircleByCenter(Graphics g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    static void paintPieces(Graphics g) throws IOException {

        List<Piece> pieces = gs.getBoardCopy().getMovables();
        for (Piece p : pieces) {
            int x_pos = p.getX() * (WIDTH / 8) + POSX;
            int y_pos = p.getY() * (HEIGHT / 8) + POSY;
            if (p.getY() < 0) {
                y_pos = HEIGHT / 10;
            }
            if (p.getY() == 4) {
                y_pos = 9 * HEIGHT / 10 - 2 * R;
            }

            if (p.isPicked()) {
                g.setColor(Color.green);

                drawCircleByCenter(g, x_pos + R, y_pos + R, 35);
                if (p.getSize() == 1) {
                    drawCircleByCenter(g, x_pos + R, y_pos + R, 20);
                }
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
    }

}
