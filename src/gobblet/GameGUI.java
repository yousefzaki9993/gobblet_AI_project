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
    private static final int R = WIDTH / 16;
    private static GameSystem gs;
    private static boolean isBlackTurn = false;
    private static boolean picked = false;
    private static boolean run  = false;
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

    public static void start(){
        run = true;
        gs = new GameSystem(new player(false), new player(true));
        
    }
    public static void pause(){
        run = false;
    }
    public static void resume(){
        run = true;
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
                if(!run) return;
                Point p = e.getPoint();
                int x = p.x;
                int y = p.y;
                int x_index = (int)Math.floor((x - POSX) / (WIDTH / 8.0));
                int y_index = (int)Math.floor((y - POSY) / (HEIGHT / 8.0)); //to account for integer approximation

                if((x_index > 3||x_index < 0 ||y_index > 4||y_index < -1)  && picked){//unpick
                    picked = !gs.unpick(isBlackTurn);
                }else if((y_index == 4||y_index == -1)  && picked){//unpick & repick
                    picked = !gs.unpick(isBlackTurn);
                    picked = gs.pick(isBlackTurn, x_index, y_index);
                }
                else if (!picked) {
                    picked = gs.pick(isBlackTurn, x_index, y_index);

                } else {
                    picked = !gs.move(isBlackTurn, x_index, y_index);

                }

                repaint();
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

    static void paintPieces(Graphics g) throws IOException {
        if(!run) return;
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
