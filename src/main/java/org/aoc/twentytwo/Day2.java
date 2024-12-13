package org.aoc.twentytwo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day2 {

    static int[] toWin = {2, 0, 1};
    static int[] toLose = {1, 2, 0};

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentytwo/day2Input.txt");
        int totalScorePart1 = 0;
        int totalScorePart2 = 0;

        List<String> list = Files.readAllLines(path);
        for(String s : list) {
            totalScorePart1 += getTotalScoreOfLinePart1(s);
            totalScorePart2 += getTotalScoreOfLinePart2(s);
        }


        System.out.println("Day 2 part 1 result: " + totalScorePart1);
        System.out.println("Day 2 part 2 result: " + totalScorePart2);

    }

    private static int getTotalScoreOfLinePart1(String s) {
        int finalScore = s.charAt(2) - 'X' + 1;

        if(toWin[s.charAt(2) - 'X'] == s.charAt(0) - 'A'){
            finalScore += 6;
        } else if((s.charAt(2) - 'X') == (s.charAt(0) - 'A')){
            finalScore += 3;
        }
        return finalScore;
    }

    private static int getTotalScoreOfLinePart2(String s) {
        int finalScore = (s.charAt(2) - 'X') * 3;

        int result = switch (s.charAt(2)) {
            case 'X' -> toWin[s.charAt(0) - 'A'] + 1;
            case 'Z' -> toLose[s.charAt(0) - 'A'] + 1;
            default -> s.charAt(0) - 'A' + 1;
        };


        return finalScore + result;
    }
}