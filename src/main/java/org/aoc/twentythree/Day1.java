package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day1 {

    static Map<String, String> toReplaceDigits = new HashMap<>();

    static {
        toReplaceDigits.put("one", "1");
        toReplaceDigits.put("two", "2");
        toReplaceDigits.put("three", "3");
        toReplaceDigits.put("four", "4");
        toReplaceDigits.put("five", "5");
        toReplaceDigits.put("six", "6");
        toReplaceDigits.put("seven", "7");
        toReplaceDigits.put("eight", "8");
        toReplaceDigits.put("nine", "9");
        toReplaceDigits.put("zero", "0");
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day1Input.txt");

        List<String> list = Files.readAllLines(path);


        System.out.println("Day 1 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 1 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static int getFirstPartAnswer(List<String> list) {
        int num = 0;

        for (String s : list) {
            if (!s.trim().isEmpty()) {
                String lineNums = s.replaceAll("[^\\d]", "");
                if (!lineNums.isEmpty()) {
                    num += Integer.parseInt(lineNums.toCharArray()[0] + ""
                            + lineNums.toCharArray()[lineNums.toCharArray().length - 1]);
                }
            }
        }

        return num;
    }

    private static int getSecondPartAnswer(List<String> list) {
        return list.stream().map(Day1::swapStringDigitsToDigAndReturnCalibration).mapToInt(i -> i).sum();
    }

    private static int swapStringDigitsToDigAndReturnCalibration(String s) {
        if(s == null || s.isEmpty()){
            return 0;
        }

        s = s.toLowerCase();
        int minPos = s.length();
        int maxPos = 0;
        List<String> numList = new ArrayList<>();

        for (Map.Entry<String, String> entry : toReplaceDigits.entrySet()) {
            if(s.contains(entry.getKey())) {
                minPos = getMinPos(s, entry.getKey(), entry.getValue(), minPos, numList);
                maxPos = getMaxPos(s, entry.getKey(), entry.getValue(), maxPos, numList);
            }

            if(s.contains(entry.getValue())) {
                minPos = getMinPos(s, entry.getValue(), entry.getValue(), minPos, numList);
                maxPos = getMaxPos(s, entry.getValue(), entry.getValue(), maxPos, numList);
            }
        }

        return getIntFromList(numList);
    }

    private static int getMaxPos(String s, String key, String value, int maxPos, List<String> numList) {
        if (s.lastIndexOf(key) > maxPos) {
            if(numList.size() > 1) {
                numList.remove(1);
            }

            numList.add(value);
            return s.lastIndexOf(key);
        }

        return maxPos;
    }

    private static int getMinPos(String s, String key, String value, int minPos, List<String> numList) {
        if (s.indexOf(key) < minPos) {
            if(!numList.isEmpty()) {
                numList.remove(0);
            }

            numList.add(0, value);
            return s.indexOf(key);
        }

        return minPos;
    }

    private static int getIntFromList(List<String> numList) {
        String num = numList.get(0);
        if(numList.size() > 1){
            num += numList.get(1);
        } else {
            num += numList.get(0);
        }
        return Integer.parseInt(num);
    }
}