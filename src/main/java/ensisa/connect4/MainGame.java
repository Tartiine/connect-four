package ensisa.connect4;

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

    //Add somthing to stop the game if draw (if board is completely 1 or 2)
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


}
