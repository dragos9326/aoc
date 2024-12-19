package org.aoc.common;

import java.util.Objects;

public class Move {
    public static final char UP = '^';
    public static final char RIGHT = '>';
    public static final char DOWN = 'V';
    public static final char LEFT = '<';

    private int i;
    private int j;
    private char dir;

    public Move(int i, int j, char dir) {
        this.i = i;
        this.j = j;
        this.dir = dir;
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
        Move move = (Move) o;
        return i == move.i && j == move.j && dir == move.dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, dir);
    }
}
