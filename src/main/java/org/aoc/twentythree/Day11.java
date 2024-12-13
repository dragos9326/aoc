package org.aoc.twentythree;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {

    public static List<Coordinates> galaxiesList = new ArrayList<>();
    public static List<Coordinates> originalGlaxiesList = new ArrayList<>();

    public static Set<Integer> galaxiesRows = new HashSet<>();
    public static Set<Integer> galaxiesColumns = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day11Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 11 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 11 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }

    private static long getFirstPartAnswer(char[][] matrix) {
        char[][] adjusted = getAdjustedGalaxy(matrix);
        System.out.println("Matrics:");
        for (char[] chars : adjusted) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
        int sum = 0;

        for (int i = 0; i < galaxiesList.size(); i++) {
            Coordinates c = galaxiesList.get(i);
            for (int j = i + 1; j < galaxiesList.size(); j++) {
                Coordinates c1 = galaxiesList.get(j);
                sum += getDistanceBetweenGalaxies(c, c1);
            }
        }
        return sum;
    }

    private static int getDistanceBetweenGalaxies(Coordinates c, Coordinates c1) {
        return Math.abs(c.getX() - c1.getX()) + Math.abs(c.getY() - c1.getY());
    }

    private static char[][] getAdjustedGalaxy(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char ch = matrix[i][j];
                if ('#' == ch) {
                    galaxiesRows.add(i);
                    galaxiesColumns.add(j);
                    originalGlaxiesList.add(new Coordinates(i, j));
                }
            }
        }

        System.out.println(galaxiesRows);
        System.out.println(galaxiesColumns);

        int newRowSize = matrix.length + (matrix.length - galaxiesRows.size());
        int newColumnSize = matrix[0].length + (matrix[0].length - galaxiesColumns.size());
        char[][] newMatrix = new char[newRowSize][newColumnSize];
        int skippedRows = 0;

        for (int i = 0; i < newRowSize; i++) {
            if (!galaxiesRows.contains(i - skippedRows)) {
                skippedRows++;
                Arrays.fill(newMatrix[i], '.');
                i++;
                Arrays.fill(newMatrix[i], '.');
            } else {
                int skippedColumns = 0;
                for (int j = 0; j < newColumnSize; j++) {
                    if (!galaxiesColumns.contains(j - skippedColumns)) {
                        skippedColumns++;
                        newMatrix[i][j] = '.';
                        j++;
                        newMatrix[i][j] = '.';
                    } else {
                        char ch = matrix[i - skippedRows][j - skippedColumns];
                        newMatrix[i][j] = ch;
                        if ('#' == ch) {
                            galaxiesList.add(new Coordinates(i, j));
                        }
                    }
                }
            }
        }

        return newMatrix;
    }

    private static BigInteger getSecondPartAnswer(char[][] matrix) {
        BigInteger sum = BigInteger.ZERO;

        List<Integer> sortedGalaxyRows = galaxiesRows.stream().sorted().toList();

        List<Integer> sortedGalaxyColumns = galaxiesColumns.stream().sorted().toList();

        System.out.println(sortedGalaxyRows);
        System.out.println(sortedGalaxyColumns);
        for (int i = 0; i < originalGlaxiesList.size(); i++) {
            Coordinates c = originalGlaxiesList.get(i);
            for (int j = i + 1; j < originalGlaxiesList.size(); j++) {
                Coordinates c1 = originalGlaxiesList.get(j);

                sum = sum.add(getDistanceBetweenGalaxiesExpanded(c, c1, 1000000));
            }
        }

        return sum;
    }

    private static BigInteger getDistanceBetweenGalaxiesExpanded(Coordinates c, Coordinates c1, int exponent) {
        int timesAdded = 0;

        BigInteger result = BigInteger.ZERO;

        for (int i = Math.min(c1.getY(), c.getY()); i < Math.max(c1.getY(), c.getY()); i++) {
            if (!galaxiesColumns.contains(i)) {
                result = result.add(BigInteger.valueOf(exponent));
                timesAdded++;
            }
        }

        for (int i = Math.min(c1.getX(), c.getX()); i < Math.max(c1.getX(), c.getX()); i++) {
            if (!galaxiesRows.contains(i)) {
                result = result.add(BigInteger.valueOf(exponent));
                timesAdded++;
            }
        }

        result = result.add(BigInteger.valueOf(Math.abs(c.getX() - c1.getX()))).add(BigInteger.valueOf(Math.abs(c.getY() - c1.getY())));
        result = result.subtract(BigInteger.valueOf(timesAdded));
        return result;

    }


    public static class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coordinates{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}