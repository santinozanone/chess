package org.example.domain.strategy;

import org.example.domain.board.Piece;

public class PieceNotNullStrategy implements FilterStrategy{
    @Override
    public boolean filter(Piece piece) {
        return piece != null;
    }
}
