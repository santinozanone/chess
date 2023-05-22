package org.example.presentation.implementation;

import org.example.application.GameApplication;
import org.example.domain.PositionDto;
import org.example.presentation.Button;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements IPresenter {
    private GameApplication gameApplication;
    private IWindowBoard windowBoard;
    private int buttonCounter = 0;
    private Button firstBoxClicked;
    private Button secondBoxClicked;
    List<PositionDto> positions = new ArrayList<>();

    public Presenter(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
    }


    @Override
    public void onBoxClick(int originX,int originY) {
        Button [][] buttonMatrix = windowBoard.getButtons();


            if (buttonCounter == 0) {
                firstBoxClicked = buttonMatrix[originX][originY];
                positions = gameApplication.getPieceMoves(originX, originY);
                positions.add(new PositionDto(originX,originY));
                windowBoard.paintButtons(positions);
                buttonCounter++;
            } else {
                secondBoxClicked = buttonMatrix[originX][originY];
                positions.add(new PositionDto(originX,originY));
                positions.add(new PositionDto(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY()));
                buttonCounter = 0;

                boolean isMovePossible = gameApplication.makeMove(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY(), secondBoxClicked.getPositionX(), secondBoxClicked.getPositionY());
                if (isMovePossible)
                    windowBoard.displayMove(firstBoxClicked.getPositionX(), firstBoxClicked.getPositionY(), secondBoxClicked.getPositionX(), secondBoxClicked.getPositionY());
                else System.err.println("movimiento incapaz");
                windowBoard.unPaintButtons(positions);
            }
    }

    @Override
    public void setView(IWindowBoard view) {
        this.windowBoard = view;

    }
}
