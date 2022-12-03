package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Puzzle3_1 {

    public static void main(String[] args) throws FileNotFoundException {
        List<Rucksack> rucksacks = readInput();

        int sumPriorities = rucksacks.stream()
                .map(Rucksack::commonCharacter)
                .map(Puzzle3_1::calculatePriority)
                .reduce(0, Integer::sum);

        System.out.println(sumPriorities);
    }

    private static int calculatePriority(char item) {
        return Character.isLowerCase(item) ? item - 'a' + 1 : item - 'A' + 27;
    }

    private static record Rucksack(String compartment1, String compartment2) {
        private char commonCharacter() {
            for (char c : compartment1.toCharArray()) {
                if (compartment2.contains(String.valueOf(c))) {
                    return c;
                }
            }
            throw new IllegalArgumentException("No common char found in " + compartment1 + "and " + compartment2);
        }
    }

    private static List<Rucksack> readInput() throws FileNotFoundException {
        File inputFile = new File("resources\\2022\\input_puzzle3.txt");
        Scanner scanner = new Scanner(inputFile);
        List<Rucksack> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String compartment1 = line.substring(0, line.length() / 2);
            String compartment2 = line.substring(line.length() / 2, line.length());
            result.add(new Rucksack(compartment1, compartment2));
        }
        return result;
    }

}
