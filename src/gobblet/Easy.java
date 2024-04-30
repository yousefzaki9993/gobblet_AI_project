package gobblet;

public class Easy implements ScoreEval {

    private Board board ;
    boolean isBlack ;

    public Easy(Board board){
        this.board=board;
        this.isBlack=false;
    }

    @Override
    public int evaluateBoard() {
    
        int score = 0;

        // Evaluate rows
        for (int row = 0; row < Board.length; row++) {
            score += evaluateLine(board,row, 0, 0, 1, isBlack);
        }

        // Evaluate columns
        for (int col = 0; col < Board.length; col++) {
            score += evaluateLine(board,0, col, 1, 0, isBlack);
        }

        // Evaluate diagonals
        score += evaluateLine(board,0, 0, 1, 1, isBlack);
        score += evaluateLine(board,0, Board.length - 1, 1, -1, isBlack);

        return score;
    }

    private int evaluateLine(Board board,int startX, int startY, int deltaX, int deltaY, boolean isBlack) {
        int score = 0;
        int count = 0;
        int opponentCount = 0;

        for (int i = 0; i < Board.length; i++) {
            int x = startX + i * deltaX;
            int y = startY + i * deltaY;
            Piece piece = board.getPiece(x, y);


            if (piece == null) {
                count = 0;
                opponentCount = 0;
            } else if (piece.isBlack() == isBlack) {
                count++;
                score += Math.pow(10, count); // Favor longer sequences
            } else {
                opponentCount++;
                score -= Math.pow(10, opponentCount); // Penalize opponent's pieces in the sequence
            }

            // Check for winning sequence
            if (piece != null && piece.isPartOfWinningSequence(board, deltaX, deltaY, isBlack)) {
                score += 1000; // Bonus for potential winning move
            }
        }

        return score;
    }
    
}
