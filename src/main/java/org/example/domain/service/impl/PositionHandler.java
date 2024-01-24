package org.example.domain.service.impl;

import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.strategy.FilterStrategy;
import org.example.domain.strategy.implementation.PieceNotNullStrategy;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class PositionHandler {
    /*  - Get the position of a piece of the board
        - detecting if a square is being attacked
        - getting positions between to pieces
        - get the positions of the pieces checking the king
         -detecting if there are pieces in between  */


    public List<PositionDto> getPositionsInBetween(Piece board[][], FilterStrategy filterStrategy, int originX, int originY, int destinationX, int destinationY) {
        List<PositionDto> piecesInBetween = new ArrayList<>();
        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            int rowSpaces = Math.abs(originX - destinationX);
            int columnSpaces = Math.abs(originY - destinationY);

            if (rowSpaces == columnSpaces) { // diagonal
                piecesInBetween = getDiagonalPositionsInBetween(board, originX, destinationX, originY, destinationY, filterStrategy);
            }
            if (rowSpaces == 0) {  // HORIZONTAL
                piecesInBetween = getHorizontalPositionsInBetween(board, originY, destinationY, originX, filterStrategy);
            }
            if (columnSpaces == 0) {  // VERTICAL
                piecesInBetween = getVerticalPositionsInBetween(board, originX, destinationX, originY, filterStrategy);
            }
        }
        return piecesInBetween;
    }


    public boolean arePiecesInBetween(Piece board[][], MoveDto move) {
        int originX = move.getOriginX(), originY = move.getOriginY(), destinationX = move.getDestinationX(), destinationY = move.getDestinationY();
        boolean arePiecesInBetween = false;
        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            arePiecesInBetween = (getPositionsInBetween(board, new PieceNotNullStrategy(), move.getOriginX(), move.getOriginY(), move.getDestinationX(), move.getDestinationY()).size()) != 0;
        }
        return arePiecesInBetween;
    }

    public int[] getPiecePosition(Piece board[][], Class<? extends Piece> piece, PieceColor turno) {
        int positions[] = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].getClass().equals(piece) && board[i][j].getColor().equals(turno)) {
                    positions[0] = i;
                    positions[1] = j;
                }
            }
        }
        return positions;
    }


    public boolean isSquareBeingAttacked(Piece board[][],PositionDto square ,PieceColor turno) {
        int x = square.getX();
        int y = square.getY();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                MoveDto move = new MoveDto(i, j, x, y);
                if (board[i][j] != null && board[i][j].getColor() != turno && board[i][j].isEatingMovementPossible(i, j, x, y) && !arePiecesInBetween(board, move)) {
                    return true;
                }
            }
        }
        return false;
    }

    public  boolean getIfPieceHasMoved(PositionDto positionOfPieceToVerify, List<Move> moves) {
        for (Move moveMade : moves) {
            if (moveMade.getMoveDto().getOriginX() == positionOfPieceToVerify.getX() && moveMade.getMoveDto().getOriginY() == positionOfPieceToVerify.getY()) {
                return true; // the piece has moved
            }
        }
        return false;
    }


    private List<PositionDto> getVerticalPositionsInBetween(Piece[][] board, int originX, int destinationX,int originY ,FilterStrategy filterStrategy){
        List<PositionDto> piecesInBetween = new ArrayList<>();
        for (Integer i : getNumbersInBetween(originX, destinationX)) {
            if (filterStrategy.filter(board[i][originY])) piecesInBetween.add(new PositionDto(originX, i));
        }
        return piecesInBetween;
    }

    private List<PositionDto> getHorizontalPositionsInBetween(Piece[][] board, int originY, int destinationY,int originX ,FilterStrategy filterStrategy){
        List<PositionDto> piecesInBetween = new ArrayList<>();
        for (Integer i : getNumbersInBetween(originY, destinationY)) {
            if (filterStrategy.filter(board[originX][i])) piecesInBetween.add(new PositionDto(originX, i));
        }
        return piecesInBetween;
    }



    private List<PositionDto> getDiagonalPositionsInBetween(Piece[][] board, int originX, int destinationX, int originY, int destinationY, FilterStrategy filterStrategy ){
        List<PositionDto> piecesInBetween = new ArrayList<>();
        List<Integer> xPositions = getNumbersInBetween(originX,destinationX);
        List<Integer> yPositions =getNumbersInBetween(originY, destinationY);
        for (int i = 0;i<xPositions.size();i++){
            if (filterStrategy.filter(board[xPositions.get(i)][yPositions.get(i)])) {
                piecesInBetween.add(new PositionDto(xPositions.get(i),yPositions.get(i)));
            }
        }
        return piecesInBetween;
    }



    private List<Integer> getNumbersInBetween(int firstNumber,int secondNumber){
        List<Integer> numbersInBetween = new ArrayList<>();
        while (true) {
            if (firstNumber > secondNumber) {
                firstNumber--;
            }
            if (firstNumber < secondNumber) {
                firstNumber++;
            }
            if (firstNumber != secondNumber) numbersInBetween.add(firstNumber);
            else return numbersInBetween;
        }
    }


}
