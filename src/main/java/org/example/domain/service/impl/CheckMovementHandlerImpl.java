package org.example.domain.service.impl;

import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.piece.Rey;
import org.example.domain.service.interfaces.CheckMovementHandler;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class CheckMovementHandlerImpl implements CheckMovementHandler {
    /*
    * - detecting checkmate
    * detecting if the king is in check
    * */
    private PositionHandler positionHandler;

    public CheckMovementHandlerImpl(PositionHandler positionHandler){
        this.positionHandler = positionHandler;
    }

    public boolean isKingCheck(Piece board[][], PieceColor turno) {
        if (getPiecesCheckingKing(board, turno).size() > 0) return true;
        return false;
    }

    public List<PositionDto> getPiecesCheckingKing(Piece board[][], PieceColor turno) {
        List<PositionDto> checkerPieces = new ArrayList<>();
        int positions[] = positionHandler.getPiecePosition(board, Rey.class, turno);
        int kingX = positions[0];
        int kingY = positions[1];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                MoveDto move = new MoveDto(i, j, kingX, kingY);
                if (board[i][j] != null && board[i][j].getColor() != turno && board[i][j].isEatingMovementPossible(i, j, kingX, kingY) && !positionHandler.arePiecesInBetween(board, move)) {
                    checkerPieces.add(new PositionDto(i, j));
                }
            }
        }
        return checkerPieces;
    }


}
