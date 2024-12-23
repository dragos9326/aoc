package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dragos
 */
@Slf4j
public class Day11 {

    private static final Logger logger = LoggerFactory.getLogger(Day11.class);
    private static final Map<BigInteger, Map<Integer, Long>> MEMO = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day11Input.txt");

        String input = Files.readString(path);
        List<BigInteger> numbers = Arrays.stream(input.split(" "))
                .map(BigInteger::new)
                .collect(Collectors.toCollection(LinkedList::new));

        logger.debug("Day 11 part 1 result: {}", getFirstPartAnswer(numbers));
        logger.debug("Day 11 part 2 result: {}", getSecondPartAnswer(numbers));
    }

    private static long getFirstPartAnswer(List<BigInteger> numbers) {
        return numbers.stream()
                .map(n -> blinkStone(n, 0, 0, 25))
                .reduce(Long::sum)
                .orElse(0L);
    }

    private static long getSecondPartAnswer(List<BigInteger> numbers) {
        return numbers.stream()
                .map(n -> blinkStone(n, 0, 0, 75))
                .reduce(Long::sum)
                .orElse(0L);
    }

    private static long blinkStone(BigInteger n, long sum, int currStep, int maxSteps) {
        if (currStep >= maxSteps) {
            return 1;
        }

        Map<Integer, Long> hist = MEMO.get(n);
        if (hist != null && hist.containsKey(maxSteps - currStep)) {
            return hist.get(maxSteps - currStep);
        }

        List<BigInteger> nextStones = getStonesAfterBlink(n);
        long interSum = nextStones.stream()
                .map(s -> blinkStone(s, sum, currStep + 1, maxSteps))
                .reduce(Long::sum)
                .orElse(0L);

        if (hist == null) {
            hist = new HashMap<>();
        }
        hist.put(maxSteps - currStep, interSum);
        MEMO.put(n, hist);
        return sum + interSum;
    }

    private static List<BigInteger> getStonesAfterBlink(BigInteger stone) {
        List<BigInteger> toRet = new LinkedList<>();
        if (stone.compareTo(BigInteger.ZERO) == 0) {
            toRet.add(BigInteger.ONE);
        } else if (String.valueOf(stone).length() % 2 == 0) {
            String no = String.valueOf(stone);
            toRet.add(new BigInteger(no.substring(0, no.length() / 2)));
            toRet.add(new BigInteger(no.substring(no.length() / 2)));
        } else {
            toRet.add(stone.multiply(BigInteger.valueOf(2024)));
        }

        return toRet;
    }

}