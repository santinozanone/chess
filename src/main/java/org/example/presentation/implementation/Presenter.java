package org.example.presentation.implementation;

import org.example.domain.Game;
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
        boolean isMovePossible = game.makeMove(move);

        if (isMovePossible)
            windowBoard.displayMove(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY(), secondBoxClicked.getPositionX(), secondBoxClicked.getPositionY());
        else System.err.println("movimiento incapaz");
        windowBoard.unPaintButtons(positions);
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
