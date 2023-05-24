package org.example.dto;

public class MoveDto {
    private int originX;
    private int originY;
    private int destinationX;
    private int destinationY;

    public MoveDto(int originX, int originY, int destinationX, int destinationY) {
        this.originX = originX;
        this.originY = originY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }
}
