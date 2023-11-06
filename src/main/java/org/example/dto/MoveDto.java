package org.example.dto;

import java.util.Objects;

public class MoveDto {
    private int originX;
    private int originY;
    private int destinationX;
    private int destinationY;

    @Override
    public String toString() {
        return "MoveDto{" +
                "originX=" + originX +
                ", originY=" + originY +
                ", destinationX=" + destinationX +
                ", destinationY=" + destinationY +
                '}';
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveDto moveDto = (MoveDto) o;
        return originX == moveDto.originX && originY == moveDto.originY && destinationX == moveDto.destinationX && destinationY == moveDto.destinationY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originX, originY, destinationX, destinationY);
    }
}
