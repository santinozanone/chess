package org.example.domain.strategy.implementation;

import org.example.domain.board.piece.Piece;
import org.example.domain.strategy.FilterStrategy;

public class PieceNotNullStrategy implements FilterStrategy {
    @Override
    public boolean filter(Piece piece) {
        return piece != null;
    }
}
