package org.aoc.twentythree;

import org.aoc.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day16Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        System.out.println("Day 16 part 1 result:");
        System.out.println(getFirstPartAnswer(matrix));
        System.out.println("Day 16 part 2 result:");
        System.out.println(getSecondPartAnswer(matrix));
    }


    private static long getFirstPartAnswer(char[][] matrix) {
        return getCountByStartingPoint(matrix, 0, 0, WEST);
    }

    private static long getCountByStartingPoint(char[][] matrix, int i, int j, int fromDirection) {

        char[][] newMatrix = new char[matrix.length][matrix.length];
        newMatrix[i][j] = '#';

        HashSet<Position> positions = new HashSet<>();
        markLightPassage(matrix, newMatrix, i, j, fromDirection, positions);

        return countPassage(newMatrix);
    }

    private static long countPassage(char[][] newMatrix) {
        long count = 0;

        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                if (newMatrix[i][j] == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * @param originalMatrix - the original matrix
     * @param newMatrix      - the matrix to map where lights go
     * @param i              - x axis
     * @param j              - y axis
     * @param fromDirection  - From where it came 1 - north, 2 - east, 3 - south, 4 - west
     * @param positions      - to check where we've been to avoid circular paths
     */
    private static void markLightPassage(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        if (i >= originalMatrix.length || j >= originalMatrix.length || i < 0 || j < 0) {
            return;
        }

        if (!positions.add(new Position(i, j, fromDirection))) {
            return;
        }

        char nextChar = originalMatrix[i][j];
        newMatrix[i][j] = '#';

        switch (nextChar) {
            case '.': {
                handleEmptySpace(originalMatrix, newMatrix, i, j, fromDirection, positions);
                break;
            }
            case '|': {
                handleVericalPipe(originalMatrix, newMatrix, i, j, fromDirection, positions);
                break;
            }
            case '-': {
                handleHorizontalPipe(originalMatrix, newMatrix, i, j, fromDirection, positions);
                break;
            }
            case '/': {
                handleSlashMirror(originalMatrix, newMatrix, i, j, fromDirection, positions);
                break;
            }
            case '\\': {
                handleEscMirror(originalMatrix, newMatrix, i, j, fromDirection, positions);
                break;
            }
            default:
                break;
        }

    }

    private static void handleEmptySpace(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        int nextI = i;
        int nextJ = j;

        if (fromDirection == NORTH) {
            nextI++;
        } else if (fromDirection == EAST) {
            nextJ--;
        } else if (fromDirection == SOUTH) {
            nextI--;
        } else if (fromDirection == WEST) {
            nextJ++;
        }
        markLightPassage(originalMatrix, newMatrix, nextI, nextJ, fromDirection, positions);
    }

    private static void handleVericalPipe(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        if (fromDirection == EAST || fromDirection == WEST) {
            markLightPassage(originalMatrix, newMatrix, i - 1, j, SOUTH, positions);
            markLightPassage(originalMatrix, newMatrix, i + 1, j, NORTH, positions);
        } else {
            if (fromDirection == NORTH) {
                markLightPassage(originalMatrix, newMatrix, i + 1, j, fromDirection, positions);
            } else if (fromDirection == SOUTH) {
                markLightPassage(originalMatrix, newMatrix, i - 1, j, fromDirection, positions);
            }
        }
    }

    private static void handleHorizontalPipe(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        if (fromDirection == NORTH || fromDirection == SOUTH) {
            markLightPassage(originalMatrix, newMatrix, i, j - 1, EAST, positions);
            markLightPassage(originalMatrix, newMatrix, i, j + 1, WEST, positions);
        } else {
            if (fromDirection == EAST) {
                markLightPassage(originalMatrix, newMatrix, i, j - 1, fromDirection, positions);
            } else if (fromDirection == WEST) {
                markLightPassage(originalMatrix, newMatrix, i, j + 1, fromDirection, positions);
            }
        }
    }

    private static void handleSlashMirror(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        if (fromDirection == WEST) {
            markLightPassage(originalMatrix, newMatrix, i - 1, j, SOUTH, positions);
        } else if (fromDirection == EAST) {
            markLightPassage(originalMatrix, newMatrix, i + 1, j, NORTH, positions);
        } else if (fromDirection == NORTH) {
            markLightPassage(originalMatrix, newMatrix, i, j - 1, EAST, positions);
        } else if (fromDirection == SOUTH) {
            markLightPassage(originalMatrix, newMatrix, i, j + 1, WEST, positions);
        }
    }

    private static void handleEscMirror(char[][] originalMatrix, char[][] newMatrix, int i, int j, int fromDirection, HashSet<Position> positions) {
        if (fromDirection == WEST) {
            markLightPassage(originalMatrix, newMatrix, i + 1, j, NORTH, positions);
        } else if (fromDirection == EAST) {
            markLightPassage(originalMatrix, newMatrix, i - 1, j, SOUTH, positions);
        } else if (fromDirection == NORTH) {
            markLightPassage(originalMatrix, newMatrix, i, j + 1, WEST, positions);
        } else if (fromDirection == SOUTH) {
            markLightPassage(originalMatrix, newMatrix, i, j - 1, EAST, positions);
        }
    }


    private static long getSecondPartAnswer(char[][] matrix) {
        long max = 0;

        for (int i = 0; i < matrix.length; i++) {
            max = Math.max(max, getCountByStartingPoint(matrix, i, 0, WEST));
            max = Math.max(max, getCountByStartingPoint(matrix, 0, i, NORTH));
            max = Math.max(max, getCountByStartingPoint(matrix, i, matrix.length - 1, EAST));
            max = Math.max(max, getCountByStartingPoint(matrix, matrix.length - 1, i, SOUTH));
        }

        return max;
    }

    private static class Position {
        private int i;
        private int j;
        private int fromDirection;

        public Position(int i, int j, int fromDirection) {
            this.i = i;
            this.j = j;
            this.fromDirection = fromDirection;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

        public int getFromDirection() {
            return fromDirection;
        }

        public void setFromDirection(int fromDirection) {
            this.fromDirection = fromDirection;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return i == position.i && j == position.j && fromDirection == position.fromDirection;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j, fromDirection);
        }
    }

}