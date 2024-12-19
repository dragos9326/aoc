package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dragos
 */
@Slf4j
public class Day7 {

    private static final Logger logger = LoggerFactory.getLogger(Day7.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day7Input.txt");

        List<String> list = Files.readAllLines(path);

        logger.debug("Day 7 part 1 result: {}", getFirstPartAnswer(list));
        logger.debug("Day 7 part 2 result: {}", getSecondPartAnswer(list));

    }

    private static BigInteger getFirstPartAnswer(List<String> list) {
        BigInteger sum = BigInteger.ZERO;

        for (String s : list) {
            sum = sum.add(getValueIfValid(s, false));
        }

        return sum;
    }

    private static BigInteger getValueIfValid(String s, boolean concat) {
        String[] splts = s.split(":");
        BigInteger expected = new BigInteger(splts[0]);
        List<BigInteger> nos = Arrays.stream(splts[1].substring(1).split(" ")).map(BigInteger::new).toList();
        int step = 0;
        BigInteger sum = addNextFactor(BigInteger.ONE, step, expected, nos, new ArrayList<>(), Operations.MULTIPLY, concat)
                .add(addNextFactor(BigInteger.ZERO, step, expected, nos, new ArrayList<>(), Operations.ADD, concat))
                .add(concat ? addNextFactor(BigInteger.ZERO, step, expected, nos, new ArrayList<>(), Operations.CONCATENATION, concat) : BigInteger.ZERO);

        if (sum.compareTo(BigInteger.ZERO) > 0) {
            return expected;
        } else {
            return BigInteger.ZERO;
        }
    }

    private static BigInteger addNextFactor(BigInteger sum, int step, BigInteger expected, List<BigInteger> nos, List<Operations> prevOp, Operations currOp, boolean concat) {
        if (step == nos.size()) {
            if (sum.equals(expected)) {
                return BigInteger.ONE;
            } else {
                return BigInteger.ZERO;
            }
        }

        if (currOp == Operations.MULTIPLY) {
            sum = sum.multiply(nos.get(step));
        } else if (currOp == Operations.ADD) {
            sum = sum.add(nos.get(step));
        } else if (currOp == Operations.CONCATENATION) {
            String newSum = String.valueOf(sum) + nos.get(step);
            sum = new BigInteger(newSum);
        }

        prevOp.add(currOp);

        return addNextFactor(sum, step + 1, expected, nos, prevOp, Operations.MULTIPLY, concat)
                .add(addNextFactor(sum, step + 1, expected, nos, prevOp, Operations.ADD, concat))
                .add(concat ? addNextFactor(sum, step + 1, expected, nos, prevOp, Operations.CONCATENATION, concat) : BigInteger.ZERO);
    }

    private static BigInteger getSecondPartAnswer(List<String> list) {
        BigInteger sum = BigInteger.ZERO;

        for (String s : list) {
            sum = sum.add(getValueIfValid(s, true));
        }

        return sum;
    }
}