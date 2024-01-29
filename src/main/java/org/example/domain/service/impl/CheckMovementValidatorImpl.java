package org.example.domain.service.impl;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.piece.Rey;
import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class CheckMovementValidatorImpl implements CheckMovementValidator {
    /*
    * - detecting checkmate
    * detecting if the king is in check
    * */
    private PositionValidator positionHandler;

    public CheckMovementValidatorImpl(PositionValidator positionHandler){
        this.positionHandler = positionHandler;
    }

    public boolean isKingCheck(DomainBoard board, PieceColor turno) {
        if (getPiecesCheckingKing(board, turno).size() > 0) return true;
        return false;
    }

    public List<PositionDto> getPiecesCheckingKing(DomainBoard board, PieceColor turno) {
        List<PositionDto> checkerPieces = new ArrayList<>();
        PositionDto positionDto = positionHandler.getPiecePosition(board, Rey.class, turno);
        int kingX = positionDto.getX();
        int kingY = positionDto.getY();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                MoveDto move = new MoveDto(i, j, kingX, kingY);
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.getColor() != turno && piece.isEatingMovementPossible(i, j, kingX, kingY) && !positionHandler.arePiecesInBetween(board, move)) {
                    checkerPieces.add(new PositionDto(i, j));
                }
            }
        }
        return checkerPieces;
    }


}
