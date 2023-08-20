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


    public MovementStatus getMoveStatus(DomainBoard domainBoard, MoveDto moveDto, PieceColor turno) {
        Piece[][] boardMatrix = domainBoard.getDeppCopyBoard();
        MovementStatus movementStatus = isMovePossible(domainBoard, moveDto, turno);
        movementStatus.setCheckMate(isCheckMate2(domainBoard.getBoard(), turno));
        if (!movementStatus.isCheckMate())
            domainBoard.setBoard(boardMatrix); // Si el movimiento no es jaque mate  volvemos a el estado original del tablero
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

            if (rowspaces == columnSpaces) { // diagonal
                int mayorY = Math.max(originY, destinationY);
                int mayorX = Math.max(originX, destinationX);
                int menorX = Math.min(originX, destinationX);
                int menorY = Math.min(originY, destinationY);
                int iterationNumber = 0;
                for (int i = menorX + 1; i < mayorX; i++) {
                    int x = 0;
                    int y = 0;
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

    public List<PositionDto> getPossibleMoves(DomainBoard domainBoard, PositionDto positionDto, PieceColor turno) {
        List<PositionDto> possibleMoves = new ArrayList<>();
        Piece[][] boardMatrix = domainBoard.getDeppCopyBoard();
        Piece[][] boardClone = domainBoard.getDeppCopyBoard();
        MovementStatus movementStatus;
        DomainBoard board = new DomainBoard();
        board.setBoard(boardMatrix);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                movementStatus = isMovePossible(board, new MoveDto(positionDto.getX(), positionDto.getY(), i, j), turno);
                if (movementStatus.isMovementPossible() && !movementStatus.isKingChecked() /*&& !status.isCheckMate() */) {
                    possibleMoves.add(new PositionDto(i, j));
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
                    for (PositionDto checker: pieceCheckingKing){
                        MoveDto move = new MoveDto(i, j, checker.getX(), checker.getY());
                        if (isMovementPossible(board, move, turno) && !arePiecesInBetween(board, move)) {
                            System.out.println(i + " " + j);
                            piecesThatSaveCheck++;
                            break outerloop;
                        }
                    }

                    for (PositionDto intermediatePosition : intermediatePositions) {
                        MoveDto move = new MoveDto(i, j, intermediatePosition.getX(), intermediatePosition.getY());
                        if ((!(board[i][j] instanceof Rey)) && isMovementPossible(board, move, turno) && !arePiecesInBetween(board, move)) {
                            piecesThatSaveCheck++;
                            break outerloop;
                        }
                    }
                }
            }
          //  System.out.println(piecesThatSaveCheck);
            if (kingPositions.size() == 0 && piecesThatSaveCheck < pieceCheckingKing.size()) {
                JOptionPane.showMessageDialog(null, "jaque mate, el  ganador es " + originalTurno);
                return true;
            }
        }
        turno = originalTurno;
        return false;
    }
}
