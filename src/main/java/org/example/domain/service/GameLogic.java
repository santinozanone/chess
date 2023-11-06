package org.example.domain.service;

import org.example.domain.board.*;
import org.example.domain.board.movements.Move;
import org.example.domain.board.movements.StandardMove;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.logger.LoggerMove;
import org.example.domain.board.movements.EnPassantMove;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.piece.Rey;
import org.example.domain.strategy.FilterStrategy;
import org.example.domain.strategy.implementation.NotFilterStrategy;
import org.example.domain.strategy.implementation.PieceNotNullStrategy;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;
import org.example.util.MatrixCopyUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private boolean isMovementPossible(Piece[][] boardMatrix, MoveDto move, PieceColor turno) {
        int originX = move.getOriginX();
        int originY = move.getOriginY();
        int destinationX = move.getDestinationX();
        int destinationY = move.getDestinationY();

        if (boardMatrix[originX][originY] == null || boardMatrix[originX][originY] == null && boardMatrix[destinationX][destinationY] == null) {
            return false;
        }
        Piece originPiece = boardMatrix[originX][originY];
        Piece destinationPiece = boardMatrix[destinationX][destinationY];

        boolean movementPossible = false;

        if (turno != originPiece.getColor()) {
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


    private int[] getPiecePosition(Piece board[][], Class<? extends Piece> piece, PieceColor turno) {
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

    private List<PositionDto> getCheckersPieces(Piece board[][], PieceColor turno) {
        List<PositionDto> checkerPieces = new ArrayList<>();
        int positions[] = getPiecePosition(board, Rey.class, turno);
        int kingX = positions[0];
        int kingY = positions[1];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                MoveDto move = new MoveDto(i, j, kingX, kingY);
                if (board[i][j] != null && board[i][j].getColor() != turno && board[i][j].isEatingMovementPossible(i, j, kingX, kingY) && !arePiecesInBetween(board, move)) {
                    checkerPieces.add(new PositionDto(i, j));
                }
            }
        }
        return checkerPieces;
    }


    private boolean isKingCheck(Piece board[][], PieceColor turno) {
        if (getCheckersPieces(board, turno).size() > 0) return true;
        return false;
    }



    private List<PositionDto> getPiecesInBetween(Piece board[][], FilterStrategy filterStrategy, int originX, int originY, int destinationX, int destinationY) {
        List<PositionDto> piecesInBetween = new ArrayList<>();
        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            int rowspaces = Math.abs(originX - destinationX);
            int columnSpaces = Math.abs(originY - destinationY);

            if (rowspaces == columnSpaces) { // diagonal
                int mayorY = Math.max(originY, destinationY);
                int mayorX = Math.max(originX, destinationX);
                int menorX = Math.min(originX, destinationX);
                int menorY = Math.min(originY, destinationY);
                int iterationNumber = 0;
                int x = 0;
                int y = 0;
                for (int i = menorX + 1; i < mayorX; i++) {
                    if (menorX == originX) x = (mayorX - 1) - iterationNumber;
                    else x = i;
                    if (menorY == originY) y = (mayorY - 1) - iterationNumber;
                    else y = (menorY + 1) + iterationNumber;
                    PositionDto positionDto = new PositionDto(x, y);
                    if (filterStrategy.filter(board[positionDto.getX()][positionDto.getY()])) {
                        piecesInBetween.add(positionDto);
                    }
                    iterationNumber++;
                }
            }

            if (Math.abs(originX - destinationX) == 0) {  // HORIZONTAL
                piecesInBetween = getPositionsInBetween(originY, destinationY, originX, board, filterStrategy, false);
            }
            if (Math.abs(originY - destinationY) == 0) {  // VERTICAL
                piecesInBetween = getPositionsInBetween(originX, destinationX, originY, board, filterStrategy, true);
            }
        }
        return piecesInBetween;
    }

    private List<PositionDto> getPositionsInBetween(int firstOrigin, int firstDestination, int secondOrigin, Piece[][] board, FilterStrategy filterStrategy, boolean verticalIteration) {
        int mayor = Math.max(firstOrigin, firstDestination);
        int menor = Math.min(firstOrigin, firstDestination);
        List<PositionDto> piecesInBetween = new ArrayList<>();
        PositionDto positionDto = null;
        for (int i = menor + 1; i < mayor; i++) {
            if (verticalIteration) positionDto = new PositionDto(i, secondOrigin);
            else positionDto = new PositionDto(secondOrigin, i);
            if (filterStrategy.filter(board[positionDto.getX()][positionDto.getY()])) {
                piecesInBetween.add(positionDto);
            }
        }
        return piecesInBetween;
    }


    private boolean arePiecesInBetween(Piece board[][], MoveDto move) {
        int originX = move.getOriginX(), originY = move.getOriginY(), destinationX = move.getDestinationX(), destinationY = move.getDestinationY();
        boolean arePiecesInBetween = false;
        if (!(Math.abs(originX - destinationX) == 2 && Math.abs(originY - destinationY) == 1) || ((Math.abs((originX - destinationX)) == 1) && (Math.abs((originY - destinationY)) == 2)))/*VERIFICA QUE NO SEA MOVIMIENTO DE CABALLO*/ {
            arePiecesInBetween = (getPiecesInBetween(board, new PieceNotNullStrategy(), move.getOriginX(), move.getOriginY(), move.getDestinationX(), move.getDestinationY()).size()) != 0;
        }
        return arePiecesInBetween;
    }

    public List<PositionDto> getPossibleMoves(DomainBoard domainBoard, List<Move> moves ,PositionDto positionDto, PieceColor turno) {
        List<PositionDto> possibleMoves = new ArrayList<>();
        Move move;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                move = isSingleMovePossible(domainBoard, moves,new MoveDto(positionDto.getX(), positionDto.getY(), i, j), turno);
                if (move != null ) {
                    possibleMoves.add(new PositionDto(i, j));
                }
            }
        }
       return  possibleMoves;
    }


    public boolean isCheckMate(Piece board[][],  List<Move> moves,PieceColor turno) {
        String turnoColor = turno.name();
        PieceColor originalTurno = PieceColor.valueOf(turnoColor);

        if (turno == PieceColor.BLACK) turno = PieceColor.WHITE;
        else turno = PieceColor.BLACK;

        if (isKingCheck(board, turno)) {
            int piecesThatSaveCheck = 0;
            int positions[] = getPiecePosition(board, Rey.class, turno);
            int kingX = positions[0];
            int kingY = positions[1];
            DomainBoard board1 = new DomainBoard();
            board1.setBoard(board);
            List<PositionDto> kingPositions = getPossibleMoves(board1, moves,new PositionDto(kingX, kingY), turno);
            List<PositionDto> pieceCheckingKing = getCheckersPieces(board, turno);
            List<PositionDto> intermediatePositions = getPiecesInBetween(board, new NotFilterStrategy(), kingX, kingY, pieceCheckingKing.get(0).getX(), pieceCheckingKing.get(0).getY());

            outerloop:
            for (int i = 0; i < 8; i++) { // verifica que las demas piezas puedan ponerse en el medio
                for (int j = 0; j < 8; j++) {
                    for (PositionDto checker: pieceCheckingKing){
                        MoveDto move = new MoveDto(i, j, checker.getX(), checker.getY()); // we loop to find if there is a position on the board that can "eat" the checkers piece
                        Move move1 = isSingleMovePossible(board1, moves,move, turno);
                        if (move1 != null) { // if it is not null, then the move is posible
                            piecesThatSaveCheck++;
                            break outerloop;
                        }
                    }
                    for (PositionDto intermediatePosition : intermediatePositions) { // we loop to find if there is a position on the board that can get in the middle of the checkers piece
                        MoveDto move = new MoveDto(i, j, intermediatePosition.getX(), intermediatePosition.getY());
                        if ((!(board[i][j] instanceof Rey)) && isSingleMovePossible(board1, moves, move, turno) != null) {
                            piecesThatSaveCheck++;
                            break outerloop;
                        }
                    }
                }
            }
            System.out.println("piecesThatSaveCheck " + piecesThatSaveCheck);

            if (kingPositions.size() == 0 && piecesThatSaveCheck < pieceCheckingKing.size()) {
                JOptionPane.showMessageDialog(null, "jaque mate, el  ganador es " + originalTurno);
                return true;
            }
        }
        turno = originalTurno;
        return false;
    }



    /*TESTING METHOD IS MOVE POSSIBLE*/
    public Move isSingleMovePossible(DomainBoard domainBoard, List<Move> moves ,MoveDto move, PieceColor turno) {
        Move moveToBeMade = null;
        List<EnPassantMove> enPassantMoves = SpecialMoves.getEnPassantMoveIfPossible(moves, domainBoard.getBoard(), new PositionDto(move.getOriginX(), move.getOriginY()),turno);
        if (enPassantMoves.isEmpty()){ // if enpassant moves and the other list that will contain the rest of the special moves available are empty then its a normal movement
            // then we have a normal movement
            if ( ! isMovementPossible(domainBoard.getBoard(), move, turno)){
                //return an error
                return  null;
            }
            if ( arePiecesInBetween(domainBoard.getBoard(),move) ){
                // return an error
                return  null;
            }
            moveToBeMade = new StandardMove(move, domainBoard.getBoard()[move.getDestinationX()][move.getDestinationY()]);
        }
        else {
            // we need to loop through the enpassant list and find the move that has the same coordinates as our and then that movement will be the one
            for (EnPassantMove enPassantMove: enPassantMoves){
                if (enPassantMove.getMoveDto().equals(move)){
                    moveToBeMade = enPassantMove; // if we find the movement, that means we can make an enPassant
                }
            }
            // upgrade the specialMove class so it has a method called isEnpassantPossible
        }
        domainBoard.makeMove2(moveToBeMade);
        domainBoard.logMove(moveToBeMade);  // we need to log the move so its added to the list of moves, that way we can undo it later
        if (isKingCheck(domainBoard.getBoard(),turno)){
            domainBoard.undoMove();
            return  null;
        }
        domainBoard.undoMove();
        return moveToBeMade;
    }




}



