package org.example.domain.service.interfaces;

import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.PositionDto;

import java.util.List;

public interface CheckMovementHandler {

    boolean isKingCheck(Piece board[][], PieceColor turno);
    List<PositionDto> getPiecesCheckingKing(Piece board[][], PieceColor turno);
}
