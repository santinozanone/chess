package org.example.domain.board.piece;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        // caballo se mueve dos en x , 1 en y  --or
        // 2 en y , 1 en x
        return (( Math.abs((originX - destinationX)) == 2) && ( Math.abs((originY - destinationY)) == 1)) || (( Math.abs((originX - destinationX)) == 1) && ( Math.abs((originY - destinationY)) == 2));


    }

    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }

}
