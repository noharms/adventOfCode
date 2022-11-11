package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Puzzle6_1 {

    public static void main(String[] args) throws FileNotFoundException {

        List<Integer> nums = read();

        int[] counter = new int[9];
        for (var num : nums) {
            counter[num]++;
        }

        System.out.println("Start: " + Arrays.stream(counter).boxed().toList());

        int days = 80;
        for (int i = 0; i < days; ++i) {
            int spawns = counter[0];
            int[] tempCounter = new int[9];
            for (var j = 1; j < 9; ++j) {
                tempCounter[j - 1] = counter[j];
            }
            tempCounter[6] += spawns;
            tempCounter[8] += spawns;
            counter = tempCounter;
            System.out.println(Arrays.stream(counter).boxed().toList());
        }

        int total = Arrays.stream(counter).sum();

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
