package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day17 {
    //this is just dijkstra
    //copied from https://github.com/ash42/adventofcode/tree/main/adventofcode2023/src/nl/michielgraat/adventofcode2023/day17
    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day17Input.txt");

        List<String> list = Files.readAllLines(path);

        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        int[][] newMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j] - '0';
            }
        }

        System.out.println("Day 17 part 1 result:");
        System.out.println(getFirstPartAnswer(newMatrix));
        System.out.println("Day 17 part 2 result:");
        System.out.println(getSecondPartAnswer(newMatrix));
    }

    private static int getSecondPartAnswer(int[][] matrix) {
        return dijkstra(matrix, false);
    }


    private static long getFirstPartAnswer(int[][] matrix) {
        return dijkstra(matrix, true);
    }


    private static int dijkstra(int[][] grid, boolean part1) {
        Queue<Element> queue = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();
        int endX = grid[grid.length - 1].length - 1;
        int endY = grid.length - 1;

        Node eastStart = new Node(1, 0, 1, Element.EAST);
        Node southStart = new Node(0, 1, 1, Element.SOUTH);
        queue.add(new Element(eastStart, grid[0][1]));
        queue.add(new Element(southStart, grid[1][0]));

        while (!queue.isEmpty()) {
            final Element current = queue.poll();
            if (visited.contains(current.getNode())) {
                continue;
            }
            visited.add(current.getNode());
            if (current.getNode().x() == endX && current.getNode().y() == endY
                    && (part1 || current.getNode().blocks() >= 4)) {
                return current.getHeatLoss();
            }

            queue.addAll(part1 ? current.getNeighbours(grid) : current.getNeighboursForPart2(grid));

        }

        return 0;
    }

    public static class Element implements Comparable<Element> {

        public static final int NORTH = 0;
        public static final int EAST = 1;
        public static final int SOUTH = 2;
        public static final int WEST = 3;

        private Node node;
        private int heatLoss;

        public Element(Node node, int heatLoss) {
            this.node = node;
            this.heatLoss = heatLoss;
        }

        public List<Element> getNeighboursForPart2(int[][] grid) {
            List<Element> neighbours = new ArrayList<>();
            if (node.blocks() >= 4) {
                Element left = getNextElement(Math.floorMod(node.direction() - 1, 4), grid, 1);
                if (left != null) {
                    neighbours.add(left);
                }

                Element right = getNextElement((node.direction() + 1) % 4, grid, 1);
                if (right != null) {
                    neighbours.add(right);
                }
            }
            if (node.blocks() < 10) {
                Element straight = getNextElement(node.direction(), grid, node.blocks() + 1);
                if (straight != null) {
                    neighbours.add(straight);
                }
            }
            return neighbours;
        }

        public List<Element> getNeighbours(int[][] grid) {
            List<Element> neighbours = new ArrayList<>();

            Element left = getNextElement(Math.floorMod(node.direction() - 1, 4), grid, 1);
            if (left != null) {
                neighbours.add(left);
            }

            Element right = getNextElement((node.direction() + 1) % 4, grid, 1);
            if (right != null) {
                neighbours.add(right);
            }

            if (node.blocks() < 3) {
                Element straight = getNextElement(node.direction(), grid, node.blocks() + 1);
                if (straight != null) {
                    neighbours.add(straight);
                }
            }

            return neighbours;
        }

        private Element getNextElement(int direction, int[][] grid, int blocks) {
            int x = getNextX(direction);
            int y = getNextY(direction);
            if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                Node nextNode = new Node(x, y, blocks, direction);
                return new Element(nextNode, heatLoss + grid[nextNode.y()][nextNode.x()]);
            }
            return null;
        }

        private int getNextX(int newDirection) {
            int nextX = newDirection == NORTH || newDirection == SOUTH ? node.x()
                    : newDirection == EAST ? node.x() + 1 : node.x() - 1;
            return nextX;
        }

        private int getNextY(int newDirection) {
            int nextY = newDirection == EAST || newDirection == WEST ? node.y()
                    : newDirection == NORTH ? node.y() - 1 : node.y() + 1;
            return nextY;
        }

        @Override
        public int compareTo(Element o) {
            if (this.heatLoss != o.getHeatLoss()) {
                return Integer.compare(this.heatLoss, o.getHeatLoss());
            } else if (this.node.direction() == o.getNode().direction() && this.node.blocks() != o.getNode().blocks()) {
                return Integer.compare(this.node.blocks(), o.getNode().blocks());
            } else if (this.node.y() != o.getNode().y()) {
                return Integer.compare(this.node.y(), o.getNode().y());
            } else {
                return Integer.compare(this.node.x(), o.getNode().x());
            }
        }

        public Node getNode() {
            return this.node;
        }

        public int getHeatLoss() {
            return this.heatLoss;
        }
    }

    public record Node(int x, int y, int blocks, int direction) {
    }
}