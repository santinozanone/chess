package org.example.domain;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.Piece;
import org.example.domain.board.PieceColor;
import org.example.domain.board.logger.LoggerMove;
import org.example.domain.service.GameLogic;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private DomainBoard board;
    private PieceColor turno = PieceColor.WHITE;
    private GameLogic gameLogic;

    private List<LoggerMove> movementsMade;

    public Game(DomainBoard board,GameLogic gameLogic) {
        this.board = board;
        this.gameLogic = gameLogic;
        movementsMade = new ArrayList<>();
    }

    public boolean makeMove(MoveDto movement){

        MovementStatus movementStatus = gameLogic.getMoveStatus(board,movement,turno);
        if(movementStatus.isMovementPossible() && !movementStatus.isCheckMate() && !movementStatus.isKingChecked()) {
            logMove(movement);
            board.makeMove(movement);
            switchTurn();
        }
        return movementStatus.isMovementPossible() &&  !movementStatus.isKingChecked();

        //refactorizar y retornar MovementStatus

    }


    private void logMove(MoveDto movement){
        Piece[][] matrix = board.getBoard();
        String origin =  matrix[movement.getOriginX()][movement.getOriginY()].getClass().getSimpleName();
        String destination = "null";
        if ( matrix[movement.getDestinationX()][movement.getDestinationY()] != null) destination =  matrix[movement.getDestinationX()][movement.getDestinationY()].getClass().getSimpleName();
        movementsMade.add(new LoggerMove(movement,origin,destination));
        System.out.println(movementsMade.get(movementsMade.size()-1).toString());
    }

    public DomainBoard getBoard(){
        return board;
    }


    public void switchTurn(){
        if (turno == PieceColor.WHITE) turno = PieceColor.BLACK;
        else turno = PieceColor.WHITE;
    }

    public List<PositionDto> getPieceMoves(int originX, int originY) {
        gameLogic.getSpecialMoves(board, movementsMade, turno);
        return gameLogic.getPossibleMoves(board, new PositionDto(originX, originY),turno);
    }
}
