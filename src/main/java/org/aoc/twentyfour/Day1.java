package org.aoc.twentyfour;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

@Slf4j
public class Day1 {

    private static final Logger logger = LoggerFactory.getLogger(Day1.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day1Input.txt");

        List<String> list = Files.readAllLines(path);

        logger.debug("Day 1 part 1 result: {}", getFirstPartAnswer(list));
        logger.debug("Day 1 part 2 result: {}", getSecondPartAnswer(list));
    }

    private static int getFirstPartAnswer(List<String> list) {
        List<Integer> arr1 = new ArrayList<>();
        List<Integer> arr2 = new ArrayList<>();

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                String[] nums = s.trim().split("\\s+");
                arr1.add(Integer.valueOf(nums[0]));
                arr2.add(Integer.valueOf(nums[1]));
            }
        }

        Collections.sort(arr1);
        Collections.sort(arr2);

        return IntStream.range(0, arr1.size())
                .map(i -> Math.abs(arr2.get(i) - arr1.get(i)))
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static int getSecondPartAnswer(List<String> list) {
        List<Integer> arr1 = new ArrayList<>();
        Map<Integer, Integer> pond = new HashMap<>();

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                String[] nums = s.trim().split("\\s+");
                arr1.add(Integer.valueOf(nums[0]));
                Integer num2 = Integer.valueOf(nums[1]);
                Integer times = pond.containsKey(num2) ? pond.get(num2) + 1 : 1;
                pond.put(num2, times);
            }
        }

        return arr1.stream()
                .map(a -> a * Optional.ofNullable(pond.get(a)).orElse(0))
                .reduce(Integer::sum)
                .orElse(0);
    }
}