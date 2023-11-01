package org.example.domain.board.piece;

public class Rey extends Piece {


    public Rey(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        int rowspaces = Math.abs(originX - destinationX);
        int columnSpaces = Math.abs(originY - destinationY);
        boolean diagonal = rowspaces == columnSpaces;
        boolean horizontalOrVertical = Math.abs(originX-destinationX) == 0 || Math.abs(originY - destinationY) == 0;

        boolean oneMove = Math.abs(originX-destinationX  )==1 && Math.abs(originY-destinationY  )==1 || Math.abs(originX-destinationX  )==0 && Math.abs(originY-destinationY  )==1 || Math.abs(originX-destinationX  )==0 && Math.abs(originY-destinationY  )==0 || Math.abs(originX-destinationX  )==1 && Math.abs(originY-destinationY  )==0;
        return  oneMove && diagonal  || oneMove && horizontalOrVertical;
    }


    @Override
    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        return isMovementPossible(originX, originY, destinationX, destinationY);
    }

}
