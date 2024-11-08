import java.util.InputMismatchException;
import java.util.Scanner;

public class GeneralizedSudokuSolver {
    private static int SIZE;
    private static int SUBGRID_SIZE;
    private static final int EMPTY = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter the size of the Sudoku board (e.g., 4 for 4x4, 9 for 9x9): ");
            SIZE = scanner.nextInt();
            SUBGRID_SIZE = (int) Math.sqrt(SIZE);

            if (SUBGRID_SIZE * SUBGRID_SIZE != SIZE) {
                throw new IllegalArgumentException("Invalid board size! Size must be a perfect square (4, 9, 16, ...).");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a numeric value.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        int[][] board = new int[SIZE][SIZE];
        boolean play = true;

        while (play) {
            System.out.println("\nEnter Sudoku puzzle (use 0 for empty cells):");
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    int input;
                    do {
                        System.out.print("Enter value for cell (" + (i + 1) + "," + (j + 1) + "): ");
                        try {
                            input = scanner.nextInt();
                            if (input < 0 || input > SIZE) {
                                throw new IllegalArgumentException("Invalid input! Please enter a number between 0 and " + SIZE + ".");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a numeric value.");
                            scanner.next();  // clear invalid input
                            input = -1;  // continue the loop
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                            input = -1;  // continue the loop
                        }
                    } while (input < 0 || input > SIZE);
                    board[i][j] = input;
                }
            }

            System.out.println("Solving Sudoku...");
            if (solveSudoku(board)) {
                System.out.println("Solution:");
                printBoard(board);
            } else {
                System.out.println("No solution exists for the provided board.");
            }

            System.out.print("Do you want to clear the board and try again? (yes/no): ");
            play = scanner.next().equalsIgnoreCase("yes");
            if (play) {
                clearBoard(board);
            }
        }

        scanner.close();
        System.out.println("Thank you for playing!");
    }

    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = EMPTY;  // Backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSafe(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }
        // Check column
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }
        // Check subgrid
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void clearBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static void printBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int d = 0; d < SIZE; d++) {
                System.out.print(board[r][d] + " ");
            }
            System.out.println();
        }
    }
}
