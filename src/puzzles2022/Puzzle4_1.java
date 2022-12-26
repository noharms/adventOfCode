package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Puzzle4_1 {


    public static void main(String[] args) throws FileNotFoundException {
        List<TwoIntervals> twoIntervals = readInput();

        long countFullyContained = twoIntervals.stream()
                .filter(
                        pair -> TwoIntervals.isXFullyContainedInY(pair.interval1, pair.interval2) ||
                                TwoIntervals.isXFullyContainedInY(pair.interval2, pair.interval1)
                )
                .count();

        System.out.println(countFullyContained);
    }

    private record Interval(int startIncl, int endIncl) {
        static Interval from(String interval) {
            String[] tokens = interval.split("-");
            int fromIncl = Integer.parseInt(tokens[0]);
            int endIncl = Integer.parseInt(tokens[1]);
            return new Interval(fromIncl, endIncl);
        }
    }

    private record TwoIntervals(Interval interval1, Interval interval2) {
        static boolean isXFullyContainedInY(Interval x, Interval y) {
            return x.startIncl >= y.startIncl && x.endIncl <= y.endIncl;
        }
    }

    private static List<TwoIntervals> readInput() throws FileNotFoundException {
        File inputFile = new File("resources\\2022\\input_puzzle4.txt");
        Scanner scanner = new Scanner(inputFile);
        List<TwoIntervals> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(",");
            Interval interval1 = Interval.from(tokens[0]);
            Interval interval2 = Interval.from(tokens[1]);
            result.add(new TwoIntervals(interval1, interval2));
        }
        return result;
    }

}
