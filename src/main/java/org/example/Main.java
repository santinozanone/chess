package org.example;

import org.example.application.GameApplication;
import org.example.domain.DomainBoard;
import org.example.domain.GameLogic;
import org.example.presentation.implementation.Board;
import org.example.presentation.implementation.Presenter;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameApplication gameApplication = new GameApplication(new DomainBoard(),new GameLogic());
        IPresenter presenter = new Presenter(gameApplication);
        IWindowBoard board = new Board(presenter);
        presenter.setView(board);
        SwingUtilities.invokeLater(() -> board.initializeBoard());
    }
}