package org.example.domain.board;

import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.*;
import org.example.presentation.Button;
import org.example.presentation.implementation.Board;
import org.example.presentation.interfaces.IWindowBoard;

import java.util.ArrayList;
import java.util.List;

public class DomainBoard   {
    private Piece board[][] = new Piece[8][8];
    private Piece whites[] = new Piece[8];
    private Piece black[] = new Piece[8];

    private List<Move> moveList = new ArrayList<>();

    private List<IWindowBoard> boards = new ArrayList<>();

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
                    board[i][j] = new Peon(PieceColor.BLACK);
                } else if (i == 6) {
                    board[i][j] = new Peon(PieceColor.WHITE);
                }

            }
        }
        System.arraycopy(whites, 0, board[7], 0, 8);
        System.arraycopy(blacks, 0, board[0], 0, 8);
    }

    public void addListener(IWindowBoard board){
        this.boards.add(board);
    }

    public void notifyChange(){
        for (IWindowBoard b:boards){
            b.updateBoard(this.board);
        }
    }

    public void makeMove(Move move){

        move.makeMove(board);
    }

    public void logMove(Move move){
        moveList.add(move);
    }
    public void undoMove(){
        if (moveList.size()>0){
            moveList.get(moveList.size()-1).undoMove(board);
            moveList.remove(moveList.size()-1);
        }
    }



    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public List<Move> getMoveList() {
        return moveList;
    }
}
