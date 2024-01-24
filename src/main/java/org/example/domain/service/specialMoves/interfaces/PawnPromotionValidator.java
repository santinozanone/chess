package org.example.domain.service.specialMoves.interfaces;

import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.impl.MoveValidatorHandler;
import org.example.dto.MoveDto;

public interface PawnPromotionValidator {
    boolean isPawnPromotionPossible(Piece[][] matrix, MoveDto moveToBeMade, PieceColor turn );

    }
