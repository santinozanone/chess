package org.example;

import org.example.domain.Game;
import org.example.domain.board.DomainBoard;
import org.example.domain.service.GameLogic;
import org.example.presentation.implementation.Board;
import org.example.presentation.implementation.Presenter;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DomainBoard board = new DomainBoard();
        GameLogic gameLogic = new GameLogic();
        Game game = new Game(board,gameLogic);

        IPresenter presenter = new Presenter(game);
        IWindowBoard WindowBoard = new Board(presenter);
        presenter.setView(WindowBoard);
        SwingUtilities.invokeLater(() -> WindowBoard.initializeBoard());
    }
}