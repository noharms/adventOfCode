package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle1_1 {


    public static void main(String[] args) throws IOException {
        List<Integer> depths = readInput();

        int countIncreases = 0;
        int prevDepth = depths.get(0);
        for (int i = 1; i < depths.size(); ++i) {
            int currDepth = depths.get(i);
            if (currDepth > prevDepth) {
                ++countIncreases;
            }
            prevDepth = currDepth;
        }

        System.out.println(countIncreases);
    }

    private static List<Integer> readInput() throws FileNotFoundException {
        File inputFile = new File("input_puzzle1.txt");
        Scanner scanner = new Scanner(inputFile);
        List<Integer> integers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                integers.add(scanner.nextInt());
            } else {
                scanner.next();
            }
        }
        return integers;
    }
}
