package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {

    public static HashMap<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day12Input.txt");

        List<String> list = Files.readAllLines(path);

        System.out.println("Day 12 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 12 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static long getFirstPartAnswer(List<String> list) {
        long sum = 0;

        cache.clear();

        for (String line : list) {
            List<Integer> groups = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::valueOf).toList();
            String row = line.split(" ")[0];

            sum += findCombinations(row, groups);
        }

        return sum;
    }

    private static long findCombinations(String row, List<Integer> groups) {
        String line = row + " " + String.join(",", groups.stream().map(String::valueOf).toList());
        if (cache.containsKey(line)) {
            return cache.get(line);
        }

        if (row.isEmpty()) {
            return groups.isEmpty() ? 1 : 0;
        }
        char firstChar = row.charAt(0);
        long permutations = 0;
        if (firstChar == '.') {
            //just skip
            permutations = findCombinations(row.substring(1), groups);
        } else if (firstChar == '?') {
            //add for working and damaged
            permutations = findCombinations("." + row.substring(1), groups)
                    + findCombinations("#" + row.substring(1), groups);
        } else {
            //is #
            if (!groups.isEmpty()) {
                int nrDamaged = groups.get(0);
                if (nrDamaged <= row.length()
                        && row.chars().limit(nrDamaged).allMatch(c -> c == '#' || c == '?')) {
                    List<Integer> newGroups = groups.subList(1, groups.size());
                    if (nrDamaged == row.length()) {
                        //length is the same, if no more gorups, return 1;
                        permutations = newGroups.isEmpty() ? 1 : 0;
                    } else if (row.charAt(nrDamaged) == '.') {
                        //added a damaged(#) and the enxt is operational(.), so skip to next
                        permutations = findCombinations(row.substring(nrDamaged + 1), newGroups);
                    } else if (row.charAt(nrDamaged) == '?') {
                        //added a damaged(#) and the next is unknown(?), so it can only be operational(.)
                        permutations = findCombinations("." + row.substring(nrDamaged + 1), newGroups);
                    }
                }
            }
        }

        cache.put(line, permutations);

        return permutations;


    }

    private static long getSecondPartAnswer(List<String> list) {
        long sum = 0;

        for (String line : list) {
            List<Integer> groups = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::valueOf).toList();
            String row = line.split(" ")[0];

            sum += findCombinations(unfoldRow(row, 5), unfoldGroups(groups, 5));
        }

        return sum;
    }

    private static List<Integer> unfoldGroups(List<Integer> groups, int times) {
        List<Integer> unfoldedGroups = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            unfoldedGroups.addAll(groups);
        }
        return unfoldedGroups;
    }

    private static String unfoldRow(String row, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times - 1; i++) {
            sb.append(row);
            sb.append("?");
        }
        sb.append(row);

        return sb.toString();
    }

}