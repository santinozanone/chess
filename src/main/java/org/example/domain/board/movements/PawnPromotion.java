package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;
import org.example.presentation.implementation.Board;

public class PawnPromotion implements Move{

    private MoveDto moveDto;
    private Piece originalPiece;
    private Piece promotedPiece;

    private Piece pieceEaten;

    public PawnPromotion(MoveDto moveDto, Piece originalPiece,Piece pieceEaten ,Piece promotedPiece) {
        this.moveDto = moveDto;
        this.originalPiece = originalPiece;
        this.promotedPiece = promotedPiece;
        this.pieceEaten = pieceEaten;
    }

    @Override
    public void makeMove(Piece[][] board) {
        board[moveDto.getDestinationX()][moveDto.getDestinationY()] = promotedPiece;
        board[moveDto.getOriginX()][moveDto.getOriginY()] = null;
    }
    @Override
    public void undoMove(Piece[][] board) {
        board[moveDto.getOriginX()][moveDto.getOriginY()] = originalPiece;
        board[moveDto.getDestinationX()][moveDto.getDestinationY()] = pieceEaten;
    }
    public void setPromotedPiece(Piece promotedPiece) {
        this.promotedPiece = promotedPiece;
    }
    @Override
    public MoveDto getMoveDto() {
        return moveDto;
    }
}
