package org.example.domain.board;

public abstract class Piece {
    private PieceColor color;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public abstract boolean isMovementPossible(int originX, int originY,int destinationX,int destinationY);

    public abstract boolean isEatingMovementPossible (int originX, int originY,int destinationX,int destinationY);

}
