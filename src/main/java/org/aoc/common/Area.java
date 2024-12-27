package org.aoc.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Area {
    private char value;
    private Set<Point> points;

    public Area(char value, Set<Point> points) {
        this.value = value;
        this.points = points;
    }

    public Area() {
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    public int getArea() {
        return points != null ? points.size() : 0;
    }

    public int getPerimeter() {
        if (points == null) {
            return 0;
        }

        int count = 0;
        for (Point p : points) {
            if (!points.contains(new Point(p.getI() - 1, p.getJ(), p.getValue()))) {
                count++;
            }
            if (!points.contains(new Point(p.getI() + 1, p.getJ(), p.getValue()))) {
                count++;
            }
            if (!points.contains(new Point(p.getI(), p.getJ() - 1, p.getValue()))) {
                count++;
            }
            if (!points.contains(new Point(p.getI(), p.getJ() + 1, p.getValue()))) {
                count++;
            }
        }

        return count;
    }

    public int getPrice() {
        return getArea() * getPerimeter();
    }

    public int getPriceBySides() {
        return getArea() * getNoOfSides();
    }

    public int getNoOfSides() {
        Set<Point> margins = transformToMargins();
        Set<Point> corners = new HashSet<>();
        for (Point p : margins) {
            corners.addAll(getCorners(p, margins));
        }

        return corners.size();

    }

    private Set<Point> transformToMargins() {
        Set<Point> margins = new HashSet<>();

        for (Point p : points) {
            margins.add(new Point(p.getI() * 2, p.getJ() * 2, p.getValue()));
            margins.add(new Point(p.getI() * 2, p.getJ() * 2 + 1, p.getValue()));
            margins.add(new Point(p.getI() * 2 + 1, p.getJ() * 2, p.getValue()));
            margins.add(new Point(p.getI() * 2 + 1, p.getJ() * 2 + 1, p.getValue()));
        }

        return margins;
    }

    private Set<Point> getCorners(Point p, Set<Point> points) {
        Set<Point> corners = new HashSet<>();

        corners.addAll(getExtCorners(p, points));
        corners.addAll(getIntCorners(p, points));

        return corners;
    }

    private Collection<? extends Point> getIntCorners(Point p, Set<Point> points) {
        Set<Point> corners = new HashSet<>();
        if (pointHasTopNeighbour(p, points)) {
            if (pointHasRightNeighbour(p, points) && !pointHasBottomNeighbour(p, points) && !pointHasLeftNeighbour(p, points)) {
                corners.add(p);
            }
            if (pointHasLeftNeighbour(p, points) && !pointHasBottomNeighbour(p, points) && !pointHasRightNeighbour(p, points)) {
                corners.add(p);
            }
        }

        if (pointHasBottomNeighbour(p, points)) {
            if (pointHasRightNeighbour(p, points) && !pointHasTopNeighbour(p, points) && !pointHasLeftNeighbour(p, points)) {
                corners.add(p);
            }
            if (pointHasLeftNeighbour(p, points) && !pointHasTopNeighbour(p, points) && !pointHasRightNeighbour(p, points)) {
                corners.add(p);
            }
        }
        return corners;
    }

    private Collection<? extends Point> getExtCorners(Point p, Set<Point> points) {
        Set<Point> corners = new HashSet<>();
        if (pointHasTopNeighbour(p, points) && pointHasRightNeighbour(p, points)
                && pointHasBottomNeighbour(p, points) && pointHasLeftNeighbour(p, points) &&
                (!pointHasDiagFrontForwardNeighbour(p, points) || !pointHasDiagBackBackwardNeighbour(p, points)
                    || !pointHasDiagFrontBackwardNeighbour(p, points) || !pointHasDiagBackForwardNeighbour(p, points))) {
                corners.add(p);

        }
        return corners;
    }

    private boolean pointHasDiagFrontForwardNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() - 1, p.getJ() + 1, p.getValue()));
    }

    private boolean pointHasDiagFrontBackwardNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() + 1, p.getJ() - 1, p.getValue()));
    }

    private boolean pointHasDiagBackForwardNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() + 1, p.getJ() + 1, p.getValue()));
    }

    private boolean pointHasDiagBackBackwardNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() - 1, p.getJ() - 1, p.getValue()));
    }

    private boolean pointHasLeftNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI(), p.getJ() - 1, p.getValue()));
    }

    private boolean pointHasRightNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI(), p.getJ() + 1, p.getValue()));
    }

    private boolean pointHasTopNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() - 1, p.getJ(), p.getValue()));
    }

    private boolean pointHasBottomNeighbour(Point p, Set<Point> points) {
        return points.contains(new Point(p.getI() + 1, p.getJ(), p.getValue()));
    }

    public boolean containsPoint(Point p) {
        if (points == null) {
            return false;
        }

        return points.contains(p);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Area area = (Area) o;
        return value == area.value && Objects.equals(points, area.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, points);
    }
}
