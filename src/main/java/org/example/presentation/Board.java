package org.example.presentation;

import org.example.application.GameApplication;
import org.example.domain.DomainBoard;
import org.example.domain.GameLogic;
import org.example.domain.Piece;
import org.example.domain.PositionDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Board implements ActionListener {
    // DomainBoard domainBoard = new DomainBoard();
    GameApplication application;

    public Board(GameApplication application) {
        this.application = application;
    }

    private Button buttonPressed1;
    private Button buttonPressed2;
    private int cont = 0;


    private Button [][] buttonMatrix= new Button[8][8];

    private List<Button> paintedButtons = new ArrayList<>();


    public void initializateBoard() {
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
                Button b = new Button(i, j);
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
        Button toggleButton = (Button) e.getSource();
        if (cont == 0) {
            buttonPressed1 = toggleButton;
            buttonPressed1.setSelected(true);
            cont++;

            List<PositionDto> possibleMoves = application.getPieceMoves(buttonPressed1.getPositionX(), buttonPressed1.getPositionY());
            try {
                paintedButtons = paintPositions(possibleMoves);
            } catch (InterruptedException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

        } else if (cont == 1) {
            buttonPressed2 = toggleButton;
            buttonPressed2.setSelected(true);
            cont = 0;
            buttonPressed1.setSelected(false);
            buttonPressed2.setSelected(false);



            boolean isPossible = application.makeMove(buttonPressed1.getPositionX(), buttonPressed1.getPositionY(), buttonPressed2.getPositionX(), buttonPressed2.getPositionY());
            if (isPossible) {
                buttonPressed2.setIcon(buttonPressed1.getIcon());
                buttonPressed1.setIcon(null);
                unPaintPositions(paintedButtons);
            } else {
                unPaintPositions(paintedButtons);
                System.err.println("moviemiento incapaz");
            }

        }
    }


    private List<Button> paintPositions(List<PositionDto> positions) throws InterruptedException, InvocationTargetException {

        List<Button> paintedButton = new ArrayList<>();

       SwingWorker<List<Button>,Void> worker = new SwingWorker<List<Button>, Void>() {
           @Override
           protected List<Button> doInBackground() throws Exception {
               for (PositionDto positionDto:positions){
                   paintedButton.add(buttonMatrix[positionDto.getX()][positionDto.getY()]);
               }
               return paintedButton;
           }

           @Override
           protected void done() {
               try {
                   get();
               } catch (InterruptedException | ExecutionException e) {
                   throw new RuntimeException(e);
               }
               for (PositionDto positionDto : positions) {
                   buttonMatrix[positionDto.getX()][positionDto.getY()].setSelected(true);
               }
           }
       };


        worker.execute();

        return paintedButton;


    }





    private void unPaintPositions(List<Button> paintedButtons) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (Button button : paintedButtons) {
                    button.setSelected(false);
                    //button.setBackground(null);
                }
            }
        });
    }


    private void loadIcons() {
        String[] PIECES = {"Torre.png", "Caballo.png", "Alfil.png", "Reina.png", "Rey.png", "Alfil.png", "Caballo.png", "Torre.png"};
        java.net.URL imgURL;

        for (int i = 0;i<8;i++){
            Button button = buttonMatrix[0][i];
            imgURL = getClass().getResource("/blackPieces/" + PIECES[button.getPositionY()]);
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            Button button = buttonMatrix[1][i];
            imgURL = getClass().getResource("/blackPieces/Peon.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            Button button = buttonMatrix[6][i];
            imgURL = getClass().getResource("/whitePieces/Peon.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0;i<8;i++){
            Button button = buttonMatrix[7][i];
            imgURL = getClass().getResource("/whitePieces/" + PIECES[button.getPositionY()]);
            button.setIcon(new ImageIcon(imgURL));
        }
    }


}




