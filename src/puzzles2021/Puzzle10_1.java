package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle10_1 {

    static List<Character> opening = List.of('(', '{', '[', '<');
    static List<Character> closing = List.of(')', '}', ']', '>');

    static Map<Character, Character> map = Map.of(')', '(', '}', '{', ']', '[', '>', '<');
    static Map<Character, Character> map2 = Map.of('(', ')', '{', '}', '[', ']', '<', '>');

    static Map<Character, Integer> penalty = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);

    static Map<Character, Integer> completeScoreMap = Map.of(')', 1, ']', 2, '}', 3, '>', 4);

    public static void main(String[] args) throws FileNotFoundException {

        List<String> lines = read();

        List<String> withoutError = new ArrayList<>();

        long errorScore = 0;
        for (var line : lines) {
            LinkedList<Character> stack = new LinkedList<>();
            long newError = getErrorScore(line, stack);
            errorScore += newError;
            if (newError == 0) {
                withoutError.add(line);
            }
        }

        List<Long> lineScores = new ArrayList<>();
        for (var line : withoutError) {
            LinkedList<Character> stack = new LinkedList<>();
            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (opening.contains(c)) {
                    stack.add(c);
                } else if (closing.contains(c)) {
                    char popped = stack.removeLast();
                } else {
                    System.out.println("found " + c);
                }
            }

            long completeScore = 0;
            while (!stack.isEmpty()) {
                char poppedOpener = stack.removeLast();
                char counterPart = map2.get(poppedOpener);
                completeScore *= 5;
                completeScore += completeScoreMap.get(counterPart);
            }
            lineScores.add(completeScore);

        }

        Collections.sort(lineScores);


        System.out.println(errorScore);
        System.out.println(lineScores.get(lineScores.size() / 2));
    }

    private static long getErrorScore(String line, LinkedList<Character> stack) {
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (opening.contains(c)) {
                stack.add(c);
            } else if (closing.contains(c)) {
                char popped = stack.removeLast();
                if (map2.get(popped) == null) {
                    System.out.println("he");
                }
                if (c != map2.get(popped)) {
                    // error
                    return penalty.get(c);
                }
            } else {
                System.out.println("found " + c);
            }
        }
        return 0;
    }

    static List<String> read() throws FileNotFoundException {
        File file = new File("puzzle10_input.txt");
        Scanner scanner = new Scanner(file);

        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            result.add(line);
        }
        return result;
    }
}
