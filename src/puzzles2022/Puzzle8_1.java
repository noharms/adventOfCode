package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class Puzzle8_1 {

    public static void main(String[] args) throws IOException {
        Forest forest = readInput();

        System.out.println(forest.computeVisibleTrees());
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

        int dimension() {
            return treeHeightsMatrix.size();
        }

        private int computeVisibleTrees() {
            // dynamic programming: keep track of visibilities to avoid redundant passes through the matrix
            List<List<FourDirectionVisibility>> visibilities = initializeVisibilities();
            traverseForestFromLeft(visibilities);
            traverseForestFromRight(visibilities);
            traverseForestFromTop(visibilities);
            traverseForestFromBottom(visibilities);
            printForDebug(visibilities);
            return countVisible(visibilities);
        }

        private void printForDebug(List<List<FourDirectionVisibility>> visibilities) {
            for (int i = 0; i < dimension(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < dimension(); j++) {
                    FourDirectionVisibility visibility = visibilities.get(i).get(j);
                    line.append(visibility.isVisible() ? "V" : "X");
                }
                System.out.println(line);
            }
        }

        private int countVisible(List<List<FourDirectionVisibility>> visibilities) {
            int countVisible = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    FourDirectionVisibility visibility = visibilities.get(i).get(j);
                    countVisible += (visibility.isVisible()) ? 1 : 0;
                }
            }
            return countVisible;
        }

        private List<List<FourDirectionVisibility>> initializeVisibilities() {
            List<List<FourDirectionVisibility>> visibilities = new ArrayList<>();
            for (int i = 0; i < dimension(); i++) {
                List<FourDirectionVisibility> row =
                    IntStream.range(0, dimension()).mapToObj(j -> new FourDirectionVisibility()).toList();
                visibilities.add(row);
            }
            return visibilities;
        }

        private void traverseForestFromLeft(List<List<FourDirectionVisibility>> visibilities) {
            for (int row = 0; row < dimension(); row++) {
                int highestFromLeft = Integer.MIN_VALUE;
                for (int column = 0; column < dimension(); column++) {
                    FourDirectionVisibility visibility = visibilities.get(row).get(column);
                    int treeHeight = treeHeightsMatrix.get(row).get(column);
                    visibility.isVisibleFromLeft = treeHeight > highestFromLeft;
                    highestFromLeft = Math.max(highestFromLeft, treeHeight);
                }
            }
        }

        private void traverseForestFromRight(List<List<FourDirectionVisibility>> visibilities) {
            for (int row = 0; row < dimension(); row++) {
                int highestFromRight = Integer.MIN_VALUE;
                for (int column = dimension() - 1; column >= 0; column--) {
                    FourDirectionVisibility visibility = visibilities.get(row).get(column);
                    int treeHeight = treeHeightsMatrix.get(row).get(column);
                    visibility.isVisibleFromRight = treeHeight > highestFromRight;
                    highestFromRight = Math.max(highestFromRight, treeHeight);
                }
            }
        }

        private void traverseForestFromTop(List<List<FourDirectionVisibility>> visibilities) {
            for (int column = 0; column < dimension(); column++) {
                int highestFromTop = Integer.MIN_VALUE;
                for (int row = 0; row < dimension(); row++) {
                    FourDirectionVisibility visibility = visibilities.get(row).get(column);
                    int treeHeight = treeHeightsMatrix.get(row).get(column);
                    visibility.isVisibleFromTop = treeHeight > highestFromTop;
                    highestFromTop = Math.max(highestFromTop, treeHeight);
                }
            }
        }

        private void traverseForestFromBottom(List<List<FourDirectionVisibility>> visibilities) {
            for (int column = 0; column < dimension(); column++) {
                int highestFromBottom = Integer.MIN_VALUE;
                for (int row = dimension() - 1; row >= 0; row--) {
                    FourDirectionVisibility visibility = visibilities.get(row).get(column);
                    int treeHeight = treeHeightsMatrix.get(row).get(column);
                    visibility.isVisibleFromBottom = treeHeight > highestFromBottom;
                    highestFromBottom = Math.max(highestFromBottom, treeHeight);
                }
            }
        }
    }

    private static class FourDirectionVisibility {

        boolean isVisibleFromLeft;
        boolean isVisibleFromRight;
        boolean isVisibleFromTop;
        boolean isVisibleFromBottom;

        boolean isVisible() {
            return isVisibleFromBottom || isVisibleFromTop || isVisibleFromLeft || isVisibleFromRight;
        }
    }
}
