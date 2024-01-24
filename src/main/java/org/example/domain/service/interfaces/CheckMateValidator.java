package org.example.domain.service.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;

import org.example.dto.PositionDto;

import java.util.List;

public interface CheckMateValidator {

    boolean isCheckMate(Piece board[][], List<Move> moves, PieceColor turno);

}
