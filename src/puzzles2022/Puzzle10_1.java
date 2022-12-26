package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;


public class Puzzle10_1 {

    private static List<Integer> RECORD_DURING_CYCLES = IntStream.range(0, 6).map(i -> 20 + 40 * i).boxed().toList();


    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = readInput();

        CPU cpu = new CPU();
        List<Integer> registerRecords = cpu.executeInstructions(instructions, new HashSet<>(RECORD_DURING_CYCLES));
        int total = 0;
        for (int i = 0; i < RECORD_DURING_CYCLES.size(); i++) {
            int cycle = RECORD_DURING_CYCLES.get(i);
            int registerValue = registerRecords.get(i);
            total += cycle * registerValue;
        }
        System.out.println(total);

    }

    private static List<Instruction> readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle10.txt");
        Path inputPath = Path.of(inputFile.getAbsolutePath());
        List<String> allLines = Files.readAllLines(inputPath);

        List<Instruction> instructions = new ArrayList<>();
        for (String line : allLines) {
            String[] tokens = line.split(" ");
            String command = tokens[0];
            Instruction instruction = switch (command) {
                case "addx" ->
                    new AddX(Integer.parseInt(tokens[1]));
                case "noop" -> new Noop();
                default -> throw new IllegalArgumentException("Unexpected " + command);
            };
            instructions.add(instruction);
        }

        return instructions;
    }

    static class CPU {

        int cycleCount;
        Register register;

        public CPU() {
            this.cycleCount = 0;
            this.register = new Register();
        }

        /**
         * Executes instructions and records register states
         */
        List<Integer> executeInstructions(List<Instruction> instructions, Set<Integer> cyclesDuringWhichToRecord) {
            List<Integer> registerRecords = new ArrayList<>();

            for (Instruction instruction : instructions) {
                for (int i = 0; i < instruction.requiredCycles(); i++) {
                    ++cycleCount;
                    if (cyclesDuringWhichToRecord.contains(cycleCount)) {
                        registerRecords.add(register.value);
                    }
                }
                // only after the required cycles are consumed, the instruction becomes effective
                instruction.executeOn(register);
            }

            return registerRecords;
        }
    }

    private interface Instruction {

        int requiredCycles();

        void executeOn(Register register);

    }

    private static class Register {

        int value = 1;

    }

    private static class Noop implements Instruction {

        private final int requiredCycles = 1;


        @Override
        public int requiredCycles() {
            return requiredCycles;
        }

        @Override
        public void executeOn(Register register) {
            // does nothing
        }
    }

    private static class AddX implements Instruction {

        private final int requiredCycles = 2;

        private final int x;

        private AddX(int x) {
            this.x = x;
        }

        @Override
        public int requiredCycles() {
            return requiredCycles;
        }

        @Override
        public void executeOn(Register register) {
            register.value += x;
        }
    }
}
