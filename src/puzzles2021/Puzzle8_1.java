package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle8_1 {

    public static List<List<String>> INPUT_WIRES = new ArrayList<>();
    public static List<List<String>> INPUT_4DIGITS = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        read();

//        Map<Integer, Integer> countSegmentsToUniqueDigit = Map.of(
//                2, 1,
//                4, 4,
//                3, 7,
//                7, 8
//        );
//
//        long occurences = INPUT_4DIGITS.stream().flatMap(Collection::stream).filter(s -> countSegmentsToUniqueDigit
//        .containsKey(s.length())).count();
//
//        System.out.println("Hello" + occurences);

        Map<String, String> segmentsToDigit = Map.of(
                "abcefg", "0",
                "cf", "1",
                "acdeg", "2",
                "acdfg", "3",
                "bcdf", "4",
                "abdfg", "5",
                "abdefg", "6",
                "acf", "7",
                "abcdefg", "8",
                "abcdfg", "9"
        );


        List<String> outputDigitStrings = new ArrayList<>();
        for (int i = 0; i < INPUT_WIRES.size(); ++i) {
            List<String> inputWires = INPUT_WIRES.get(i);
            List<String> fourDigitList = INPUT_4DIGITS.get(i);

            Map<Character, Character> cipher = new HashMap();


            // MAP c, f
            String oneAsSegments = inputWires.stream()
                                             .filter(segments -> segments.length() == 2)
                                             .findAny()
                                             .orElseThrow();

            // f should appear in all but one occurence, i.e. 9 times, while c should appear 8 times
            long countFirstSegment = inputWires.stream().filter(s -> s.indexOf(oneAsSegments.charAt(0)) > -1).count();
            long countSecondSegment = inputWires.stream().filter(s -> s.indexOf(oneAsSegments.charAt(1)) > -1).count();

            char currentF = countFirstSegment > countSecondSegment ? oneAsSegments.charAt(0) : oneAsSegments.charAt(1);
            char currentC = countFirstSegment > countSecondSegment ? oneAsSegments.charAt(1) : oneAsSegments.charAt(0);

            cipher.put(currentC, 'c');
            cipher.put(currentF, 'f');

            // MAP a
            String sevenAsSegments = inputWires.stream()
                                               .filter(segments -> segments.length() == 3)
                                               .findAny()
                                               .orElseThrow();
            String notContainedInOneButInSeven = removeChars(sevenAsSegments, oneAsSegments);
            cipher.put(notContainedInOneButInSeven.charAt(0), 'a');

            // MAP b, d
            String fourAsSegments = inputWires.stream()
                                              .filter(segments -> segments.length() == 4)
                                              .findAny()
                                              .orElseThrow();
            String notContainedInOneButInFour = removeChars(fourAsSegments, oneAsSegments);

            if (notContainedInOneButInFour.length() != 2) {
                throw new IllegalStateException("Damn");
            }

            // b should appear in 6 occurence, d should appear in 7
            countFirstSegment = inputWires.stream().filter(s -> s.indexOf(notContainedInOneButInFour.charAt(0)) > -1).count();
            countSecondSegment = inputWires.stream().filter(s -> s.indexOf(notContainedInOneButInFour.charAt(1)) > -1).count();

            char currentD = countFirstSegment > countSecondSegment ? notContainedInOneButInFour.charAt(0) : notContainedInOneButInFour.charAt(1);
            char currentB = countFirstSegment > countSecondSegment ? notContainedInOneButInFour.charAt(1) : notContainedInOneButInFour.charAt(0);

            cipher.put(currentB, 'b');
            cipher.put(currentD, 'd');

            // MAP e, g
            StringBuilder sb = new StringBuilder();
            for (var c : cipher.keySet()) {
                sb.append(c);
            }

            String remainingTwoChars = removeChars("abcdefg", sb.toString());

            countFirstSegment = inputWires.stream().filter(s -> s.indexOf(remainingTwoChars.charAt(0)) > -1).count();
            countSecondSegment = inputWires.stream().filter(s -> s.indexOf(remainingTwoChars.charAt(1)) > -1).count();

            // e should appear in 3 occurence, g should appear in 7
            char currentG = countFirstSegment > countSecondSegment ? remainingTwoChars.charAt(0) : remainingTwoChars.charAt(1);
            char currentE = countFirstSegment > countSecondSegment ? remainingTwoChars.charAt(1) : remainingTwoChars.charAt(0);

            cipher.put(currentE, 'e');
            cipher.put(currentG, 'g');

            // DECODE
            List<String> decodedOutputStrings = fourDigitList.stream().map(s -> decode(s, cipher)).toList();
            String concatenated = decodedOutputStrings.stream().map(segmentsToDigit::get).collect(Collectors.joining());
            outputDigitStrings.add(concatenated);

        }

        List<Integer> outputDigits = outputDigitStrings.stream().map(s -> Integer.parseInt(s, 10))
                                                       .collect(Collectors.toList());

        System.out.println(outputDigits.stream().mapToInt(i -> i).sum());

    }

    private static String removeChars(String sevenAsSegments, String oneAsSegments) {
        for (int i = 0; i < oneAsSegments.length(); ++i) {
            char c = oneAsSegments.charAt(i);
            sevenAsSegments = sevenAsSegments.replace(String.valueOf(c), "");
        }
        return sevenAsSegments;
    }

    static String decode(String str, Map<Character, Character> cipher) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            Character old = str.charAt(i);
            sb.append(cipher.get(old));
        }
        String result = sb.toString();

        char[] chars = result.toCharArray();

        Arrays.sort(chars);
        return new String(chars);
    }

    static void read() throws FileNotFoundException {
        File file = new File("puzzle8_input.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] splitAtDelim = line.split("\\|");
            String[] splitInput = splitAtDelim[0].split(" ");
            String[] splitOutput = splitAtDelim[1].split(" ");
            List<char[]> inputWiresArrays = Arrays.stream(splitInput).map(String::toCharArray)
                                                  .collect(Collectors.toList());
            List<char[]> input4DigitValueArrays = Arrays.stream(splitOutput).map(String::toCharArray)
                                                        .collect(Collectors.toList());
            inputWiresArrays.forEach(Arrays::sort);
            input4DigitValueArrays.forEach(Arrays::sort);
            List<String> inputWires = inputWiresArrays.stream().map(String::new).toList();
            List<String> input4DigitValue = input4DigitValueArrays.stream().map(String::new).filter(s -> !s.isEmpty())
                                                                  .toList();
            INPUT_WIRES.add(inputWires);
            INPUT_4DIGITS.add(input4DigitValue);
        }
    }
}
