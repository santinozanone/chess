package org.example.domain.board.piece;

public class Rook extends Piece {

    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
       return Math.abs(originX-destinationX) == 0 || Math.abs(originY - destinationY) == 0;
    }

    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }
}
