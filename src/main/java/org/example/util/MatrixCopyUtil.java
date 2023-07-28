package org.example.util;

import org.example.domain.board.Piece;

public class MatrixCopyUtil {
    public static Piece[][] copyMatrix(Piece [][] board){
        Piece [][] copy = new Piece[board.length][board[0].length];
        for (int i=0;i<board.length;i++){
            System.arraycopy( board[i],0,copy[i],0,board.length);
        }
        return copy;
    }
}
