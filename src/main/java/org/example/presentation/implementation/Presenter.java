package org.example.presentation.implementation;

import org.example.domain.application.Game;
import org.example.dto.MovementStatus;
import org.example.domain.board.GameStatus;
import org.example.domain.board.MovementState;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;
import org.example.presentation.Button;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements IPresenter {
    private Game game;
    private IWindowBoard windowBoard;
    private int buttonCounter = 0;
    private Button firstBoxClicked;
    private Button secondBoxClicked;
    List<PositionDto> positions = new ArrayList<>();

    public Presenter(Game game) {
        this.game = game;

    }
    @Override
    public void onBoxClick(int originX,int originY) {
        Button [][] buttonMatrix = windowBoard.getButtons();
            if (buttonCounter == 0) {
                firstBoxClicked(originX,originY,buttonMatrix);
            } else {
                secondBoxClicked(originX,originY,buttonMatrix);
                handleMove();
            }
    }


    private void handleMove(){
        MoveDto move = new MoveDto(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY(), secondBoxClicked.getPositionX(), secondBoxClicked.getPositionY());
        MovementStatus movementStatus = game.isMovePossible(move);
        if (movementStatus.getState() == MovementState.POSSIBLE){
            game.makeMove(movementStatus.getMove());
        }
        if (movementStatus.getState() == MovementState.PAWN_PROMOTION){
           new PawnPromotionWindow(game.getTurn(),game, movementStatus.getMove());
        }
        GameStatus gameStatus = game.getGameStatus();

        if (gameStatus == GameStatus.KING_IN_CHECK){
            windowBoard.displayMessage( gameStatus.getValue(),movementStatus.getState().getValue());
        }
        else if (gameStatus == GameStatus.CHECKMATE){
             // should display checkmate window
        }
        else if (gameStatus == GameStatus.STALEMATE){
              // should display stalemate window
        }
        windowBoard.displayMessage( gameStatus.getValue(),movementStatus.getState().getValue());

        windowBoard.unPaintButtons(positions);
        windowBoard.displayTurn(game.getTurn().toString());
    }

    private void firstBoxClicked( int originX,int originY, Button [][] buttonMatrix){
        firstBoxClicked = buttonMatrix[originX][originY];
        positions = game.getPieceMoves(originX, originY);
        positions.add(new PositionDto(originX,originY));
        windowBoard.paintButtons(positions);
        buttonCounter++;
    }

    private void secondBoxClicked( int originX,int originY, Button [][] buttonMatrix){
        secondBoxClicked = buttonMatrix[originX][originY];
        positions.add(new PositionDto(originX,originY));
        positions.add(new PositionDto(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY()));
        buttonCounter = 0;
    }


    @Override
    public void setView(IWindowBoard view) {
        this.windowBoard = view;
    }
}
