package org.example.domain;

import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private PieceColor turno = PieceColor.WHITE;

    public boolean isMovementPossible(Piece[][] board, MoveDto move) {
        int originX = move.getOriginX();
        int originY = move.getOriginY();
        int destinationX = move.getDestinationX();
        int destinationY = move.getDestinationY();

        if (board[originX][originY] == null || board[originX][originY] == null  && board[destinationX][destinationY] == null ){
            return false;
        }
        Piece originPiece = board[originX][originY];
        Piece destinationPiece = board[destinationX][destinationY];

        boolean movementPossible = false;

        if (turno != originPiece.getColor()){
            return false;
        }


        if (destinationPiece != null && originPiece.getColor().equals(destinationPiece.getColor())) {
            return false;
        }

        if (destinationPiece != null) {  // SE DESEA COMER
            movementPossible = originPiece.isEatingMovementPossible(originX, originY, destinationX, destinationY);
        }

        if (destinationPiece == null) {
            movementPossible = originPiece.isMovementPossible(originX, originY, destinationX, destinationY);
        }

        return movementPossible;

    }


    public void changeTurn(){
        if (turno.equals(PieceColor.WHITE)) turno = PieceColor.BLACK; else turno = PieceColor.WHITE;
    }

    private  Piece[][] copyBoard(Piece [][] board){   // HACER STATIC UTILITY CLASS
        Piece [][] copy = new Piece[board.length][board[0].length];
        for (int i=0;i<board.length;i++){
            System.arraycopy( board[i],0,copy[i],0,board.length);
        }
        return copy;
    }

    public boolean isCheckMate(Piece board[][]){
       // return isKingCheck(board)

         // UN MOVIMIENTO MIO GENERA JAQUE MATE EN EL OTRO JUGADOR.

       String turnoColor = turno.name();
       PieceColor originalTurno = PieceColor.valueOf(turnoColor);

            if (turno == PieceColor.BLACK) turno = PieceColor.WHITE ; else turno = PieceColor.BLACK;



        if (isKingCheck(board)) {

            Piece[][] boardClone = copyBoard(board);
            int checkedPositions = 0;
            int kingX = 0;
            int kingY = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] instanceof Rey && board[i][j].getColor().equals(turno)) {
                        kingX = i;
                        kingY = j;
                    }
                }
            }
            List<PositionDto> piecesCheckingKing = new ArrayList<>();
            List<PositionDto> kingPositions = getPieceMoves(board, kingX, kingY); // el getmoves considera todos los lugares, incluso los con jaque
            System.out.println("king positions " + kingPositions.size());
            if (kingPositions.size() > 0) {

                int kingMoves = kingPositions.size();
                for (PositionDto position : kingPositions) {
                    board[position.getX()][position.getY()] = board[kingX][kingY];
                    board[kingX][kingY] = null;
                    if (isKingCheck(board)) {
                        checkedPositions++;
                        List<PositionDto> pieces = getCheckersPieces(board);
                        for (PositionDto p : pieces) {
                            piecesCheckingKing.add(p);
                        }
                    }
                    board = copyBoard(boardClone);
                }

                System.out.println("checked positions " + checkedPositions);


                int piecesThatSaveCheck = 0;


                System.out.println("piecesCheckingKing " + piecesCheckingKing.size());

                List<PositionDto> intermediatePositions = null;
                for (PositionDto position : piecesCheckingKing) {
                    intermediatePositions = getPiecesInBetween(board, position.getX(), position.getY(), kingX, kingY);
                    boolean canSaveCheck = false;
                    for (int i = 0; i < 8; i++) { // verifica que las demas piezas puedan ponerse en el medio
                        for (int j = 0; j < 8; j++) {
                            for (PositionDto intermediatePosition : intermediatePositions) {
                                if (canSaveCheck) continue;
                                MoveDto move = new MoveDto(i,j,intermediatePosition.getX(), intermediatePosition.getY());
                                if (isMovementPossible(board, move) && !arePiecesInBetween(board,move)) {
                                    piecesThatSaveCheck++;
                                    canSaveCheck = true;
                                }
                            }
                        }
                    }
                }
                System.out.println("pieces that save " + piecesThatSaveCheck + " checkers piece " + piecesCheckingKing.size());

                if (checkedPositions == kingMoves && piecesThatSaveCheck < piecesCheckingKing.size()) {
                    System.out.println("jaque mate");
                    JOptionPane.showMessageDialog(null, "jaque mate, el  ganador es " + originalTurno);
                    return true;
                }
            }
        }




        // obtener posicion de piezas que pueden hacer jaque, obtener sus posiciones intermedias y preguntar si algunas de mis piezas puede ponerse en el medio






        /* mover el rey por las distintas posiciones y preguntar si esta checked, si lo esta en todas y ninguna pieza mia puede ponerse el el medio el jaque es jaque mate */
        // ver metodo para encontrar las piezas del medio


        turno = originalTurno;

        return false;
    }


    private List<PositionDto> getCheckersPieces(Piece board[][]){
        List<PositionDto> checkerPieces = new ArrayList<>();
        int kingX = 0;
        int kingY = 0;
        for (int i = 0;i<8;i++){
            for (int j=0;j<8;j++){
                if (board[i][j] instanceof Rey && board[i][j].getColor().equals(turno)){
                    kingX = i;
                    kingY = j;
                }
            }
        }

        for (int i = 0;i<8;i++){
            for (int j=0;j<8;j++){
                MoveDto move = new MoveDto(i,j,kingX,kingY);
                if (board[i][j] != null && board[i][j].getColor() != turno && board[i][j].isEatingMovementPossible(i,j,kingX,kingY) && !arePiecesInBetween(board,move)){
                    checkerPieces.add(new PositionDto(i,j));
                }
            }
        }
        return checkerPieces;
    }



    public boolean isKingCheck(Piece board[][]){
        if (getCheckersPieces(board).size() > 0) return true;
        return false;
    }


    public List<PositionDto> getPieceMoves(Piece board[][], int originX, int originY){
        List<PositionDto> positions = new ArrayList<>();
        if (board[originX][originY] == null){
            return positions;
        }

        for (int i = 0;i<8;i++){
            for (int j=0;j<8;j++) {
                MoveDto move = new MoveDto(originX,originY,i,j);
                if(isMovementPossible(board,move) && !arePiecesInBetween(board,move)){
                    positions.add(new PositionDto(i,j));
                }
            }
        }
        return positions;
    }



    private List<PositionDto> getPiecesInBetween(Piece board[][],int originX, int originY, int destinationX, int destinationY){
        List<PositionDto> piecesInBetween = new ArrayList<>();
        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            int rowspaces = Math.abs(originX - destinationX);
            int columnSpaces = Math.abs(originY - destinationY);


            if (rowspaces == columnSpaces ) {  // DIAGONAL

                if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}


                for (int i=0;i<rowspaces-1;i++){
                    //    System.out.println(destinationX + " orig");
                    //   System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        piecesInBetween.add(new PositionDto(destinationX,destinationY));
                    }else{
                        //  System.out.println(destinationX + " " +destinationY  + " is null");
                    }
                    if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                    if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                    if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                    if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}
                }
                return piecesInBetween;
            }





            if (Math.abs(originX-destinationX) == 0 /*&& (originY - destinationY) == columnSpaces*/ ) {  // HORIZONTAL

                if (originY<destinationY) destinationY--;
                if (originY>destinationY) destinationY++;

                for (int i=0;i<columnSpaces-1;i++){
                    //     System.out.println(destinationX + " orig");
                    //     System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        piecesInBetween.add(new PositionDto(destinationX,destinationY));
                    }
                    if (originY<destinationY) destinationY--;
                    if (originY>destinationY) destinationY++;
                }
                return piecesInBetween;

            }

            if (/*(originX-destinationX) == rowspaces &&*/ Math.abs(originY - destinationY) == 0 ) {  // VERTICAL


                if (originX<destinationX) destinationX--;
                if (originX>destinationX) destinationX++;

                for (int i=0;i<rowspaces-1;i++){

                    // System.out.println(destinationX +  " " +destinationY);

                    if (board[destinationX][destinationY] != null){
                        piecesInBetween.add(new PositionDto(destinationX,destinationY));
                    }
                    if (originX<destinationX) destinationX--;
                    if (originX>destinationX) destinationX++;
                }
                return piecesInBetween;

            }



            return piecesInBetween;
        }


        return piecesInBetween;


    }



    public boolean arePiecesInBetween(Piece board[][],MoveDto move) {
        int originX = move.getOriginX();
        int originY = move.getOriginY();
        int destinationX = move.getDestinationX();
        int destinationY = move.getDestinationY();

        // para no contar al caballo, tenemos en cuenta los movientos entre diagonal, en la misma fila, en la misma columna.

        // diagonal es cuando originx - destinationx es igual  a originY - destinationY

        /*Solo Necesitamos obtener las piezas intermedias de los moviemientos diagonales,en la misma fila o en la misma columna */





        /*
         *
         *  SOLUCIONAR IF HACIENDO QUE LE RESTE 1 AL ORIGEN Y DESITNO O LE SUME DEPENDIENDO
         * SUPONGAMOS QUE VAMOS DESDE CASILLA  0-0 A 0-4, EL FOR TIENE QUE IR DE 0-1 A 0-3
         *
         * */




        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            int rowspaces = Math.abs(originX - destinationX);
            int columnSpaces = Math.abs(originY - destinationY);


            if (rowspaces == columnSpaces ) {  // DIAGONAL
            //    System.out.println("diagonal");

                if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}


                for (int i=0;i<rowspaces-1;i++){
                //    System.out.println(destinationX + " orig");
                 //   System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        return true;
                    }else{
                      //  System.out.println(destinationX + " " +destinationY  + " is null");
                    }
                    if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                    if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                    if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                    if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}
                }
                return false;
            }





            if (Math.abs(originX-destinationX) == 0 /*&& (originY - destinationY) == columnSpaces*/ ) {  // HORIZONTAL
         //       System.out.println("horizontal");
                if (originY<destinationY) destinationY--;
                if (originY>destinationY) destinationY++;

                for (int i=0;i<columnSpaces-1;i++){
               //     System.out.println(destinationX + " orig");
               //     System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        return true;
                    }
                    if (originY<destinationY) destinationY--;
                    if (originY>destinationY) destinationY++;
                }
                return false;

            }

            if (/*(originX-destinationX) == rowspaces &&*/ Math.abs(originY - destinationY) == 0 ) {  // VERTICAL
           //     System.out.println("vertical");


                if (originX<destinationX) destinationX--;
                if (originX>destinationX) destinationX++;

                for (int i=0;i<rowspaces-1;i++){

                   // System.out.println(destinationX +  " " +destinationY);

                    if (board[destinationX][destinationY] != null){
                        return true;
                    }
                    if (originX<destinationX) destinationX--;
                    if (originX>destinationX) destinationX++;
                }
                return false;

            }



            return false;
        }


        return false;


    }

}
