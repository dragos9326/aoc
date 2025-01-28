package org.aoc.common;

public class VelocityPoint extends AxisPoint {
    private long horizontalMove;
    private long verticalMove;

    public VelocityPoint(long horizontalMove, long verticalMove, long x, long y) {
        super(x, y);
        this.horizontalMove = horizontalMove;
        this.verticalMove = verticalMove;
    }

    public long getHorizontalMove() {
        return horizontalMove;
    }

    public void setHorizontalMove(long horizontalMove) {
        this.horizontalMove = horizontalMove;
    }

    public long getVerticalMove() {
        return verticalMove;
    }

    public void setVerticalMove(long verticalMove) {
        this.verticalMove = verticalMove;
    }

    @Override
    public String toString() {
        return "VelocityPoint{" +
                "horizontalMove=" + horizontalMove +
                ", verticalMove=" + verticalMove +
                ", x=" + getX() +
                ", y=" + getY() +
                '}';
    }

    public void move(int steps, int maxWidth, int maxHeight) {
        long newX = (getX() + horizontalMove * steps) % maxWidth;
        if (newX < 0) {
            newX = maxWidth - Math.abs(newX);
        }
        setX(newX);

        long newY = (getY() + verticalMove * steps) % maxHeight;
        if (newY < 0) {
            newY = maxHeight - Math.abs(newY);
        }
        setY(newY);
    }
}
