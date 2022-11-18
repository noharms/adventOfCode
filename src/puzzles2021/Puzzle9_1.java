package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Puzzle9_1 {

    public static void main(String[] args) throws FileNotFoundException {

        List<String> input = read();

        List<Coordinates> lowPoints = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i) {
            String row = input.get(i);
            for (int j = 0; j < row.length(); ++j) {
                int candidateVal = Integer.parseInt(String.valueOf(row.charAt(j)));
                List<Integer> neighbours = getNeighbours(input, i, j);
                if (neighbours.stream().allMatch(neighbor -> neighbor > candidateVal)) {
                    lowPoints.add(new Coordinates(i, j));
                }
            }
        }

        // part 1
        int finalResult = lowPoints.stream().map(coors -> Integer.parseInt(String.valueOf(input.get(coors.rowIndex)
                                                                                               .charAt(coors.colIndex))) + 1)
                                   .mapToInt(i -> i).sum();

        System.out.println(finalResult);


        // part 2

        List<Integer> basinSizes = lowPoints.stream().map(coors -> mapToBasinSize(coors, input)).collect(Collectors.toList());

        Collections.sort(basinSizes);
        Collections.reverse(basinSizes);

        System.out.println(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));

    }

    private static int mapToBasinSize(Coordinates coors, List<String> matrix) {


        List<Coordinates> basinPoints = new ArrayList<>();
        basinPoints.add(coors);

        boolean foundNewPoint = true;
        while (foundNewPoint) {
            foundNewPoint = false;

            List<Coordinates> newBasinPoints = new ArrayList<>();
            for (var point : basinPoints) {
                int pointVal = Integer.parseInt(String.valueOf(matrix.get(point.rowIndex).charAt(point.colIndex)));
                List<Coordinates> neighbours = getNeighboursCoors(matrix, point.rowIndex, point.colIndex);

                for (var neighbor : neighbours) {
                    int neighborVal = Integer.parseInt(String.valueOf(matrix.get(neighbor.rowIndex)
                                                                            .charAt(neighbor.colIndex)));
                    if (!basinPoints.contains(neighbor) && !newBasinPoints.contains(neighbor) && neighborVal > pointVal && neighborVal != 9) {
                        newBasinPoints.add(neighbor);
                        foundNewPoint = true;
                    }
                }
            }
            basinPoints.addAll(newBasinPoints);
        }

        return basinPoints.size();
    }

    private static List<List<Boolean>> createEmptyMatrix(int n, int m) {
        List<List<Boolean>> newMatrix = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            List<Boolean> newRow = new ArrayList<>(Collections.nCopies(m, false));
            newMatrix.add(newRow);
        }
        return newMatrix;
    }

    private static List<Coordinates> getNeighboursCoors(List<String> input, int i, int j) {
        List<Coordinates> result = new ArrayList<>();
        if (i > 0) {
            result.add(new Coordinates(i - 1, j));
        }
        if (i < input.size() - 1) {
            result.add(new Coordinates(i + 1, j));
        }
        if (j > 0) {
            result.add(new Coordinates(i, j - 1));
        }
        if (j < input.get(0).length() - 1) {
            result.add(new Coordinates(i, j + 1));
        }
        return result;
    }

    private static List<Integer> getNeighbours(List<String> input, int i, int j) {
        List<Integer> result = new ArrayList<>();
        if (i > 0) {
            result.add(Integer.parseInt(String.valueOf(input.get(i - 1).charAt(j))));
        }
        if (i < input.size() - 1) {
            result.add(Integer.parseInt(String.valueOf(input.get(i + 1).charAt(j))));
        }
        if (j > 0) {
            result.add(Integer.parseInt(String.valueOf(input.get(i).charAt(j - 1))));
        }
        if (j < input.get(0).length() - 1) {
            result.add(Integer.parseInt(String.valueOf(input.get(i).charAt(j + 1))));
        }
        return result;
    }

    static List<String> read() throws FileNotFoundException {
        File file = new File("puzzle9_input.txt");
        Scanner scanner = new Scanner(file);

        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            result.add(line);
        }
        return result;
    }

    public static record Coordinates(int rowIndex, int colIndex) {

    }
}
