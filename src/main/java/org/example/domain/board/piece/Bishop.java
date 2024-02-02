package org.example.domain.board.piece;

public class Bishop extends Piece {

    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        int rowSpaces = Math.abs(originX - destinationX);
        int columnSpaces = Math.abs(originY - destinationY);
        return  rowSpaces == columnSpaces;
    }

    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }
}
