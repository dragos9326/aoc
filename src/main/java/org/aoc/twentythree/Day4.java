package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day4 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day4Input.txt");

        List<String> list = Files.readAllLines(path);


        System.out.println("Day 4 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 4 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static int getFirstPartAnswer(List<String> list) {
        int sum = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                sum += getLineWorth(s);
            }
        }

        return sum;
    }

    private static int getLineWorth(String s) {
        long doubles = getLineMatches(s);
        if (doubles > 0) {
            return (int) Math.pow(2, (double) doubles - 1);
        } else {
            return 0;
        }
    }

    private static long getLineMatches(String s) {
        String[] cardSplit = s.split(":");
        String[] numbers = cardSplit[1].split("\\|");
        Set<Integer> drawnNumbers = Arrays.stream(numbers[1].split("\\s+")).filter(s1 -> !s1.isEmpty()).mapToInt(Integer::parseInt).boxed().collect(Collectors.toSet());

        return Arrays.stream(numbers[0].split("\\s+")).filter(s1 -> !s1.isEmpty()).mapToInt(Integer::parseInt).filter(i -> !drawnNumbers.add(i)).count();

    }

    private static int getSecondPartAnswer(List<String> list) {
        List<Long> matchesList = new ArrayList<>();
        for (String s : list) {
            if (!s.trim().isEmpty()) {
                matchesList.add(getLineMatches(s));
            }
        }

        int[][] matchesAndCopies = new int[2][matchesList.size()];
        //on 0 matches
        //on 1 current copies
        matchesAndCopies[0] = matchesList.stream().mapToInt(Long::intValue).toArray();
        for (int i = 0; i < matchesList.size(); i++) {
            matchesAndCopies[1][i] = 1;
        }

        for (int i = 0; i < matchesList.size(); i++) {
            //for each copy
            for (int j = 0; j < matchesAndCopies[1][i]; j++) {
                //add to the folowing
                for (int k = 0; k < matchesAndCopies[0][i]; k++) {
                    matchesAndCopies[1][k + i + 1] = matchesAndCopies[1][k + i + 1] + 1;
                }
            }
        }

        return Arrays.stream(matchesAndCopies[1]).reduce(Integer::sum).orElse(0);
    }

}