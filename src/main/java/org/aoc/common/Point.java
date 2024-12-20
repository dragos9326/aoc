package org.aoc.common;

import java.util.Objects;

public class Point {
    private int i;
    private int j;
    private char value;

    public Point(int i, int j, char value) {
        this.i = i;
        this.j = j;
        this.value = value;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return i == point.i && j == point.j && value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, value);
    }

    @Override
    public String toString() {
        return "Point{" +
                "i=" + i +
                ", j=" + j +
                ", value=" + value +
                '}';
    }
}
