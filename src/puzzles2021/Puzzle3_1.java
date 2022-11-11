package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Puzzle3_1 {

    public static void main(String[] args) throws FileNotFoundException {

        var asStrings = read();

        List<List<Integer>> asLists = new ArrayList<>();

        for (var numAsString : asStrings) {
            List<Integer> num = new ArrayList<>();
            for (int i = 0; i < numAsString.length(); ++i) {
                var character = numAsString.charAt(i);
                if (character == '0') {
                    num.add(0);
                } else {
                    num.add(1);
                }
            }
            Collections.reverse(num);
            asLists.add(num);
        }

        int[] counts = new int[asLists.get(0).size()];
        for (var num : asLists) {
            for (int i = 0; i < num.size(); ++i) {
                if (num.get(i) == 1) {
                    counts[i]++;
                }
            }
        }

        List<Integer> gamma = new ArrayList<>();

        List<Integer> epsilon = new ArrayList<>();


        for (int i = 0; i < counts.length; ++i) {
            if (counts[i] > asLists.size() - counts[i]) {
                gamma.add(1);
                epsilon.add(0);
            } else {
                gamma.add(0);
                epsilon.add(1);
            }
        }

        double gammaLong = 0;
        double epsilonLong = 0;
        for (int i = gamma.size() - 1; i >= 0; --i) {
            gammaLong += Math.pow(2, i) * gamma.get(i);
            epsilonLong += Math.pow(2, i) * epsilon.get(i);
        }

        System.out.println((long)gammaLong * (long)epsilonLong);
    }


    public static List<String> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "binaries.txt");
        Scanner scanner = new Scanner(file);
        List<String> strings = new ArrayList<>();
        while (scanner.hasNext()) {
            strings.add(scanner.next());
        }
        return strings;
    }
}
