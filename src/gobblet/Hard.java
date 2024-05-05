package gobblet;
import java.util.HashMap;

public class Hard implements ScoreEval {

    public static final int WHITE_PLAYER = 0;
    public static final int BLACK = 4;
    public static final int BOARD_SIZE = 4;
    public static final int PIECES_COUNT = 4;
    public static final int[] PIECES = {1, 2, 4, 8};
    public static final int DUMMY = -1;
    protected HashMap<Long, Integer> positionsCounter = new HashMap<Long, Integer>();
    private Board board;

    
        /**
         * Creates a map of player positions on the board.
         *
         * @return The map of player positions.
         */
        protected final int[][] getPlayerMap() {
            int[][] result = new int[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    Piece piece = board.getPiece(i, j);
                    int player = DUMMY;
                    if (piece != null) {
                        player = (piece.isBlack()) ? BLACK : WHITE_PLAYER;
                    }
                    result[i][j] = player;
                }
            }
            return result;
        }
    
        /**
         * Evaluates the score of a tile on the board at position (x, y).
         *
         * @param x The x-coordinate of the tile.
         * @param y The y-coordinate of the tile.
         * @return The score of the tile.
         */
        protected final int evaluateTile(int x, int y) {
            int white = 0;
            int black = 0;
    
            int weakTile = (((x - y) == 0) || ((x + y) == BOARD_SIZE - 1)) ? 2 : 1;
    
            Piece piece = board.getPiece(x, y);
            if (piece != null) {
                int wTile = (piece.isBlack()) ? 0 : 1;
                int bTile = (piece.isBlack()) ? 1 : 0;
    
                int stones = 0;
    
                for (int i = PIECES_COUNT - 1; i >= 0; i--) {
                    white += ((wTile & PIECES[i]) >> i) * (i + 1) * (1 - stones / PIECES_COUNT);
                    black += ((bTile & PIECES[i]) >> i) * (i + 1) * (1 - stones / PIECES_COUNT);
                    stones += ((wTile & PIECES[i]) >> i) + ((bTile & PIECES[i]) >> i);
                }
            }
    
            return (white - black) * weakTile;
        }
    
        @Override
        public final int evaluateBoard(Board board) {
            this.board = board;

            int[][] groupsMap = getBonusMap();
    
            int result = 0;
    
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    result += groupsMap[i][j] * evaluateTile(i, j);
                }
            }
    
            return result;
        }
        /**
         * Generates a map representing the groups on the game board based on rows, columns, and diagonals.
         * Each cell in the map contains the cumulative bonus points assigned to the corresponding group.
         *
         * @return A 2D array representing the totalBonus.
         */
        protected final int[][] getBonusMap() {
            // Initialize 2D arrays to store cumulative bonuses for row, column, and diagonal groups.
            int[][] totalBonus = new int[BOARD_SIZE][BOARD_SIZE];
            int[][] rowBonus = new int[BOARD_SIZE][BOARD_SIZE];
            int[][] colBonus = new int[BOARD_SIZE][BOARD_SIZE];
            int[][] diagonalBonus = new int[BOARD_SIZE][BOARD_SIZE];
            
            // Get the current player positions on the board.
            int[][] playerMap = getPlayerMap();
        
            // Iterate through each cell on the board.
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    // Skip cells with no player piece.
                    if (playerMap[i][j] == DUMMY) continue;
        
                    // Check and update groups in rows.
                    if (rowBonus[i][j] == 0) {
                        int size = 0;
                        // Count consecutive pieces in the same row.
                        for (int l = j; l < BOARD_SIZE; l++) {
                            if ((playerMap[i][j] == playerMap[i][l])) {
                                size++;
                            } else break;
                        }
                        // Calculate bonus based on the group length.
                        int bonus = mapGroupLengthToBonus(size);
                        // Update bonuses for each cell in the group.
                        for (int l = j; l < j + size; l++) {
                            rowBonus[i][l] += bonus;
                        }
                    }
        
                    // Check and update groups in columns.
                    if (colBonus[i][j] == 0) {
                        int size = 0;
                        // Count consecutive pieces in the same column.
                        for (int l = i; l < BOARD_SIZE; l++) {
                            if ((playerMap[i][j] == playerMap[l][j])) {
                                size++;
                            } else break;
                        }
                        // Calculate bonus based on the group length.
                        int bonus = mapGroupLengthToBonus(size);
                        // Update bonuses for each cell in the group.
                        for (int l = i; l < i + size; l++) {
                            colBonus[l][j] += bonus;
                        }
                    }
        
                    // Check and update groups for the diagonals.
                    if ((diagonalBonus[i][j] == 0) && (((i - j) == 0) || ((i + j) == BOARD_SIZE - 1))) {
                        int size = 0;
                        if ((i - j) == 0) {
                            // Count consecutive pieces in the main diagonal.
                            for (int l = i; l < BOARD_SIZE; l++) {
                                if ((playerMap[i][j] == playerMap[l][l])) {
                                    size++;
                                } else break;
                            }
                        } else { // (i+j) == BOARD_SIZE-1)
                            // Count consecutive pieces in the anti-diagonal.
                            for (int l = i; l < BOARD_SIZE; l++) {
                                if ((playerMap[i][j] == playerMap[l][BOARD_SIZE - 1 - l])) {
                                    size++;
                                } else break;
                            }
                        }
                        // Calculate bonus based on the group length.
                        int bonus = mapGroupLengthToBonus(size);
                        // Update bonuses for each cell in the group.
                        for (int l = i; l < i + size; l++) {
                            if ((i - j) == 0) {
                                diagonalBonus[l][l] += bonus;
                            } else {
                                diagonalBonus[l][BOARD_SIZE - 1 - l] += bonus;
                            }
                        }
                    }
        
                    // Sum the bonuses from row, column, and diagonal groups for the current cell.
                    totalBonus[i][j] = rowBonus[i][j] + colBonus[i][j] + diagonalBonus[i][j];
                }
            }
        
            // Return the final map of groups with cumulative bonuses.
            return totalBonus;
        }
    
        /**
         * Maps the length of a group to a bonus value.
         *
         * @param length The length of the group.
         * @return The corresponding bonus value.
         */
        protected final int mapGroupLengthToBonus(int length) {
            switch (length) {
                case 1:
                    return 1;
                case 2:
                    return 10;
                case 3:
                    return 20;
                case 4:
                    return 1000;
                default:
                    throw new IllegalArgumentException("Illegal Group Length:" + length);
            }
        }
    }
    