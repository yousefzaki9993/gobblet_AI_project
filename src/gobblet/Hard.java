package gobblet;
import java.util.HashMap;

public class Hard implements ScoreEval {

    public static final int WHITE_PLAYER = 0;
    public static final int BLACK = 4;
    public static final int BOARD_SIZE = 4;
    public static final int PIECES_COUNT = 4;
    public static final int[] PIECES = {1, 2, 4, 8};
    public static final int DUMMY = -1;
    public boolean draw = false;
    protected HashMap<Long, Integer> positionsCounter = new HashMap<Long, Integer>();
    private Board board;

    public Hard(Board board) {
        this.board = board;
    }

    public long calculateSimpleHash() {
        long hash = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                hash = 31 * hash + board.getPiece(i, j).getSize(); 
            }
        }
        return hash;
    }

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


    protected final int evaluateTile(int x, int y) {
        int white = 0;
        int black = 0;

        int weak_tile = (((x - y) == 0) || ((x + y) == BOARD_SIZE - 1)) ? 2 : 1;

        Piece piece = board.getPiece(x, y);
        if (piece != null) {
            int w_tile = (piece.isBlack()) ? 0 : 1; // Contribution of the current piece to the white score
            int b_tile = (piece.isBlack()) ? 1 : 0; // Contribution of the current piece to the black score

            int stones = 0;

            for (int i = PIECES_COUNT - 1; i >= 0; i--) {
                white += ((w_tile & PIECES[i]) >> i) * (i + 1) * (1 - stones / PIECES_COUNT);
                black += ((b_tile & PIECES[i]) >> i) * (i + 1) * (1 - stones / PIECES_COUNT);
                stones += ((w_tile & PIECES[i]) >> i) + ((b_tile & PIECES[i]) >> i);
            }
        }

        return (white - black) * weak_tile;
    }

    @Override
    public final int evaluateBoard() {
        if (draw) return 0;

        int[][] groupsMap = getGroupsMap();

        int result = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                result += groupsMap[i][j] * evaluateTile(i, j);
            }
        }

        return result;
    }
    protected final int[][] getGroupsMap() {
        int[][] groupsMap = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] rowGroupsMap = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] colGroupsMap = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] diagGroupsMap = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] playerMap = getPlayerMap();
    
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (playerMap[i][j] == DUMMY) continue;
    
                // checks the groups in rows
                if (rowGroupsMap[i][j] == 0) {
                    int size = 0;
                    for (int l = j; l < BOARD_SIZE; l++) {
                        if ((playerMap[i][j] == playerMap[i][l])) {
                            size++;
                        } else break;
                    }
                    int bonus = mapGroupLengthToBonus(size);
                    for (int l = j; l < j + size; l++) {
                        rowGroupsMap[i][l] += bonus;
                    }
                }
    
                // checks the groups in columns
                if (colGroupsMap[i][j] == 0) {
                    int size = 0;
                    for (int l = i; l < BOARD_SIZE; l++) {
                        if ((playerMap[i][j] == playerMap[l][j])) {
                            size++;
                        } else break;
                    }
                    int bonus = mapGroupLengthToBonus(size);
                    for (int l = i; l < i + size; l++) {
                        colGroupsMap[l][j] += bonus;
                    }
                }
    
                // checks the groups for the diagonals
                if ((diagGroupsMap[i][j] == 0) && (((i - j) == 0) || ((i + j) == BOARD_SIZE - 1))) {
                    int size = 0;
                    if ((i - j) == 0) {
                        for (int l = i; l < BOARD_SIZE; l++) {
                            if ((playerMap[i][j] == playerMap[l][l])) {
                                size++;
                            } else break;
                        }
                        int bonus = mapGroupLengthToBonus(size);
                        for (int l = i; l < i + size; l++) {
                            diagGroupsMap[l][l] += bonus;
                        }
                    } else { // (i+j) == BOARD_SIZE-1)
                        for (int l = i; l < BOARD_SIZE; l++) {
                            if ((playerMap[i][j] == playerMap[l][BOARD_SIZE - 1 - l])) {
                                size++;
                            } else break;
                        }
                        int bonus = mapGroupLengthToBonus(size);
                        for (int l = i; l < i + size; l++) {
                            diagGroupsMap[l][BOARD_SIZE - 1 - l] += bonus;
                        }
                    }
    
                }
    
                // sums together
                groupsMap[i][j] = rowGroupsMap[i][j] + colGroupsMap[i][j] + diagGroupsMap[i][j];
            }
        }
    
        return groupsMap;
    }
    // bonus
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
