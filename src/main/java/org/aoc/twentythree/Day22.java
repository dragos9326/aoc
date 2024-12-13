package org.aoc.twentythree;

import org.aoc.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day22 {

    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;
    public static final int WEST = 4;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day22Input.txt");

        List<String> list = Files.readAllLines(path);

        List<Brick> bricks = getBricks(list);

        //order by z
        bricks.sort(Comparator.comparingInt(a -> a.z1));
        bricks.forEach( b-> System.out.println(b));

        System.out.println("Day 22 part 1 result:");
        System.out.println(getFirstPartAnswer(bricks));
        System.out.println("Day 22 part 2 result:");
        System.out.println(getSecondPartAnswer(bricks));
    }

    private static List<Brick> getBricks(List<String> list) {
        List<Brick> bricks = new ArrayList<>();
        for(String s : list){
            String[] coordinatesStrings = s.split("~");
            String[] co1 = coordinatesStrings[0].split(",");
            String[] co2 = coordinatesStrings[1].split(",");
            bricks.add(new Brick(co1, co2));
        }

        return bricks;
    }

    public static boolean areXOYProjectionsOverlapping(Brick b1, Brick b2){
        return (b1.x2 >= b2.x1 || b1.x1 <= b2.x2)
                && (b1.y1 <= b2.y2 || b1.y2 >= b2.x2);
    }

    private static long getFirstPartAnswer(List<Brick> bricks) {
        return 1l;

    }

    private static long getSecondPartAnswer(List<Brick> bricks) {
        return 1l;
    }

    private static class Brick {
        private int x1;
        private int y1;
        private int z1;
        private int x2;
        private int y2;
        private int z2;

        public Brick(int x1, int y1, int z1, int x2, int y2, int z2) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
        }

        public Brick(String[] co1, String[] co2) {
            this(Integer.parseInt(co1[0]), Integer.parseInt(co1[1]), Integer.parseInt(co1[2]),
                    Integer.parseInt(co2[0]), Integer.parseInt(co2[1]), Integer.parseInt(co2[2]));
        }

        @Override
        public String toString() {
            return "Brick{" +
                    "x1=" + x1 +
                    ", y1=" + y1 +
                    ", z1=" + z1 +
                    ", x2=" + x2 +
                    ", y2=" + y2 +
                    ", z2=" + z2 +
                    '}';
        }
    }

}