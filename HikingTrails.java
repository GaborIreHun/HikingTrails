import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Hiking Trails
 * This program reads a grid of integers from standard input, where each integer represents the height of a cell.
 * It computes the total number of reachable cells with height 9 from all cells with height 0.
 * The program uses BFS to explore the grid and find reachable cells. BFS is used here because it is well-suited for finding the shortest path in an unweighted grid.
 * The program also uses a queue to keep track of cells to be explored and a set to store the reachable cells.
 */
public class HikingTrails {
    // Directions for moving up, down, left, right
    // Each direction is represented as an array of two integers: {row_change, col_change}
    // Using multidimensional array for better readability and maintainability
    // This allows us to easily add more directions if needed in the future
    private static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};

    /** 
     * Each cell in the grid is represented by a Cell object with row, column, and height
     * The Position class is used to store the coordinates of reachable cells
     * Using record for better readability and immutability, record is a feature that allows you to create data classes
     */ 
    private static record Cell(int row, int col, int height) { }
    /**
     * The Position class is used to store the coordinates of reachable cells
     * It is used to avoid duplicates in the result set
     * Using record for better readability and immutability, record is a feature that allows you to create data classes
     */
    private static record Position(int row, int col) { }

    public static void main(String[] args) throws IOException {
        int[][] grid;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            grid = readGrid(br);
        }
        List<Integer> scores = computeScores(grid);
        int totalScore = scores.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Trailheads: " + scores);
        System.out.println("Score:      " + totalScore);
    }

    /**
     * Computes the scores for each cell in the grid
     * The score for a cell is the number of reachable cells with height 9 from that cell
     * @param grid The grid of integers representing heights
     * @throws IllegalArgumentException If the grid is empty or invalid
     * @return A list of scores for each cell in the grid
     */
    private static List<Integer> computeScores(int[][] grid) {
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    int count = findReachableNines(grid, i, j).size();
                    scores.add(count);
                }
            }
        }
        return scores;
    }

    /**
     * Finds all reachable cells with height 9 from the starting cell (sr, sc)
     * The method uses BFS to explore the grid and find reachable cells
     * @param grid The grid of integers representing heights
     * @param sr The starting row index
     * @param sc The starting column index
     * @return A set of positions of reachable cells with height 9
     */
    private static Set<Position> findReachableNines(int[][] grid, int sr, int sc) {
        int R = grid.length, C = grid[0].length;
        boolean[][] visited = new boolean[R][C];
        Queue<Cell> q = new ArrayDeque<>();
        Set<Position> result = new HashSet<>();
        q.add(new Cell(sr, sc, 0));
        visited[sr][sc] = true;

        while (!q.isEmpty()) {
            Cell cur = q.remove();
            if (cur.height == 9) {
                result.add(new Position(cur.row, cur.col));
                continue;
            }
            // Explore all 4 directions (up, down, left, right)
            // Check if the next cell is within bounds and has the expected height
            // If so, mark it as visited and add it to the queue
            // nc- new column, nr- new row
            // cur- current cell, d- direction
            for (var d : DIRS) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (nr >= 0 && nr < R && nc >= 0 && nc < C
                    && !visited[nr][nc]
                    && grid[nr][nc] == cur.height + 1) {
                    visited[nr][nc] = true;
                    q.add(new Cell(nr, nc, cur.height + 1));
                }
            }
        }
        return result;
    }

    /**
     * Reads a digit-map from the console:
     * 1) Prompts for a positive integer (column count).  Keeps asking until you type one.
     * 2) Prompts for each row (exactly that many digits).  Keeps asking until you type a valid row or END.
     *
     * @param br  the BufferedReader wrapping System.in
     * @return    a rectangular int[][] of size [rowCount][colCount]
     * @throws IOException  on I/O errors (not on user-typing null/EOF)
     */
    private static int[][] readGrid(BufferedReader br) throws IOException {
        // 1) Ask for column count
        int cols = -1;
        while (cols < 0) {
            System.out.print("Enter number of columns (positive integer): ");
            String input = br.readLine();
            if (input == null) {
                // User hit EOF / ctrl-d; treat it like bad input and re-prompt
                System.out.println("  ▶ A positive integer is required for columns. Please try again.");
                continue;
            }
            input = input.trim();
            try {
                cols = Integer.parseInt(input);
                if (cols <= 0) {
                    System.out.println("  ▶ Please enter a number greater than zero.");
                    cols = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ▶ Invalid entry. You must enter a positive integer.");
            }
        }

        // 2) Read each row until the user types END
        List<String> rows = new ArrayList<>();
        while (true) {
            System.out.print("Enter a row of " + cols + " digits (or END to finish): ");
            String line = br.readLine();
            if (line == null) {
                // Treat EOF like an invalid row; re-prompt
                System.out.println("  ▶ Expected " + cols + " digits or END. Please try again.");
                continue;
            }
            line = line.trim();

            if (line.equalsIgnoreCase("END")) {
                break;
            }
            if (line.length() != cols || !line.matches("\\d{" + cols + "}")) {
                System.out.println("  ▶ Invalid row: must be exactly " 
                                + cols + " digits (0–9). Try again.");
                continue;
            }
            rows.add(line);
        }

        // 3) Ensure at least one row was entered
        if (rows.isEmpty()) {
            System.out.println("No rows entered. Exiting.");
            System.exit(1);
        }

        // 4) Build and return the numeric grid
        int[][] grid = new int[rows.size()][cols];
        for (int r = 0; r < rows.size(); r++) {
            String row = rows.get(r);
            for (int c = 0; c < cols; c++) {
                grid[r][c] = row.charAt(c) - '0';
            }
        }
        return grid;
    }
}
