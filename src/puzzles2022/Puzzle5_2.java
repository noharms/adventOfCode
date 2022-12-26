package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;


public class Puzzle5_2 {


    public static void main(String[] args) throws IOException {
        ProblemDescription problemDescription = readInput();

        var stacks = problemDescription.stacks;
        for (Instruction instruction : problemDescription.instructions) {
            int nMoves = instruction.amountOfCrates;
            int fromIndex = instruction.fromStack - 1;
            int toIndex = instruction.toStack - 1;

            LinkedList<Character> batch = new LinkedList<>();
            while (nMoves > 0) {
                Character crate = stacks.get(fromIndex).removeFirst();
                batch.addFirst(crate);
                --nMoves;
            }

            while (!batch.isEmpty()) {
                stacks.get(toIndex).addFirst(batch.removeFirst());
            }

        }

        String result = "";
        for (var stack : stacks) {
            result += stack.isEmpty() ? "" : stack.getFirst();
        }

        System.out.println(result);

    }

    private record ProblemDescription(List<LinkedList<Character>> stacks, List<Instruction> instructions) {
    }

    private record Instruction(int fromStack, int toStack, int amountOfCrates) {
    }

    private static ProblemDescription readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle5.txt");
        Path inputPath = Path.of(inputFile.getAbsolutePath());
        List<String> allLines = Files.readAllLines(inputPath);

        int indexSeparatorLine = findBlankLineIndex(allLines);
        String stackIndicesLine = allLines.get(indexSeparatorLine - 1);
        String[] stackNumbers = stackIndicesLine.split(" ");
        int nStacks = Integer.parseInt(stackNumbers[stackNumbers.length - 1]);

        List<LinkedList<Character>> stacks = IntStream.range(0, nStacks).mapToObj(i -> new LinkedList<Character>()).toList();
        for (int i = indexSeparatorLine - 2; i >= 0; --i) {
            String row = allLines.get(i);
            for (int j = 0; j < nStacks; j++) {
                int crateCharacterIndex = computeCrateCharacterIndex(j);
                if (crateCharacterIndex < row.length()) {
                    char crateCharacter = row.charAt(crateCharacterIndex);
                    if (Character.isLetter(crateCharacter)) {
                        stacks.get(j).addFirst(crateCharacter);
                    }
                }
            }
        }

        List<Instruction> instructions = new ArrayList<>();
        for (int i = indexSeparatorLine + 1; i < allLines.size(); i++) {
            String[] tokens = allLines.get(i).split(" ");
            int amount = Integer.parseInt(tokens[1]);
            int from = Integer.parseInt(tokens[3]);
            int to = Integer.parseInt(tokens[5]);
            instructions.add(new Instruction(from, to, amount));
        }

        return new ProblemDescription(stacks, instructions);
    }

    private static int computeCrateCharacterIndex(int j) {
        if (j == 0) {
            return 1;
        } else if (j == 1) {
            return 5;
        } else {
            return 9 + (j - 2) * 4;
        }
    }

    private static int findBlankLineIndex(List<String> lines) {
        int i = 0;
        while (!lines.get(i).isBlank()) {
            ++i;
        }
        return i;
    }

}
