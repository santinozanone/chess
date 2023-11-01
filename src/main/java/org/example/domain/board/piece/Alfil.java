package org.example.domain.board.piece;

public class Alfil extends Piece {

    public Alfil(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        int rowspaces = Math.abs(originX - destinationX);
        int columnSpaces = Math.abs(originY - destinationY);
        return  rowspaces == columnSpaces;
    }

    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }
}
