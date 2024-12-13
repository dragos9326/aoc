package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day2 {

    static Map<String, Integer> maxCubes = new HashMap<>();

    static {
        maxCubes.put("red", 12);
        maxCubes.put("green", 13);
        maxCubes.put("blue", 14);
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day2Input.txt");

        List<String> list = Files.readAllLines(path);


        System.out.println("Day 2 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 2 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static int getFirstPartAnswer(List<String> list) {
        int sum = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                sum += checkTheResults(s);
            }
        }

        return sum;
    }

    private static int checkTheResults(String s) {
        int validGameId = 0;

        //cut between game number and results
        String[] divisions = s.split(":");
        if (divisions.length != 2) {
            return validGameId;
        }

        String[] results = divisions[1].split("[,;]");

        //check if any of the cubes drawn are maximum than allowed
        Optional<String> notValidResult = Arrays.stream(results).filter(res -> {
            String[] singleResult = res.split(" ");

            return Integer.valueOf(singleResult[1]).compareTo(maxCubes.get(singleResult[2])) > 0;
        }).findFirst();

        if (notValidResult.isPresent()) {
            return validGameId;
        }

        return Integer.parseInt(divisions[0].split(" ")[1]);
    }

    private static int getSecondPartAnswer(List<String> list) {
        int sum = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                sum += getThePower(s);
            }
        }

        return sum;
    }

    private static int getThePower(String s) {
        Map<String, Integer> minMap = new HashMap<>();

        //cut between game number and results
        String[] divisions = s.split(":");
        if (divisions.length != 2) {
            return 0;
        }

        String[] results = divisions[1].split("[,;]");

        //check if any of the cubes drawn are maximum than allowed
        for (String res : results) {
            String[] singleResult = res.split(" ");
            Integer prevMin = minMap.get(singleResult[2]) != null ? minMap.get(singleResult[2]) : 0;

            minMap.put(singleResult[2], Integer.valueOf(singleResult[1]).compareTo(prevMin) > 0 ? Integer.parseInt(singleResult[1]) : prevMin);
        }


        return minMap.values().stream().reduce(1, (a, b) -> a*b);
    }


}