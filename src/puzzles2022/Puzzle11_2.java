package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Puzzle11_2 {

    static final int N_ROUNDS = 10000;

    /**
     * Chinese remainder theorem:
     *
     * for the purpose of modular divisibility by two prime numbers x and y, a third number z is equivalent to
     * mod(z,  (x * y)). That is, mod(z, x) = mod(mod(z, (x * y)), x) and mod(z, y) = mod(mod(z, (x * y)), y).
     *
     * For example:
     *
     * z = 27
     * x = 3
     * y = 5
     *
     * --> mod(27, 3) = 0
     * --> mod(27, 5) = 2
     *
     * And also using mod(27,  15) = 12
     *
     * --> mod(12, 3) = 0
     * --> mod(12, 5) = 2
     *
     * ----------------------------------
     *
     * In the context of this puzzle, this theorem allows to keep numbers small enough because we only care about
     * their modular divisibility by the numbers 2, 3, 5, 7, 11, 13, 17, 19.
     *
     */
    static final long ALL_DIVISORS_PRODUCT = 2 * 3 * 5 * 7 * 11 * 13 * 17 * 19;
//    static final long ALL_DIVISORS_PRODUCT = 13 * 17 * 19 * 23;

    public static void main(String[] args) throws IOException {
        List<MonkeyStrategy> monkeyStrategies = readInput();

        int nMonkeys = monkeyStrategies.size();
        List<List<BigInteger>> currentWorryLevelsPerMonkeyId =
            monkeyStrategies.stream().map(s -> new ArrayList<>(s.worryLevelsItems)).collect(Collectors.toList());
        List<Integer> inspectionsPerMonkeyId = new ArrayList<>(Collections.nCopies(nMonkeys, 0));
        for (int i = 0; i < N_ROUNDS; i++) {
            for (int j = 0; j < nMonkeys; j++) {
                MonkeyStrategy monkeyStrategy = monkeyStrategies.get(j);
                List<BigInteger> worryLevels = currentWorryLevelsPerMonkeyId.get(j);
                for (BigInteger worryLevel : worryLevels) {
                    BigInteger newWorryLevel = monkeyStrategy.binaryOperation().apply(worryLevel);
                    inspectionsPerMonkeyId.set(j, inspectionsPerMonkeyId.get(j) + 1);
                    // newWorryLevel = newWorryLevel.div(BigInteger.valueOf(3));
                    newWorryLevel = newWorryLevel.mod(BigInteger.valueOf(ALL_DIVISORS_PRODUCT));
                    boolean isDivisible = isDivisible(newWorryLevel, monkeyStrategy.divisorForTest);
                    int targetMonkey = isDivisible ?
                        monkeyStrategy.targetMonkeyIdIfTrue :
                        monkeyStrategy.targetMonkeyIdIfFalse;
                    List<BigInteger> targetMonkeyWorryLevels = currentWorryLevelsPerMonkeyId.get(targetMonkey);
                    targetMonkeyWorryLevels.add(newWorryLevel);
                }
                currentWorryLevelsPerMonkeyId.set(j, new ArrayList<>());
            }

            System.out.println("Round: " + i);
        }

        Collections.sort(inspectionsPerMonkeyId);

        long mostActiveMonkeyInspections = inspectionsPerMonkeyId.get(nMonkeys - 1);
        long nextMostActiveMonkeyInspections = inspectionsPerMonkeyId.get(nMonkeys - 2);
        System.out.println(mostActiveMonkeyInspections * nextMostActiveMonkeyInspections);
    }

    private static boolean isDivisible(BigInteger dividend, int divisor) {
        return dividend.mod(BigInteger.valueOf(divisor)).equals(BigInteger.ZERO);
    }

    private static List<MonkeyStrategy> readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle11.txt");
        List<String> lines = Files.readAllLines(inputFile.toPath());

        List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
        int lineIndex = 0;
        while (lineIndex < lines.size()) {
            String monkeyIdLine = lines.get(lineIndex++);
            String startingItemsLine = lines.get(lineIndex++);
            String operationLine = lines.get(lineIndex++);
            String testLine = lines.get(lineIndex++);
            String ifTrueLine = lines.get(lineIndex++);
            String ifFalseLine = lines.get(lineIndex++);

            int monkeyId = extractMonkeyId(monkeyIdLine);
            List<BigInteger> worryLevelsItems = extractWorryLevels(startingItemsLine);
            BinaryOperation binaryOperation = extractOperation(operationLine);
            int divisorForTest = useLastTokenAsInteger(testLine);
            int targetMonkeyIdIfTrue = useLastTokenAsInteger(ifTrueLine);
            int targetMonkeyIdIfFalse = useLastTokenAsInteger(ifFalseLine);

            MonkeyStrategy monkeyStrategy = new MonkeyStrategy(monkeyId,
                                                               worryLevelsItems,
                                                               binaryOperation,
                                                               divisorForTest,
                                                               targetMonkeyIdIfTrue,
                                                               targetMonkeyIdIfFalse);
            monkeyStrategies.add(monkeyStrategy);

            lineIndex++; // skip one blank line after each block
        }

        return monkeyStrategies;
    }

    private static int extractMonkeyId(String testLine) {
        String[] tokens = testLine.split(" ");
        String lastToken = tokens[tokens.length - 1];
        return Integer.parseInt(lastToken.substring(0, lastToken.length() - 1)); // -1 because there is the final ":"
        // included in the token
    }

    private static int useLastTokenAsInteger(String testLine) {
        String[] tokens = testLine.split(" ");
        String lastToken = tokens[tokens.length - 1];
        return Integer.parseInt(lastToken);
    }

    private static BinaryOperation extractOperation(String operationLine) {
        String prefix = "  Operation: new = ";
        String withoutPrefix = operationLine.substring(prefix.length());
        String[] tokens = withoutPrefix.split(" ");
        Operand operand1;
        if (tokens[0].equals("old")) {
            operand1 = new OldValue();
        } else {
            operand1 = new Value(Integer.parseInt(tokens[0]));
        }
        Operator operator = Operator.from(tokens[1]);
        Operand operand2;
        if (tokens[2].equals("old")) {
            operand2 = new OldValue();
        } else {
            operand2 = new Value(Integer.parseInt(tokens[2]));
        }
        return new BinaryOperation(operand1, operand2, operator);
    }

    private static List<BigInteger> extractWorryLevels(String startingItemsLine) {
        String prefix = "  Starting items:";
        String withoutPrefix = startingItemsLine.substring(prefix.length());
        String[] numbers = withoutPrefix.split(",");
        return Arrays.stream(numbers).map(String::trim).map(Integer::parseInt).map(BigInteger::valueOf).toList();
    }

    private record BinaryOperation(Operand operand1, Operand operand2, Operator operator) {
        public BigInteger apply(BigInteger worryLevel) {
            BigInteger intOperand1 = operand1 instanceof OldValue ? worryLevel : ((Value) operand1).value;
            BigInteger intOperand2 = operand2 instanceof OldValue ? worryLevel : ((Value) operand2).value;
            return operator.equals(Operator.SUMMATION) ? intOperand1.add(intOperand2) :
                intOperand1.multiply(intOperand2);
        }
    }

    private interface Operand {
    }

    private static class OldValue implements Operand {

    }

    private static class Value implements Operand {
        final BigInteger value;

        private Value(int value) {
            this.value = BigInteger.valueOf(value);
        }
    }

    private enum Operator {
        MULTIPLICATION,
        SUMMATION;

        static Operator from(String symbol) {
            return switch (symbol) {
                case "*" -> MULTIPLICATION;
                case "+" -> SUMMATION;
                default -> throw new IllegalArgumentException("Unknown symbol " + symbol);
            };
        }
    }

    private record MonkeyStrategy(
        int monkeyId,
        List<BigInteger> worryLevelsItems,
        BinaryOperation binaryOperation,
        int divisorForTest,
        int targetMonkeyIdIfTrue,
        int targetMonkeyIdIfFalse
    ) {
    }
}
