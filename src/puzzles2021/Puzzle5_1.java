package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Puzzle5_1 {

    public static void main(String[] args) throws FileNotFoundException {

        //List<Line> lines = read().stream().filter(line -> line.isHorizontal() || line.isVertical()).toList();

        List<Line> lines = read();

        int maxVal =
            lines.stream().map(line -> List.of(line.start.x, line.start.y, line.end.x, line.end.y))
                 .flatMap(Collection::stream)
                 .mapToInt(x -> x)
                 .max().orElse(1000) + 1;
        List<List<Integer>> board = new ArrayList<>();
        for (int i = 0; i < maxVal; ++i) {
            board.add(new ArrayList<>());
            for (int j = 0; j < maxVal; ++j) {
                board.get(i).add(0);
            }
        }

        for (var line : lines) {
            Point start = line.start;
            Point end = line.end;
            Point ascentReduced = line.computeAscent().reduceByGCD();
            Point hit = start;
            while (!hit.equals(end)) {
                int rowIndex = hit.y;
                int colIndex = hit.x;
                var oldVal = board.get(rowIndex).get(colIndex);
                board.get(rowIndex).set(colIndex, oldVal + 1);
                hit = new Point(hit.x + ascentReduced.x, hit.y + ascentReduced.y);
            }
            int rowIndex = hit.y;
            int colIndex = hit.x;
            var oldVal = board.get(rowIndex).get(colIndex);
            board.get(rowIndex).set(colIndex, oldVal + 1);
        }

        long countGtr1 = board.stream().flatMap(Collection::stream).filter(x -> x > 1).count();

        System.out.println(countGtr1);

    }

    private static List<Line> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "puzzle5_input.txt");
        Scanner scanner = new Scanner(file);

        List<Line> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" -> ");
            String[] point1 = parts[0].split(",");
            String[] point2 = parts[1].split(",");
            Point start = new Point(Integer.parseInt(point1[0]), Integer.parseInt(point1[1]));
            Point end = new Point(Integer.parseInt(point2[0]), Integer.parseInt(point2[1]));
            result.add(new Line(start, end));
        }
        return result;
    }

    public static record Line(Point start, Point end) {

        Point computeAscent() {
            return new Point(end.x - start.x, end.y - start.y);
        }

        boolean isHorizontal() {
            return start.y == end.y;
        }

        boolean isVertical() {
            return start.x == end.x;
        }
    }

    public static record Point(int x, int y) {

        public static int gcd(int x, int y) {
            return y == 0 ? x : gcd(y, x % y);
        }

        public Point reduceByGCD() {
            if (x == 0 && y == 0) {
                return this;
            } else if (x == 0) {
                return new Point(0, y > 0 ? 1 : -1);
            } else if (y == 0) {
                return new Point(x > 0 ? 1 : -1, 0);
            }
            int gcd = gcd(x < 0 ? -x : x, y < 0 ? -y : y);
            return new Point(x / gcd, y / gcd);
        }

    }


}
