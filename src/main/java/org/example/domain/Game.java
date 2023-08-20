package org.example.domain;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.PieceColor;
import org.example.dto.MoveDto;
import org.example.dto.MovementStatus;

public class Game {
    private DomainBoard board;
    private PieceColor turno = PieceColor.WHITE;

    public Game(DomainBoard board) {
        this.board = board;
    }

    public void makeMove(MoveDto movement){
            board.makeMove(movement);
            switchTurn();
    }


    public PieceColor getTurno() {
        return turno;
    }

    public DomainBoard getBoard(){
        return board;
    }


    public void switchTurn(){
        if (turno == PieceColor.WHITE) turno = PieceColor.BLACK;
        else turno = PieceColor.WHITE;
    }


}
