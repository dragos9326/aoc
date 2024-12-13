package org.aoc.twentythree;

import org.aoc.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    // part 2 based onn HyperNeutrino's code:
    //https://github.com/hyper-neutrino/advent-of-code/blob/main/2023/day21p2.py

    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day21Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 21 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 21 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }


    private static long getFirstPartAnswer(char[][] matrix) {
        int[] startingPos = getStartingPosition(matrix);

        return getPlotReachedNo(matrix, startingPos[0], startingPos[1], 64);
    }

    private static long getSecondPartAnswer(char[][] matrix) {
        //lets assume the grid is a square
        if (matrix.length != matrix[0].length) {
            throw new RuntimeException("Grid should be a square");
        }

        int size = matrix.length;
        int steps = 26501365;

        int[] startingPos = getStartingPosition(matrix);

        //the starting point should be in the middle(it is in our example - can be adapted for more general use cases)
        if (startingPos[0] != startingPos[1] || startingPos[0] != size / 2) {
            throw new RuntimeException("Grid should be a square");
        }
        System.out.println(startingPos[0] + " " + startingPos[1]);

        //in our example if we start from the middle of the square
        //and we go straight in any direction,
        // we should get to the end of a grid
        if (steps % size != size / 2) {
            throw new RuntimeException("We should get to the end of the grid");
        }


        long plotReachedSum = 0;
        // our final grid mash will look like a diamond because of Manhattan route;
        // we have 5 categories of grids
        // 1 - grids filled with odd number of steps
        // 2 - grids filled with even number of steps
        // 3 - 4 corners
        // 4 - minority filled grids(ex: next to each corner)
        // 5 - majority filled grids(ex: on the sides between minority filled grids)

        //get grid mash width excluding starting grid
        int gridMashWidth = steps / size - 1;
        System.out.println(gridMashWidth);
        long oddGridsNo = (long) Math.pow((gridMashWidth / 2 * 2 + 1), 2);
        System.out.println("odd: " + oddGridsNo);
        long evenGridsNo = (long) Math.pow(((gridMashWidth + 1) / 2 * 2), 2);
        System.out.println("even: " + evenGridsNo);

        // Get Size for 1 - grids filled with odd number of steps
        //size*2+1 is enough to get from corner to the other so it covers all grid;
        long oddGridPoints = getPlotReachedNo(matrix, startingPos[0], startingPos[1], size * 2 + 1);
        System.out.println("odd points: " + oddGridPoints);
        plotReachedSum += oddGridsNo * oddGridPoints;
        // Get size for 2 - grids filled with even number of steps
        long evenGridPoints = getPlotReachedNo(matrix, startingPos[0], startingPos[1], size * 2);
        System.out.println("even points: " + evenGridPoints);
        plotReachedSum += evenGridsNo * evenGridPoints;

        // Get Size for 3 - 4 corners
        //to get the corner size it will look like a v symmetrical to the middle axis
        //we start on the first row from the bottom, at the middle and we go size - 1 steps(to get to the top and left, right corners
        long cornerTop = getPlotReachedNo(matrix, size - 1, startingPos[1], size - 1);
        plotReachedSum += cornerTop;

        //to get the right corner, we start at the start of the middle row(our "axis")
        long cornerRight = getPlotReachedNo(matrix, startingPos[0], 0, size - 1);
        plotReachedSum += cornerRight;

        //to get the bottom corner, we start at the middle of the first row(our "axis")
        long cornerBottom = getPlotReachedNo(matrix, 0, startingPos[1], size - 1);
        plotReachedSum += cornerBottom;

        //to get the left corner, we start at the end of the middle row(our "axis")
        long cornerLeft = getPlotReachedNo(matrix, startingPos[0], size - 1, size - 1);
        plotReachedSum += cornerLeft;


        // Get size for 4 - minority filled grids(ex: next to each corner)
        //for the top right cadran we start at the start of the bottom row, and we go to the middle
        long smallTopRight = getPlotReachedNo(matrix, size - 1, 0, size / 2 - 1);
        plotReachedSum += (gridMashWidth + 1) * smallTopRight;

        //for the top left cadran we start at the end of the bottom row, and we go to the middle
        long smallTopLeft = getPlotReachedNo(matrix, size - 1, size - 1, size / 2 - 1);
        plotReachedSum += (gridMashWidth + 1) * smallTopLeft;

        //for the bottom right cadran we start at the start of the top row, and we go to the middle
        long smallBottomRight = getPlotReachedNo(matrix, 0, 0, size / 2 - 1);
        plotReachedSum += (gridMashWidth + 1) * smallBottomRight;

        //for the bottom left cadran we start at the end of the top row, and we go to the middle
        long smallBottomLeft = getPlotReachedNo(matrix, 0, size - 1, size / 2 - 1);
        plotReachedSum += (gridMashWidth + 1) * smallBottomLeft;

        // Get Size of 5 - majority filled grids(ex: on the sides between minority filled grids)
        // we start from the top. This type of grid is directly below of a type 4 grid
        // so we add an "size" amount of steps to the formula of the previous grid to get the steps needed.
        //for the top right cadran we start at the start of the bottom row, and we go to the middle
        long largeTopRight = getPlotReachedNo(matrix, size - 1, 0, 3 * size / 2 - 1);
        plotReachedSum += gridMashWidth * largeTopRight;

        //for the top left cadran we start at the end of the bottom row, and we go to the middle
        long largeTopLeft = getPlotReachedNo(matrix, size - 1, size - 1, 3 * size / 2 - 1);
        plotReachedSum += gridMashWidth * largeTopLeft;

        //for the bottom right cadran we start at the start of the top row, and we go to the middle
        long largeBottomRight = getPlotReachedNo(matrix, 0, 0, 3 * size / 2 - 1);
        plotReachedSum += gridMashWidth * largeBottomRight;

        //for the bottom left cadran we start at the end of the top row, and we go to the middle
        long largeBottomLeft = getPlotReachedNo(matrix, 0, size - 1, 3 * size / 2 - 1);
        plotReachedSum += gridMashWidth * largeBottomLeft;

        return plotReachedSum;
    }

    private static long getPlotReachedNo(char[][] matrix, int startX, int startY, int steps) {
        Set<Position> ans = new HashSet<>();
        Set<Position> seen = new HashSet<>();
        Queue<QueueRecord> q = new LinkedList<>();
        Position start = new Position(startX, startY);
        seen.add(start);
        q.add(new QueueRecord(start, steps));
        markNextSteps(matrix, ans, seen, q);

        return ans.size();
    }

    private static void showOnMatrics(char[][] matrix, Set<Position> ans) {
        ans.forEach(p -> matrix[p.i][p.j] = 'O');
        Main.printMatrix(matrix);
    }

    private static void markNextSteps(char[][] matrix, Set<Position> ans, Set<Position> seen, Queue<QueueRecord> q) {
        while (!q.isEmpty()) {
            QueueRecord qr = q.remove();
            int times = qr.times;
            if (times % 2 == 0) {
                ans.add(qr.pos);
            }
            if (times == 0) {
                continue;
            }

            int i = qr.pos.i;
            int j = qr.pos.j;
            //check south
            if (i < matrix.length - 1 && matrix[i + 1][j] != '#' && !seen.contains(new Position(i + 1, j))) {
                Position newPos = new Position(i + 1, j);
                seen.add(newPos);
                q.add(new QueueRecord(newPos, times - 1));
            }
            //check north
            if (i > 0 && matrix[i - 1][j] != '#' && !seen.contains(new Position(i - 1, j))) {
                Position newPos = new Position(i - 1, j);
                seen.add(newPos);
                q.add(new QueueRecord(newPos, times - 1));
            }
            //check east
            if (j < matrix[i].length - 1 && matrix[i][j + 1] != '#' && !seen.contains(new Position(i, j + 1))) {
                Position newPos = new Position(i, j + 1);
                seen.add(newPos);
                q.add(new QueueRecord(newPos, times - 1));
            }
            //check west
            if (j > 0 && matrix[i][j - 1] != '#' && !seen.contains(new Position(i, j - 1))) {
                Position newPos = new Position(i, j - 1);
                seen.add(newPos);
                q.add(new QueueRecord(newPos, times - 1));
            }
        }

    }

    private static int[] getStartingPosition(char[][] matrix) {
        int[] startPos = new int[2];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 'S') {
                    startPos[0] = i;
                    startPos[1] = j;
                    return startPos;
                }
            }
        }
        return startPos;
    }

    private static class QueueRecord {

        private Position pos;
        private int times;

        public QueueRecord(Position pos, int times) {
            this.pos = pos;
            this.times = times;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QueueRecord that = (QueueRecord) o;
            return times == that.times && Objects.equals(pos, that.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, times);
        }

        @Override
        public String toString() {
            return "QueueRecord{" +
                    "pos=" + pos +
                    ", times=" + times +
                    '}';
        }
    }

    private static class Position {
        private int i;
        private int j;


        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return i == position.i && j == position.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }
    }
}