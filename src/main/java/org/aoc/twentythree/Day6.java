package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day6Input.txt");

        List<String> list = Files.readAllLines(path);

        System.out.println("Day 6 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 6 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static List<Long> getNumbersListFromLine(String s) {
        return Arrays.stream(s.split(":")[1].strip().split("\\s+")).map(Long::parseLong).toList();
    }

    private static long getFirstPartAnswer(List<String> list) {
        List<Long> time = getNumbersListFromLine(list.get(0));
        List<Long> distance = getNumbersListFromLine(list.get(1));

        int prod = 0;

        for (int i = 0; i < time.size(); i++) {
            int ways = getNoOfWaysToBeatRecord(time, distance, i);
            if (prod != 0 && ways != 0) {
                prod *= ways;
            } else if (ways != 0) {
                prod += ways;
            }
        }

        return prod;
    }

    private static int getNoOfWaysToBeatRecord(List<Long> time, List<Long> distance, int index) {
        Long raceTime = time.get(index);
        Long raceDistance = distance.get(index);
        int ways = 0;

        for (int i = 0; i < raceTime; i++) {
            if (i * (raceTime - i) > raceDistance) {
                ways++;
            }
        }

        return ways;
    }

    private static long getSecondPartAnswer(List<String> list) {
        long time = Long.parseLong(list.get(0).split(":")[1].strip().replaceAll("\\s+", ""));
        long distance = Long.parseLong(list.get(1).split(":")[1].strip().replaceAll("\\s+", ""));

        int ways = 0;

        for (int i = 0; i < time; i++) {
            if (i * (time - i) > distance) {
                ways++;
            }
        }

        return ways;
    }

}