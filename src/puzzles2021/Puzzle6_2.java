package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Puzzle6_2 {

    public static void main(String[] args) throws FileNotFoundException {

        List<Integer> nums = read();

        BigInteger[] counter = new BigInteger[9];
        for (int i = 0; i < 9; ++i) {
            counter[i] = BigInteger.ZERO;
        }
        for (var num : nums) {
            counter[num] = counter[num].add(BigInteger.ONE);
        }

        System.out.println("Start: " + Arrays.stream(counter).reduce(BigInteger::add).get());

        int days = 256;
        for (int i = 0; i < days; ++i) {
            BigInteger spawns = counter[0];
            BigInteger[] tempCounter = new BigInteger[9];
            for (int k = 0; k < 9; ++k) {
                tempCounter[k] = BigInteger.ZERO;
            }
            for (var j = 1; j < 9; ++j) {
                tempCounter[j - 1] = counter[j];
            }
            tempCounter[6] = tempCounter[6].add(spawns);
            tempCounter[8] = tempCounter[8].add(spawns);
            counter = tempCounter;
            System.out.println(Arrays.stream(counter).toList());
        }

        BigInteger total = Arrays.stream(counter).reduce(BigInteger::add).get();

        System.out.println("fish: " + total);

    }

    private static List<Integer> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "puzzle6_input.txt");
        Scanner scanner = new Scanner(file);

        String line = scanner.nextLine();
        String[] numsAsString = line.split(",");
        return Arrays.stream(numsAsString).map(Integer::parseInt).toList();
    }
}
