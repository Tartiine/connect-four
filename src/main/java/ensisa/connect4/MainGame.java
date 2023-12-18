package ensisa.connect4;

import java.util.ArrayList;
import java.util.List;

public class MainGame{
    private int rows = 6;
    private int cols = 7;
    private int[][] board; // 0 = vide, 1 = joueur 1, 2 = joueur 2
    private int currentPlayer;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer % 2) + 1;
    }

    public MainGame() {
        board = new int[rows][cols];
        currentPlayer = 1; 
    }

    public int placeToken(int col) {
        for (int i = rows - 1; i >= 0; i--) {
            if(board[i][col] == 0){
                board[i][col] = currentPlayer;
                return i;
            }
        }
        return -1;
    }


    public boolean checkForWin() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (board[row][col] != 0 && 
                    board[row][col] == board[row][col + 1] &&
                    board[row][col] == board[row][col + 2] &&
                    board[row][col] == board[row][col + 3]) {
                    return true;
                }
            }
        }
    
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] != 0 && 
                    board[row][col] == board[row + 1][col] &&
                    board[row][col] == board[row + 2][col] &&
                    board[row][col] == board[row + 3][col]) {
                    return true;
                }
            }
        }
    
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (board[row][col] != 0 && 
                    board[row][col] == board[row + 1][col + 1] &&
                    board[row][col] == board[row + 2][col + 2] &&
                    board[row][col] == board[row + 3][col + 3]) {
                    return true;
                }
            }
        }
    
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 3; col < cols; col++) {
                if (board[row][col] != 0 && 
                    board[row][col] == board[row + 1][col - 1] &&
                    board[row][col] == board[row + 2][col - 2] &&
                    board[row][col] == board[row + 3][col - 3]) {
                    return true;
                }
            }
        }
    
        return false;
    }

    public boolean isDraw() {
        for (int row = 0; row < rows; row ++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] == 0){
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isColumnFull(int col) {
        return board[0][col] != 0;
    }

    public int decideAITurn() {
        List<Integer> availableColumns = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            if (!isColumnFull(i)) {
                availableColumns.add(i);
            }
        }

        if (availableColumns.isEmpty()) {
            return -1;
        }

        int[][] boardCopy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            boardCopy[i] = board[i].clone();
        }

        AI ai = new AI(this, 3, true); // Replace with the desired depth and pruning flag
        int aiColumn = ai.minimax(boardCopy, ai.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE, true)[0];
        return aiColumn;
    }
}
