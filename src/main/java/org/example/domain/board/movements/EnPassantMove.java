package org.example.domain.board.movements;

import org.example.domain.board.piece.Piece;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

public class EnPassantMove extends StandardMove {
    private PositionDto positionDto;

    public EnPassantMove(MoveDto moveDto, Piece pieceEaten, PositionDto positionOfPieceEaten) {
        super(moveDto, pieceEaten);
        this.positionDto = positionOfPieceEaten;
    }

    @Override
    public void makeMove(Piece[][] board) {
        super.makeMove(board);
        board[positionDto.getX()][positionDto.getY()] = null;
    }

    @Override
    public void undoMove(Piece[][] board) {
        int originX = getMoveDto().getOriginX();
        int originY =  getMoveDto().getOriginY();
        int destinationX =  getMoveDto().getDestinationX();
        int destinationY =  getMoveDto().getDestinationY();
        board[originX][originY] = board[destinationX][destinationY];
        board[destinationX][destinationY] = null;
        board[positionDto.getX()][positionDto.getY()] = super.getPieceEaten();
    }


}
