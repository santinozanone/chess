package org.example.domain.service.specialMoves.interfaces;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.MoveDto;

public interface PawnPromotionValidator {
    boolean isPawnPromotionPossible(DomainBoard domainBoard, MoveDto moveToBeMade, PieceColor turn );

    }
