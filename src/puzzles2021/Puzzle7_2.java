package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Math.abs;

public class Puzzle7_2 {

    public static void main(String[] args) throws FileNotFoundException {

        List<Integer> inputPositions = read();

        int sum = inputPositions.stream().mapToInt(x -> x).sum();

        int minPos = inputPositions.stream().mapToInt(x -> x).min().getAsInt();
        int maxPos = inputPositions.stream().mapToInt(x -> x).max().getAsInt();

        int minTotalDistance = Integer.MAX_VALUE;
        int possiblePositions = maxPos - minPos + 1;
        List<Integer> bestResult = new ArrayList<>();
        for (int i = minPos; i <= maxPos; ++i) {
            List<Integer> distancesToTarget = new ArrayList<>(Collections.nCopies(possiblePositions, 0));
            for (int j = 0; j < inputPositions.size(); ++j) {
                int distance = abs(inputPositions.get(j) - i);
                int fuel = distance * (distance + 1) / 2;
                distancesToTarget.set(j, fuel);
            }
            int totalDistances = distancesToTarget.stream().mapToInt(x -> x).sum();
            if (totalDistances < minTotalDistance) {
                minTotalDistance = totalDistances;
                bestResult = distancesToTarget;
            }
        }

        System.out.println(minTotalDistance);
//        System.out.println(bestResult.stream().mapToInt(x -> x).sum());

    }

    static List<Integer> read() throws FileNotFoundException {
        File file = new File("puzzle7_input.txt");
        Scanner scanner = new Scanner(file);

        String line = scanner.nextLine();
        String[] numsAsString = line.split(",");

        return Arrays.stream(numsAsString).map(Integer::parseInt).toList();
    }
}
