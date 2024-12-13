package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 {
    public static List<String> mistakes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day13Input.txt");

        List<LavaMap> lavaMaps = getLavaMaps(path);

        System.out.println("Day 13 part 1 result:");
        System.out.println(getFirstPartAnswer(lavaMaps));
        System.out.println("Day 13 part 2 result:");
        System.out.println(getSecondPartAnswer(lavaMaps));
    }

    private static List<LavaMap> getLavaMaps(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<String> currentLines = new ArrayList<>();
        List<LavaMap> lavaMaps = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!currentLines.isEmpty()) {
                    lavaMaps.add(new LavaMap(currentLines));
                    currentLines = new ArrayList<>();
                }
            } else {
                currentLines.add(line);
            }
        }
        if (!currentLines.isEmpty()) {
            lavaMaps.add(new LavaMap(currentLines));
        }

        return lavaMaps;
    }

    private static long getFirstPartAnswer(List<LavaMap> lavaMaps) {
        int sum = 0;

        for (LavaMap map : lavaMaps) {
            sum += map.getSummary();
        }

        return sum;
    }

    private static long getSecondPartAnswer(List<LavaMap> lavaMaps) {
        int sum = 0;

        for (LavaMap map : lavaMaps) {
            int res = map.getSmudgeSummary();
            if (res < 0) {
                System.out.println(map + "" + res);
            }
            sum += res;
        }

        return sum;
    }


    public static class LavaMap {
        private List<String> rows;

        public LavaMap(List<String> rows) {
            this.rows = new ArrayList<>(rows);
        }

        public List<String> getRows() {
            return rows;
        }

        public void setRows(List<String> rows) {
            this.rows = rows;
        }

        @Override
        public String toString() {
            StringBuilder map = new StringBuilder();
            for (String line : rows) {
                map.append(line);
                map.append(System.getProperty("line.separator"));

            }
            return map.toString();
        }

        public int getSummary() {
            //try reflection horizontally
            int reflectionHorizont = getReflectionHorizontally(0);
            if (reflectionHorizont > 0) {
                return reflectionHorizont;
            } else {
                //try reflection vertically
                int reflectionVert = getReflectionVertically(0);
                if (reflectionVert > 0) {
                    return 100 * reflectionVert;
                }
            }

            return 0;
        }

        private int getReflectionVertically(int mistakesAllowed) {
            int size = rows.size();
            for (int i = 1; i < size; i++) {
                if (isReflectionVertically(i, mistakesAllowed)) {
                    return i;
                }
            }

            return -1;
        }

        private boolean isReflectionVertically(int i, int mistakesAllowed) {
            for (int j = 1; j + i - 1 < rows.size() && i - j >= 0; j++) {
                String before = rows.get(i - j);
                String after = rows.get(i + j - 1);
                mistakesAllowed = compareStrings(before, after, mistakesAllowed);
                if (mistakesAllowed < 0) {
                    return false;
                }
            }

            //all mistakes gotta be used
            return mistakesAllowed == 0;
        }

//        s =8
//        l =2,r =6; 0-2; 2-4;
//        l =3,r =5;,0-3,3-6
//        l =5,r =3; 2-5,5-8;


        private int getReflectionHorizontally(int mistakesAllowed) {
            int width = rows.get(0).length();
            for (int i = 1; i < width; i++) {
                if (isReflectionHorizontally(i, mistakesAllowed)) {
                    return i;
                }
            }

            return -1;
        }

        private boolean isReflectionHorizontally(int i, int mistakesAllowed) {
            int width = rows.get(0).length();
            for (String row : rows) {
                String before = new StringBuilder(row.substring(i - Math.min(i, width - i), i)).reverse().toString();
                String after = row.substring(i, i + Math.min(i, width - i));
                mistakesAllowed = compareStrings(before, after, mistakesAllowed);
                if (mistakesAllowed < 0) {
                    return false;
                }
            }

            //all mistakes gotta be used
            return mistakesAllowed == 0;
        }

        private int compareStrings(String before, String after, int mistakesAllowed) {
            if (mistakesAllowed < 0) {
                return mistakesAllowed;
            } else if (mistakesAllowed == 0) {
                if (!before.equals(after)) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return compareStringsWithMistakes(before, after, mistakesAllowed);
            }
        }

        private int compareStringsWithMistakes(String before, String after, int mistakesAllowed) {
            if (before.equals(after)) {
                return mistakesAllowed;
            } else {
                for (int i = 0; i < before.length(); i++) { // go from first to last character index the words
                    if (before.charAt(i) != after.charAt(i)) { // if this character from word 1 does not equal the character from word 2
                        mistakesAllowed--; // reduce one mistake allowed
                        if (mistakesAllowed < 0) { // and if you have more mistakes than allowed
                            return -1; // return false
                        }
                    }
                }

                return mistakesAllowed;
            }
        }

        public int getSmudgeSummary() {
            //try reflection horizontally
            int reflectionHorizont = getReflectionHorizontally(1);
            if (reflectionHorizont > 0) {
                return reflectionHorizont;
            } else {
                //try reflection vertically
                int reflectionVert = getReflectionVertically(1);
                if (reflectionVert > 0) {
                    return 100 * reflectionVert;
                }
            }

            return 0;
        }
    }

}