import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PuzzleSolverFX extends Application {
    private static final int CELL_SIZE = 50; // Size of each grid cell
    private char[][] board;
    private GridPane boardGrid;
    private Label resultLabel;
    private Label timeLabel;
    private ArrayList<ArrayList<ArrayList<Integer>>> pieces;
    private ArrayList<Boolean> isUsed;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle Solver");

        board = new char[][] {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        }; // Example 3x3 empty board

        pieces = new ArrayList<>();
        isUsed = new ArrayList<>();

        // Example pieces (add actual pieces)
        addExamplePieces();

        boardGrid = new GridPane();
        drawBoard();

        Button solveButton = new Button("Solve Puzzle");
        solveButton.setOnAction(e -> solvePuzzleGUI());

        resultLabel = new Label("Press 'Solve Puzzle' to begin.");
        timeLabel = new Label("");

        VBox root = new VBox(10, boardGrid, solveButton, resultLabel, timeLabel);
        Scene scene = new Scene(root, 400, 450);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solvePuzzleGUI() {
        AtomicInteger steps = new AtomicInteger(0);
        long startTime = System.nanoTime();
        boolean solved = solvePuzzle(pieces, board, isUsed, 0, 0, steps);
        long endTime = System.nanoTime();
        long elapsedTimeMs = (endTime - startTime) / 1_000_000;

        drawBoard(); // Update board UI

        if (solved) {
            resultLabel.setText("Solution Found in " + steps.get() + " steps!");
        } else {
            resultLabel.setText("No solution found.");
        }

        timeLabel.setText("Execution Time: " + elapsedTimeMs + " ms");
    }

    private void drawBoard() {
        boardGrid.getChildren().clear();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setFill(getColorForPiece(board[row][col]));

                boardGrid.add(cell, col, row);
            }
        }
    }

    private Color getColorForPiece(char piece) {
        switch (piece) {
            case 'A': return Color.RED;
            case 'B': return Color.BLUE;
            case 'C': return Color.GREEN;
            case 'D': return Color.ORANGE;
            case 'E': return Color.PURPLE;
            case 'F': return Color.CYAN;
            case 'G': return Color.YELLOW;
            default: return Color.WHITE; // Empty spaces
        }
    }

    private void addExamplePieces() {
        ArrayList<ArrayList<Integer>> pieceA = new ArrayList<>();
        pieceA.add(new ArrayList<>(List.of(0, 0)));
        pieceA.add(new ArrayList<>(List.of(1, 0)));
        pieceA.add(new ArrayList<>(List.of(0, 1)));

        pieces.add(pieceA);
        isUsed.add(false);
    }

    public static boolean solvePuzzle(ArrayList<ArrayList<ArrayList<Integer>>> pieces, char[][] board, ArrayList<Boolean> isUsed, int row, int col, AtomicInteger steps) {
        if (row >= board.length) return false;

        int nextRow = (col + 1 == board[0].length) ? row + 1 : row;
        int nextCol = (col + 1) % board[0].length;

        if (board[row][col] != ' ') return solvePuzzle(pieces, board, isUsed, nextRow, nextCol, steps);

        for (int i = 0; i < pieces.size(); i++) {
            if (isUsed.get(i)) continue;

            ArrayList<ArrayList<Integer>> piece = pieces.get(i);

            if (isFit(piece, board, row, col)) {
                placePiece(piece, board, row, col, (char) ('A' + i));
                isUsed.set(i, true);

                if (solvePuzzle(pieces, board, isUsed, nextRow, nextCol, steps)) return true;

                removePiece(piece, board, row, col);
                isUsed.set(i, false);
            }
        }

        return false;
    }

    public static boolean isFit(ArrayList<ArrayList<Integer>> piece, char[][] board, int row, int col) {
        for (ArrayList<Integer> coord : piece) {
            int r = row + coord.get(0);
            int c = col + coord.get(1);

            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c] != ' ') {
                return false;
            }
        }
        return true;
    }

    public static void placePiece(ArrayList<ArrayList<Integer>> piece, char[][] board, int row, int col, char pieceChar) {
        for (ArrayList<Integer> coord : piece) {
            int r = row + coord.get(0);
            int c = col + coord.get(1);
            board[r][c] = pieceChar;
        }
    }

    public static void removePiece(ArrayList<ArrayList<Integer>> piece, char[][] board, int row, int col) {
        for (ArrayList<Integer> coord : piece) {
            int r = row + coord.get(0);
            int c = col + coord.get(1);
            board[r][c] = ' ';
        }
    }
}
