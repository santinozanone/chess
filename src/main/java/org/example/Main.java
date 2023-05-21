package org.example;

import org.example.application.GameApplication;
import org.example.domain.DomainBoard;
import org.example.domain.GameLogic;
import org.example.presentation.Board;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameApplication gameApplication = new GameApplication(new DomainBoard(),new GameLogic());
        SwingUtilities.invokeLater(() -> new Board(gameApplication).initializateBoard());
    }
}