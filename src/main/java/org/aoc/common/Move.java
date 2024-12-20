package org.aoc.common;

import java.util.Objects;

public class Move extends Point {
    public static final char UP = '^';
    public static final char RIGHT = '>';
    public static final char DOWN = 'V';
    public static final char LEFT = '<';

    private char dir;

    public Move(int i, int j, char dir) {
        super(i, j, '0');
        this.dir = dir;
    }

    public char getDir() {
        return dir;
    }

    public void setDir(char dir) {
        this.dir = dir;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Move move = (Move) o;
        return dir == move.dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dir);
    }
}
