package org.example.domain.service.specialMoves.impl;

import org.example.domain.board.movements.CastlingMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.piece.Rey;
import org.example.domain.board.piece.Torre;
import org.example.domain.service.impl.PositionHandler;
import org.example.domain.service.specialMoves.interfaces.CastlingMoveValidator;
import org.example.domain.service.interfaces.CheckMovementHandler;
import org.example.domain.strategy.implementation.NotFilterStrategy;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public class CastlingMoveValidatorImpl implements CastlingMoveValidator {
    public Move getCastlingMoveIfPossible(Piece[][] matrix, PositionDto actualPosition, MoveDto moveToMake, PieceColor turn, List<Move> moves, PositionHandler positionHandler, CheckMovementHandler checkMovementHandler) {
    /* - castling is only possible if neither the king nor the rook has moved
       - there must not be any pieces between the king and the rook
       - the king may not be in check
       -the square the king goes to and any intervening squares may not be under attack
        however, there is nothing to prevent castling if the rook is under attack */
        int originYPositionKing = 4;
        int xPositionByTurn = -1;
        int rookDestinationY = 0;
        int kingDestinationY = 0;
        int rookOriginY = 0;
        Move castlingMovement = null;
        //verify that the piece selected is a rook or king
        Piece actualPiece = matrix[actualPosition.getX()][actualPosition.getY()];
        if (checkMovementHandler.isKingCheck(matrix, turn)) {
            return null; // error
        }

        if (turn == PieceColor.WHITE) {
            xPositionByTurn = 7; // if its white turn that means we need to check the 7th row
        } else {
            xPositionByTurn = 0; // otherwise its black turn and we have to check th 0 row
        }

        if (!(actualPiece instanceof Rey) || moveToMake.getOriginX() != xPositionByTurn || moveToMake.getOriginY() != originYPositionKing) {
            return null; // error
        }

        if (moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 6 || moveToMake.getDestinationX() == xPositionByTurn && moveToMake.getDestinationY() == 2) {
            if (positionHandler.getIfPieceHasMoved(new PositionDto(xPositionByTurn, originYPositionKing), moves)) { // check that the king (the origin piece should be a king for being able to castle) hasnt moved
                return null; // error
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
            Piece rook = matrix[xPositionByTurn][rookOriginY];
            if (!(rook instanceof Torre)) return null;
            if (positionHandler.getIfPieceHasMoved(new PositionDto(xPositionByTurn, rookOriginY), moves)) {
                return null;
            }
            MoveDto castlingMove = new MoveDto(xPositionByTurn, originYPositionKing, xPositionByTurn, rookOriginY);
            if ((positionHandler.arePiecesInBetween(matrix, castlingMove))) { // if there are pieces in the middle we return
                return null;
            }
            for (PositionDto positionDto : positionHandler.getPositionsInBetween(matrix, new NotFilterStrategy(), xPositionByTurn, originYPositionKing, xPositionByTurn, rookOriginY)) {
                if (positionHandler.isSquareBeingAttacked(matrix, positionDto, turn)) { // if a position is being attacked, castling cannot be made
                    return null; // error
                }
                castlingMovement = new CastlingMove(new MoveDto(xPositionByTurn, rookOriginY, xPositionByTurn, rookDestinationY), new MoveDto(xPositionByTurn, originYPositionKing, xPositionByTurn, kingDestinationY));
            }
        }
        return castlingMovement;
    }

}
