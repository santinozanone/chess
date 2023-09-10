package org.example.domain.board;

public class Peon extends Piece {

    private boolean firstMovementRealized = false;

    private Piece board[][];

    public Peon(PieceColor color,Piece board [][]) {
        super(color);
        this.board = board;
    }

    @Override
    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        boolean movement;
        boolean whiteMovement = (((originX - destinationX) == 1) && (originY == destinationY));
        boolean blackMovement = (((originX - destinationX) == -1) && (originY == destinationY));

        if (getColor().equals(PieceColor.WHITE)) {
            movement = whiteMovement;
        } else {
            movement = blackMovement;
        }

        // CHEQUEAR SI PEON QUE HAY DETRAS REALIZO MOVIMIENTO DOBLE EN EL TURNO ANTERIOR Y HACER PEASANT

        if  ( (getColor().equals(PieceColor.BLACK) && originX != 1) || (getColor().equals(PieceColor.WHITE) && originX != 6) ) { // verify if pawn is at its original position
            return movement;
        } else {
            movement = (movement || getColor().equals(PieceColor.WHITE)  && (Math.abs(originX - destinationX) == 2) && (originY == destinationY)
                    || getColor().equals(PieceColor.BLACK) && (Math.abs(originX - destinationX) == 2) && (originY == destinationY));
        }
        return movement;
    }



    public boolean isEatingMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        boolean movement;
        boolean whiteMovement = ((originX - destinationX) == 1) && (originY - destinationY == 1) || ((originX - destinationX) == 1) && (originY - destinationY == -1);
        boolean blackMovement = (((originX - destinationX) == -1) && (originY - destinationY) == -1) || (((originX - destinationX) == -1) && (originY - destinationY) == 1);


        if (getColor().equals(PieceColor.WHITE)) {
            if (((originX - destinationX) == 1) && (originY - destinationY == 0)) {
                movement = false;
            } else {
                movement = whiteMovement;
            }
        } else {
            if (((originX - destinationX) == -1) && (originY - destinationY == 0)) {
                movement = false;
            } else {
                movement = blackMovement;
            }
        }

        return movement;

    }


}
