package org.example.domain;

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

        for (int i = 7; i <= 7; i++) {
            System.arraycopy(whites, 0, board[i], 0, 8);
        }

        for (int i = 0; i <= 0; i++) {
            System.arraycopy(blacks, 0, board[i], 0, 8);
        }
    }


    /* DE ACA PARA ABAJO REFACTORIZAR EN CLASE GAME*/

    public boolean isMovementPossible(int originX, int originY, int destinationX, int destinationY) {
        Piece originPiece = board[originX][originY];
        Piece destinationPiece = board[destinationX][destinationY];
        boolean movementPossible = false;

        if (turno != originPiece.getColor()){
            return false;
        }

        if (originPiece == null) {
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


        if (movementPossible) {
            System.out.println(" es posible");
            if ( ! arePiecesInBetween(originX, originY, destinationX, destinationY)) {
                board[destinationX][destinationY] = board[originX][originY];
                board[originX][originY] = null;
                if (turno.equals(PieceColor.WHITE)) turno = PieceColor.BLACK; else turno = PieceColor.WHITE;
            }else{
                movementPossible = false;
            }
        }

        //arePiecesInBetween(originX, originY, destinationX, destinationY);
        return movementPossible;



        /*  MODIFICAR FUNCION, SOLO TIENE QUE RETORNAR BOOLEANOS, NO ACTUALIZAR TABLERO   */
    }


    public boolean arePiecesInBetween(int originX, int originY, int destinationX, int destinationY) {

        // para no contar al caballo, tenemos en cuenta los movientos entre diagonal, en la misma fila, en la misma columna.

        // diagonal es cuando originx - destinationx es igual  a originY - destinationY

        /*Solo Necesitamos obtener las piezas intermedias de los moviemientos diagonales,en la misma fila o en la misma columna */





        /*
        *
        *  SOLUCIONAR IF HACIENDO QUE LE RESTE 1 AL ORIGEN Y DESITNO O LE SUME DEPENDIENDO
        * SUPONGAMOS QUE VAMOS DESDE CASILLA  0-0 A 0-4, EL FOR TIENE QUE IR DE 0-1 A 0-3
        *
        * */






        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2))) {
            int rowspaces = Math.abs(originX - destinationX);
            int columnSpaces = Math.abs(originY - destinationY);


            if (rowspaces == columnSpaces ) {  // DIAGONAL
                System.out.println("diagonal");

                if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}


                for (int i=0;i<rowspaces-1;i++){
                    System.out.println(destinationX + " orig");
                    System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        return true;
                    }
                    if (originX < destinationX && originY < destinationY) {destinationX--; destinationY--;}
                    if (originX > destinationX && originY > destinationY) {destinationX++; destinationY++;}

                    if (originX < destinationX && originY > destinationY) {destinationX--; destinationY++;}
                    if (originX > destinationX && originY < destinationY) {destinationX++; destinationY--;}
                }
                return false;
            }





            if (Math.abs(originX-destinationX) == 0 /*&& (originY - destinationY) == columnSpaces*/ ) {  // HORIZONTAL
                System.out.println("horizontal");
                if (originY<destinationY) destinationY--;
                if (originY>destinationY) destinationY++;

                for (int i=0;i<columnSpaces-1;i++){
                    System.out.println(destinationX + " orig");
                    System.out.println(destinationX +  " " +destinationY);
                    if (board[destinationX][destinationY] != null){
                        return true;
                    }
                    if (originY<destinationY) destinationY--;
                    if (originY>destinationY) destinationY++;
                }
                return false;

            }

            if (/*(originX-destinationX) == rowspaces &&*/ Math.abs(originY - destinationY) == 0 ) {  // VERTICAL
                System.out.println("vertical");


                if (originX<destinationX) destinationX--;
                if (originX>destinationX) destinationX++;

                for (int i=0;i<rowspaces-1;i++){

                    System.out.println(destinationX +  " " +destinationY);

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
