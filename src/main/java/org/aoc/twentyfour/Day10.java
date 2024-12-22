package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dragos
 */
@Slf4j
public class Day10 {

    private static final Logger logger = LoggerFactory.getLogger(Day10.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day10Input.txt");

        List<String> list = Files.readAllLines(path);
        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }


        logger.debug("Day 10 part 1 result: {}", getFirstPartAnswer(matrix));
        logger.debug("Day 10 part 2 result: {}", getSecondPartAnswer(matrix));
    }

    private static int getFirstPartAnswer(char[][] matrix) {
        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                char ch = matrix[i][j];
                if (ch == '0') {
                    Set<Point> peaks = new HashSet<>();
                    walkTrail(matrix, i + 1, j, ch, peaks);
                    walkTrail(matrix, i, j + 1, ch, peaks);
                    walkTrail(matrix, i - 1, j, ch, peaks);
                    walkTrail(matrix, i, j - 1, ch, peaks);
                    sum += peaks.size();
                }
            }
        }

        return sum;
    }

    private static long getSecondPartAnswer(char[][] matrix) {
        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                char ch = matrix[i][j];
                if (ch == '0') {
                    sum += getTrailheadRating(matrix, i + 1, j, ch)
                        + getTrailheadRating(matrix, i, j + 1, ch)
                        + getTrailheadRating(matrix, i - 1, j, ch)
                        + getTrailheadRating(matrix, i, j - 1, ch);

                }
            }
        }

        return sum;
    }

    private static void walkTrail(char[][] matrix, int i, int j, char prevNo, Set<Point> peaks) {
        if (i < 0 || i >= matrix.length) {
            return;
        }

        if (j < 0 || j >= matrix.length) {
            return;
        }

        char currCh = matrix[i][j];

        if (currCh - 1 != prevNo) {
            return;
        }

        if (currCh == '9') {
            peaks.add(new Point(i, j, currCh));
            return;
        }

        walkTrail(matrix, i + 1, j, currCh, peaks);
        walkTrail(matrix, i, j + 1, currCh, peaks);
        walkTrail(matrix, i - 1, j, currCh, peaks);
        walkTrail(matrix, i, j - 1, currCh, peaks);
    }

    private static int getTrailheadRating(char[][] matrix, int i, int j, char prevNo) {
        if (i < 0 || i >= matrix.length) {
            return 0;
        }

        if (j < 0 || j >= matrix.length) {
            return 0;
        }

        char currCh = matrix[i][j];

        if (currCh - 1 != prevNo) {
            return 0;
        }

        if (currCh == '9') {
            return 1;
        }

        return getTrailheadRating(matrix, i + 1, j, currCh)
                + getTrailheadRating(matrix, i, j + 1, currCh)
                + getTrailheadRating(matrix, i - 1, j, currCh)
                + getTrailheadRating(matrix, i, j - 1, currCh);
    }

}