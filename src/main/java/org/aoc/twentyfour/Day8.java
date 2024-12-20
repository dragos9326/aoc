package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dragos
 */
@Slf4j
public class Day8 {

    private static final Logger logger = LoggerFactory.getLogger(Day8.class);

    private static final char MARK = '#';

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day8Input.txt");

        List<String> list = Files.readAllLines(path);
        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        logger.debug("Day 8 part 1 result: {}", getFirstPartAnswer(matrix));
        logger.debug("Day 8 part 2 result: {}", getSecondPartAnswer(matrix));

    }

    private static int getFirstPartAnswer(char[][] matrix) {
        HashMap<Character, List<Point>> groups = getGroups(matrix);
        char[][] marks = new char[matrix.length][matrix.length];
        for (Map.Entry<Character, List<Point>> entry : groups.entrySet()) {
            markAntinodes(marks, entry.getValue(), false);
        }

        int count = 0;
        for (char[] line : marks) {
            for (char ch : line) {
                if (ch == MARK) {
                    count++;
                }
            }
        }

        return count;
    }

    private static void markAntinodes(char[][] marks, List<Point> points, boolean harmonics) {
        if (points == null || points.isEmpty()) {
            return;
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (i == j) {
                    continue;
                }

                Point a = points.get(i);
                Point b = points.get(j);
                markHorizontally(marks, a, b);
                markVertically(marks, a, b);
                markDiagonally(marks, a, b, harmonics);
            }
        }
    }

    private static void markHorizontally(char[][] marks, Point a, Point b) {
        if (a.getI() != b.getI()) {
            return;
        }

        if (a.getJ() > b.getJ()) {
            int diff = a.getJ() - b.getJ();
            if (b.getJ() - diff > -1) {
                marks[a.getI()][b.getJ() - diff] = MARK;
            }
            if (a.getJ() + diff < marks.length) {
                marks[a.getI()][a.getJ() + diff] = MARK;
            }
        } else {
            int diff = b.getJ() - a.getJ();
            if (a.getJ() - diff > -1) {
                marks[b.getI()][a.getJ() - diff] = MARK;
            }
            if (b.getJ() + diff < marks.length) {
                marks[b.getI()][b.getJ() + diff] = MARK;
            }
        }
    }

    private static void markVertically(char[][] marks, Point a, Point b) {
        if (a.getJ() != b.getJ()) {
            return;
        }
        int diff = Math.abs(a.getI() - b.getI());
        if (a.getI() > b.getI()) {
            if (b.getI() - diff > -1) {
                marks[b.getI() - diff][b.getJ()] = MARK;
            }
            if (a.getI() + diff < marks.length) {
                marks[a.getI() + diff][a.getJ()] = MARK;
            }
        } else {
            if (a.getI() - diff > -1) {
                marks[a.getI() - diff][a.getJ()] = MARK;
            }
            if (b.getI() + diff < marks.length) {
                marks[b.getI() + diff][b.getJ()] = MARK;
            }
        }
    }

    private static void markDiagonally(char[][] marks, Point a, Point b, boolean harmonics) {
        if (b.getI() > a.getI() && b.getJ() < a.getJ()) {
            // forward diag
            markForwardDiag(marks, a, b, harmonics);
        } else if (a.getI() < b.getI() && a.getJ() < b.getJ()) {
            // backward diag
            markBackwardDiag(marks, a, b, harmonics);
        }

        if (harmonics) {
            marks[a.getI()][a.getJ()] = MARK;
            marks[b.getI()][b.getJ()] = MARK;
        }
    }

    private static void markForwardDiag(char[][] marks, Point a, Point b, boolean harmonics) {
        int diffI = Math.abs(a.getI() - b.getI());
        int diffJ = Math.abs(a.getJ() - b.getJ());

        if (a.getI() - diffI > -1 && a.getJ() + diffJ < marks.length) {
            int newI = a.getI() - diffI;
            int newJ = a.getJ() + diffJ;
            do {
                marks[newI][newJ] = MARK;
                newI -= diffI;
                newJ += diffJ;
            } while (harmonics && newI > -1 && newJ < marks.length);
        }
        if (b.getI() + diffI < marks.length && b.getJ() - diffJ > -1) {
            int newI = b.getI() + diffI;
            int newJ = b.getJ() - diffJ;
            do {
                marks[newI][newJ] = MARK;
                newI += diffI;
                newJ -= diffJ;
            } while (harmonics && newI < marks.length && newJ > -1);
        }
    }

    private static void markBackwardDiag(char[][] marks, Point a, Point b, boolean harmonics) {
        int diffI = Math.abs(a.getI() - b.getI());
        int diffJ = Math.abs(a.getJ() - b.getJ());

        if (a.getI() - diffI > -1 && a.getJ() - diffJ > -1) {
            int newI = a.getI() - diffI;
            int newJ = a.getJ() - diffJ;
            do {
                marks[newI][newJ] = MARK;
                newI -= diffI;
                newJ -= diffJ;
            } while (harmonics && newI > -1 && newJ > -1);
        }
        if (b.getI() + diffI < marks.length && b.getJ() + diffJ < marks.length) {
            int newI = b.getI() + diffI;
            int newJ = b.getJ() + diffJ;
            do {
                marks[newI][newJ] = MARK;
                newI += diffI;
                newJ += diffJ;
            } while (harmonics && newI < marks.length && newJ < marks.length);
        }
    }

    private static HashMap<Character, List<Point>> getGroups(char[][] matrix) {
        HashMap<Character, List<Point>> groups = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                char ch = matrix[i][j];
                if (Character.isDigit(ch) || Character.isLetter(ch)) {
                    List<Point> group = groups.get(ch);
                    if (group == null) {
                        group = new ArrayList<>();
                    }

                    group.add(new Point(i, j, ch));
                    groups.put(ch, group);
                }
            }
        }

        return groups;
    }

    private static int getSecondPartAnswer(char[][] matrix) {
        HashMap<Character, List<Point>> groups = getGroups(matrix);
        char[][] marks = new char[matrix.length][matrix.length];
        for (Map.Entry<Character, List<Point>> entry : groups.entrySet()) {
            markAntinodes(marks, entry.getValue(), true);
        }

        int count = 0;
        for (char[] line : marks) {
            for (char ch : line) {
                if (ch == MARK) {
                    count++;
                }
            }
        }

        return count;
    }
}