package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;

public interface Move {
    void makeMove(Piece board[][]);

    void undoMove(Piece board[][]);

    MoveDto getMoveDto();
}
