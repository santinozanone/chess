package org.example.domain.application;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.GameStatus;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.impl.CheckMateValidatorImpl;
import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.service.interfaces.MoveValidator;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;

import java.util.List;

public class Game {
    private DomainBoard board;
    private PieceColor turn = PieceColor.WHITE;
    private MoveValidator moveValidatorHandler;

    private PositionValidator positionValidator;

    private CheckMovementValidator checkMovementValidator;

    public Game(DomainBoard board, MoveValidator moveValidatorHandler, PositionValidator positionValidator, CheckMovementValidator checkMovementValidator) {
        this.board = board;
        this.moveValidatorHandler = moveValidatorHandler;
        this.positionValidator = positionValidator;
        this.checkMovementValidator = checkMovementValidator;
    }


    public MovementStatus isMovePossible(MoveDto movement){
        return moveValidatorHandler.isSingleMovePossible(board, board.getMoveList(), movement, turn);
    }


    public void makeMove(Move movement){
            board.makeMove(movement);
            board.logMove(movement);
            board.notifyChange();
            switchTurn();
    }

    public GameStatus getGameStatus(){
       if (checkMovementValidator.isKingCheck(board, turn)){
            if (new CheckMateValidatorImpl(positionValidator, moveValidatorHandler, checkMovementValidator).isCheckMate(board, board.getMoveList(), turn)) {
                return GameStatus.CHECKMATE;
            }
            return GameStatus.KING_IN_CHECK;
        }
        if (!(moveValidatorHandler.doPossibleMovementExists(board, board.getMoveList(), turn))){
            return GameStatus.STALEMATE;
        }
        return GameStatus.ACTIVE;
    }

    public PieceColor getTurn(){
        return turn;
    }

    public void switchTurn(){
        if (turn == PieceColor.WHITE) turn = PieceColor.BLACK;
        else turn = PieceColor.WHITE;
    }
    public List<PositionDto> getPieceValidMovements(int originX, int originY) {
        return moveValidatorHandler.getPossibleMovesOfPiece(board,board.getMoveList(), new PositionDto(originX, originY), turn);
    }


}
