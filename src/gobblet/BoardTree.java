package gobblet;

public class BoardTree {

    private final BoardNode root;
    private final ScoreEval ev;
    private int destX;
    private int destY;
    private Piece PieceToMove;
    private int bestScore;

    public BoardTree(Board board, ScoreEval ev, boolean isBlackTurn) {
        this.root = new BoardNode(board, null, 0, 0);
        if (isBlackTurn) {
            this.root.setMinimizer();
        } else {
            this.root.setMaximizer();
        }
        this.ev = ev;
    }

    public void evaluate(int depth) {

        root.evaluate(depth, ev, null, null, Integer.MAX_VALUE);
        destX = root.getChildren().get(0).getPrevX();
        destY = root.getChildren().get(0).getPrevY();
        PieceToMove = root.getChildren().get(0).getPrevPiece();
        System.out.println("");
        for (BoardNode q : root.getChildren()) {
            System.out.print(", " + q.getScore());
        }
        System.out.println("");
        bestScore = root.getScore();
    }

    public int getBestScore() {
        return bestScore;
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
