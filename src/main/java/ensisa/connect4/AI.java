package ensisa.connect4;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class AI {
    private static final int EMPTY = 0;
    private static final int ROW_COUNT = 6;
    private static final int COLUMN_COUNT = 7;
    private static final int WINDOW_LENGTH = 4;

    private final MainGame game;
    private final int depth;
    private final boolean pruning;

    public AI(MainGame game, int depth, boolean pruning) {
        this.game = game;
        this.depth = depth;
        this.pruning = pruning;
    }

    public void dropPiece(int[][] board, int row, int col, int piece) {
        board[row][col] = piece;
    }


    public int getNextOpenRow(int[][] board, int col) {
        for (int r = ROW_COUNT - 1; r >= 0; r--) {
            if (board[r][col] == 0) {
                return r;
            }
        }
        return -1;
    }


    public boolean winningMove(int[][] board, int piece) {
        // Check horizontal locations for win
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 0; r < ROW_COUNT; r++) {
                if (board[r][c] == piece && board[r][c+1] == piece && board[r][c+2] == piece && board[r][c+3] == piece) {
                    return true;
                }
            }
        }

        // Check vertical locations for win
        for (int c = 0; c < COLUMN_COUNT; c++) {
            for (int r = 0; r < ROW_COUNT - 3; r++) {
                if (board[r][c] == piece && board[r + 1][c] == piece && board[r + 2][c] == piece && board[r + 3][c] == piece) {
                    return true;
                }
            }
        }

        // Check positively sloped diagonals
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 0; r < ROW_COUNT - 3; r++) {
                if (board[r][c] == piece &&
                        board[r + 1][c + 1] == piece &&
                        board[r + 2][c + 2] == piece &&
                        board[r + 3][c + 3] == piece) {
                    return true;
                }
            }
        }

        // Check negatively sloped diagonals
        for (int c = 0; c < COLUMN_COUNT - 3; c++) {
            for (int r = 3; r < ROW_COUNT; r++) {
                if (board[r][c] == piece &&
                        board[r - 1][c + 1] == piece &&
                        board[r - 2][c + 2] == piece &&
                        board[r - 3][c + 3] == piece) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isTerminalNode(int[][] board) {
        return winningMove(board, 1) || winningMove(board, 2) || isBoardFull(board);
    }

    private boolean isBoardFull(int[][] board) {
        for (int col = 0; col < COLUMN_COUNT; col++) {
            if (!isColumnFull(board, col)) {
                return false;
            }
        }
        return true;
    }

    public int evaluateWindow(int[] window, int piece) {
        int score = 0;
        int oppPiece = (piece == 1) ? 2 : 1;
    
        int myCount = countOccurrences(window, piece);
        int oppCount = countOccurrences(window, oppPiece);
        int emptyCount = countOccurrences(window, EMPTY);
    
        if (myCount == 4) {
            score += 100;
        } else if (myCount == 3 && emptyCount == 1) {
            score += 5;
        } else if (myCount == 2 && emptyCount == 2) {
            score += 2;
        }
    
        if (oppCount == 3 && emptyCount == 1) {
            score -= 3;
        }
    
        return score;
    }

    public int immediateThreatColumn(int[][] board, int oppPiece) {
        for (int c = 0; c < COLUMN_COUNT; c++) {
            for (int r = 0; r < ROW_COUNT; r++) {
                int[] window = getWindow(board, c, r);
                if (window == null) continue; 
    
                if (countOccurrences(window, oppPiece) == 3 && countOccurrences(window, EMPTY) == 1) {
                    int emptyIndex = findEmptyIndex(window);
                    if (r + emptyIndex < ROW_COUNT && board[r + emptyIndex][c] == EMPTY) {
                        return c; 
                    }
                }
            }
        }
        return -1; 
    }
    
    private int findEmptyIndex(int[] window) {
        for (int i = 0; i < window.length; i++) {
            if (window[i] == EMPTY) {
                return i;
            }
        }
        return -1; // Return -1 if no empty spot is found
    }
    
    private int[] getWindow(int[][] board, int col, int rowStart) {
        if (col >= 0 && col < COLUMN_COUNT && rowStart >= 0 && rowStart + WINDOW_LENGTH <= ROW_COUNT) {
            int[] window = new int[WINDOW_LENGTH];
            for (int i = 0; i < WINDOW_LENGTH; i++) {
                window[i] = board[rowStart + i][col];
            }
            return window;
        }
        return null; // Return null if the window is out of bounds
    }
    

    public int getDepth() {
        return depth;
    }

    public int scorePosition(int[][] board, int piece) {
        int score = 0;

        int centerColumn = COLUMN_COUNT / 2;
        int[] centerArray = new int[ROW_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            centerArray[i] = board[i][centerColumn];
        }
        int centerCount = countOccurrences(centerArray, piece);
        score += centerCount * 3;

        // Score Horizontal
        for (int r = 0; r < ROW_COUNT; r++) {
            for (int c = 0; c < COLUMN_COUNT - WINDOW_LENGTH + 1; c++) {
                int[] window = Arrays.copyOfRange(board[c], r, r + WINDOW_LENGTH);
                score += evaluateWindow(window, piece);
            }
        }

        // Score Vertical
        for (int c = 0; c < COLUMN_COUNT; c++) {
            for (int r = 0; r < ROW_COUNT - WINDOW_LENGTH + 1; r++) {
                int[] window = Arrays.copyOfRange(board[r], c, c + WINDOW_LENGTH);
                score += evaluateWindow(window, piece);
            }
        }

        // Score positive sloped diagonal
        for (int r = 0; r < ROW_COUNT - 3; r++) {
            for (int c = 0; c < COLUMN_COUNT - 3; c++) {
                int[] window = new int[WINDOW_LENGTH];
                for (int i = 0; i < WINDOW_LENGTH; i++) {
                    window[i] = board[r + i][c + i];
                }
                score += evaluateWindow(window, piece);
            }
        }

        // Score negative sloped diagonal
        for (int r = 0; r < ROW_COUNT - 3; r++) {
            for (int c = 3; c < COLUMN_COUNT; c++) {
                int[] window = new int[WINDOW_LENGTH];
                for (int i = 0; i < WINDOW_LENGTH; i++) {
                    window[i] = board[r + i][c - i];
                }
                score += evaluateWindow(window, piece);
            }
        }


        return score;
    }


public int[] minimax(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
    List<Integer> validLocations = getValidLocations(board);
    boolean isTerminal = isTerminalNode(board);

    if (depth == 0 || isTerminal) {
        if (isTerminal) {
            if (winningMove(board, game.getCurrentPlayer())) {
                return new int[]{-1, Integer.MAX_VALUE};
            } else if (winningMove(board, (game.getCurrentPlayer() == 1) ? 2 : 1)) {
                return new int[]{-1, Integer.MIN_VALUE};
            } else {
                return new int[]{-1, 0};
            }
        } else {
            return new int[]{-1, scorePosition(board, game.getCurrentPlayer())};
        }
    }

    if (maximizingPlayer) {
        int value = Integer.MIN_VALUE;
        int column = validLocations.get(0);
        for (int col : validLocations) {
            int row = getNextOpenRow(board, col);
            int[][] bCopy = copyBoard(board);
            dropPiece(bCopy, row, col, game.getCurrentPlayer());
            int newScore = minimax(bCopy, depth - 1, alpha, beta, false)[1];
            if (newScore > value) {
                value = newScore;
                column = col;
            }
            alpha = Math.max(alpha, value);
            if (alpha >= beta) break;
        }
        return new int[]{column, value};
    } else {
        int value = Integer.MAX_VALUE;
        int column = validLocations.get(0);
        for (int col : validLocations) {
            int row = getNextOpenRow(board, col);
            int[][] bCopy = copyBoard(board);
            dropPiece(bCopy, row, col, (game.getCurrentPlayer() == 1) ? 2 : 1);
            int newScore = minimax(bCopy, depth - 1, alpha, beta, true)[1];
            if (newScore < value) {
                value = newScore;
                column = col;
            }
            beta = Math.min(beta, value);
            if (alpha >= beta) break;
        }
        return new int[]{column, value};
    }
}



    private int countOccurrences(int[] array, int value) {
        int count = 0;
        for (int elem : array) {
            if (elem == value) {
                count++;
            }
        }
        return count;
    }

    private List<Integer> getValidLocations(int[][] board) {
        List<Integer> validLocations = new ArrayList<>();
        for (int col = 0; col < COLUMN_COUNT; col++) {
            if (!isColumnFull(board, col)) {
                validLocations.add(col);
            }
        }
        return validLocations;
    }

    private boolean isColumnFull(int[][] board, int col) {
        return board[0][col] != 0;
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            copy[i] = Arrays.copyOf(original[i], COLUMN_COUNT);
        }
        return copy;
    }

}