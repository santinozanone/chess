package org.example.domain.service.specialMoves.impl;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.piece.Peon;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.specialMoves.interfaces.PawnPromotionValidator;
import org.example.dto.MoveDto;

public class PawnPromotionValidatorImpl implements PawnPromotionValidator {
    public boolean isPawnPromotionPossible(DomainBoard board, MoveDto moveToBeMade, PieceColor turn) {
        /*once a pawn gets to the other side he must exchange that pawn as part of the same move*/
        int destinationX = 0; // as default we initialize them as white
        if (turn == PieceColor.BLACK) {
            destinationX = 7;
        }
        if (!(board.getPiece(moveToBeMade.getOriginX(),moveToBeMade.getOriginY()) instanceof Peon)) {
            return false;
        }
            if (moveToBeMade.getDestinationX() == destinationX) {
                return true;
        }
        return false;
    }
}
