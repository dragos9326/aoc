package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day14Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 14 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 14 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }

    private static long getFirstPartAnswer(char[][] matrix) {
        matrix = tiltNorth(matrix);

        return getTotalLoad(matrix);
    }

    private static long getTotalLoad(char[][] matrix) {
        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 'O') {
                    sum += matrix.length - i;
                }
            }
        }

        return sum;
    }

    private static char[][] tiltNorth(char[][] matrix) {
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char ch = matrix[i][j];
                if (ch == 'O') {
                    int k = i;
                    char upChar = matrix[k - 1][j];
                    while (upChar == '.') {
                        matrix[k - 1][j] = 'O';
                        matrix[k][j] = '.';
                        k--;
                        if (k > 0) {
                            upChar = matrix[k - 1][j];
                        } else {
                            upChar = 'X';
                        }
                    }
                }
            }
        }

        return matrix;
    }


    private static long getSecondPartAnswer(char[][] matrix) {
        List<String> grids = new ArrayList<>();
        List<Long> scores = new ArrayList<>();

        for (int i = 0; i < 1000000000; i++) {
            matrix = cycleMatrix(matrix);
            grids.add(getMatrixToString(matrix));
            scores.add(getTotalLoad(matrix));
            HashSet<String> diff = new HashSet<>(grids);
            if (diff.size() != scores.size()) {
                break;
            }
        }

        int last = grids.size() - 1;
        int first = grids.indexOf(grids.get(last));
        return scores.get(first + (1000000000 - 1 - first) % (last - first));
    }

    private static String getMatrixToString(char[][] matrix) {
        return IntStream.range(0, matrix.length)
                .mapToObj(y -> Arrays.stream(matrix).map(chars -> String.valueOf(chars[y]))
                        .collect(Collectors.joining())).collect(Collectors.joining("\n"));
    }

    private static char[][] cycleMatrix(char[][] matrix) {
        //til north
        matrix = tiltNorth(matrix);

        //tilt west
        matrix = rotate90Left(matrix);
        matrix = tiltNorth(matrix);

        //tilt south
        matrix = rotate90Left(matrix);
        matrix = tiltNorth(matrix);

        //tilt east
        matrix = rotate90Left(matrix);
        matrix = tiltNorth(matrix);

        //get back to original position
        matrix = rotate90Left(matrix);

        return matrix;
    }

    private static char[][] rotate90Left(char[][] matrix) {
        char[][] newMatrix = new char[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                newMatrix[j][matrix.length - 1 - i] = matrix[i][j];
            }
        }

        return newMatrix;
    }

}