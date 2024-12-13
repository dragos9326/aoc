package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day15 {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day15Input.txt");

        List<String> list = getInitSeq(path);

        System.out.println("Day 15 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 15 part 2 result:");
        System.out.println(getSecondPartAnswer(path));
    }

    private static List<String> getInitSeq(Path path) throws IOException {
        List<String> list = Files.readAllLines(path);
        List<String> toRet = new ArrayList<>();
        list.forEach(s -> toRet.addAll(Arrays.asList(s.split(","))));

        return toRet;
    }

    private static long getFirstPartAnswer(List<String> list) {
        long sum = 0;

        for (String s : list) {
            sum += Lens.getHash(s);
        }

        return sum;
    }


    private static long getSecondPartAnswer(Path path) throws IOException {
        List<Lens> lenses = getAllLenses(path);

        HashMap<Integer, List<Lens>> map = new HashMap<>();
        for (int i = 0; i < lenses.size(); i++) {
            Lens l = lenses.get(i);
            List<Lens> lensList = map.get(l.hashCode());
            if (lensList == null) {
                lensList = new ArrayList<>();
            }

            if (l.getOperation() == '-') {
                lensList.remove(l);
            } else if (l.getOperation() == '=') {
                if (lensList.contains(l)) {
                    lensList.set(lensList.indexOf(l), l);
                } else {
                    lensList.add(l);
                }
            }

            map.put(l.hashCode(), lensList);
        }

        int sum = 0;
        for (Map.Entry<Integer, List<Lens>> entry : map.entrySet()) {
            sum += getListPowerTotal(entry);
        }
        return sum;
    }

    private static int getListPowerTotal(Map.Entry<Integer, List<Lens>> entry) {
        List<Lens> list = entry.getValue();
        int sum = 0;
        int pos = entry.getKey();
        for (int i = 0; i < list.size(); i++) {
            Lens l = list.get(i);
            sum += (pos + 1) * (i + 1) * l.getFocusLength();
        }

        return sum;
    }

    private static List<Lens> getAllLenses(Path path) throws IOException {
        List<String> list = Files.readAllLines(path);
        List<Lens> toRet = new ArrayList<>();
        list.forEach(s -> Arrays.stream(s.split(",")).forEach(l -> toRet.add(getLenseFromString(l))));

        return toRet;
    }

    private static Lens getLenseFromString(String l) {
        int indexOfOp = l.indexOf('=') >= 0 ? l.indexOf('=') : l.indexOf('-');
        String label = l.substring(0, indexOfOp);
        char op = l.charAt(indexOfOp);
        Integer focusLength = null;
        if (op == '=') {
            focusLength = Integer.valueOf(l.substring(l.length() - 1));
        }

        return new Lens(label, op, focusLength);
    }


    private static class Lens {
        private String label;
        private char operation;
        private Integer focusLength;

        public Lens(String label, char operation, Integer focusLength) {
            this.label = label;
            this.operation = operation;
            this.focusLength = focusLength;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public char getOperation() {
            return operation;
        }

        public void setOperation(char operation) {
            this.operation = operation;
        }

        public Integer getFocusLength() {
            return focusLength;
        }

        public void setFocusLength(Integer focusLength) {
            this.focusLength = focusLength;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public int hashCode() {
            return getHash(label);
        }

        public static int getHash(String s) {
            int sum = 0;
            for (char c : s.toCharArray()) {
                sum += c;
                sum *= 17;
                sum %= 256;
            }

            return sum;
        }
    }
}