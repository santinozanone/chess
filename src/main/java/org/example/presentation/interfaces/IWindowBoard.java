package org.example.presentation.interfaces;

import org.example.domain.PositionDto;
import org.example.presentation.Button;

import java.util.List;

public interface IWindowBoard {
    Button[][] getButtons();

    void initializeBoard();
    void paintButtons(List<PositionDto> positions);
    void unPaintButtons(List<PositionDto> positions);
    void displayMove(int originX,int originY,int destinationX,int destinationY);
}
