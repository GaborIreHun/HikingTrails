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
     * Reads the grid from standard input
     * The grid is read until "END" is encountered
     * Each line of the grid is trimmed and validated
     * @param br The BufferedReader to read from
     * @return The grid of integers representing heights
     * @throws IOException If an I/O error occurs
     */
    private static int[][] readGrid(BufferedReader br) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("END")) break;
            if (!line.isEmpty()) lines.add(line);
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("No map data provided");
        }
        int cols = lines.get(0).length();
        for (String l : lines) {
            if (l.length() != cols || !l.matches("\\d+"))
                throw new IllegalArgumentException("Invalid row: " + l);
        }
        int[][] grid = new int[lines.size()][cols];
        for (int i = 0; i < lines.size(); i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = lines.get(i).charAt(j) - '0';
        return grid;
    }
}
