package org.example;

import org.example.presentation.Board;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Board().initializateBoard());
    }
}