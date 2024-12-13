package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day9Input.txt");

        List<String> list = Files.readAllLines(path);

        List<OasisLine> oasisLines = getOasisLines(list);

        System.out.println("Day 9 part 1 result:");
        System.out.println(getFirstPartAnswer(oasisLines));
        System.out.println("Day 9 part 2 result:");
        System.out.println(getSecondPartAnswer(oasisLines));
    }

    private static List<OasisLine> getOasisLines(List<String> list) {
        return list.stream().map(OasisLine::new).collect(Collectors.toList());
    }

    private static long getFirstPartAnswer(List<OasisLine> oasisLines) {
        int sum = 0;
        for (OasisLine line : oasisLines) {
            sum += line.getNextElement();
        }

        return sum;
    }

    private static long getSecondPartAnswer(List<OasisLine> oasisLines) {
        int sum = 0;
        for (OasisLine line : oasisLines) {
            sum += line.getPreviousElement();
        }

        return sum;
    }

    public static class OasisLine {
        private List<List<Integer>> pyramid;

        public OasisLine(String line) {
            String[] numbers = line.split(" ");
            List<Integer> newLine = Arrays.stream(numbers).map(Integer::parseInt).collect(Collectors.toList());

            pyramid = new ArrayList<>();
            pyramid.add(newLine);
            boolean shouldStop = false;
            do {
                shouldStop = generateNextLine(pyramid);
            }
            while (!shouldStop);
        }

        private boolean generateNextLine(List<List<Integer>> pyramid) {
            List<Integer> lastLine = pyramid.get(pyramid.size() - 1);
            List<Integer> newLine = new ArrayList<>();
            boolean shouldFinish = true;
            for (int i = 0; i < lastLine.size() - 1; i++) {
                int newValue = lastLine.get(i + 1) - lastLine.get(i);
                if (newValue != 0) {
                    shouldFinish = false;
                }
                newLine.add(newValue);
            }

            pyramid.add(newLine);

            return shouldFinish;
        }

        public List<List<Integer>> getPyramid() {
            return pyramid;
        }

        public void setPyramid(List<List<Integer>> pyramid) {
            this.pyramid = pyramid;
        }

        @Override
        public String toString() {
            return "OasisLine{" +
                    "line=" + pyramid +
                    '}';
        }

        public int getNextElement() {
            pyramid.get(pyramid.size() - 1).add(0);
            for (int i = pyramid.size() - 2; i >= 0; i--) {
                List<Integer> nextLine = pyramid.get(i + 1);
                List<Integer> currentLine = pyramid.get(i);
                int nextValue = nextLine.get(nextLine.size() - 1) + currentLine.get(currentLine.size() - 1);
                currentLine.add(nextValue);
            }

            List<Integer> firstLine = pyramid.get(0);
            return firstLine.get(firstLine.size() - 1);
        }

        public int getPreviousElement() {
            pyramid.get(pyramid.size() - 1).add(0, 0);
            for (int i = pyramid.size() - 2; i >= 0; i--) {
                List<Integer> nextLine = pyramid.get(i + 1);
                List<Integer> currentLine = pyramid.get(i);
                int nextValue = currentLine.get(0) - nextLine.get(0);
                currentLine.add(0, nextValue);
            }

            List<Integer> firstLine = pyramid.get(0);
            return firstLine.get(0);
        }
    }
}