package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PuzzleUtils {

    public static final String RESOURCES_2021 = "resources\\2021\\";

    public static void printMatrix(List<List<Integer>> matrix) {
        int nRows = matrix.size();
        int nCols = matrix.get(0).size();

        System.out.println("Matrix: ");
        for (int i = 0; i < nRows; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < nCols; j++) {
                sb.append(matrix.get(i).get(j));
            }
            System.out.println(sb);
        }
        System.out.println("");
    }

    public static List<String> readLines(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            result.add(scanner.nextLine());
        }
        return result;
    }

    public static List<List<Integer>> readMatrix(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        List<List<Integer>> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            List<Integer> row = new ArrayList<>();
            for (var c : line.toCharArray()) {
                Integer digit = Integer.parseInt(String.valueOf(c));
                row.add(digit);
            }
            result.add(row);
        }
        return result;
    }

    public int[][] toArray(List<List<Integer>> matrix) {
        int nRows = matrix.size();
        int nCols = matrix.get(0).size();

        int[][] result = new int[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                result[i][j] = matrix.get(i).get(j);
            }
        }
        return result;
    }
}
