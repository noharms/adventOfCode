package puzzles2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Puzzle16_1 {

    public static void main(String[] args) throws FileNotFoundException {

        read();

    }

    static void read() throws FileNotFoundException {
        File file = new File("puzzle16_input.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
        }
    }

}
