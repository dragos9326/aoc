package org.aoc.twentytwo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        LinkedList<Integer> elfsCalories = new LinkedList<>();
        elfsCalories.add(0);
        Path path = Paths.get("src/main/resources/twentytwo/day1Input.txt");

        List<String> list = Files.readAllLines(path);
        for(String s : list) {
            if(!s.trim().isEmpty()){
                elfsCalories.add(elfsCalories.removeLast()  + Integer.parseInt(s.trim()));
            } else {
                elfsCalories.add(0);
            }
        }

        elfsCalories.sort((a, b) -> b - a);

        System.out.println("Day 1 part 1 result:");
        System.out.println(elfsCalories.getFirst());
        System.out.println("Day 1 part 2 result:");
        System.out.println(elfsCalories.stream().limit(3).mapToInt(i -> i).sum());
    }
}