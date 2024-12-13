package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Day8 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day8Input.txt");

        List<String> list = Files.readAllLines(path);

        HashMap<String, Node> nodesMap = getNodesMap(list);

        System.out.println("Day 8 part 1 result:");
        System.out.println(getFirstPartAnswer(nodesMap, list.get(0).strip()));
        System.out.println("Day 8 part 2 result:");
        System.out.println(getSecondPartAnswer(nodesMap, list.get(0).strip()));
    }

    private static HashMap<String, Node> getNodesMap(List<String> list) {
        HashMap<String, Node> nodesMap = new HashMap<>();

        for (int i = 2; i < list.size(); i++) {
            Node node = getNodeFromLine(list.get(i));
            nodesMap.put(node.getName(), node);
        }

        return nodesMap;
    }

    private static Node getNodeFromLine(String s) {
        String[] parts = s.split("=");
        String[] nextNodes = parts[1].substring(2, parts[1].length() - 1).split(",");

        return new Node(parts[0].strip(), nextNodes[0].strip(), nextNodes[1].strip());
    }

    private static long getFirstPartAnswer(HashMap<String, Node> nodesMap, String directions) {
        Node current = nodesMap.get("AAA");
        return getStepsNeeded(nodesMap, current, directions, "ZZZ");
    }

    private static long getStepsNeeded(HashMap<String, Node> nodesMap, Node current, String directions, String ending) {
        char[] dir = directions.toCharArray();
        int index = 0;
        int steps = 0;
        while (!current.getName().endsWith(ending)) {
            if (dir[index] == 'R') {
                current = nodesMap.get(current.getRight());
            } else if (dir[index] == 'L') {
                current = nodesMap.get(current.getLeft());
            }
            if (index + 1 == dir.length) {
                index = 0;
            } else {
                index++;
            }
            steps++;
        }
        return steps;
    }

    private static long getSecondPartAnswer(HashMap<String, Node> nodesMap, String directions) {
        List<Node> currentNodes = getStartingNodes(nodesMap);

        return currentNodes.stream().map(n -> getStepsNeeded(nodesMap, n, directions, "Z")).reduce(1L, (a, b) -> (a * b) / gcd(a, b));
    }

    private static long gcd(Long a, Long b) {
        //greates common denominator
        return b == 0 ? a : gcd(b, a % b);
    }

    private static List<Node> getStartingNodes(HashMap<String, Node> nodesMap) {
        List<Node> startingNodes = new ArrayList<>();
        for (Map.Entry<String, Node> entry : nodesMap.entrySet()) {
            if (entry.getKey().endsWith("A")) {
                startingNodes.add(entry.getValue());
            }
        }

        return startingNodes;
    }

    public static class Node {
        private String name;
        private String left;
        private String right;

        public Node(String node, String left, String right) {
            this.name = node;
            this.left = left;
            this.right = right;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", left='" + left + '\'' +
                    ", right='" + right + '\'' +
                    '}';
        }
    }
}