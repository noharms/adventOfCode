package puzzles2022.graph.maze;


import puzzles2022.graph.UnweightedGraph;

/**
 * Converts a rectangular integer height map to a graph of cells, where a cell is a pair of row-index, column-index.
 * <br>
 * A cell is only connected to a neighbor in the height map, if the neighbor is at maximum one higher/lower.
 */
public class RectangularMaze {

    public static UnweightedGraph<Cell> createGraph(int[][] maze) {
        validate(maze);

        UnweightedGraph<Cell> mazeGraph = new UnweightedGraph<>();
        addNodes(mazeGraph, maze);
        addEdges(mazeGraph, maze);
        return mazeGraph;
    }

    private static void addNodes(UnweightedGraph<Cell> mazeGraph, int[][] maze) {
        int nRows = maze.length;
        int nCols = maze[0].length;
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                mazeGraph.addNode(new Cell(i, j));
            }
        }
    }

    private static void addEdges(UnweightedGraph<Cell> mazeGraph, int[][] maze) {
        int nRows = maze.length;
        int nCols = maze[0].length;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (canConnectLeft(maze, row, col)) {
                    connectLeftNeighbor(mazeGraph, row, col);
                }
                if (canConnectRight(maze, row, col)) {
                    connectRightNeighbor(mazeGraph, row, col);
                }
                if (canConnectTop(maze, row, col)) {
                    connectTopNeighbor(mazeGraph, row, col);
                }
                if (canConnectBottom(maze, row, col)) {
                    connectBottomNeighbor(mazeGraph, row, col);
                }
            }
        }
    }

    private static boolean canConnectLeft(int[][] maze, int row, int col) {
        if (col <= 0)
            return false;
        int heightDifference = maze[row][col] - maze[row][col - 1];
        return heightDifference >= -1;
    }

    private static boolean canConnectRight(int[][] maze, int row, int col) {
        int nCols = maze[0].length;
        if (col >= nCols - 1) {
            return false;
        }
        int heightDifference = maze[row][col] - maze[row][col + 1];
        return heightDifference >= -1;
    }

    private static boolean canConnectTop(int[][] maze, int row, int col) {
        if (row <= 0)
            return false;
        int heightDifference = maze[row][col] - maze[row - 1][col];
        return heightDifference >= -1;
    }

    private static boolean canConnectBottom(int[][] maze, int row, int col) {
        int nRows = maze.length;
        if (row >= nRows - 1)
            return false;
        int heightDifference = maze[row][col] - maze[row + 1][col];
        return heightDifference >= -1;
    }

    private static void connectBottomNeighbor(UnweightedGraph<Cell> mazeGraph, int row, int col) {
        Cell current = new Cell(row, col);
        Cell bottom = new Cell(row + 1, col);
        mazeGraph.addDirectedEdge(current, bottom);
    }

    private static void connectTopNeighbor(UnweightedGraph<Cell> mazeGraph, int row, int col) {
        Cell current = new Cell(row, col);
        Cell top = new Cell(row - 1, col);
        mazeGraph.addDirectedEdge(current, top);
    }

    private static void connectRightNeighbor(UnweightedGraph<Cell> mazeGraph, int row, int col) {
        Cell current = new Cell(row, col);
        Cell right = new Cell(row, col + 1);
        mazeGraph.addDirectedEdge(current, right);
    }

    private static void connectLeftNeighbor(UnweightedGraph<Cell> mazeGraph, int row, int col) {
        Cell current = new Cell(row, col);
        Cell left = new Cell(row, col - 1);
        mazeGraph.addDirectedEdge(current, left);
    }

    private static void validate(int[][] maze) {
        if (maze.length == 0) {
            throw new IllegalArgumentException("Maze cannot be empty.");
        }
        int width = maze[0].length;
        for (int[] row : maze) {
            if (row.length != width) {
                throw new IllegalArgumentException("Each row of the maze must have the same width.");
            }
        }
    }

}
