package org.example.domain;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
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

        Move move  = gameLogic.isSingleMovePossible(board, board.getMoveList(), movement, turno);
        if (move != null){
            board.makeMove2(move);
            board.logMove(move);
            gameLogic.isCheckMate(board.getBoard(), board.getMoveList(), turno); // find a place to call this method
            switchTurn();

            return true;
        }
        System.out.println("no se puede hacer");
        return false;

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
       // gameLogic.getSpecialMoves(board, board.getMoveList(), new PositionDto(originX, originY),turno);
        return gameLogic.getPossibleMoves(board,board.getMoveList(), new PositionDto(originX, originY),turno);
    }
}
