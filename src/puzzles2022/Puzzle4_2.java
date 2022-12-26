package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Puzzle4_2 {


    public static void main(String[] args) throws FileNotFoundException {
        List<TwoIntervals> twoIntervals = readInput();

        long countOverlaps = twoIntervals.stream()
                .filter(TwoIntervals::hasOverlap)
                .count();

        System.out.println(countOverlaps);

        List<TwoIntervals> nonOverlap = twoIntervals.stream().filter(twoIntervals1 -> !twoIntervals1.hasOverlap()).toList();
        for (var list :
                nonOverlap) {
            System.out.println(list);
        }
    }

    private record Interval(int startIncl, int endIncl) {

        boolean contains(int value) {
            return value >= startIncl && value <= endIncl;
        }

        static Interval from(String interval) {
            String[] tokens = interval.split("-");
            int fromIncl = Integer.parseInt(tokens[0]);
            int endIncl = Integer.parseInt(tokens[1]);
            return new Interval(fromIncl, endIncl);
        }
    }

    private record TwoIntervals(Interval interval1, Interval interval2) {
        public boolean hasOverlap() {
            return interval1.contains(interval2.startIncl) || interval1.contains(interval2.endIncl) ||
                    interval2.contains(interval1.startIncl) || interval2.contains(interval1.endIncl);
        }

        @Override
        public String toString() {
            return "%d-%d, %d-%d".formatted(interval1.startIncl, interval1.endIncl, interval2.startIncl, interval2.endIncl);
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
