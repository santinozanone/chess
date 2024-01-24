package org.example.domain.service.specialMoves.interfaces;

import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.impl.PositionHandler;
import org.example.domain.service.interfaces.CheckMovementHandler;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public interface CastlingMoveValidator {
    Move getCastlingMoveIfPossible(Piece[][] matrix, PositionDto actualPosition, MoveDto moveToMake, PieceColor turn, List<Move> moves, PositionHandler positionHandler, CheckMovementHandler checkMovementHandler);
}


