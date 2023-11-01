package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;

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

    public Piece getPieceEaten() {
        return pieceEaten;
    }

    public MoveDto getMoveDto() {
        return moveDto;
    }
}
