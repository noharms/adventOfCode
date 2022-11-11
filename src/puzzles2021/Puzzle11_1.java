package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle11_1 {

    public static void main(String[] args) throws FileNotFoundException {

        List<List<Integer>> matrix = read();

        // part 1
//        printMatrix(matrix);
//
//        int countFlashes = 0;
//
//        int stepCount = 0;
//        int n = 100;
//        while (stepCount++ < n) {
//            int flashesThisRound = evolve(matrix);
//            countFlashes += flashesThisRound;
//            printMatrix(matrix);
//        }

//        printMatrix(matrix);
//
//        System.out.println("countFlashes = " + countFlashes);


        // part 2
        int flashesRound = 0;
        int stepCount = 0;
        while (flashesRound < matrix.size() * matrix.get(0).size()) {
            flashesRound = evolve(matrix);
            ++stepCount;
        }

        System.out.println("flashesRound = " + stepCount);
    }

    private static void printMatrix(List<List<Integer>> matrix) {
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

    private static int evolve(List<List<Integer>> matrix) {

        int flashCount = 0;

        int nRows = matrix.size();
        int nCols = matrix.get(0).size();

        List<List<Boolean>> hasFlashed = new ArrayList<>();
        for (int i = 0; i < nRows; i++) {
            List<Boolean> row = new ArrayList<>(nCols);
            for (int j = 0; j < nCols; j++) {
                row.add(false);
            }
            hasFlashed.add(row);
        }

        boolean willFlash = false;
        for (List<Integer> row : matrix) {
            for (int j = 0; j < nCols; j++) {
                int oldVal = row.get(j);
                if (oldVal == 9) {
                    willFlash = true;
                }
                row.set(j, oldVal + 1);
            }
        }

        // account for flashes
        while (willFlash) {
            willFlash = false;
            for (int i = 0; i < nRows; i++) {
                List<Integer> row = matrix.get(i);
                for (int j = 0; j < nCols; j++) {
                    int val = row.get(j);
                    if (val >= 10 && !hasFlashed.get(i).get(j)) {
                        willFlash |= incrementNeighbors(matrix, i, j);
                        ++flashCount;
                        hasFlashed.get(i).set(j, true);
                        row.set(j, 0);
                    }
                }
            }
        }

        return flashCount;
    }

    private static boolean incrementNeighbors(List<List<Integer>> matrix, int i, int j) {
        // abc
        // hxd
        // gfe

        int nRows = matrix.size();
        int nCols = matrix.get(0).size();

        boolean raisedBeyondNine = false;
        // a
        if (i > 0 && j > 0) {
            int rowIndex = i - 1;
            int colIndex = j - 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // b
        if (i > 0) {
            int rowIndex = i - 1;
            int colIndex = j;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // c
        if (i > 0 && j < nCols - 1) {
            int rowIndex = i - 1;
            int colIndex = j + 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // d
        if (j < nCols - 1) {
            int rowIndex = i;
            int colIndex = j + 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // e
        if (i < nRows - 1 && j < nCols - 1) {
            int rowIndex = i + 1;
            int colIndex = j + 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // f
        if (i < nRows - 1) {
            int rowIndex = i + 1;
            int colIndex = j;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // g
        if (i < nRows - 1 && j > 0) {
            int rowIndex = i + 1;
            int colIndex = j - 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        // h
        if (j > 0) {
            int rowIndex = i;
            int colIndex = j - 1;
            raisedBeyondNine |= increment(matrix, rowIndex, colIndex);
        }

        return raisedBeyondNine;
    }

    private static boolean increment(List<List<Integer>> matrix, int rowIndex, int colIndex) {
        int oldVal = matrix.get(rowIndex).get(colIndex);
        if (oldVal != 0) {
            matrix.get(rowIndex).set(colIndex, oldVal + 1);
        }
        return oldVal == 9;
    }

    private static List<List<Integer>> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "puzzle11_input.txt");
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
}
