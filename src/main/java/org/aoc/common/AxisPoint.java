package org.aoc.common;

public class AxisPoint {
    private long x;
    private long y;

    public AxisPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "AxisPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
