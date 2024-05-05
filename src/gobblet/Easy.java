package gobblet;

/**
 * Represents an easy-level score evaluation for the Gobblet game.
 */
public class Easy implements ScoreEval {

    private Board board;
    private boolean isBlack;

    /**
     * Evaluates the given game board using a simplified scoring mechanism for an easy-level AI.
     *
     * @param board The game board to be evaluated.
     * @return The calculated score based on the evaluation.
     */
    @Override
    public int evaluateBoard(Board board) {
        this.board = board;
        this.isBlack = false; // Indicates the color of the player being evaluated.
        int score = 0;

        // Evaluate rows
        for (int row = 0; row < Board.length; row++) {
            score += evaluateLine(this.board, row, 0, 0, 1, isBlack);
        }

        // Evaluate columns
        for (int col = 0; col < Board.length; col++) {
            score += evaluateLine(this.board, 0, col, 1, 0, isBlack);
        }

        // Evaluate diagonals
        score += evaluateLine(this.board, 0, 0, 1, 1, isBlack);
        score += evaluateLine(this.board, 0, Board.length - 1, 1, -1, isBlack);

        return score;
    }

    /**
     * Evaluates a line (row, column, or diagonal) on the game board based on a given starting position and direction.
     *
     * @param board    The game board.
     * @param startX   The starting x-coordinate of the line.
     * @param startY   The starting y-coordinate of the line.
     * @param deltaX   The change in x-coordinate per step.
     * @param deltaY   The change in y-coordinate per step.
     * @param isBlack  Indicates if the evaluation is for the black player.
     * @return The calculated score for the evaluated line.
     */
    private int evaluateLine(Board board, int startX, int startY, int deltaX, int deltaY, boolean isBlack) {
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
