package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day18Input.txt");

        List<String> list = Files.readAllLines(path);

        System.out.println("Day 18 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 18 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }


    private static long getSecondPartAnswer(List<String> list) {
        List<Node> nodes = digHoles(list, true);

        long shoelaceArea = getShoelaceArea(nodes);
        long pathArea = getPathArea(nodes);
        //picks theorem + path area
        return shoelaceArea - (pathArea / 2) + 1 + pathArea;
    }


    private static long getFirstPartAnswer(List<String> list) {
        List<Node> nodes = digHoles(list, false);

        long shoelaceArea = getShoelaceArea(nodes);
        long pathArea = getPathArea(nodes);
        //picks theorem + path area
        return shoelaceArea - (pathArea / 2) + 1 + pathArea;
    }

    private static long getPathArea(List<Node> nodes) {
        long total = 0;

        for (int i = 0; i < nodes.size(); i++) {
            Node n1 = nodes.get(i);
            Node n2 = nodes.get((i + 1) % nodes.size());
            long lat = Math.abs(n1.getX() - n2.getX()) + Math.abs(n1.getY() - n2.getY());

            total += lat;
        }

        return total;
    }

    private static long getShoelaceArea(List<Node> nodes) {
        long area = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node now = nodes.get(i);
            Node next = nodes.get(i == nodes.size() - 1 ? 0 : i + 1);

            area += now.getX() * next.getY() - now.getY() * next.getX();
        }

        return Math.abs(area) / 2;
    }

    private static List<Node> digHoles(List<String> list, boolean part2) {
        List<Node> nodes = new ArrayList<>();

        Node current = new Node(0, 0, '#', null);
        nodes.add(current);
        for (int i = 0; i < list.size(); i++) {
            String l = list.get(i);
            String[] parts = l.split(" ");
            long size = part2 ? Long.decode(parts[2].substring(1, parts[2].length() - 2)) : Long.valueOf(parts[1]);
            String color = parts[2].substring(1, parts[2].length() - 1);
            char direction = part2 ? getPart2Direction(l.charAt(l.length() - 2)) : l.charAt(0);
            current = getNextCoordinate(current, direction, size, color);
            if (i != list.size() - 1) {
                nodes.add(current);
            }
        }

        return nodes;
    }

    private static char getPart2Direction(char c) {
        switch (c) {
            case '0':
                return 'R';
            case '1':
                return 'D';
            case '2':
                return 'L';
            default:
                return 'U';
        }
    }

    private static Node getNextCoordinate(Node current, char direction, long size, String color) {
        switch (direction) {
            case 'R':
                return new Node(current.getX(), current.getY() + size, '#', color);
            case 'L':
                return new Node(current.getX(), current.getY() - size, '#', color);
            case 'U':
                return new Node(current.getX() - size, current.getY(), '#', color);
            default:
                return new Node(current.getX() + size, current.getY(), '#', color);
        }
    }


    public static class Node {
        private long x;

        private long y;

        private char symbol;

        private String color;

        public Node(long x, long y, char symbol, String color) {
            this.x = x;
            this.y = y;
            this.symbol = symbol;
            this.color = color;
        }

        public long getX() {
            return x;
        }

        public void setX(long x) {
            this.x = x;
        }

        public long getY() {
            return y;
        }

        public void setY(long y) {
            this.y = y;
        }

        public char getSymbol() {
            return symbol;
        }

        public void setSymbol(char symbol) {
            this.symbol = symbol;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    ", symbol=" + symbol +
                    ", color='" + color + '\'' +
                    '}';
        }
    }
}