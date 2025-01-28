package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Main;
import org.aoc.common.VelocityPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dragos
 */
@Slf4j
public class Day14 {

    private static final Logger logger = LoggerFactory.getLogger(Day14.class);
    private static final Pattern ROBOT_DEFS = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day14Input.txt");

        List<String> list = Files.readAllLines(path);

        List<VelocityPoint> points = getVelocityPoints(list);
        logger.debug("Day 14 part 1 result: {}", getFirstPartAnswer(points));

        points = getVelocityPoints(list);
        logger.debug("Day 14 part 2 result: {}", getSecondPartAnswer(points));
    }

    private static int getFirstPartAnswer(List<VelocityPoint> points) {
        points.forEach(p -> p.move(100, WIDTH, HEIGHT));

        int sumQ1 = 0;
        int sumQ2 = 0;
        int sumQ3 = 0;
        int sumQ4 = 0;
        for (VelocityPoint p : points) {
            int midX = WIDTH / 2;
            int midY = HEIGHT / 2;

            if (p.getX() < midX) {
                if (p.getY() < midY) {
                    sumQ2++;
                } else if (p.getY() > midY) {
                    sumQ3++;
                }
            }

            if (p.getX() > midX) {
                if (p.getY() < midY) {
                    sumQ1++;
                } else if (p.getY() > midY) {
                    sumQ4++;
                }
            }

        }
        return sumQ1 * sumQ2 * sumQ3 * sumQ4;
    }

    private static int getSecondPartAnswer(List<VelocityPoint> points) {
        for (int i = 0; i < 100000000; i++) {
            points.forEach(p -> p.move(1, WIDTH, HEIGHT));
            if (isTree(points)) {
                return i + 1;
            }
        }

        return 0;
    }

    private static boolean isTree(List<VelocityPoint> points) {
        //just assume that if we find a block of 5x5 in the middle, it means that is a tree
        char[][] matrix = new char[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                matrix[i][j] = '.';
            }
        }
        points.forEach(p -> matrix[(int) p.getY()][(int) p.getX()] = '#');

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (isSquareFilled(matrix, i, j)) {
                    logger.debug("{} {}", i, j);
                    Main.printMatrix(matrix);
                    return true;
                }
            }
        }


        return false;
    }

    private static boolean isSquareFilled(char[][] matrix, int startI, int startJ) {
        if (startI + 8 > HEIGHT || startJ + 8 > WIDTH) {
            return false;
        }
        for (int i = startI; i < startI + 8; i++) {
            for (int j = startJ; j < startJ + 8; j++) {
                if (matrix[i][j] != '#') {
                    return false;
                }
            }
        }

        return true;
    }

    private static List<VelocityPoint> getVelocityPoints(List<String> list) {
        List<VelocityPoint> vps = new ArrayList<>();
        for (String s : list) {
            Matcher m = ROBOT_DEFS.matcher(s);
            if (m.find()) {
                long x = Long.parseLong(m.group(1));
                long y = Long.parseLong(m.group(2));
                long hm = Long.parseLong(m.group(3));
                long vm = Long.parseLong(m.group(4));
                VelocityPoint p = new VelocityPoint(hm, vm, x, y);
                vps.add(p);
            }
        }
        return vps;
    }

}