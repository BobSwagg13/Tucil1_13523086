import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

public class functions {
    public static boolean isFull(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void rotate(ArrayList<ArrayList<Integer>> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            int x = coordinates.get(i).get(0);
            int y = coordinates.get(i).get(1);
            coordinates.get(i).set(0, -y);
            coordinates.get(i).set(1, x);
        }
    }

    public static void printBoard(char[][] board) {
        String RESET = "\033[0m"; 
        String[] COLORS = { 
            "\033[31m", 
            "\033[32m", 
            "\033[33m", 
            "\033[34m", 
            "\033[35m", 
            "\033[36m", 
            "\033[37m", 
            "\033[90m", 
            "\033[91m", 
            "\033[92m", 
            "\033[93m", 
            "\033[94m", 
            "\033[95m", 
            "\033[96m", 
            "\033[97m", 
            "\033[38;5;208m", 
            "\033[38;5;130m", 
            "\033[38;5;27m",  
            "\033[38;5;82m",  
            "\033[38;5;226m", 
            "\033[38;5;201m", 
            "\033[38;5;51m",  
            "\033[38;5;202m", 
            "\033[38;5;118m", 
            "\033[38;5;129m", 
            "\033[38;5;190m"  
        };
    
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char piece = board[i][j];

                if (piece == ' ' || piece == '.') {
                    System.out.print(RESET + piece); 
                } 
                else {
                    int colorIndex = (piece - 'A') % COLORS.length; 
                    System.out.print(COLORS[colorIndex] + piece + RESET); 
                }
            }
            System.out.println();
        }
    }
    

    public static boolean isFit(ArrayList<ArrayList<Integer>>  coordinates, char[][] board, int indexX, int indexY) {
        for (int i = 0; i < coordinates.size(); i++) {
            int x = coordinates.get(i).get(0) + indexX;
            int y = coordinates.get(i).get(1) + indexY;
            if (x < 0 || y < 0 || x >= board.length || y >= board[0].length) {
                return false;
            }
            else{
                if(board[x][y] != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    public static void placePiece(ArrayList<ArrayList<Integer>> coordinates, char[][] board, int indexX, int indexY, char piece) {
        for (int i = 0; i < coordinates.size(); i++) {
            int x = coordinates.get(i).get(0);
            int y = coordinates.get(i).get(1);
            board[x + indexX][y + indexY] = piece;
        }
    }

    public static void removePiece(ArrayList<ArrayList<Integer>> coordinates, char[][] board, int indexX, int indexY) {
        for (int i = 0; i < coordinates.size(); i++) {
            int x = coordinates.get(i).get(0);
            int y = coordinates.get(i).get(1);
            board[x + indexX][y + indexY] = ' ';
        }
    }
    
    public static boolean isBoardFull(char[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }

    public static boolean areAllUsed(ArrayList<Boolean> isUsed) {
        for (boolean used : isUsed) {
            if (!used) return false;
        }
        return true;
    }
    
    public static boolean solvePuzzle(ArrayList<ArrayList<ArrayList<Integer>>> pieces, char[][] board, ArrayList<Boolean> isUsed, int row, int col, AtomicInteger steps) {
        if (areAllUsed(isUsed)) return isBoardFull(board); 
        if (row >= board.length) return false; 
        int nextRow = (col + 1 == board[0].length) ? row + 1 : row;
        int nextCol = (col + 1) % board[0].length;
    
        if (board[row][col] != ' ') return solvePuzzle(pieces, board, isUsed, nextRow, nextCol,steps);
    
        boolean foundValidPlacement = false; 
        for (int i = 0; i < pieces.size(); i++) {
            if (isUsed.get(i)) continue;
    
            ArrayList<ArrayList<Integer>> piece = pieces.get(i);
    
            for (int j = 0; j < piece.size(); j++) {
                setPivot(piece, j);
    
                for (int rotation = 0; rotation < 4; rotation++) {
                    rotate(piece);
    
                    if (tryPlace(pieces, piece, board, isUsed, row, col, i, steps)) {
                        foundValidPlacement = true;
                        return true;
                    }
    
                    mirror(piece);
                    if (tryPlace(pieces, piece, board, isUsed, row, col, i, steps)) {
                        foundValidPlacement = true;
                        return true;
                    }
                    mirror(piece);
                }
            }
        }
    
        return foundValidPlacement;
    }
    

    public static boolean tryPlace(ArrayList<ArrayList<ArrayList<Integer>>> pieces, ArrayList<ArrayList<Integer>> piece, char[][] board, ArrayList<Boolean> isUsed, int row, int col, int pieceIndex, AtomicInteger steps) {
        steps.incrementAndGet();
        if (isFit(piece, board, row, col)) {
            placePiece(piece, board, row, col, (char) (pieceIndex + 'A'));
            isUsed.set(pieceIndex, true);
            
            int nextRow = (col + 1 == board[0].length) ? row + 1 : row;
            int nextCol = (col + 1) % board[0].length;
    
            if (solvePuzzle(pieces, board, isUsed, nextRow, nextCol, steps)) return true;
    
            removePiece(piece, board, row, col);
            isUsed.set(pieceIndex, false);
        }
        return false;
    }
    
    

    public static boolean checkFit(ArrayList<ArrayList<ArrayList<Integer>>> pieces, char[][] board, ArrayList<Boolean> isUsed, int indexX, int indexY){
        for(int i = 0; i < pieces.size(); i++){
            if(isUsed.get(i)){
                continue;
            }
            for(int j = 0; j < pieces.get(i).size(); j++){
                setPivot(pieces.get(i), j);
                for(int k = 0; k < 4; k++){
                    if(isFit(pieces.get(i), board, indexX, indexY)){
                        for(int l = 0; l < pieces.get(i).size(); l++){
                            int x = pieces.get(i).get(l).get(0);
                            int y = pieces.get(i).get(l).get(1);
                            board[x + indexX][y + indexY] = (char) (i + 'A');
                        }
                        isUsed.set(i, true);
                        return true;
                    }
                    rotate(pieces.get(i));
                }
                mirror(pieces.get(i));
                for(int k = 0; k < 4; k++){
                    if(isFit(pieces.get(i), board, indexX, indexY)){
                        for(int l = 0; l < pieces.get(i).size(); l++){
                            int x = pieces.get(i).get(l).get(0);
                            int y = pieces.get(i).get(l).get(1);
                            board[x + indexX][y + indexY] = (char) (i + 'A');
                        }
                        isUsed.set(i, true);
                        return true;
                    }
                    rotate(pieces.get(i));
                }
            }
        }
        return false;
    }
    public static void mirror(ArrayList<ArrayList<Integer>> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            int x = coordinates.get(i).get(0);
            coordinates.get(i).set(0, -x);
        }
    }

    public static void setPivot(ArrayList<ArrayList<Integer>> coordinates, int index) {
        int xPivot = coordinates.get(index).get(0);
        int yPivot = coordinates.get(index).get(1);
        for (int i = 0; i < coordinates.size(); i++) {
            coordinates.get(i).set(0, coordinates.get(i).get(0) - xPivot);
            coordinates.get(i).set(1, coordinates.get(i).get(1) - yPivot);
        }
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your path name: ");
        String pathName = scanner.nextLine();
        scanner.close();

        long startTime = System.nanoTime();
        ArrayList<ArrayList<ArrayList<Integer>>> pieces = new ArrayList<>();
        ArrayList<ArrayList<Integer>> currentPiece = new ArrayList<>();
        ArrayList<ArrayList<Character>> tempBoard = new ArrayList<>();
        ArrayList<Boolean> isUsed = new ArrayList<>();
        int N = 0;
        int M = 0;
        int P = 0;
        String type = "";
        

        try {
            // INPUT N M P
            File file = new File(pathName); 
            Scanner fileScanner = new Scanner(file);
            String line = fileScanner.nextLine();
            char[] lineArray = line.toCharArray();
            String[] parts = line.trim().split("\\s+");
            N = Integer.parseInt(parts[0]);
            M = Integer.parseInt(parts[1]);
            P = Integer.parseInt(parts[2]);

            //VALIDATE N M P
            if  (N < 1 || M < 1 || P < 1 || P > 26){
                System.out.println("Invalid input.");
                System.exit(0);
            }

            //INPUT S
            line = fileScanner.nextLine();
            type = line;
            
            if  (line.trim().equals("CUSTOM")){
                for(int i = 0; i < N; i++){
                    line = fileScanner.nextLine();
                    lineArray = line.toCharArray();
                    ArrayList<Character> row = new ArrayList<>();
                    for(int j = 0; j < M; j++){
                        row.add(lineArray[j]);
                    }
                    tempBoard.add(row);
                }
            }
            else    {
                if(!line.trim().equals("DEFAULT")){
                    System.out.println("Invalid input.");
                    System.exit(0);
                }
            }
            
            //INPUT PIECES
            int x = 0;
            int y = 0;
            char currentChar = ' ';

            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine(); 
                lineArray = line.toCharArray();
                
                while (x < lineArray.length && lineArray[x] == ' ') {
                    x++;
                }

                if (x < lineArray.length && currentChar != lineArray[x]) {
                    if (!currentPiece.isEmpty()) {
                        pieces.add(currentPiece);
                        isUsed.add(false);
                    }
                    currentPiece = new ArrayList<>();
                    y = 0;
                    currentChar = lineArray[x];
                }

                for (int i = x; i < lineArray.length; i++){
                    if  (lineArray[i] != ' '){
                        ArrayList<Integer> coordinate = new ArrayList<>();
                        coordinate.add(x);
                        coordinate.add(y);
                        currentPiece.add(coordinate);
                    }
                    x++;
                }
                y++;
                x = 0;
            }
            pieces.add(currentPiece);
            isUsed.add(false);
            fileScanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }


        char[][] board = new char[N][M];
        if  (type.equals("DEFAULT")){
            for (int i = 0; i < N; i++){
                for (int j = 0; j < M; j++){
                    board[i][j] = ' ';
                }
            }
        }
        if  (type.equals("CUSTOM")){
            for (int i = 0; i < N; i++){
                for (int j = 0; j < M; j++){
                    if  (tempBoard.get(i).get(j) == 'X'){
                        board[i][j] = ' ';
                    }
                    else{
                        board[i][j] = tempBoard.get(i).get(j);
                    }
                }
            }
        }
        
        AtomicInteger steps = new AtomicInteger(0);
        boolean solved = solvePuzzle(pieces, board, isUsed, 0, 0, steps);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;;
        if(solved)  {
            printBoard(board);
            System.out.println("Banyak kasus yang ditinjau: " + steps);
            System.out.println("Waktu pencarian: " + duration + " ms");
        } 
        else{
            System.out.println("No solution found.");
            System.out.println("Banyak kasus yang ditinjau: " + steps);
            System.out.println("Waktu pencarian: " + duration + " ms");
        }
    }
}
