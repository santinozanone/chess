package org.example.domain.service.specialMoves.interfaces;

import org.example.domain.board.movements.EnPassantMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public interface EnPassantMoveValidator {
      EnPassantMove getEnPassantMoveIfPossible(List<Move> moves, Piece[][] matrix, PositionDto actualPosition, PieceColor turn, MoveDto moveToMake);
    }
