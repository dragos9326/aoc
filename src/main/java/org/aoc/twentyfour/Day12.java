package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.Area;
import org.aoc.common.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author dragos
 */
@Slf4j
public class Day12 {

    private static final Logger logger = LoggerFactory.getLogger(Day12.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day12Input.txt");

        List<String> list = Files.readAllLines(path);
        char[][] matrix = new char[list.size()][list.get(0).length()];

        for (int i = 0; i < list.size(); i++) {
            matrix[i] = list.get(i).toCharArray();
        }

        Map<Character, Set<Area>> areas = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                Point p = new Point(i, j, matrix[i][j]);
                if (!isAlreadyAdded(areas, p)) {
                    addAreaToMap(areas, matrix, p);
                }
            }
        }

        logger.debug("Day 12 part 1 result: {}", getFirstPartAnswer(areas));
        logger.debug("Day 12 part 2 result: {}", getSecondPartAnswer(areas));
    }

    private static long getFirstPartAnswer(Map<Character, Set<Area>> areas) {
        return areas.values().stream()
                .flatMap(Collection::stream)
                .map(Area::getPrice)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static long getSecondPartAnswer(Map<Character, Set<Area>> areas) {
        return areas.values().stream()
                .flatMap(Collection::stream)
                .map(Area::getPriceBySides)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static void addAreaToMap(Map<Character, Set<Area>> areas, char[][] matrix, Point p) {
        Area a = getArea(matrix, p);
        Set<Area> areaList = areas.get(p.getValue());
        if (areaList == null) {
            areaList = new HashSet<>();
        }

        areaList.add(a);
        areas.put(p.getValue(), areaList);
    }

    private static Area getArea(char[][] matrix, Point p) {
        Set<Point> points = new HashSet<>();
        points.add(p);
        traverseMap(matrix, points, p.getValue(), p.getI() - 1, p.getJ());
        traverseMap(matrix, points, p.getValue(), p.getI() + 1, p.getJ());
        traverseMap(matrix, points, p.getValue(), p.getI(), p.getJ() - 1);
        traverseMap(matrix, points, p.getValue(), p.getI(), p.getJ() + 1);
        return new Area(p.getValue(), points);
    }

    private static void traverseMap(char[][] matrix, Set<Point> points, char value, int i, int j) {
        if (i < 0 || i >= matrix.length) {
            return;
        }
        if (j < 0 || j >= matrix.length) {
            return;
        }

        if (matrix[i][j] != value) {
            return;
        }

        if (!points.add(new Point(i, j, value))) {
            return;
        }

        traverseMap(matrix, points, value, i - 1, j);
        traverseMap(matrix, points, value, i + 1, j);
        traverseMap(matrix, points, value, i, j - 1);
        traverseMap(matrix, points, value, i, j + 1);
    }

    private static boolean isAlreadyAdded(Map<Character, Set<Area>> areas, Point p) {
        Set<Area> areasList = areas.get(p.getValue());
        if (areasList == null) {
            return false;
        }

        for (Area a : areasList) {
            if (a.containsPoint(p)) {
                return true;
            }
        }

        return false;
    }

}