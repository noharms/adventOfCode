package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Puzzle3_2 {

    public static void main(String[] args) throws FileNotFoundException {

        var asStrings = read();

        List<List<Integer>> asLists = new ArrayList<>();

        for (var numAsString : asStrings) {
            List<Integer> num = new ArrayList<>();
            for (int i = 0; i < numAsString.length(); ++i) {
                var character = numAsString.charAt(i);
                if (character == '0') {
                    num.add(0);
                } else {
                    num.add(1);
                }
            }
//            Collections.reverse(num);
            asLists.add(num);
        }

        // filter
        List<List<Integer>> sameBitAsMostCommonBit = new ArrayList<>(asLists);
        List<List<Integer>> notSameBitAsMostCommonBit = new ArrayList<>(asLists);


        int tryIndex = 0;
        while (sameBitAsMostCommonBit.size() > 1) {

            int counts = 0;
            for (var num : sameBitAsMostCommonBit) {
                if (num.get(tryIndex) == 1) {
                    counts++;
                }
            }


            boolean keep1s = counts >= sameBitAsMostCommonBit.size() - counts;

            for (Iterator<List<Integer>> iter = sameBitAsMostCommonBit.listIterator(); iter.hasNext(); ) {

                var num = iter.next();
                if (num.get(tryIndex).equals(1)) {
                    if (keep1s) {

                    } else {
                        iter.remove();
                    }
                } else {
                    if (keep1s) {
                        iter.remove();
                    }
                }
            }

            ++tryIndex;
        }

        tryIndex = 0;
        while (notSameBitAsMostCommonBit.size() > 1) {

            int counts = 0;
            for (var num : notSameBitAsMostCommonBit) {
                if (num.get(tryIndex) == 1) {
                    counts++;
                }
            }


            boolean keep1s = counts < notSameBitAsMostCommonBit.size() - counts;

            for (Iterator<List<Integer>> iter = notSameBitAsMostCommonBit.listIterator(); iter.hasNext(); ) {

                var num = iter.next();
                if (num.get(tryIndex).equals(1)) {
                    if (keep1s) {

                    } else {
                        iter.remove();
                    }
                } else {
                    if (keep1s) {
                        iter.remove();
                    }
                }
            }

            ++tryIndex;
        }


        double oxygen = 0;
        double co2 = 0;
        int maxPow = sameBitAsMostCommonBit.get(0).size() - 1;
        for (int i = sameBitAsMostCommonBit.get(0).size() - 1; i >= 0; --i) {
            oxygen += Math.pow(2, maxPow - i) * sameBitAsMostCommonBit.get(0).get(i);
            co2 += Math.pow(2, maxPow - i) * notSameBitAsMostCommonBit.get(0).get(i);
        }

        System.out.printf("%s %s %s%n", (long) oxygen * (long) co2, (long) oxygen, (long) co2);
    }


//    List<Integer> findRemaining(List<List<Integer>> all, List<Integer> mostCommonBit) {
//
//    }

    public static List<String> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "binaries.txt");
        Scanner scanner = new Scanner(file);
        List<String> strings = new ArrayList<>();
        while (scanner.hasNext()) {
            strings.add(scanner.next());
        }
        return strings;
    }
}
