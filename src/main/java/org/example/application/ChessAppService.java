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
       /* Piece[][] boardClone = board.getBoard();

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


        return false;*/

        DomainBoard board = game.getBoard();
        PieceColor turno = game.getTurno();
        MovementStatus movementStatus = gameLogic.getMoveStatus(board,move,turno);

        game.makeMove(move,movementStatus);




        /*
        * DICEW JAQUE MATE CUANDO NO ES PORQUE EN VEZ DE OBTENER CUALQUIER CASILLA INTERMEDIA, OBTIENE LAS QUE TIENEN ALGUNA PIEZA DENTRO
        *
        *
        *
        *
        * */

        return movementStatus.isMovementPossible() &&  !movementStatus.isKingChecked();




    }


    public List<PositionDto> getPieceMoves(int originX, int originY) {
        return gameLogic.getPossibleMoves(game.getBoard(), new PositionDto(originX, originY),game.getTurno());
    }


}
