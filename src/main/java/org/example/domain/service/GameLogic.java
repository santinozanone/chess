package org.example.domain.service;

import org.example.domain.board.*;
import org.example.domain.board.Piece;
import org.example.domain.strategy.FilterStrategy;
import org.example.domain.strategy.NotFilterStrategy;
import org.example.domain.strategy.PieceNotNullStrategy;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;
import org.example.util.MatrixCopyUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic {

    private MovementStatus isMovePossible(DomainBoard domainBoard, MoveDto moveDto, PieceColor turno) {
        Piece[][] boardMatrix = domainBoard.getBoard();
        MovementStatus movementStatus = new MovementStatus();
        boolean movementPossible = isMovementPossible(boardMatrix, moveDto, turno);
        if (!movementPossible) {
            movementStatus.setMovementPossible(false);
            return movementStatus;
        }
        boolean piecesInBetween = arePiecesInBetween(boardMatrix, moveDto);
        if (!piecesInBetween) {
            movementStatus.setMovementPossible(true);
            boardMatrix[moveDto.getDestinationX()][moveDto.getDestinationY()] = boardMatrix[moveDto.getOriginX()][moveDto.getOriginY()];
            boardMatrix[moveDto.getOriginX()][moveDto.getOriginY()] = null;
            if (!isKingCheck(boardMatrix, turno)) {
                movementStatus.setKingChecked(false);
            } else {
                movementStatus.setKingChecked(true);
            }
        }
        return movementStatus;
    }


    public MovementStatus getMoveStatus(DomainBoard domainBoard, MoveDto moveDto, PieceColor turno){
        Piece[][] boardMatrix = domainBoard.getDeppCopyBoard();
        MovementStatus movementStatus = isMovePossible(domainBoard, moveDto, turno);
        movementStatus.setCheckMate(isCheckMate2(domainBoard.getBoard(), turno));
        if (!movementStatus.isCheckMate()) domainBoard.setBoard(boardMatrix); // Si el movimiento no es jaque mate  volvemos a el estado original del tablero
        return movementStatus;
    }

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


            if (rowspaces == columnSpaces) { // vertical
                int mayorX = 0, mayorY = 0, menorX = 0, menorY = 0;

                mayorY = Math.max(originY, destinationY);
                mayorX = Math.max(originX, destinationX);
                menorX = Math.min(originX, destinationX);
                menorY = Math.min(originY, destinationY);


                List<Integer> listX = new ArrayList<>();
                List<Integer> listY = new ArrayList<>();

                for (int i = menorX + 1; i < mayorX; i++) {
                    listX.add(i);
                }
                if (originX != menorX) {
                    Collections.reverse(listX);
                }

                for (int j = menorY + 1; j < mayorY; j++) {
                    listY.add(j);
                }

                if (originY != menorY) {
                    Collections.reverse(listY);
                }


                if (listX.isEmpty() || listY.isEmpty()) {
                    return piecesInBetween;
                }

                for (int i = 0; i < listX.size(); i++) {
                    if (filterStrategy.filter(board[listX.get(i)][listY.get(i)])) {
                        piecesInBetween.add(new PositionDto(listX.get(i), listY.get(i)));
                    }
                }
                return piecesInBetween;
            }

            if (Math.abs(originX - destinationX) == 0) {  // HORIZONTAL

                if (originY < destinationY) destinationY--;
                if (originY > destinationY) destinationY++;

                for (int i = 0; i < columnSpaces - 1; i++) {
                    if (filterStrategy.filter(board[destinationX][destinationY])) {
                        piecesInBetween.add(new PositionDto(destinationX, destinationY));
                    }
                    if (originY < destinationY) destinationY--;
                    if (originY > destinationY) destinationY++;
                }
                return piecesInBetween;

            }

            if (Math.abs(originY - destinationY) == 0) {  // VERTICAL
                if (originX < destinationX) destinationX--;
                if (originX > destinationX) destinationX++;
                for (int i = 0; i < rowspaces -1; i++) {
                    if (filterStrategy.filter(board[destinationX][destinationY])) {
                        piecesInBetween.add(new PositionDto(destinationX, destinationY));
                    }
                    if (originX < destinationX) destinationX--;
                    if (originX > destinationX) destinationX++;
                }
                return piecesInBetween;
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



    public List<PositionDto> getPossibleMoves(DomainBoard domainBoard,PositionDto positionDto, PieceColor turno) {
        List<PositionDto> possibleMoves = new ArrayList<>();
        Piece[][] boardMatrix = domainBoard.getDeppCopyBoard();
        Piece[][] boardClone = domainBoard.getDeppCopyBoard();
        MovementStatus movementStatus;
        DomainBoard board = new DomainBoard();
        board.setBoard(boardMatrix);

        for (int i = 0;i<8;i++){
            for (int j = 0; j<8; j++){
                movementStatus = isMovePossible(board,new MoveDto(positionDto.getX(), positionDto.getY(),i,j),turno);
                if (movementStatus.isMovementPossible() && !movementStatus.isKingChecked() /*&& !status.isCheckMate() */){
                    possibleMoves.add(new PositionDto(i,j));
                }
                board.setBoard(MatrixCopyUtil.copyMatrix(boardClone));
            }
        }
        return possibleMoves;
    }


    private boolean isCheckMate2(Piece board[][], PieceColor turno) {
        String turnoColor = turno.name();
        PieceColor originalTurno = PieceColor.valueOf(turnoColor);

        if (turno == PieceColor.BLACK) turno = PieceColor.WHITE;
        else turno = PieceColor.BLACK;

        if (isKingCheck(board, turno)) {
            System.out.println("jaque");
            int piecesThatSaveCheck = 0;
            int positions[] = getPiecePosition(board, Rey.class, turno);
            int kingX = positions[0];
            int kingY = positions[1];
            DomainBoard board1 = new DomainBoard();
            board1.setBoard(board);
            List<PositionDto> kingPositions = getPossibleMoves(board1, new PositionDto(kingX, kingY), turno);
            List<PositionDto> pieceCheckingKing = getCheckersPieces(board, turno);
            List<PositionDto> intermediatePositions = getPiecesInBetween(board, new NotFilterStrategy(), kingX, kingY, pieceCheckingKing.get(0).getX(), pieceCheckingKing.get(0).getY());

            outerloop:
            for (int i = 0; i < 8; i++) { // verifica que las demas piezas puedan ponerse en el medio
                for (int j = 0; j < 8; j++) {
                    for (PositionDto intermediatePosition : intermediatePositions) {
                        MoveDto move = new MoveDto(i, j, intermediatePosition.getX(), intermediatePosition.getY());
                        if ((!(board[i][j] instanceof Rey)) && isMovementPossible(board, move, turno) && !arePiecesInBetween(board, move)) {
                            piecesThatSaveCheck++;
                            break outerloop;
                        }
                    }
                }
            }
            if (kingPositions.size() == 0 && piecesThatSaveCheck < pieceCheckingKing.size()) {
                JOptionPane.showMessageDialog(null, "jaque mate, el  ganador es " + originalTurno);
                return true;
            }
        }
        turno = originalTurno;
        return false;
    }
}
