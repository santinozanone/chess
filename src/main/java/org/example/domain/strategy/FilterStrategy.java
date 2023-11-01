package org.example.domain.strategy;

import org.example.domain.board.piece.Piece;

public interface FilterStrategy {
    boolean filter(Piece piece);
}
