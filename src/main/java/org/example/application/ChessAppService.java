package org.example.application;

import org.example.domain.Game;
import org.example.domain.board.DomainBoard;
import org.example.domain.board.PieceColor;
import org.example.domain.service.GameLogic;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;
import org.example.dto.PositionDto;

import java.util.List;

public class ChessAppService {
    private GameLogic gameLogic;

    private Game game;

    public ChessAppService(GameLogic gameLogic, Game game) {
        this.gameLogic = gameLogic;
        this.game = game;
    }

    public boolean makeMove(MoveDto move) {
        DomainBoard board = game.getBoard();
        PieceColor turno = game.getTurno();
        MovementStatus movementStatus = gameLogic.getMoveStatus(board,move,turno);

        if(movementStatus.isMovementPossible() && !movementStatus.isCheckMate() && !movementStatus.isKingChecked()) {
            game.makeMove(move);
        }

        return movementStatus.isMovementPossible() &&  !movementStatus.isKingChecked();
    }


    public List<PositionDto> getPieceMoves(int originX, int originY) {
        return gameLogic.getPossibleMoves(game.getBoard(), new PositionDto(originX, originY),game.getTurno());
    }


}
