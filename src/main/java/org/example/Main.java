package org.example;

import org.example.domain.application.Game;
import org.example.domain.board.DomainBoard;
import org.example.domain.service.impl.CheckMovementValidatorImpl;
import org.example.domain.service.impl.MoveValidatorImpl;
import org.example.domain.service.impl.PositionValidatorImpl;
import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.service.interfaces.MoveValidator;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.presentation.implementation.Board;
import org.example.presentation.implementation.Presenter;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

public class Main {
    public static void main(String[] args) {
        DomainBoard board = new DomainBoard();
        PositionValidator positionValidator = new PositionValidatorImpl();
        CheckMovementValidator checkMovementValidator = new CheckMovementValidatorImpl(positionValidator);
        MoveValidator moveValidator = new MoveValidatorImpl(positionValidator,checkMovementValidator);
        Game game = new Game(board,moveValidator,positionValidator,checkMovementValidator);


        IPresenter presenter = new Presenter(game);
        IWindowBoard WindowBoard = new Board(presenter);
        presenter.setView(WindowBoard);
        board.addListener(WindowBoard);

    }
}