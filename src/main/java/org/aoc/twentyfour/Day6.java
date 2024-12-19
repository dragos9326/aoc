package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.Move;
import org.aoc.common.StopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import static org.aoc.common.Move.*;

/**
 * @author dragos
 */
@Slf4j
public class Day6 {

    private static final Logger logger = LoggerFactory.getLogger(Day6.class);

    private static int startI = 0;
    private static int startJ = 0;
    private static char startDir;
    private static final char MARK = 'X';
    private static final char OBSTACLE = '#';

    public static void main(String[] args) throws IOException, StopException {
        Path path = Paths.get("src/main/resources/twentyfour/day6Input.txt");

        List<String> list = Files.readAllLines(path);
        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        getStartingPosition(matrix);

        logger.debug("Day 6 part 1 result: {}", getFirstPartAnswer(matrix));
        logger.debug("Day 6 part 2 result: {}", getSecondPartAnswer(matrix));

    }

    private static int getFirstPartAnswer(char[][] matrix) throws StopException {
        move(matrix, startI, startJ, startDir, null);

        int count = 0;
        for (char[] line : matrix) {
            for (char ch : line) {
                if (ch == MARK) {
                    count++;
                }
            }
        }

        return count;
    }

    private static void getStartingPosition(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                char ch = matrix[i][j];
                if (ch == UP || ch == RIGHT || ch == DOWN || ch == LEFT) {
                    startI = i;
                    startJ = j;
                    startDir = matrix[startI][startJ];
                    return;
                }
            }
        }
    }

    private static void move(char[][] matrix, int i, int j, char dir, HashSet<Move> moves) throws StopException {
        if (dir == UP) {
            checkDirUp(matrix, i, j, moves);
        } else if (dir == RIGHT) {
            checkDirRight(matrix, i, j, moves);
        } else if (dir == DOWN) {
            checkDirDown(matrix, i, j, moves);
        } else if (dir == LEFT) {
            checkDirLeft(matrix, i, j, moves);
        }
    }

    private static void checkDirUp(char[][] matrix, int i, int j, HashSet<Move> moves) throws StopException {
        while (i > 0 && matrix[i - 1][j] != OBSTACLE) {
            if (moves == null) {
                matrix[i - 1][j] = MARK;
            } else {
                if (!moves.add(new Move(i - 1, j, UP))) {
                    throw new StopException();
                }
            }
            i--;
        }
        if (i - 1 < 0) {
            return;
        }
        move(matrix, i, j, RIGHT, moves);
    }

    private static void checkDirRight(char[][] matrix, int i, int j, HashSet<Move> moves) throws StopException {
        while (j + 1 < matrix.length && matrix[i][j + 1] != OBSTACLE) {
            if (moves == null) {
                matrix[i][j + 1] = MARK;
            } else {
                if (!moves.add(new Move(i, j + 1, RIGHT))) {
                    throw new StopException();
                }
            }
            j++;
        }
        if (j + 1 >= matrix.length) {
            return;
        }
        move(matrix, i, j, DOWN, moves);
    }

    private static void checkDirDown(char[][] matrix, int i, int j, HashSet<Move> moves) throws StopException {
        while (i + 1 < matrix.length && matrix[i + 1][j] != OBSTACLE) {
            if (moves == null) {
                matrix[i + 1][j] = MARK;
            } else {
                if (!moves.add(new Move(i + 1, j, DOWN))) {
                    throw new StopException();
                }
            }
            i++;
        }
        if (i + 1 >= matrix.length) {
            return;
        }
        move(matrix, i, j, LEFT, moves);
    }

    private static void checkDirLeft(char[][] matrix, int i, int j, HashSet<Move> moves) throws StopException {
        while (j > 0 && matrix[i][j - 1] != OBSTACLE) {
            if (moves == null) {
                matrix[i][j - 1] = MARK;
            } else {
                if (!moves.add(new Move(i, j - 1, LEFT))) {
                    throw new StopException();
                }
            }
            j--;
        }
        if (j - 1 < 0) {
            return;
        }
        move(matrix, i, j, UP, moves);
    }

    private static int getSecondPartAnswer(char[][] matrix) {
        int count = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                boolean revert = matrix[i][j] != OBSTACLE;
                matrix[i][j] = OBSTACLE;
                HashSet<Move> moves = new HashSet<>();
                try {
                    move(matrix, startI, startJ, startDir, moves);
                } catch (StopException e) {
                    count++;
                }
                if (revert) {
                    matrix[i][j] = '.';
                }
            }
        }

        return count;
    }
}