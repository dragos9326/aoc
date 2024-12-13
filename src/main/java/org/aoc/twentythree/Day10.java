package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    protected static Set<Character> pipeSymbols = new HashSet<>();
    protected static Set<Character> curves = new HashSet<>();

    static {
        curves.add('L');
        curves.add('J');
        curves.add('7');
        curves.add('F');

        pipeSymbols.addAll(curves);
        pipeSymbols.add('|');
        pipeSymbols.add('-');
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day10Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 10 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 10 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }

    private static long getFirstPartAnswer(char[][] matrix) {
        Coordinates c = new Coordinates(0, 0);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 'S') {
                    c = new Coordinates(i, j);
                    break;
                }
            }
        }

        List<Coordinates> pipe = getPipe(matrix, c);

        return pipe.size() / 2;
    }

    private static List<Coordinates> getPipe(char[][] matrix, Coordinates c) {
        Coordinates before = null;
        List<Coordinates> pipe = new ArrayList<>();
        pipe.add(c);
        do {
            Coordinates next = getNextPipePosition(matrix, c, before);
            before = c;
            c = next;
            pipe.add(next);
        } while (matrix[c.x][c.y] != 'S');

        return pipe;
    }

    private static Coordinates getNextPipePosition(char[][] matrix, Coordinates now, Coordinates before) {
        if (before == null) {
            return getFirstPosition(matrix, now);
        }

        return getNextPipePositionFromSymbol(now, before, matrix[now.getX()][now.getY()]);
    }

    private static Coordinates getFirstPosition(char[][] matrix, Coordinates now) {
        //check right
        if (now.getY() + 1 < matrix[now.getX()].length) {
            char mr = matrix[now.getX()][now.getY() + 1];
            if (mr == '-' || mr == 'J' || mr == '7') {
                return new Coordinates(now.getX(), now.getY() + 1);
            }
        }

        //check bottom
        if (now.getX() + 1 < matrix.length) {
            char mr = matrix[now.getX() + 1][now.getY()];
            if (mr == '|' || mr == 'L' || mr == 'J') {
                return new Coordinates(now.getX() + 1, now.getY());
            }
        }

        //check left
        if (now.getY() - 1 >= 0) {
            char mr = matrix[now.getX()][now.getY() - 1];
            if (mr == '-' || mr == 'L' || mr == 'F') {
                return new Coordinates(now.getX(), now.getY() - 1);
            }
        }

        //check top
        if (now.getX() - 1 >= 0) {
            char mr = matrix[now.getX() - 1][now.getY()];
            if (mr == '|' || mr == '7' || mr == 'F') {
                return new Coordinates(now.getX() - 1, now.getY());
            }
        }

        return now;
    }

    private static Coordinates getNextPipePositionFromSymbol(Coordinates now, Coordinates before, char c) {
        switch (c) {
            case '|': {
                if (before.getX() + 1 == now.getX()) {
                    return new Coordinates(now.getX() + 1, now.getY());
                } else {
                    return new Coordinates(now.getX() - 1, now.getY());
                }
            }
            case '-': {
                if (before.getY() + 1 == now.getY()) {
                    return new Coordinates(now.getX(), now.getY() + 1);
                } else {
                    return new Coordinates(now.getX(), now.getY() - 1);
                }
            }
            case 'L': {
                if (before.getY() - 1 == now.getY()) {
                    return new Coordinates(now.getX() - 1, now.getY());
                } else {
                    return new Coordinates(now.getX(), now.getY() + 1);
                }
            }
            case 'J': {
                if (before.getX() + 1 == now.getX()) {
                    return new Coordinates(now.getX(), now.getY() - 1);
                } else {
                    return new Coordinates(now.getX() - 1, now.getY());
                }
            }
            case '7': {
                if (before.getY() + 1 == now.getY()) {
                    return new Coordinates(now.getX() + 1, now.getY());
                } else {
                    return new Coordinates(now.getX(), now.getY() - 1);
                }
            }
            case 'F': {
                if (before.getY() - 1 == now.getY()) {
                    return new Coordinates(now.getX() + 1, now.getY());
                } else {
                    return new Coordinates(now.getX(), now.getY() + 1);
                }
            }
            default:
                return now;
        }

    }

    private static long getSecondPartAnswer(char[][] matrix) {
        Coordinates c = new Coordinates(0, 0);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 'S') {
                    c = new Coordinates(i, j);
                    break;
                }
            }
        }

        List<Coordinates> pipe = getPipe(matrix, c);

        char[][] newMatrix = new char[matrix.length][matrix[0].length];

        for (char[] chars : newMatrix) {
            Arrays.fill(chars, '.');
        }

        for (Coordinates co : pipe) {
            newMatrix[co.getX()][co.getY()] = matrix[co.getX()][co.getY()];
        }


        //check if right or left
        boolean isLeft = isLeft(newMatrix, pipe);

        //set all the right/left positions to 'I' till you find another symbol
        setInsideThePipe(newMatrix, isLeft, pipe);

        System.out.println("Matrics:");
        for (char[] chars : newMatrix) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }

        //count all the 'I'
        int symbols = 0;
        for (char[] aCh : newMatrix) {
            for (char ch : aCh) {
                if (ch == 'I') {
                    symbols++;
                }
            }
        }

        return symbols;
    }

    private static void fillThePipe(char[][] newMatrix, int r, int c) {
        if (!pipeSymbols.contains(newMatrix[r][c])) {
            newMatrix[r][c] = 'I';

            fillThePipe(newMatrix, r + 1, c);
            fillThePipe(newMatrix, r - 1, c);
            fillThePipe(newMatrix, r, c + 1);
            fillThePipe(newMatrix, r, c - 1);
        }
    }

    private static void setInsideThePipe(char[][] newMatrix, boolean isLeft, List<Coordinates> pipes) {
        Set<Character> walls = new HashSet<>();
        walls.add('|');
        walls.add('J');
        walls.add('L');
        walls.add('7');
        walls.add('F');
        boolean enclosed = false;
        char startingJoint = 'Z';
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix[i].length; j++) {
                var tile = newMatrix[i][j];
                if (tile == '.') {
                    // We're not in the pipe and inside the loop, possibly count the territory
                    if (enclosed) {
                        newMatrix[i][j] = 'I';
                    }
                } else {
                    // We're in the pipe
                    if (tile == '|') {
                        enclosed = !enclosed;
                    } else if ("FL".contains(String.valueOf(tile))) {
                        startingJoint = tile;
                    } else if (tile == 'J') {
                        if (startingJoint == 'F') {
                            enclosed = !enclosed;
                        }
                        startingJoint = 'Z';
                    } else if (tile == '7') {
                        if (startingJoint == 'L') {
                            enclosed = !enclosed;
                        }
                        startingJoint = 'Z';
                    }
                }
            }
        }
    }

    private static boolean isLeft(char[][] newMatrix, List<Coordinates> pipe) {
        int right = 0;
        int left = 0;

        for (int i = 1; i < pipe.size(); i++) {
            Coordinates now = pipe.get(i);
            Coordinates before = pipe.get(i - 1);

            char currentChar = newMatrix[now.getX()][now.getY()];
            if (curves.contains(currentChar)) {
                if (isLeftCurve(currentChar, now, before)) {
                    left++;
                } else {
                    right++;
                }
            }
        }


        return left > right;
    }

    private static boolean isLeftCurve(char currentChar, Coordinates now, Coordinates before) {
        switch (currentChar) {
            case 'L': {
                return before.getX() + 1 == now.getX();
            }
            case 'J': {
                return before.getY() + 1 == now.getY();
            }
            case '7': {
                return before.getX() - 1 == now.getX();
            }
            case 'F': {
                return before.getY() - 1 == now.getY();
            }
            default:
                return true;//should not happen
        }
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
    }
}