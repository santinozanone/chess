package org.example;

import org.example.domain.application.Game;
import org.example.domain.board.DomainBoard;
import org.example.domain.service.impl.CheckMovementHandlerImpl;
import org.example.domain.service.impl.MoveValidatorHandler;
import org.example.domain.service.impl.PositionHandler;
import org.example.presentation.implementation.Board;
import org.example.presentation.implementation.Presenter;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

public class Main {
    public static void main(String[] args) {
        DomainBoard board = new DomainBoard();
        PositionHandler positionHandler = new PositionHandler();
        Game game = new Game(board,new MoveValidatorHandler(positionHandler, new CheckMovementHandlerImpl(positionHandler)));
        IPresenter presenter = new Presenter(game);
        IWindowBoard WindowBoard = new Board(presenter);
        presenter.setView(WindowBoard);
        board.addListener(WindowBoard);

    }
}