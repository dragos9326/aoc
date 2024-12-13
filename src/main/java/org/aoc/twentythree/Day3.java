package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day3 {

    static Map<String, Integer> maxCubes = new HashMap<>();

    static {
        maxCubes.put("red", 12);
        maxCubes.put("green", 13);
        maxCubes.put("blue", 14);
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day3Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 2 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 2 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }

    private static int getFirstPartAnswer(char[][] matrix) {
        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            char[] line = matrix[i];
            StringBuilder digit = new StringBuilder();
            for (int j = 0; j < line.length; j++) {
                sum += checkChar(matrix, i, j, digit);
            }
        }

        return sum;
    }

    private static int checkChar(char[][] matrix, int i, int j, StringBuilder digit) {
        int sum = 0;
        char[] line = matrix[i];
        if (Character.isDigit(line[j])) {
            digit.append(line[j]);

            if (j == line.length - 1) {
                if (!digit.isEmpty() && (checkIfIsAdjacent(matrix, i, j - digit.length() + 1, j))) {
                    sum += Integer.parseInt(digit.toString());
                }
                digit.setLength(0);
            }

        } else {
            if (!digit.isEmpty()) {
                if (checkIfIsAdjacent(matrix, i, j - digit.length(), j - 1)) {
                    sum += Integer.parseInt(digit.toString());
                }
                digit.setLength(0);
            }
        }

        return sum;
    }

    private static boolean checkIfIsAdjacent(char[][] matrix, int lineNo, int start, int end) {
        if (lineNo > matrix.length - 1) {
            return false;
        }

        //check before line
        if (lineNo > 0) {
            char[] prevLine = matrix[lineNo - 1];

            for (int i = Math.max(start - 1, 0); i < Math.min(end + 2, prevLine.length); i++) {
                if (isSpecialCharacter(prevLine[i])) {
                    return true;
                }
            }
        }

        //check current line
        if (start > 0 && isSpecialCharacter(matrix[lineNo][start - 1])) {
            return true;
        }

        if (end + 1 < matrix[lineNo].length && isSpecialCharacter(matrix[lineNo][end + 1])) {
            return true;
        }

        //check after line
        if (lineNo + 1 < matrix.length) {
            char[] nextLine = matrix[lineNo + 1];

            for (int i = Math.max(start - 1, 0); i < Math.min(end + 2, nextLine.length); i++) {
                if (isSpecialCharacter(nextLine[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isSpecialCharacter(char ch) {
        return Character.toString(ch).matches("[^a-zA-Z0-9.]");
    }

    private static int getSecondPartAnswer(char[][] matrix) {
        int sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            char[] line = matrix[i];
            for (int j = 0; j < line.length; j++) {
                sum += checkIfGear(matrix, i, j);
            }
        }
        return sum;
    }

    private static int checkIfGear(char[][] matrix, int i, int j) {
        int sum = 0;
        if (matrix[i][j] == '*') {
            //check if gear.
            int nums = 0;
            //check previous line
            nums += getNumsOnLine(matrix, i - 1, j);
            //check current line
            if (Character.isDigit(matrix[i][j - 1])) {
                nums++;
            }
            if (Character.isDigit(matrix[i][j + 1])) {
                nums++;
            }

            //check next line
            nums += getNumsOnLine(matrix, i + 1, j);

            if (nums != 2) {
                //it's not gear
                return sum;
            }

            return getGearValue(matrix, i, j);
        }
        return sum;
    }

    private static int getGearValue(char[][] matrix, int i, int j) {
        int value = 0;


        //check previous line
        value += getNumsValueOnLine(matrix, i - 1, j, true);

        //check current line
        if (value != 0) {
            value *= Math.max(1, getNumsValueOnLine(matrix, i, j, false));
        } else {
            value += getNumsValueOnLine(matrix, i, j, false);
        }

        //check next line
        if (value != 0) {
            value *= Math.max(1, getNumsValueOnLine(matrix, i + 1, j, true));
        } else {
            value += getNumsValueOnLine(matrix, i + 1, j, true);
        }

        return value;
    }

    private static int getNumsValueOnLine(char[][] matrix, int i, int j, boolean checkMiddle) {
        if (checkMiddle && Character.isDigit(matrix[i][j])) {
            StringBuilder num = new StringBuilder(Character.toString(matrix[i][j]));
            //if mid is digit
            appendLeftSideNumber(matrix, i, j, num);
            appendRightSideNumber(matrix, i, j, num);

            return Integer.parseInt(num.toString());
        } else {
            StringBuilder left = new StringBuilder();
            StringBuilder right = new StringBuilder();

            if (Character.isDigit(matrix[i][j - 1])) {
                left.append(matrix[i][j - 1]);
                appendLeftSideNumber(matrix, i, j - 1, left);
            }

            if (Character.isDigit(matrix[i][j + 1])) {
                right.append(matrix[i][j + 1]);
                appendRightSideNumber(matrix, i, j + 1, right);
            }

            if (!left.isEmpty() && !right.isEmpty()) {
                return Integer.parseInt(left.toString()) * Integer.parseInt(right.toString());
            } else {
                if (!left.isEmpty()) {
                    return Integer.parseInt(left.toString());
                } else if (!right.isEmpty()) {
                    return Integer.parseInt(right.toString());
                }
            }
        }

        return 0;
    }

    private static void appendRightSideNumber(char[][] matrix, int i, int j, StringBuilder right) {
        j++;
        while (j < matrix[i].length && Character.isDigit(matrix[i][j])) {
            right.append(matrix[i][j]);
            j++;
        }
    }

    private static void appendLeftSideNumber(char[][] matrix, int i, int j, StringBuilder left) {
        j--;
        while (j >= 0 && Character.isDigit(matrix[i][j])) {
            left.insert(0, matrix[i][j]);
            j--;
        }
    }

    private static int getNumsOnLine(char[][] matrix, int i, int j) {
        int nums = 0;
        if (Character.isDigit(matrix[i][j - 1])) {
            nums++;
            if (Character.isDigit(matrix[i][j + 1]) && !Character.isDigit(matrix[i][j])) {
                nums++;
            }
        } else if (Character.isDigit(matrix[i][j])) {
            nums++;
        } else if (Character.isDigit(matrix[i][j + 1])) {
            nums++;
        }
        return nums;
    }
}