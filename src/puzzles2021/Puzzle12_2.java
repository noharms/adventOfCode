package puzzles2021;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle12_2 {

    public static void main(String[] args) throws FileNotFoundException {

        List<String> lines = PuzzleUtils.readLines(PuzzleUtils.RESOURCES_2021 + "puzzle12_input.txt");

        Map<String, List<String>> graph = convertInput(lines);

        int nDistinctPaths = findDistinctPaths(graph);

        System.out.println("nDistinctPaths = " + nDistinctPaths);

    }

    private static int findDistinctPaths(Map<String, List<String>> graph) {

        List<LinkedList<String>> paths = new ArrayList<>();

        LinkedList<String> currentPath = new LinkedList<>();
        currentPath.add("start");

        findPaths(graph, currentPath, paths);

        for (var path : paths) {
            System.out.println(path.toString());
        }

        return paths.size();
    }

    private static void findPaths(Map<String, List<String>> graph,
                                  LinkedList<String> currentPath,
                                  List<LinkedList<String>> paths) {
        String currentNode = currentPath.getLast();
        if (currentNode.equals("end")) {
            paths.add(new LinkedList<>(currentPath));
            return;
        }
        List<String> neighbors = graph.get(currentNode);
        for (var neighbor : neighbors) {
            if (!neighbor.equals("start") &&
                (isUpperCase(neighbor) || !currentPath.contains(neighbor) || !containsASmallCaveTwice(currentPath))) {
                currentPath.add(neighbor);
                findPaths(graph, currentPath, paths);
                currentPath.removeLast();
            }
        }
    }

    private static boolean containsASmallCaveTwice(LinkedList<String> currentPath) {
        return currentPath.stream()
                          .filter(str -> str.toLowerCase(Locale.ROOT).equals(str))
                          .collect(Collectors.groupingBy(str -> str, Collectors.counting()))
                          .values()
                          .stream()
                          .anyMatch(x -> x > 1);
    }

    private static boolean isUpperCase(String neighbor) {
        return neighbor.toUpperCase(Locale.ROOT).equals(neighbor);
    }

    private static Map<String, List<String>> convertInput(List<String> lines) {
        Map<String, List<String>> graph = new HashMap<>();
        for (var line : lines) {
            String[] nodes = line.split("-");
            String node1 = nodes[0];
            String node2 = nodes[1];
            graph.computeIfAbsent(node1, str -> new ArrayList<>()).add(node2);
            graph.computeIfAbsent(node2, str -> new ArrayList<>()).add(node1);
        }
        return graph;
    }
}
