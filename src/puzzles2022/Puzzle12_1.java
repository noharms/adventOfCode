package puzzles2022;

import puzzles2022.graph.UnweightedGraph;
import puzzles2022.graph.maze.Cell;
import puzzles2022.graph.maze.RectangularMaze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Puzzle12_1 {

    public static final char START_CHARACTER = 'S';
    public static final char END_CHARACTER = 'E';

    public static void main(String[] args) throws IOException {
        HeightMap heightMap = readInput();
        UnweightedGraph<Cell> graph = RectangularMaze.createGraph(heightMap.matrixAsArrays());
        List<Cell> path = graph.shortestPath(heightMap.startCell, heightMap.endCell);
        System.out.println(path);
        System.out.println("--> " + (path.size() - 1) + " steps");
    }

    private static HeightMap readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle12.txt");
        List<String> lines = Files.readAllLines(inputFile.toPath());

        List<List<Character>> charMatrix = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            List<Character> row = new ArrayList<>();
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                row.add(c);
            }
            charMatrix.add(row);
        }

        return toHeightMap(charMatrix);
    }

    private static HeightMap toHeightMap(List<List<Character>> charMatrix) {
        List<List<Integer>> matrix = new ArrayList<>();
        Cell startPosition = null;
        Cell endPosition = null;
        for (int i = 0, charMatrixSize = charMatrix.size(); i < charMatrixSize; i++) {
            List<Character> row = charMatrix.get(i);
            List<Integer> integerRow = new ArrayList<>();
            for (int j = 0, rowSize = row.size(); j < rowSize; j++) {
                Character c = row.get(j);
                char actualChar = Character.isLowerCase(c) ? c : (c == START_CHARACTER ? 'a' : 'z');
                int heightValue = actualChar - 'a';
                integerRow.add(heightValue);
                if (c.equals(START_CHARACTER)) {
                    startPosition = new Cell(i, j);
                } else if (c.equals(END_CHARACTER)) {
                    endPosition = new Cell(i, j);
                }
            }
            matrix.add(integerRow);
        }
        return new HeightMap(matrix, startPosition, endPosition);
    }

    record HeightMap(List<List<Integer>> matrix, Cell startCell, Cell endCell) {

        int[][] matrixAsArrays() {
            int[][] asArrays = new int[matrix.size()][];
            for (int i = 0; i < matrix.size(); i++) {
                List<Integer> row = matrix.get(i);
                int[] rowAsArray = new int[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    rowAsArray[j] = row.get(j);
                }
                asArrays[i] = rowAsArray;
            }
            return asArrays;
        }
    }
}
