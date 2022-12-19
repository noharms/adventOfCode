package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle1_1 {


    public static void main(String[] args) throws FileNotFoundException {

        List<List<Integer>> caloriesPerElf = readInput();

        int maxCalories = -1;
        for (int i = 0; i < caloriesPerElf.size(); i++) {
            int calories = caloriesPerElf.get(i).stream().mapToInt(k -> k).sum();
            if (calories > maxCalories) {
                maxCalories = calories;
            }
        }

        System.out.println(maxCalories);
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
