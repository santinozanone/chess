package org.example.presentation.interfaces;

import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.dto.PositionDto;
import org.example.presentation.Button;

import java.util.List;

public interface IWindowBoard {
    Button[][] getButtons();

    void initializeBoard();
    void paintButtons(List<PositionDto> positions);
    void unPaintButtons(List<PositionDto> positions);

    void displayMessage(String messageLabel1,String messageLabel2);
    void updateBoard(Piece pieceMatrix [][]);
    void displayTurn(String turn );
}
