package org.example.domain;

public class Reina extends Piece{


    public Reina(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        int rowspaces = Math.abs(originX - destinationX);
        int columnSpaces = Math.abs(originY - destinationY);
        boolean diagonal = rowspaces == columnSpaces;
        boolean horizontalOrVertical = Math.abs(originX-destinationX) == 0 || Math.abs(originY - destinationY) == 0;
        return  diagonal  || horizontalOrVertical;

    }

    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }
}
