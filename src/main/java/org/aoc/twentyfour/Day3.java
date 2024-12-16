package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dragos
 */
@Slf4j
public class Day3 {

    private static final Logger logger = LoggerFactory.getLogger(Day3.class);
    private static final Pattern MULT_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    private static final Pattern DO_PATTERN = Pattern.compile("(do|don't)\\(\\)");

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day3Input.txt");

        List<String> list = Files.readAllLines(path);

        logger.debug("Day 3 part 1 result: {}", getFirstPartAnswer(list));
        logger.debug("Day 3 part 2 result: {}", getSecondPartAnswer(list));

    }

    private static int getFirstPartAnswer(List<String> list) {
        int score = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                score += getResult(s.trim(), true, new AtomicBoolean(false));
            }
        }

        return score;
    }

    private static int getSecondPartAnswer(List<String> list) {
        int score = 0;

        AtomicBoolean dont = new AtomicBoolean(false);
        for (String s : list) {
            if (!s.trim().isEmpty()) {
                score += getResult(s.trim(), false, dont);
            }
        }

        return score;
    }

    private static int getResult(String trim, boolean ignoreDos, AtomicBoolean dont) {
        int sum = 0;
        int lastEnd = 0;
        Matcher m = MULT_PATTERN.matcher(trim);
        while (m.find()) {
            String first = m.group(1);
            String second = m.group(2);
            dont.set(shouldNotDo(trim, lastEnd, m.start(), dont));
            if (ignoreDos || !dont.get()) {
                sum += Integer.parseInt(first) * Integer.parseInt(second);
            }
            lastEnd = m.end();
        }

        return sum;
    }

    private static boolean shouldNotDo(String s, int start, int end, AtomicBoolean defValue) {
        if (end == 0 || start == 0) {
            return defValue.get();
        }

        boolean dont = defValue.get();
        String toCheck = s.substring(start, end);
        Matcher m = DO_PATTERN.matcher(toCheck);
        while (m.find()) {
            dont = m.group(0).startsWith("don't");
        }

        return dont;
    }
}