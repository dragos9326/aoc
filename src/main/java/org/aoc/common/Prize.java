package org.aoc.common;

public class Prize {
    private int axDelta;
    private int ayDelta;
    private int bxDelta;
    private int byDelta;
    private AxisPoint location;

    public Prize(int axDelta, int ayDelta, int bxDelta, int byDelta, AxisPoint location) {
        this.axDelta = axDelta;
        this.ayDelta = ayDelta;
        this.bxDelta = bxDelta;
        this.byDelta = byDelta;
        this.location = location;
    }

    public Prize() {
    }

    public int getAxDelta() {
        return axDelta;
    }

    public void setAxDelta(int axDelta) {
        this.axDelta = axDelta;
    }

    public int getAyDelta() {
        return ayDelta;
    }

    public void setAyDelta(int ayDelta) {
        this.ayDelta = ayDelta;
    }

    public int getBxDelta() {
        return bxDelta;
    }

    public void setBxDelta(int bxDelta) {
        this.bxDelta = bxDelta;
    }

    public int getByDelta() {
        return byDelta;
    }

    public void setByDelta(int byDelta) {
        this.byDelta = byDelta;
    }

    public AxisPoint getLocation() {
        return location;
    }

    public void setLocation(AxisPoint location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Prize{" +
                "axDelta=" + axDelta +
                ", ayDelta=" + ayDelta +
                ", bxDelta=" + bxDelta +
                ", byDelta=" + byDelta +
                ", location=" + location +
                '}';
    }
}
