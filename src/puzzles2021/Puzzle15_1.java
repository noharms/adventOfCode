package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Puzzle15_1 {

    public static void main(String[] args) throws FileNotFoundException {

        List<List<Integer>> nodes = read();
        int nRows = nodes.size();
        int nCols = nodes.get(0).size();

        Set<Coordinates> processed = new HashSet<>();
        processed.add(new Coordinates(0, 0));

        List<List<Integer>> nodesToCosts = new ArrayList<>();
        for (int i = 0; i < nRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < nCols; j++) {
                row.add(Integer.MAX_VALUE);
            }
            nodesToCosts.add(row);
        }
        nodesToCosts.get(0).set(0, nodes.get(0).get(0));
        nodesToCosts.get(0).set(1, nodes.get(0).get(1));
        nodesToCosts.get(1).set(0, nodes.get(1).get(0));

        List<List<Coordinates>> nodesToParents = new ArrayList<>();
        for (int i = 0; i < nRows; i++) {
            nodesToParents.add(new ArrayList<>());
            for (int j = 0; j < nCols; j++) {
                nodesToParents.get(i).add(null);
            }
        }
        nodesToParents.get(0).set(1, new Coordinates(0, 0));
        nodesToParents.get(1).set(0, new Coordinates(0, 0));


        Optional<Coordinates> next = findCheapestUnvisitedCell(nodesToCosts, processed);
        while (next.isPresent()) {
            Coordinates coors = next.get();
            Integer cost = nodesToCosts.get(coors.rowIndex).get(coors.colIndex);
            Set<Coordinates> neighbors = getNeighbors(coors);
            for (Coordinates neighbor : neighbors) {
                if (neighbor.colIndex >= 0 &&
                    neighbor.colIndex < nCols &&
                    neighbor.rowIndex >= 0 &&
                    neighbor.rowIndex < nRows) {
                    if (!processed.contains(neighbor)) {
                        Integer toNeighborCost = nodes.get(neighbor.rowIndex).get(neighbor.colIndex);
                        Integer totalNeighborCost = cost + toNeighborCost;
                        if (totalNeighborCost < nodesToCosts.get(neighbor.rowIndex).get(neighbor.colIndex)) {
                            nodesToCosts.get(neighbor.rowIndex).set(neighbor.colIndex, totalNeighborCost);
                            nodesToParents.get(neighbor.rowIndex).set(neighbor.colIndex, coors);
                        }
                    }
                }
            }
            processed.add(coors);
            next = findCheapestUnvisitedCell(nodesToCosts, processed);
        }

        System.out.println("hello " + nodesToCosts.get(nRows - 1).get(nCols - 1));

        Coordinates parent = nodesToParents.get(nRows - 1).get(nCols - 1);
        while (parent != null) {
            System.out.println(parent.rowIndex + " " + parent.colIndex);
            parent = nodesToParents.get(parent.rowIndex).get(parent.colIndex);
        }
    }

    private static Set<Coordinates> getNeighbors(Coordinates coors) {
        return Set.of(new Coordinates(coors.rowIndex, coors.colIndex + 1),
                      new Coordinates(coors.rowIndex + 1, coors.colIndex),
                      new Coordinates(coors.rowIndex, coors.colIndex - 1),
                      new Coordinates(coors.rowIndex - 1, coors.colIndex));
    }

    private static Optional<Coordinates> findCheapestUnvisitedCell(List<List<Integer>> nodesToCosts,
                                                                   Set<Coordinates> visited) {
        Coordinates cheapest = null;
        int cheapestCost = Integer.MAX_VALUE;
        for (int i = 0; i < nodesToCosts.size(); i++) {
            for (int j = 0; j < nodesToCosts.get(0).size(); j++) {
                Coordinates coors = new Coordinates(i, j);
                if (!visited.contains(coors)) {
                    int cost = nodesToCosts.get(i).get(j);
                    if (cost < cheapestCost) {
                        cheapestCost = cost;
                        cheapest = coors;
                    }
                }
            }
        }
        return Optional.ofNullable(cheapest);
    }


    private static List<List<Integer>> read() throws FileNotFoundException {
        File file = new File(PuzzleUtils.RESOURCES_2021 + "puzzle15_input.txt");
        Scanner scanner = new Scanner(file);

        List<List<Integer>> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            List<Integer> row = new ArrayList<>();
            for (var c : line.toCharArray()) {
                Integer digit = Integer.parseInt(String.valueOf(c));
                row.add(digit);
            }
            result.add(row);
        }
        return result;
    }

    static record Coordinates(int rowIndex, int colIndex) {
    }

    static record Cell(Coordinates coors, int value) {
    }
}
