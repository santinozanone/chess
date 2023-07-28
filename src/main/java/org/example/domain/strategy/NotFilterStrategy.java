package org.example.domain.strategy;

import org.example.domain.board.Piece;

public class NotFilterStrategy implements FilterStrategy{
    @Override
    public boolean filter(Piece piece) {
        return true;
    }
}
