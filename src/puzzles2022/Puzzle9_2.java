package puzzles2022;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class Puzzle9_2 {

    public static final Point START_POSITION = new Point(0, 0);

    public static void main(String[] args) throws IOException {
        List<Move> headMoves = readInput();

        Rope rope = new Rope();
        LinkedList<Point> pathTail = new LinkedList<>(List.of(START_POSITION));
        for (Move headMove : headMoves) {
            Move remainingMoves = headMove;
            while (remainingMoves.distance > 0) {
                Move oneStep = new Move(remainingMoves.direction, 1);
                remainingMoves = new Move(remainingMoves.direction, remainingMoves.distance - 1);

                rope.moveHeadBy(oneStep);
                pathTail.addLast(rope.getTailPosition());
            }
        }

        Set<Point> distinctPositions = new HashSet<>(pathTail);
        System.out.println(distinctPositions.size());
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
                case LEFT -> new Point(row, column - move.distance);
                case RIGHT -> new Point(row, column + move.distance);
                case UP -> new Point(row - move.distance, column );
                case DOWN -> new Point(row + move.distance, column );
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

    private static class Rope {

        private static final int N_KNOTS = 10;

        private LinkedList<Point> knotPositions;

        public Rope() {
            this.knotPositions = new LinkedList<>(Collections.nCopies(N_KNOTS, START_POSITION));
        }

        private static boolean isTouchingSuccessor(Point current, Point successor) {
            Point shift = current.minus(successor);
            return Math.abs(shift.row) <= 1 && Math.abs(shift.column) <= 1;
        }

        public void moveHeadBy(Move oneStep) {
            Point oldHeadPosition = knotPositions.getFirst();
            Point newHeadPosition = oldHeadPosition.moveBy(oneStep);
            knotPositions.set(0, newHeadPosition);
            dragSuccessorsAlong();
        }

        private void dragSuccessorsAlong() {
            for (int i = 1; i < N_KNOTS; i++) {
                Point current = knotPositions.get(i - 1);
                Point oldSuccessorPosition = knotPositions.get(i);
                Point newSuccessorPosition = dragAlong(current, oldSuccessorPosition);
                knotPositions.set(i, newSuccessorPosition);
            }
        }

        private static Point dragAlong(Point currentKnot, Point successorKnot) {
            if (isTouchingSuccessor(currentKnot, successorKnot)) {
                return successorKnot;
            }
            Point shift = currentKnot.minus(successorKnot);
            if (shift.row == 0) {
                if (shift.column > 1) {
                    return successorKnot.right();
                } else {
                    return successorKnot.left();
                }
            } else if (shift.column == 0) {
                if (shift.row > 1) {
                    return successorKnot.bottom();
                } else {
                    return successorKnot.top();
                }
            } else {
                if (shift.row < 0 && shift.column < 0) { // tail is bottom right from head
                    return successorKnot.topLeft();
                } else if (shift.row < 0 && shift.column > 0) { // tail is bottom left
                    return successorKnot.topRight();
                } else if (shift.row > 0 && shift.column < 0) { // tails is top right
                    return successorKnot.bottomLeft();
                } else if (shift.row > 0 && shift.column > 0) { // tail is top left
                    return successorKnot.bottomRight();
                }
            }
            throw new IllegalStateException("Unexpected case miss");
        }

        public Point getTailPosition() {
            return knotPositions.getLast();
        }
    }
}
