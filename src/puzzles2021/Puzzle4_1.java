package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle4_1 {

    private static List<Integer> DRAWN_NUMS;
    private static final List<Board> BOARDS = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        read();

        boolean winnerFound = false;
        for (var drawnNum : DRAWN_NUMS) {
            for (Iterator<Board> iter = BOARDS.listIterator(); iter.hasNext(); ) {
                Board board = iter.next();
                if (board.process(drawnNum)) {
                    System.out.println("Board " + board.id + " wins with :" + board.score());
                    if (BOARDS.size() > 1) {
                        iter.remove();
                    } else {
                        System.out.println("Last board is " + board.id + " with score " + board.score());
                        winnerFound = true;
                    }
                }
            }
            if (winnerFound) {
                break;
            }
        }

    }

    public static void read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "puzzle4_input.txt");
        Scanner scanner = new Scanner(file);

        // first line
        String firstLine = scanner.nextLine();
        String[] nums = firstLine.split(",");
        DRAWN_NUMS = stringsToIntegerList(nums);

        // boards
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                Board board = new Board();
                for (int i = 0; i < 5; ++i) {
                    String boardLine = scanner.nextLine();
                    String[] boardNums = boardLine.split(" ");
                    board.addRow(stringsToIntegerList(boardNums));
                }
                BOARDS.add(board);
            }
        }
    }

    private static List<Integer> stringsToIntegerList(String[] nums) {
        return Arrays.stream(nums).filter(str -> !str.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static class Board {

        public final Integer id;
        private List<List<Cell>> cells = new ArrayList<>();

        private Cell lastDrawn = null;

        public Board() {
            this.id = BOARDS.size() - 1;
        }

        public void addRow(List<Integer> integerList) {
            List<Cell> cellsInRow = integerList.stream().map(val -> new Cell(val, false)).toList();
            cells.add(cellsInRow);
        }

        /**
         * @param rowIndex from left 0 to right n
         * @param colIndex from top 0 to bottom m
         */
        public boolean wasDrawn(int rowIndex, int colIndex) {
            return cells.get(rowIndex).get(colIndex).drawn;
        }

        public boolean hasCompleteRow() {
            for (int i = 0; i < 5; ++i) {
                if (cells.get(i).stream().allMatch(cell -> cell.drawn)) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasCompleteColumn() {
            for (int i = 0; i < 5; ++i) {
                boolean allDrawn = true;
                for (int j = 0; j < 5; ++j) {
                    if (!cells.get(j).get(i).drawn) {
                        allDrawn = false;
                    }
                }
                if (allDrawn) {
                    return true;
                }
            }
            return false;
        }

        public int score() {
            int sum = 0;
            for (var row : cells) {
                sum += row.stream().filter(cell -> !cell.drawn).mapToInt(cell -> cell.value).sum();
            }
            return sum * lastDrawn.value;
        }

        public void setLastDrawn(int rowIndex, int colIndex) {
            this.lastDrawn = cells.get(rowIndex).get(colIndex);
        }

        public boolean process(Integer drawnNum) {
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.value == drawnNum) {
                        cell.drawn = true;
                        setLastDrawn(i, j);
                    }
                }
            }
            return hasCompleteColumn() || hasCompleteRow();
        }
    }

    private static class Cell {
        int value;
        boolean drawn;

        public Cell(int value, boolean drawn) {
            this.value = value;
            this.drawn = drawn;
        }

        public void setDrawn(boolean drawn) {
            this.drawn = drawn;
        }
    }
}
