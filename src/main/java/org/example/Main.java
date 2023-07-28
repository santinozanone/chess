package org.example;

import org.example.application.ChessAppService;
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
        Game game = new Game(board);
        GameLogic gameLogic = new GameLogic();
        ChessAppService chessAppService = new ChessAppService(gameLogic,game);


        IPresenter presenter = new Presenter(chessAppService);
        IWindowBoard WindowBoard = new Board(presenter);
        presenter.setView(WindowBoard);
        SwingUtilities.invokeLater(() -> WindowBoard.initializeBoard());
    }
}