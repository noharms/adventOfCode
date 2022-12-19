package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle1_2 {


    public static void main(String[] args) throws FileNotFoundException {

        List<List<Integer>> caloriesPerElf = readInput();

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        for (List<Integer> calories : caloriesPerElf) {
            int summedCalories = calories.stream().reduce(0, Integer::sum);
            maxHeap.add(summedCalories);
        }

        int max1 = maxHeap.remove();
        int max2 = maxHeap.remove();
        int max3 = maxHeap.remove();
        System.out.println(max1 + max2 + max3);
    }



    private static List<List<Integer>> readInput() throws FileNotFoundException {
        File inputFile = new File("resources\\2022\\input_puzzle1_1.txt");
        Scanner scanner = new Scanner(inputFile);
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                result.add(new ArrayList<>(current));
                current.clear();
            } else {
                int number = Integer.parseInt(line);
                current.add(number);
            }
        }
        return result;
    }

}
