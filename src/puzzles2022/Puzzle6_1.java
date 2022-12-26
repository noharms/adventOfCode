package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class Puzzle6_1 {


    public static void main(String[] args) throws IOException {
        String dataPackage = readInput();

        LinkedList<Character> lastFourCharacters = new LinkedList<>();
        lastFourCharacters.addLast(dataPackage.charAt(0));
        lastFourCharacters.addLast(dataPackage.charAt(1));
        lastFourCharacters.addLast(dataPackage.charAt(2));
        lastFourCharacters.addLast(dataPackage.charAt(3));

        int currentIndex = 4;
        while (hasDuplicates(lastFourCharacters)) {
            lastFourCharacters.removeFirst();
            lastFourCharacters.addLast(dataPackage.charAt(currentIndex));
            ++currentIndex;
        }

        System.out.println(currentIndex);

    }

    private static boolean hasDuplicates(LinkedList<Character> lastFourCharacters) {
        return new HashSet<>(lastFourCharacters).size() < 4;
    }

    private static String readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle6.txt");
        Path inputPath = Path.of(inputFile.getAbsolutePath());
        List<String> allLines = Files.readAllLines(inputPath);
        return allLines.get(0);
    }

}
