package org.example.domain.strategy;

import org.example.domain.board.Piece;

public interface FilterStrategy {
    boolean filter(Piece piece);
}
