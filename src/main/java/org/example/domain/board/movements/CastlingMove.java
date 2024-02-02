package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

public class CastlingMove implements Move{
    private MoveDto rookMovement;
    private MoveDto kingMovement;

    public CastlingMove(MoveDto rookMovement, MoveDto kingMovement) {
        this.rookMovement = rookMovement;
        this.kingMovement = kingMovement;
    }

    @Override
    public void makeMove(Piece[][] board) {
        board[rookMovement.getDestinationX()][rookMovement.getDestinationY()] = board[rookMovement.getOriginX()][rookMovement.getOriginY()];
        board[rookMovement.getOriginX()][rookMovement.getOriginY()] = null;
        board[kingMovement.getDestinationX()][kingMovement.getDestinationY()] = board[kingMovement.getOriginX()][kingMovement.getOriginY()];
        board[kingMovement.getOriginX()][kingMovement.getOriginY()] = null;
    }

    @Override
    public void undoMove(Piece[][] board) {
        board[rookMovement.getOriginX()][rookMovement.getOriginY()] = board[rookMovement.getDestinationX()][rookMovement.getDestinationY()];
        board[rookMovement.getDestinationX()][rookMovement.getDestinationY()] = null;
        board[kingMovement.getOriginX()][kingMovement.getOriginY()] = board[kingMovement.getDestinationX()][kingMovement.getDestinationY()];
        board[kingMovement.getDestinationX()][kingMovement.getDestinationY()] = null;
    }

    @Override
    public boolean hasPositionMoved(PositionDto positionOfPieceToVerify) {
        for (MoveDto moveDto: new MoveDto[]{kingMovement,rookMovement})
            if (moveDto.getOriginX() == positionOfPieceToVerify.getX() && moveDto.getOriginY() == positionOfPieceToVerify.getY()) {
                return true; // the piece has moved
            }
        return false;
    }

    @Override
    public MoveDto getMoveDto() {
        return kingMovement;
    }
}
