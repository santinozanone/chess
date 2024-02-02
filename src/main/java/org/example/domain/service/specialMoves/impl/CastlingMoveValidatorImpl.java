package org.example.domain.service.specialMoves.impl;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.CastlingMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.piece.King;
import org.example.domain.board.piece.Rook;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.domain.service.specialMoves.interfaces.CastlingMoveValidator;
import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.strategy.implementation.NotFilterStrategy;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public class CastlingMoveValidatorImpl implements CastlingMoveValidator {
    public Move getCastlingMoveIfPossible(DomainBoard board, MoveDto moveToMake, PieceColor turn, List<Move> moves, PositionValidator positionValidator, CheckMovementValidator checkMovementValidator) {
    /* - castling is only possible if neither the king nor the rook has moved
       - there must not be any pieces between the king and the rook
       - the king may not be in check
       -the square the king goes to and any intervening squares may not be under attack
        however, there is nothing to prevent castling if the rook is under attack */
        int originYPositionKing = 4;
        int xPositionByTurn = 0;
        int rookDestinationY = 0;
        int kingDestinationY = 0;
        int rookOriginY = 0;

        if (checkMovementValidator.isKingCheck(board, turn)) {
            return null; // error
        }
        xPositionByTurn = getXPositionByTurn(turn);

        if (!isPositionSelectedValid(moveToMake, board, xPositionByTurn, originYPositionKing, moves, positionValidator)) {
            return null;
        }

        if (moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 2) {
            rookDestinationY = 3;
            kingDestinationY = 2;
        }
        if (moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 6) {
            rookDestinationY = 5;
            kingDestinationY = 6;
            rookOriginY = 7;
        }
        if (!isRookPositionValid(board, xPositionByTurn, rookOriginY, positionValidator, moves)) {
            return null;
        }
        MoveDto castlingMove = new MoveDto(xPositionByTurn, originYPositionKing, xPositionByTurn, rookOriginY);
        if ((positionValidator.arePiecesInBetween(board, castlingMove))) { // if there are pieces in the middle we return
            return null;
        }
        List<PositionDto> positionsBetweenKingAndRook = positionValidator.getPositionsInBetween(board, new NotFilterStrategy(), xPositionByTurn, originYPositionKing, xPositionByTurn, rookOriginY);
        if (isCastingPossible(board, positionValidator, turn, positionsBetweenKingAndRook))
            return new CastlingMove(new MoveDto(xPositionByTurn, rookOriginY, xPositionByTurn, rookDestinationY), new MoveDto(xPositionByTurn, originYPositionKing, xPositionByTurn, kingDestinationY));
        else return null;
    }

    private boolean isCastingPossible(DomainBoard board, PositionValidator positionValidator, PieceColor turn, List<PositionDto> positionsInBetween) {
        for (PositionDto positionDto : positionsInBetween) {
            if (positionValidator.isSquareBeingAttacked(board, positionDto, turn)) { // if a position is being attacked, castling cannot be made
                return false; // error
            }
            return true;
        }
        return false;
    }

    private boolean isPositionSelectedValid(MoveDto moveToMake, DomainBoard board, int xPositionByTurn, int originYPositionKing, List<Move> moves, PositionValidator positionValidator) {
        Piece actualPiece = board.getPiece(moveToMake.getOriginX(), moveToMake.getOriginY());
        if (!(actualPiece instanceof King) || moveToMake.getOriginX() != xPositionByTurn || moveToMake.getOriginY() != originYPositionKing) {
            return false; // error
        }
        if (!areCastlingDestinationPositionsValid(moveToMake, xPositionByTurn)) {
            return false;
        }
        if (positionValidator.hasPieceMoved(new PositionDto(xPositionByTurn, originYPositionKing), moves)) { // check that the king (the origin piece should be a king for being able to castle) hasnt moved
            return false; // error
        }
        return true;
    }

    private boolean isRookPositionValid(DomainBoard board, int xPositionByTurn, int rookOriginY, PositionValidator positionValidator, List<Move> moves) {
        Piece rook = board.getPiece(xPositionByTurn, rookOriginY);
        if (!(rook instanceof Rook)) return false;

        if (positionValidator.hasPieceMoved(new PositionDto(xPositionByTurn, rookOriginY), moves)) {
            return false;
        }
        return true;
    }

    private boolean areCastlingDestinationPositionsValid(MoveDto moveToMake, int xPositionByTurn) {
        return moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 6 || moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 2;
    }


    private int getXPositionByTurn(PieceColor turn){
        if (turn == PieceColor.WHITE) {
            return  7; // if its white turn that means we need to check the 7th row
        } else {
            return  0; // otherwise its black turn and we have to check th 0 row
        }
    }

}
