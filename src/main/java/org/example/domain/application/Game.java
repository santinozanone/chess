package org.example.domain.application;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.GameStatus;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.impl.CheckMateValidatorImpl;
import org.example.domain.service.impl.MoveValidatorHandler;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;

import java.util.List;

public class Game {
    private DomainBoard board;
    private PieceColor turn = PieceColor.WHITE;
    private MoveValidatorHandler moveValidatorHandler;

    public Game(DomainBoard board, MoveValidatorHandler moveValidatorHandler) {
        this.board = board;
        this.moveValidatorHandler = moveValidatorHandler;
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
       if (moveValidatorHandler.getCheckMovementHandler().isKingCheck(board.getBoard(), turn)){
            if (new CheckMateValidatorImpl(moveValidatorHandler.getPositionHandler(), moveValidatorHandler, moveValidatorHandler.getCheckMovementHandler()).isCheckMate(board.getBoard(), board.getMoveList(), turn)) {
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
    public List<PositionDto> getPieceMoves(int originX, int originY) {
        return moveValidatorHandler.getPossibleMovesOfPiece(board,board.getMoveList(), new PositionDto(originX, originY), turn);
    }


}
