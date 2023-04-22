package puzzles2022.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Note: in general, since there is no order in a graph, we cannot allow duplicate values in it - we need something
 * to uniquely identify any given node of the graph. If a client would want to have a graph with duplicate values,
 * they would nevertheless need to put something unique to the graph, that is to distinguish the duplicate values
 * they would need to wrap all their values and decorating them with some other identifier.
 */
public abstract class Graph<T> {

    /**
     * based on the equals method of the generic type T
     */
    abstract public boolean contains(T value);

    abstract public List<T> shortestPath(T from, T to);

    abstract public Set<T> allNeighbors(T value);

    abstract public void addNode(T value);

    abstract public int size();

    void throwIfFound(T value) {
        if (contains(value)) {
            String msg = "Duplicate nodes are not allowed - %s already exists in graph".formatted(value);
            throw new IllegalArgumentException(msg);
        }
    }

    void throwIfNotFound(T value) {
        if (!contains(value)) {
            throw new IllegalArgumentException("Node %s is not found in graph".formatted(value));
        }
    }

    /**
     * Finds a breadth-first path from node {@code from} to node {@code to}.
     * <br>
     * Note: this works both on a weighted and an unweighted graph -however, only on a unweighted graph does the bfs
     * path correspond to the shortest path between the two nodes.
     *
     * @return a list of nodes starting with {@code from} and ending with {@code to} that describes a path connecting
     * the two nodes; the path is minimal in terms of the number of nodes contained; if no connection exists an
     * empty list is returned
     */
    public List<T> bfsPath(T from, T to) {
        Queue<T> searchQueue = new LinkedList<>(List.of(from));
        Set<T> processed = new HashSet<>(Set.of(from));
        Map<T, T> nodeToParent = new HashMap<>();

        T current = null;
        while (!searchQueue.isEmpty() && !to.equals(current)) {
            current = searchQueue.remove();
            for (T neighbor : allNeighbors(current)) {
                // need to check that the neighbor was not yet added to the search queue
                // imagine e.g. a 3x3 matrix
                // 000
                // 000
                // 000
                // when (0,0) is processed, (1,0) and (0,1) are in the search queue
                // when (1,0) is processed  (0,1), (2,0), (1, 1) are in the search queue
                // when (0,1) is processed  (2,0), (1,1) are still in the queue and (0,2), (1, 1) are the neighbors
                // but we dont want to add (1,1) again to the search queue
                if (!processed.contains(neighbor)) {
                    processed.add(neighbor);
                    searchQueue.add(neighbor);
                    nodeToParent.put(neighbor, current);
                }
            }
        }

        boolean connectionFound = to.equals(current);
        return connectionFound ? reconstructPath(to, nodeToParent) : new LinkedList<>();
    }

    Set<T> unvisitedNeighbors(T current, Set<T> visited) {
        return allNeighbors(current).stream()
                                    .filter(neighbor -> !visited.contains(neighbor))
                                    .collect(Collectors.toSet());
    }

    static <U> List<U> reconstructPath(U endNode, Map<U, U> nodeToParent) {
        List<U> path = new LinkedList<>();
        path.add(endNode);
        U current = endNode;
        while (nodeToParent.get(current) != null) {
            current = nodeToParent.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Checks by a depth-first-search if two nodes {@code from} and {@code to} are connected.
     */
    public boolean dfsIsConnected(T from, T to) {
        throwIfNotFound(from);
        throwIfNotFound(to);
        return dfsRecursive(from, to, new HashSet<>());
    }

    private boolean dfsRecursive(T candidate, T target, HashSet<T> visited) {
        if (candidate.equals(target)) {
            return true;
        }
        visited.add(candidate);
        for (T neighbor : unvisitedNeighbors(candidate, visited)) {
            if (dfsRecursive(neighbor, target, visited)) {
                return true;
            }
        }
        return false;
    }
}
