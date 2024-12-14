package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author dragos
 */
@Slf4j
public class Day2 {

    private static final Logger logger = LoggerFactory.getLogger(Day2.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day2Input.txt");

        List<String> list = Files.readAllLines(path);

        logger.debug("Day 2 part 1 result: {}", getFirstPartAnswer(list));
        logger.debug("Day 2 part 2 result: {}", getSecondPartAnswer(list));
    }

    private static int getFirstPartAnswer(List<String> list) {
        int safetyScore = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                String[] nums = s.split("\\s+");
                int sc = getSafetyScore(nums);
                safetyScore += sc;
            }
        }

        return safetyScore;
    }

    private static int getSecondPartAnswer(List<String> list) {
        int safetyScore = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                String[] nums = s.split("\\s+");
                int sc = getSafetyScore(nums);
                if (sc == 0) {
                    //try to remove each element and test the new arr
                    sc = IntStream.range(0, nums.length)
                            .mapToObj(
                                    i ->
                                            IntStream.range(0, nums.length)
                                                    .filter(index -> index != i)
                                                    .mapToObj(index -> nums[index])
                                                    .toArray(String[]::new))
                            .anyMatch(arr -> getSafetyScore(arr) > 0) ? 1 : 0;
                }
                safetyScore += sc;
            }
        }

        return safetyScore;
    }

    private static int getSafetyScore(String[] nums) {
        boolean asc = isAsc(nums);

        for (int i = 1; i < nums.length; i++) {
            if (!isNumbersOk(Integer.parseInt(nums[i]), Integer.parseInt(nums[i - 1]), asc)) {
                return 0;
            }
        }

        return 1;
    }

    private static boolean isAsc(String[] nums) {
        return Integer.parseInt(nums[1]) - Integer.parseInt(nums[0]) > 0;
    }

    private static boolean isNumbersOk(int no1, int no2, boolean asc) {

        int diff = no1 - no2;
        if (Math.abs(diff) > 3 || Math.abs(diff) < 1) {
            return false;
        }
        if (asc && diff < 0) {
            return false;
        }

        if (!asc && diff > 0) {
            return false;
        }

        return true;
    }
}