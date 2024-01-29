package org.example.domain.service.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;

import java.util.List;

public interface MoveValidator {
    List<PositionDto> getPossibleMovesOfPiece(DomainBoard domainBoard, List<Move> moves, PositionDto positionDto, PieceColor turno);
    boolean doPossibleMovementExists(DomainBoard domainBoard, List<Move> moves,PieceColor turno);
    MovementStatus isSingleMovePossible(DomainBoard domainBoard, List<Move> moves, MoveDto move, PieceColor turno);
}
