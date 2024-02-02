package org.example.domain.board.piece;

public class King extends Piece {


    public King(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        int rowSpaces = Math.abs(originX - destinationX);
        int columnSpaces = Math.abs(originY - destinationY);
        boolean diagonal = rowSpaces == columnSpaces;
        boolean horizontalOrVertical = Math.abs(originX-destinationX) == 0 || Math.abs(originY - destinationY) == 0;

        boolean oneMove = rowSpaces==1 && columnSpaces==1 || rowSpaces==0 && columnSpaces==1 ||  rowSpaces==1 && columnSpaces==0 ;
        return  oneMove && diagonal  || oneMove && horizontalOrVertical;
    }


    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }

}
