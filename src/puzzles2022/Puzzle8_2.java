package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Puzzle8_2 {

    public static void main(String[] args) throws IOException {
        Forest forest = readInput();

        System.out.println(forest.maxScenicScore());
    }

    private static Forest readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle8.txt");
        List<String> lines = Files.readAllLines(inputFile.toPath());
        List<List<Integer>> treeHeightsMatrix = new ArrayList<>();
        for (String line : lines) {
            List<Integer> treeHeights = new ArrayList<>();
            for (char c : line.toCharArray()) {
                treeHeights.add(Integer.parseInt(String.valueOf(c)));
            }
            treeHeightsMatrix.add(treeHeights);
        }
        return new Forest(treeHeightsMatrix);
    }

    private record Forest(List<List<Integer>> treeHeightsMatrix) {

        public Forest {
            validateDimensions(treeHeightsMatrix);
        }

        private static void validateDimensions(List<List<Integer>> treeHeightsMatrix) {
            int dim = treeHeightsMatrix.size();
            for (List<Integer> row : treeHeightsMatrix) {
                int n = row.size();
                if (n != dim) {
                    throw new IllegalArgumentException("Expect quadratic forest");
                }
            }
        }

        int heightAt(int row, int column) {
            return treeHeightsMatrix.get(row).get(column);
        }

        int dimension() {
            return treeHeightsMatrix.size();
        }

        int maxScenicScore() {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < dimension(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < dimension(); j++) {
                    int scenicScore = computeScenicScore(i, j);
                    maxScore = Math.max(maxScore, scenicScore);
                    sb.append(" %03d ".formatted(scenicScore));
                }
                System.out.println(sb);
            }
            return maxScore;
        }

        int computeScenicScore(int row, int column) {
            int visibleTrees = 1;
            int visibleLeft = visibleTreesToLeft(row, column);
            int visibleRight = visibleTreesToRight(row, column);
            int visibleTop = visibleTreesToTop(row, column);
            int visibleBottom = visibleTreesToBottom(row, column);
            visibleTrees *= visibleLeft;
            visibleTrees *= visibleRight;
            visibleTrees *= visibleTop;
            visibleTrees *= visibleBottom;
            return visibleTrees;
        }

        private int visibleTreesToLeft(int row, int column) {
            int height = heightAt(row, column);
            int boundaryColumn = column - 1;
            while (boundaryColumn >= 0 && heightAt(row, boundaryColumn) < height) {
                --boundaryColumn;
            }
            return column - (Math.max(boundaryColumn, 0));
        }

        private int visibleTreesToRight(int row, int column) {
            int height = heightAt(row, column);
            int boundaryColumn = column + 1;
            while (boundaryColumn < dimension() && heightAt(row, boundaryColumn) < height) {
                ++boundaryColumn;
            }
            return Math.min(boundaryColumn, dimension() - 1) - column;
        }

        private int visibleTreesToBottom(int row, int column) {
            int height = heightAt(row, column);
            int boundaryRow = row + 1;
            while (boundaryRow < dimension() && heightAt(boundaryRow, column) < height) {
                ++boundaryRow;
            }
            return Math.min(boundaryRow, dimension() - 1) - row;
        }

        private int visibleTreesToTop(int row, int column) {
            int height = heightAt(row, column);
            int boundaryRow = row - 1;
            while (boundaryRow >= 0 && heightAt(boundaryRow, column) < height) {
                --boundaryRow;
            }
            return row - Math.max(boundaryRow, 0);
        }
    }
}
