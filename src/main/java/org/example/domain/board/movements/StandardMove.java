package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

public class StandardMove implements Move{
    private MoveDto moveDto;
    private Piece pieceEaten;
    public StandardMove(MoveDto moveDto,Piece pieceEaten) {
        this.moveDto = moveDto;
        this.pieceEaten = pieceEaten;
    }

    @Override
    public void makeMove(Piece[][] board) {
        int originX = moveDto.getOriginX();
        int originY = moveDto.getOriginY();
        int destinationX = moveDto.getDestinationX();
        int destinationY = moveDto.getDestinationY();
        board[destinationX][destinationY] = board[originX][originY];
        board[originX][originY] = null;
    }

    @Override
    public void undoMove(Piece[][] board) {
        int originX = moveDto.getOriginX();
        int originY = moveDto.getOriginY();
        int destinationX = moveDto.getDestinationX();
        int destinationY = moveDto.getDestinationY();
        board[originX][originY] = board[destinationX][destinationY];
        board[destinationX][destinationY] = pieceEaten;

    }

    @Override
    public boolean hasPositionMoved(PositionDto positionOfPieceToVerify) {
        if (moveDto.getOriginX() == positionOfPieceToVerify.getX() && moveDto.getOriginY() == positionOfPieceToVerify.getY()) {
            return true; // the piece has moved
        }
        return false;
    }

    protected Piece getPieceEaten() {
        return pieceEaten;
    }

    @Override
    public MoveDto getMoveDto() {
        return moveDto;
    }


}
