package org.example.domain.service.specialMoves.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.impl.PositionValidatorImpl;
import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public interface CastlingMoveValidator {
    Move getCastlingMoveIfPossible(DomainBoard board, PositionDto actualPosition, MoveDto moveToMake, PieceColor turn, List<Move> moves, PositionValidator positionHandler, CheckMovementValidator checkMovementValidator);
}


