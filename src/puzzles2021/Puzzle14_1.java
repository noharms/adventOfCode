package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle14_1 {

    private static String TEMPLATE;
    private static List<Rule> RULES = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        read();

        String current = TEMPLATE;
        for (int k = 0; k < 10; ++k) {
            int i = 0;
            while (i < current.length() - 1) {
                StringBuilder sb = new StringBuilder(current);
                String substring = current.substring(i, i + 2);
                boolean matched = false;
                for (var rule : RULES) {
                    if (rule.match(substring)) {
                        sb.insert(i + 1, rule.c);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    ++i;
                } else {
                    current = sb.toString();
                    i += 2;
                }
            }
        }

        Map<Character, Long> frequencies = new HashMap<>();
        for (Character c : current.toCharArray()) {
            frequencies.merge(c, 1L, Long::sum);
        }
        Character max = null;
        long maxCount = 0;
        for (var entry : frequencies.entrySet()) {
            if (entry.getValue() > maxCount) {
                max = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        Character min = null;
        long minCount = Integer.MAX_VALUE;
        for (var entry : frequencies.entrySet()) {
            if (entry.getValue() < minCount) {
                min = entry.getKey();
                minCount = entry.getValue();
            }
        }
        System.out.println("Hello: " + max + " + " + min);
        System.out.println(frequencies.get(max) - frequencies.get(min));
    }

    static void read() throws FileNotFoundException {
        File file = new File("puzzle14_input.txt");
        Scanner scanner = new Scanner(file);

        TEMPLATE = scanner.nextLine();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            String[] pieces = line.split(" -> ");
            Rule newRule = new Rule(pieces[0].charAt(0), pieces[0].charAt(1), pieces[1].charAt(0));
            RULES.add(newRule);
        }
    }

    private static record Rule(Character a, Character b, Character c) {

        boolean match(String ab) {
            return ab.equals(String.valueOf(a) + String.valueOf(b));
        }

        String apply(String x) {
            StringBuilder sb = new StringBuilder(x);
             sb.insert(1, c);
            return sb.toString();
        }

//        String apply(String x) {
//            String searchPattern = String.valueOf(a) + String.valueOf(b);
//            List<Integer> matches = new ArrayList<>();
//            for (int i = 0; i <= x.length() - searchPattern.length(); ++i) {
//                String candidate = x.substring(i, i + searchPattern.length());
//                if (candidate.equals(searchPattern)) {
//                    matches.add(i);
//                }
//            }
//            StringBuilder sb = new StringBuilder(x);
//            for (var matchIndex : matches) {
//                sb.insert(matchIndex + 1, c);
//            }
//            return sb.toString();
//        }
    }
}
