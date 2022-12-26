package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Puzzle6_2 {


    public static final int WINDOW_LENGTH = 14;

    public static void main(String[] args) throws IOException {
        String dataPackage = readInput();

        Map<Character, Integer> characterToFrequency = new HashMap<>();
        for (int i = 0; i < WINDOW_LENGTH; i++) {
            characterToFrequency.merge(dataPackage.charAt(i), 1, Integer::sum);
        }

        int currentIndex = WINDOW_LENGTH;
        while (hasDuplicates(characterToFrequency)) {
            char nextCharacter = dataPackage.charAt(currentIndex);
            characterToFrequency.merge(nextCharacter, 1, Integer::sum);
            char removedCharacter = dataPackage.charAt(currentIndex - WINDOW_LENGTH);
            characterToFrequency.computeIfPresent(removedCharacter, (key, oldValue) -> oldValue > 1 ? oldValue - 1 : null);
            ++currentIndex;
        }

        System.out.println(currentIndex);

    }

    private static boolean hasDuplicates(Map<Character, Integer> characterToFrequency) {
        return characterToFrequency.size() < WINDOW_LENGTH;
    }

    private static String readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle6.txt");
        Path inputPath = Path.of(inputFile.getAbsolutePath());
        List<String> allLines = Files.readAllLines(inputPath);
        return allLines.get(0);
    }

}
