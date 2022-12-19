package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class Puzzle9_2 {

    public static void main(String[] args) throws IOException {
        List<Move> headMoves = readInput();

        Point headPosition = new Point(0, 0);
        Point tailPosition = new Point(0, 0);
        LinkedList<Point> pathTail = new LinkedList<>(List.of(tailPosition));
        for (Move headMove : headMoves) {
            Move remainingMoves = headMove;
            while (remainingMoves.distance > 0) {
                Move oneStep = new Move(remainingMoves.direction, 1);
                remainingMoves = new Move(remainingMoves.direction, remainingMoves.distance - 1);

                headPosition = headPosition.moveBy(oneStep);
                tailPosition = dragTail(headPosition, tailPosition);
                pathTail.addLast(tailPosition);
            }
        }

        Set<Point> distinctPositions = new HashSet<>(pathTail);
        System.out.println(distinctPositions.size());
    }

    private static Point dragTail(Point headPosition, Point tailPosition) {
        if (areTouching(headPosition, tailPosition)) {
            return tailPosition;
        }
        Point shift = headPosition.minus(tailPosition);
        if (shift.row == 0) {
            if (shift.column > 1) {
                return tailPosition.right();
            } else {
                return tailPosition.left();
            }
        } else if (shift.column == 0) {
            if (shift.row > 1) {
                return tailPosition.bottom();
            } else {
                return tailPosition.top();
            }
        } else {
            if (shift.row < 0 && shift.column < 0) { // tail is bottom right from head
                return tailPosition.topLeft();
            } else if (shift.row < 0 && shift.column > 0) { // tail is bottom left
                return tailPosition.topRight();
            } else if (shift.row > 0 && shift.column < 0) { // tails is top right
                return tailPosition.bottomLeft();
            } else if (shift.row > 0 && shift.column > 0) { // tail is top left
                return tailPosition.bottomRight();
            }
        }
        throw new IllegalStateException("Unexpected case miss");
    }

    private static boolean areTouching(Point headPosition, Point tailPosition) {
        Point shift = headPosition.minus(tailPosition);
        return Math.abs(shift.row) <= 1 && Math.abs(shift.column) <= 1;
    }

    private static List<Move> readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle9.txt");
        List<String> lines = Files.readAllLines(inputFile.toPath());
        List<Move> moves = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.split(" ");
            char directionCharacter = tokens[0].charAt(0);
            int distance = Integer.parseInt(tokens[1]);
            Move newMove = new Move(Direction.from(directionCharacter),
                                    distance);
            moves.add(newMove);
        }
        return moves;
    }

    private record Move(Direction direction, int distance) {
    }

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN;

        static Direction from(char c) {
            return switch (c) {
                case 'L' -> LEFT;
                case 'R' -> RIGHT;
                case 'U' -> UP;
                case 'D' -> DOWN;
                default -> throw new IllegalStateException("Unexpected value: " + c);
            };
        }
    }

    private record Point(int row, int column) {

        Point moveBy(Move move) {
            return switch (move.direction) {
                case LEFT -> new Point(row - move.distance, column);
                case RIGHT -> new Point(row + move.distance, column);
                case UP -> new Point(row, column - move.distance);
                case DOWN -> new Point(row, column + move.distance);
            };
        }

        Point minus(Point other) {
            return new Point(row - other.row, column - other.column);
        }

        Point topLeft() {
            return top().left();
        }

        Point topRight() {
            return top().right();
        }

        Point bottomLeft() {
            return bottom().left();
        }

        Point bottomRight() {
            return bottom().right();
        }

        Point top() {
            return new Point(row - 1, column);
        }

        Point bottom() {
            return new Point(row + 1, column);
        }

        Point left() {
            return new Point(row, column - 1);
        }

        Point right() {
            return new Point(row, column + 1);
        }
    }
}
