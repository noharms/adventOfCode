package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle2_2 {

    public static void main(String[] args) throws FileNotFoundException {

        var commands = read();

        long horizontal = 0;
        long depth = 0;
        long aim = 0;
        for (var command : commands) {
            int val = command.getValue();
            Direction dir = command.getKey();
            if (dir.equals(Direction.UP)) {
                aim -= val;
            } else if (dir.equals(Direction.DOWN)) {
                aim += val;
            } else {
                horizontal += val;
                depth += aim * val;
            }
        }
        System.out.printf("%d %d %d", horizontal, depth, horizontal * depth);
    }

    public static List<Map.Entry<Direction, Integer>> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "example.txt");
        Scanner scanner = new Scanner(file);
        List<Map.Entry<Direction, Integer>> commands = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] input = line.split(" ");
            Direction direction = Direction.valueOf(input[0].toUpperCase(Locale.ROOT));
            int value = Integer.parseInt(input[1]);
            commands.add(new AbstractMap.SimpleEntry<Direction, Integer>(direction, value));
        }
        return commands;
    }

    public enum Direction {
        FORWARD,
        UP,
        DOWN
    }

    public static List<Integer> read2() throws FileNotFoundException {
        File file = new File("example.txt");
        Scanner scanner = new Scanner(file);
        List<Integer> integers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                integers.add(scanner.nextInt());
            } else {
                scanner.next();
            }
        }
        return integers;
    }
}
