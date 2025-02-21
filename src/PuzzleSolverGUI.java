import javax.swing.*;
import java.awt.*;
import java.io.*;

public class PuzzleSolverGUI extends JFrame {
    private JTextArea textArea;
    private JButton uploadButton, solveButton, saveButton;
    private File selectedFile;
    private JLabel stepLabel, timeLabel;
    private JEditorPane boardDisplay;
    private String lastSolution = ""; 

    public PuzzleSolverGUI() {
        setTitle("Puzzle Solver by Bob Kunanda");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Upload Button
        uploadButton = new JButton("Upload Puzzle File");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 16));
        uploadButton.addActionListener(e -> chooseFile());

        // Solve Button
        solveButton = new JButton("Solve Puzzle");
        solveButton.setFont(new Font("Arial", Font.BOLD, 16));
        solveButton.setEnabled(false);
        solveButton.addActionListener(e -> solvePuzzle());

        // Save Button
        saveButton = new JButton("Save Solution");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setEnabled(false); 
        saveButton.addActionListener(e -> saveSolution());

        // Text Area for Output
        textArea = new JTextArea(10, 50);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Board Display
        boardDisplay = new JEditorPane();
        boardDisplay.setContentType("text/html");
        boardDisplay.setEditable(false);
        boardDisplay.setText("<html><pre style='font-size:16px;'>No board loaded</pre></html>");
        JScrollPane boardScroll = new JScrollPane(boardDisplay);

        // Info Labels
        stepLabel = new JLabel("Steps Checked: 0");
        timeLabel = new JLabel("Runtime: 0 ms");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(saveButton);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(stepLabel);
        infoPanel.add(timeLabel);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.WEST);
        add(boardScroll, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Puzzle File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            textArea.setText("Selected File: " + selectedFile.getAbsolutePath() + "\n");
            solveButton.setEnabled(true);
            saveButton.setEnabled(false); 
        }
    }

    public static String getBoardHTML(char[][] board) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><pre style='font-size:16px; font-family:monospace;'>");

        String[] COLORS = {
            "red", "green", "yellow", "blue", "purple", "cyan", "darkblue", "gray",
            "darkred", "darkgreen", "gold", "navy", "magenta", "teal", "silver",
            "brown", "lime", "orange", "indigo", "violet", "aqua", "darkgray"
        };

        for (char[] row : board) {
            for (char piece : row) {
                if (piece == ' ' || piece == '.') {
                    sb.append("&nbsp;");
                } else {
                    int colorIndex = (piece - 'A') % COLORS.length;
                    sb.append("<font color='" + COLORS[colorIndex] + "'>" + piece + "</font>");
                }
            }
            sb.append("<br>");
        }

        sb.append("</pre></html>");
        return sb.toString();
    }

    private void solvePuzzle() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "No file selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        textArea.append("\nSolving Puzzle...\n");

        int[] totalSteps = {0};
        int[] runtime = {0};

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return functions.puzzleSolver(selectedFile.getAbsolutePath(), totalSteps, runtime);
            }

            @Override
            protected void done() {
                try {
                    lastSolution = get(); 
                    textArea.append("\n" + lastSolution + "\n");

                    stepLabel.setText("Steps Checked: " + totalSteps[0]);
                    timeLabel.setText("Runtime: " + runtime[0] + " ms");

                    boardDisplay.setText(getBoardHTML(resultToBoard(lastSolution)));

                    saveButton.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error solving puzzle!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void saveSolution() {
        if (lastSolution == null || lastSolution.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No solution to save!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File solutionsDir = new File("test/solutions");
            if (!solutionsDir.exists()) {
                solutionsDir.mkdir();
            }
            String fileName = selectedFile.getName().replace(".txt", "_solution.txt");
            File outputFile = new File(solutionsDir, fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(lastSolution);
            }

            JOptionPane.showMessageDialog(this, "Solution saved to: " + outputFile.getAbsolutePath(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving solution!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private char[][] resultToBoard(String result) {
        String[] lines = result.split("\n");
        char[][] board = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            board[i] = lines[i].toCharArray();
        }
        return board;
    }

    public static void main(String[] args) {
        new PuzzleSolverGUI();
    }
}
