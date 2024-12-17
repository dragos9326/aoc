package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author dragos
 */
@Slf4j
public class Day4 {

    private static final Logger logger = LoggerFactory.getLogger(Day4.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day4Input.txt");

        List<String> list = Files.readAllLines(path);
        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        logger.debug("Day 4 part 1 result: {}", getFirstPartAnswer(matrix));
        logger.debug("Day 4 part 2 result: {}", getSecondPartAnswer(matrix));

    }

    private static int getFirstPartAnswer(char[][] matrix) {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sum += getXmasVertScore(matrix, i, j);
                sum += getXmasDiagBackScore(matrix, i, j);
                sum += getXmasDiagForwardScore(matrix, i, j);
                sum += getXmasHorizScore(matrix, i, j);
            }
        }

        return sum;
    }

    private static int getXmasHorizScore(char[][] matrix, int i, int j) {
        if (j < 3) {
            return 0;
        }

        String word = "" + matrix[i][j - 3] + matrix[i][j - 2] + matrix[i][j - 1] + matrix[i][j];
        if ("XMAS".equals(word) || "SAMX".equals(word)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getXmasDiagBackScore(char[][] matrix, int i, int j) {
        if (j < 3 || i < 3) {
            return 0;
        }

        String word = "" + matrix[i - 3][j - 3] + matrix[i - 2][j - 2] + matrix[i - 1][j - 1] + matrix[i][j];
        if ("XMAS".equals(word) || "SAMX".equals(word)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getXmasDiagForwardScore(char[][] matrix, int i, int j) {
        if (j > matrix.length - 4 || i < 3) {
            return 0;
        }

        String word = "" + matrix[i - 3][j + 3] + matrix[i - 2][j + 2] + matrix[i - 1][j + 1] + matrix[i][j];
        if ("XMAS".equals(word) || "SAMX".equals(word)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getXmasVertScore(char[][] matrix, int i, int j) {
        if (i < 3) {
            return 0;
        }

        String word = "" + matrix[i - 3][j] + matrix[i - 2][j] + matrix[i - 1][j] + matrix[i][j];
        if ("XMAS".equals(word) || "SAMX".equals(word)) {
            return 1;
        } else {
            return 0;
        }
    }


    private static int getSecondPartAnswer(char[][] matrix) {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (isXmasIntersection(matrix, i, j)) {
                    sum++;
                }
            }
        }

        return sum;
    }

    private static boolean isXmasIntersection(char[][] matrix, int i, int j) {
        if (i > matrix.length - 2 || i < 1) {
            return false;
        }
        if (j > matrix.length - 2 || j < 1) {
            return false;
        }
        if ('A' != matrix[i][j]) {
            return false;
        }

        String diag1 = "" + matrix[i - 1][j - 1] + matrix[i][j] + matrix[i + 1][j + 1];
        String diag2 = "" + matrix[i - 1][j + 1] + matrix[i][j] + matrix[i + 1][j - 1];
        return ("MAS".equals(diag1) || "SAM".equals(diag1)) && ("MAS".equals(diag2) || "SAM".equals(diag2));
    }

}