package puzzles2021;

import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle14_2 {

    private static String TEMPLATE;
    private static Map<String, Character> RULES = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {

        List<String> lines = PuzzleUtils.readLines(PuzzleUtils.RESOURCES_2021 + "puzzle14_input.txt");

        convertLines(lines);

        Map<String, Long> pairsInTemplate = new HashMap<>();
        for (int i = 1; i < TEMPLATE.length(); i++) {
            String pair = TEMPLATE.substring(i - 1, i + 1);
            pairsInTemplate.merge(pair, 1L, Long::sum);
        }
        pairsInTemplate.put(TEMPLATE.charAt(TEMPLATE.length() - 1) + "\n", 1L);

        int n = 10;
        for (int i = 0; i < n; i++) {
            Map<String, Long> copy = new HashMap<>(pairsInTemplate);
            for (var pair : copy.keySet()) {
                if (RULES.containsKey(pair)) {
                    String newPair1 = pair.charAt(0) + String.valueOf(RULES.get(pair));
                    String newPair2 = String.valueOf(RULES.get(pair)) + pair.charAt(1);
                    pairsInTemplate.merge(newPair1, 1L, Long::sum);
                    pairsInTemplate.merge(newPair2, 1L, Long::sum);
                }
            }
        }

        Map<Character, Long> counts = new HashMap<>();
        for (var entry : pairsInTemplate.entrySet()) {
            char c = entry.getKey().charAt(0);
            long count = entry.getValue();
            counts.merge(c, count, Long::sum);
        }

        long maxCount = counts.entrySet()
                             .stream()
                             .max(Map.Entry.comparingByValue())
                             .get()
                             .getValue();

        long minCount = counts.entrySet()
                              .stream()
                              .min(Map.Entry.comparingByValue())
                              .get()
                              .getValue();


        System.out.println(maxCount - minCount);
    }

    private static void convertLines(List<String> lines) {

        TEMPLATE = lines.get(0);

        for (int i = 2; i < lines.size(); i++) {
            String[] pieces = lines.get(i).split(" -> ");
            RULES.put(pieces[0], pieces[1].charAt(0));
        }

    }


}
