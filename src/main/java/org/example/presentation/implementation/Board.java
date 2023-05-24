package org.example.presentation.implementation;

import org.example.dto.PositionDto;
import org.example.presentation.Button;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Board implements ActionListener, IWindowBoard {

    IPresenter presenter;


    public Board(IPresenter presenter) {
        this.presenter = presenter;
    }

    private org.example.presentation.Button[][] buttonMatrix= new org.example.presentation.Button[8][8];


    @Override
    public void initializeBoard() {
        GridLayout g = new GridLayout(8, 8);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 800));
        panel.setDoubleBuffered(true);
        panel.setLayout(g);
        JFrame frame = new JFrame("Chess game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);  // PONE EL FRAME EN EL MEDIO DE LA PANTALLA
        frame.setMinimumSize(new Dimension(500, 400));
        addButtonToPanel(panel);

    }

    private void addButtonToPanel(JPanel panel) {
        Color color = new Color(102, 51, 0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                org.example.presentation.Button b = new org.example.presentation.Button(i, j);
                if ((i % 2) == 0) {
                    if ((j % 2) == 0) b.setBackground(Color.WHITE);
                    else b.setBackground(color);
                } else {
                    if ((j % 2) == 0) b.setBackground(color);
                    else b.setBackground(Color.WHITE);
                }
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.addActionListener(this);
                buttonMatrix[i][j] = b;
                panel.add(b);
            }
        }
        loadIcons();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        org.example.presentation.Button toggleButton = (org.example.presentation.Button) e.getSource();
        presenter.onBoxClick(toggleButton.getPositionX(),toggleButton.getPositionY());
    }


    private void loadIcons() {
        String[] PIECES = {"Torre.png", "Caballo.png", "Alfil.png", "Reina.png", "Rey.png", "Alfil.png", "Caballo.png", "Torre.png"};
        java.net.URL imgURL;

        for (int i = 0;i<8;i++){
            org.example.presentation.Button button = buttonMatrix[0][i];
            imgURL = getClass().getResource("/blackPieces/" + PIECES[button.getPositionY()]);
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            org.example.presentation.Button button = buttonMatrix[1][i];
            imgURL = getClass().getResource("/blackPieces/Peon.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            org.example.presentation.Button button = buttonMatrix[6][i];
            imgURL = getClass().getResource("/whitePieces/Peon.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            Button button = buttonMatrix[7][i];
            imgURL = getClass().getResource("/whitePieces/" + PIECES[button.getPositionY()]);
            button.setIcon(new ImageIcon(imgURL));
        }
    }


    @Override
    public Button[][] getButtons() {
        return buttonMatrix;
    }



    @Override
    public void paintButtons(List<PositionDto> buttonsPositions) {

        SwingWorker<List<PositionDto>,Void> worker = new SwingWorker<List<PositionDto>, Void>() {
            @Override
            protected List<PositionDto> doInBackground() throws Exception {
                return buttonsPositions;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                for (PositionDto positionDto : buttonsPositions) {
                    buttonMatrix[positionDto.getX()][positionDto.getY()].setSelected(true);
                }
            }
        };
        worker.execute();
    }

    @Override
    public void unPaintButtons(List<PositionDto> ButtonsPositions) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (PositionDto positionDto  : ButtonsPositions) {
                    buttonMatrix[positionDto.getX()][positionDto.getY()].setSelected(false);                    //button.setBackground(null);
                }
            }
        });
    }

    @Override
    public void displayMove(int originX, int originY, int destinationX, int destinationY) {
       buttonMatrix[destinationX][destinationY].setIcon(buttonMatrix[originX][originY].getIcon());
       buttonMatrix[originX][originY].setIcon(null);
    }
}




