package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author dragos
 */
@Slf4j
public class Day5 {

    private static final Logger logger = LoggerFactory.getLogger(Day5.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day5Input.txt");

        List<String> list = Files.readAllLines(path);

        Map<Integer, List<Integer>> beforeNums = new HashMap<>();
        List<String> pages = new ArrayList<>();

        boolean checkRules = false;
        for (String s : list) {
            if (s.trim().isEmpty()) {
                checkRules = true;
                continue;
            }

            if (!checkRules) {
                addRule(s, beforeNums);
            } else {
                pages.add(s);
            }
        }


        logger.debug("Day 5 part 1 result: {}", getFirstPartAnswer(pages, beforeNums));
        logger.debug("Day 5 part 2 result: {}", getSecondPartAnswer(pages, beforeNums));

    }

    private static int getFirstPartAnswer(List<String> pages, Map<Integer, List<Integer>> beforeNums) {
        int sum = 0;

        for (String s : pages) {
            sum += verifyAndGetMiddle(s, beforeNums);
        }
        return sum;
    }

    private static void addRule(String s, Map<Integer, List<Integer>> beforeNums) {
        int first = Integer.parseInt(s.substring(0, 2));
        int second = Integer.parseInt(s.substring(3, 5));

        List<Integer> beforeList = beforeNums.get(second);
        if (beforeList == null) {
            beforeList = new ArrayList<>();
        }
        beforeList.add(first);
        beforeNums.put(second, beforeList);
    }

    private static int verifyAndGetMiddle(String s, Map<Integer, List<Integer>> beforeNums) {
        List<Integer> row = Arrays.stream(s.split(",")).map(Integer::parseInt).toList();
        for (int i = 0; i < row.size(); i++) {
            for (int j = 0; j < row.size(); j++) {
                if (j > i && !isBeforeListValid(beforeNums, row.get(i), row.get(j))) {
                    return 0;
                }
            }
        }

        return row.get((row.size() - 1) / 2);
    }

    private static boolean isBeforeListValid(Map<Integer, List<Integer>> beforeNums, Integer curr, Integer toCheck) {
        List<Integer> beforeList = beforeNums.get(curr);
        if (beforeList == null || beforeList.isEmpty()) {
            return true;
        }
        return !beforeList.contains(toCheck);
    }

    private static int getSecondPartAnswer(List<String> pages, Map<Integer, List<Integer>> beforeNums) {
        List<String> notSortedPages = new ArrayList<>();

        for (String s : pages) {
            if (verifyAndGetMiddle(s, beforeNums) == 0) {
                notSortedPages.add(s);
            }
        }

        int sum = 0;
        for (String s : notSortedPages) {
            sum += sortAndGetMiddle(s, beforeNums);
        }

        return sum;
    }

    private static int sortAndGetMiddle(String s, Map<Integer, List<Integer>> beforeNums) {
        List<Integer> row = new ArrayList<>(Arrays.stream(s.split(",")).map(Integer::parseInt).toList());
        row.sort((o1, o2) -> {
            List<Integer> beforeList = beforeNums.get(o1);
            if (o1.equals(o2)) {
                return 0;
            }
            return beforeList != null && !beforeList.isEmpty() && beforeList.contains(o2) ? 1 : -1;
        });
        return row.get((row.size() - 1) / 2);
    }
}