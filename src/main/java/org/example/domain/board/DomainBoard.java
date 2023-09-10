package org.example.domain.board;

import org.example.dto.MoveDto;
import org.example.dto.PositionDto;
import org.example.util.MatrixCopyUtil;

public class DomainBoard {
    private Piece board[][] = new Piece[8][8];
    private Piece whites[] = new Piece[8];
    private Piece black[] = new Piece[8];

    PieceColor turno = PieceColor.WHITE;
    public DomainBoard() {
        loadPieces(whites, PieceColor.WHITE);
        loadPieces(black, PieceColor.BLACK);
        loadBoard(board, whites, black);
    }

    private void loadPieces(Piece pieces[], PieceColor color) {
        pieces[0] = new Torre(color);
        pieces[1] = new Caballo(color);
        pieces[2] = new Alfil(color);
        pieces[3] = new Reina(color);
        pieces[4] = new Rey(color);
        pieces[5] = new Alfil(color);
        pieces[6] = new Caballo(color);
        pieces[7] = new Torre(color);
    }

    private void loadBoard(Piece board[][], Piece whites[], Piece blacks[]) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 1) {
                    board[i][j] = new Peon(PieceColor.BLACK, board);
                } else if (i == 6) {
                    board[i][j] = new Peon(PieceColor.WHITE, board);
                }

            }
        }
        System.arraycopy(whites, 0, board[7], 0, 8);
        System.arraycopy(blacks, 0, board[0], 0, 8);
    }




   public void makeMove(MoveDto move){
       int originX = move.getOriginX();
       int originY = move.getOriginY();
       int destinationX = move.getDestinationX();
       int destinationY = move.getDestinationY();
        board[destinationX][destinationY] = board[originX][originY];
        board[originX][originY] = null;
   }

   public void makeEnPassantMove(MoveDto moveDto, PositionDto positionDto){
        makeMove(moveDto);
        board[positionDto.getX()][positionDto.getY()] = null;
   }

    public Piece[][] getDeppCopyBoard() {
        return MatrixCopyUtil.copyMatrix(board);
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

}
