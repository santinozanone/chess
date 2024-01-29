package org.example.domain.service.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.PositionDto;

import java.util.List;

public interface CheckMovementValidator {

    boolean isKingCheck(DomainBoard board, PieceColor turno);
    List<PositionDto> getPiecesCheckingKing(DomainBoard board, PieceColor turno);
}
