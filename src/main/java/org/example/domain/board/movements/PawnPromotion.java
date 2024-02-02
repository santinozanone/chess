package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;

public class PawnPromotion extends StandardMove{

    private Piece originalPiece;
    private Piece promotedPiece;

    public PawnPromotion(MoveDto moveDto, Piece originalPiece,Piece pieceEaten ,Piece promotedPiece) {
        super(moveDto,pieceEaten);
        this.originalPiece = originalPiece;
        this.promotedPiece = promotedPiece;
    }

    @Override
    public void makeMove(Piece[][] board) {
        board[getMoveDto().getDestinationX()][getMoveDto().getDestinationY()] = promotedPiece;
        board[getMoveDto().getOriginX()][getMoveDto().getOriginY()] = null;
    }
    @Override
    public void undoMove(Piece[][] board) {
        board[getMoveDto().getOriginX()][getMoveDto().getOriginY()] = originalPiece;
        board[getMoveDto().getDestinationX()][getMoveDto().getDestinationY()] = getPieceEaten();
    }


    public void setPromotedPiece(Piece promotedPiece) {
        this.promotedPiece = promotedPiece;
    }

}
