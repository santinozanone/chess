package org.example.dto;

public class PositionDto {
    private int X;
    private int Y;

    public PositionDto(int x, int y) {
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

}
