package puzzles2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static puzzles2022.Puzzle2_2.RPS.*;


public class Puzzle2_2 {

    public static void main(String[] args) throws FileNotFoundException {
        List<Pairing> pairings = readInput();

        int scorePlayer1 = pairings.stream().map(Pairing::scorePlayer2).reduce(0, Integer::sum);

        System.out.println(scorePlayer1);

    }

    enum RPS {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        private final int weaponScore;

        RPS(int weaponScore) {
            this.weaponScore = weaponScore;
        }

        private static RPS from(String letter) {
            return switch (letter) {
                case "A", "X" -> ROCK;
                case "B", "Y" -> PAPER;
                case "C", "Z" -> SCISSORS;
                default -> throw new IllegalStateException("Unexpected value: " + letter);
            };
        }
    }

    record Pairing(RPS player1, RPS player2) {

        private int gameResult() {
            if (player1.equals(player2)) {
                return 0;
            } else {
                if (player1.equals(ROCK)) {
                    if (player2.equals(PAPER)) {
                        return -1;
                    } else if (player2.equals(SCISSORS)) {
                        return 1;
                    }
                } else if (player1.equals(PAPER)) {
                    if (player2.equals(ROCK)) {
                        return 1;
                    } else if (player2.equals(SCISSORS)) {
                        return -1;
                    }
                } else if (player1.equals(SCISSORS)) {
                    if (player2.equals(ROCK)) {
                        return -1;
                    } else if (player2.equals(PAPER)) {
                        return 1;
                    }
                }
            }
            throw new IllegalArgumentException("Unexpected combi " + player1 + " " + player2);
        }

        private int scorePlayer2() {
            int weaponScore = player2.weaponScore;
            return weaponScore + switch (gameResult()) {
                case 1 -> 0;
                case 0 -> 3;
                case -1 -> 6;
                default -> throw new IllegalStateException("Unexpected game result value: " + gameResult());
            };
        }

    }


    private static List<Pairing> readInput() throws FileNotFoundException {
        File inputFile = new File("resources\\2022\\input_puzzle2_1.txt");
        Scanner scanner = new Scanner(inputFile);
        List<Pairing> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            RPS player1 = RPS.from(tokens[0]);
            int winLoseDrawPlayer2 = switch (tokens[1]) {
                case "X" -> -1;
                case "Y" -> 0;
                case "Z" -> 1;
                default -> throw new IllegalStateException("Unexpected " + tokens[1]);
            };
            RPS player2 = deducePlayer2Weapon(player1, winLoseDrawPlayer2);
            Pairing pairing = new Pairing(player1, player2);
            result.add(pairing);
        }
        return result;
    }

    private static RPS deducePlayer2Weapon(RPS player1, int winLoseDrawPlayer2) {
        if (winLoseDrawPlayer2 == 0) {
            return player1;
        } else {
            if (player1.equals(ROCK)) {
                if (winLoseDrawPlayer2 == -1) {
                    return SCISSORS;
                } else if (winLoseDrawPlayer2 == 1) {
                    return PAPER;
                }
            } else if (player1.equals(PAPER)) {
                if (winLoseDrawPlayer2 == -1) {
                    return ROCK;
                } else if (winLoseDrawPlayer2 == 1) {
                    return SCISSORS;
                }
            } else if (player1.equals(SCISSORS)) {
                if (winLoseDrawPlayer2 == -1) {
                    return PAPER;
                } else if (winLoseDrawPlayer2 == 1) {
                    return ROCK;
                }
            }
        }
        throw new IllegalArgumentException("Illegal combination");
    }

}
