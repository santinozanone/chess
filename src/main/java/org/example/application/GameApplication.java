package org.example.application;

import org.example.domain.DomainBoard;
import org.example.domain.GameLogic;
import org.example.domain.Piece;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public class GameApplication {
    private DomainBoard board;
    private GameLogic gameLogic;

    public GameApplication(DomainBoard board, GameLogic gameLogic) {
        this.board = board;
        this.gameLogic = gameLogic;
    }

    public boolean makeMove(MoveDto move) {
        Piece[][] boardClone = board.getBoard();

        boolean succesful = false;

        boolean movementPossible = gameLogic.isMovementPossible(board.getBoard(),move);
        if (movementPossible) {
            boolean piecesInBetween = gameLogic.arePiecesInBetween(board.getBoard(), move);
            if (!piecesInBetween) {
                board.makeMove(move);
                if (!gameLogic.isKingCheck(board.getBoard())) {
                    succesful = true;
                } else {
                    board.setBoard(boardClone);
                    succesful = false;
                }
            }
            gameLogic.isCheckMate(board.getBoard());
            if (succesful) gameLogic.changeTurn();
            return succesful;
        }


        return false;
    }


    public List<PositionDto> getPieceMoves(int originX, int originY) {
        return gameLogic.getPieceMoves(board.getBoard(), originX, originY);
    }


}
