package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Puzzle3_2 {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) throws FileNotFoundException {
        List<Group> groups = readInput();

        int sumPriorities = groups.stream()
                .map(Group::commonCharacter)
                .map(Puzzle3_2::calculatePriority)
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

        private String allItems() {
            return compartment1 + compartment2;
        }
    }

    private static record Group(List<Rucksack> rucksacks) {

        private char commonCharacter() {
            Map<Character, Integer> characterToDifferentRucksacksCount = new HashMap<>();
            for (char c : ALPHABET.toCharArray()) {
                characterToDifferentRucksacksCount.put(c, 0);
            }
            for (int i = 0; i < rucksacks.size(); i++) {
                Rucksack rucksack = rucksacks.get(i);
                for (char c : rucksack.allItems().toCharArray()) {
                    if (characterToDifferentRucksacksCount.containsKey(c) && characterToDifferentRucksacksCount.get(c) == i) {
                        characterToDifferentRucksacksCount.put(c, i + 1);
                    }
                }
            }
            for (var entry : characterToDifferentRucksacksCount.entrySet()) {
                if (entry.getValue() == rucksacks.size()) {
                    return entry.getKey();
                }
            }
            throw new IllegalArgumentException("No common letter found.");
        }
    }

    private static List<Group> readInput() throws FileNotFoundException {
        File inputFile = new File("resources\\2022\\input_puzzle3.txt");
        Scanner scanner = new Scanner(inputFile);
        List<Rucksack> rucksacks = new ArrayList<>();
        List<Group> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String compartment1 = line.substring(0, line.length() / 2);
            String compartment2 = line.substring(line.length() / 2, line.length());
            rucksacks.add(new Rucksack(compartment1, compartment2));
            if (rucksacks.size() == 3) {
                result.add(new Group(new ArrayList<>(rucksacks)));
                rucksacks.clear();
            }
        }
        return result;
    }

}
