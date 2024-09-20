import java.util.Scanner;

public class Main {

    public static class Move {
        int row, column;
    }

    static char player = 'x';
    static char opponent = 'o';
    static boolean boardUpdates = false;

    public static void printBoard(char [][] board) {
        System.out.println("");
        String line = " ";
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                line += board[i][j];
                if (j < 2) {
                    line += "  |  ";
                } 
            }
            System.out.println(line);
            if (i != 2) {
                System.out.println("---------------");
            }
            line = " ";
            
        }
        if (board[0][0] != '1' && boardUpdates) {
                String mateIn;
                System.out.println("The score of this board is: " + scoreOfBoard(board, false));
                if (scoreOfBoard(board, false) > 0) {
                    mateIn = "mate in " + Integer.toString((10 - scoreOfBoard(board, false))/2);
                } else {
                    mateIn = "no mate avalible";
                }
                System.out.println("Mate status: " + mateIn + ".");
        }
        System.out.println("");
    }

    public static int evaluateBoard(char [][] board) {
        // checking rows for win
        for (int i=0; i<3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == player) {
                    return 10;
                } else if (board[i][0] == opponent) {
                    return -10;
                }
            }
        }
        // checking columns for win
        for (int j=0; j<3; j++) {
            if (board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                if (board[0][j] == player) {
                    return 10;
                } else if (board[0][j] == opponent) {
                    return -10;
                }
            }
        }
        // checking diagnols for win
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == player) {
                    return 10;
            } else if (board[0][0] == opponent) {
                    return -10;
            }
        }
        if(board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == player) {
                    return 10;
            } else if (board[0][2] == opponent) {
                    return -10;
            }
        }
        if(!isMoveAvalible(board)){
            return 0;
        }
        return -100; // means undecided
    }

    public static boolean isMoveAvalible(char [][] board) {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    public static int scoreOfBoard(char [][] board, boolean isMaximizing) {
        if (evaluateBoard(board) != -100) {
            return evaluateBoard(board);
        }
        else {
            int bestScore = 0;
            if (isMaximizing) {
                bestScore = -1000;
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        if (board[i][j] == ' ') {
                            board[i][j] = player;
                            int newScore = scoreOfBoard(board, false);
                            bestScore = Math.max(bestScore, newScore);
                            board[i][j] = ' '; 
                        }
                    }
                }
            }
            if (!isMaximizing) {
                bestScore = 1000;
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        if (board[i][j] == ' ') {
                            board[i][j] = opponent;
                            int newScore = scoreOfBoard(board, true);
                            bestScore = Math.min(bestScore, newScore);
                            board[i][j] = ' '; 
                        }
                    }
                }
            }
            if (bestScore == 0) {
                return bestScore;
            } else {
                return bestScore - 1;
            }
        }
    }

    public static Move bestMoveFinder(char [][] board) {
        Move bestMove = new Move();
        int bestScore = -1000;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    int score = scoreOfBoard(board, false);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove.row = i;
                        bestMove.column = j;
                        bestScore = score;
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return bestMove;
    }

    public static void AskUserToMove(char[][] board, Scanner scanner) {
        boolean myTurn = false;
        if(evaluateBoard(board) == -100) {
            try {
                int userInput = Integer.valueOf(scanner.nextLine());
                int userColumn = ((userInput-1) % 3);
                int userRow = (int) (Math.floor((userInput-1)/3));
                if (board[userRow][userColumn] == ' '){
                    board[userRow][userColumn] = opponent;
                    myTurn = true;
                } else if (board[userRow][userColumn] == player || board[userRow][userColumn] == opponent) {
                    System.out.println("\nDude you can't place there, try again.\n");
                    AskUserToMove(board, scanner);
                }
                if (isMoveAvalible(board) && evaluateBoard(board) == -100 && myTurn) {
                    Move myMove = new Move();
                    myMove = bestMoveFinder(board);
                    board[myMove.row][myMove.column] = player;
                    printBoard(board);
                } else {
                    return;
                }
            } catch (Throwable t) {
                System.out.println("\nAlright bro that's not even a valid move. Try again.\n");
                AskUserToMove(board, scanner);
            }
        }
    }
        
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        char[][] board = {
            {'1','2','3'},
            {'4','5','6'},
            {'7','8','9'}
        };

        System.out.println("Heh... let's play tic tac toe... baka...");
        printBoard(board);
        System.out.println("Type what number you wanna put 'o' in, and I'll play 'x'.\n");
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                board[i][j] = ' ';
            }
        }
        board[0][0] = ' ';
        while(evaluateBoard(board) == -100) {
            AskUserToMove(board, scanner);
        }
        if(evaluateBoard(board) == 10){
            System.out.println("\nHeh! Another win for the sigma...");
        } else if (evaluateBoard(board) == -10) {
            System.out.println("\nThis should not be possible for you.");
        } else {
            printBoard(board);
            System.out.println("\nHeh... draw... dattebayo...");
        }

        scanner.close();
    }
}