package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

public interface  Move {
      void makeMove(Piece board[][]);

     void undoMove(Piece board[][]);

     boolean hasPositionMoved(PositionDto positionOfPieceToVerify);

     MoveDto getMoveDto();
}
