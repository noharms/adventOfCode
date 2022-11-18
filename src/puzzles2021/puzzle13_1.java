package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class puzzle13_1 {

    public static final List<Coordinate> DOT_COORS = new ArrayList<>();
    public static final List<Instruction> INSTRUCTIONS = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        read();

        List<Coordinate> foldedOnce = fold(DOT_COORS, INSTRUCTIONS.get(0));

        System.out.println((long) foldedOnce.size());


        // part 2

        List<Coordinate> old = DOT_COORS;
        List<Coordinate> folded = null;
        for (var instruction : INSTRUCTIONS) {
            folded = fold(old, instruction) ;
            old = folded;
        }

        int maxDim = folded.stream().flatMap(c -> Stream.of(c.x, c.y)).mapToInt(i -> i).max().orElseThrow() + 1;

        int[][] matrix = new int[maxDim][maxDim];
        for (var coor : folded) {
            matrix[coor.x][coor.y] = 1;
        }

        for (int i = 0; i < maxDim; ++i) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maxDim; ++j) {
                int val = matrix[i][j];
                if (val == 0) {
                    sb.append(".");
                } else {
                    sb.append("#");
                }
            }
            String line = sb.toString();
            System.out.println(line);
        }
    }

    private static List<Coordinate> fold(List<Coordinate> dotCoors, Instruction fold) {
        List<Coordinate> folded = new ArrayList<>();
        int mirror = fold.value;
        for (var coors : dotCoors) {
            if (fold.dir == Direction.Y) {
                int newX = coors.x;
                if (mirror > coors.y) {
                    if (!folded.contains(coors)) {
                        folded.add(coors); // keep old point
                    }
                } else if (mirror == coors.y) {
                    // do nothing
                } else { // mirror < coors.y
                    int distance = mirror - coors.y;
                    int newY = mirror + distance;
                    Coordinate newCoor = new Coordinate(newX, newY);
                    if (!folded.contains(newCoor)) {
                        folded.add(newCoor);
                    }
                }
            } else {
                int newY = coors.y;
                if (mirror > coors.x) {
                    if (!folded.contains(coors)) {
                        folded.add(coors); // keep old point
                    }
                } else if (mirror == coors.x) {
                    // do nothing
                } else { // mirror < coors.x
                    int distance = mirror - coors.x;
                    int newX = mirror + distance;
                    Coordinate newCoor = new Coordinate(newX, newY);
                    if (!folded.contains(newCoor)) {
                        folded.add(newCoor);
                    }
                }
            }
        }
        return folded;
    }

    static void read() throws FileNotFoundException {
        File file = new File("puzzle13_input.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("fold")) {
                String[] pieces = line.split("=");
                Instruction newInstruction;
                if (pieces[0].endsWith("x")) {
                    newInstruction = new Instruction(Direction.X, Integer.parseInt(pieces[1]));
                } else if (pieces[0].endsWith("y")) {
                    newInstruction = new Instruction(Direction.Y, Integer.parseInt(pieces[1]));
                } else {
                    throw new IllegalStateException("Err");
                }
                INSTRUCTIONS.add(newInstruction);
            } else if (!line.isEmpty()) {
                String[] pieces = line.split(",");
                DOT_COORS.add(new Coordinate(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1])));
            }
        }
    }

    public enum Direction {
        X,
        Y;
    }

    public static record Instruction(Direction dir, int value) {
    }

    public static record Coordinate(int x, int y) {
    }
}
