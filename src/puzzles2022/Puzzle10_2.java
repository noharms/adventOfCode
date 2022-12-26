package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


public class Puzzle10_2 {

    private static final int PIXELS_IN_ROW = 40;
    private static final int N_ROWS = 6;

    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = readInput();

        CPU cpu = new CPU();
        List<Integer> registerRecords = cpu.executeInstructions(instructions);
        Set<Integer> cyclesToDraw = new HashSet<>(registerRecords);

        for (int j = 0; j < N_ROWS; j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < PIXELS_IN_ROW; i++) {
                int currentCycle = j * PIXELS_IN_ROW + i + 1;
                sb.append(cyclesToDraw.contains(currentCycle) ? "#" : ".");
            }
            System.out.println(sb);
        }

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
                case "addx" -> new AddX(Integer.parseInt(tokens[1]));
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
         * Executes instructions and records those cycles during which the sprite is drawn (i.e. during which the
         * register value is +-1 matching the cycle count.
         */
        List<Integer> executeInstructions(List<Instruction> instructions) {
            List<Integer> registerRecords = new ArrayList<>();

            for (Instruction instruction : instructions) {
                for (int i = 0; i < instruction.requiredCycles(); i++) {
                    ++cycleCount;
                    int crtPixelToDraw = (cycleCount - 1) % PIXELS_IN_ROW;
                    if (Math.abs(register.value - crtPixelToDraw) < 2) {
                        registerRecords.add(cycleCount);
                    }
                    System.out.println("Cycle " + cycleCount + " : " + register.value);
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
