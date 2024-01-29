package org.example.domain.service.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.strategy.FilterStrategy;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public interface PositionValidator {
    List<PositionDto> getPositionsInBetween(DomainBoard board, FilterStrategy filterStrategy, int originX, int originY, int destinationX, int destinationY);
    boolean arePiecesInBetween(DomainBoard board, MoveDto move);
    PositionDto getPiecePosition(DomainBoard board, Class<? extends Piece> piece, PieceColor turno);
    boolean isSquareBeingAttacked(DomainBoard board,PositionDto square ,PieceColor turno);
    boolean hasPieceMoved(PositionDto positionOfPieceToVerify, List<Move> moves);
}
