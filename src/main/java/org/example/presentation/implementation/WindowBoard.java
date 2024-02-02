package org.example.presentation.implementation;

import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.dto.PositionDto;
import org.example.presentation.Button;
import org.example.presentation.interfaces.IPresenter;
import org.example.presentation.interfaces.IWindowBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WindowBoard implements ActionListener, IWindowBoard {

    private IPresenter presenter;
    private JLabel movementStatusLabel;
    private JLabel kingInCheckLabel;
    private JLabel turnLabel;

    private JPanel buttonPanel;

    public WindowBoard(IPresenter presenter) {
        this.presenter = presenter;
        SwingUtilities.invokeLater(() -> initializeBoard());
    }

    private org.example.presentation.Button[][] buttonMatrix = new org.example.presentation.Button[8][8];


    private void initializeBoard() {
        JPanel gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(1300, 800));
        gamePanel.setLayout(new BorderLayout());

        GridLayout g = new GridLayout(8, 8);
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(1000, 800));
        buttonPanel.setDoubleBuffered(true);
        buttonPanel.setLayout(g);

        gamePanel.add(buttonPanel, BorderLayout.WEST);

        JPanel messagePanel = new JPanel();
        messagePanel.setPreferredSize(new Dimension(300, 400));
        messagePanel.setLayout(new GridLayout(3, 1));

        turnLabel = new JLabel();
        turnLabel.setText("TURN :" + " WHITE");
        turnLabel.setFont(new Font("arial", Font.PLAIN, 25));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);


        movementStatusLabel = new JLabel();
        movementStatusLabel.setText("Game Started");
        movementStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        movementStatusLabel.setFont(new Font("arial", Font.PLAIN, 20));

        kingInCheckLabel = new JLabel();
        kingInCheckLabel.setText("");
        kingInCheckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        kingInCheckLabel.setFont(new Font("arial", Font.PLAIN, 25));


        messagePanel.add(turnLabel);
        messagePanel.add(movementStatusLabel);
        messagePanel.add(kingInCheckLabel);


        gamePanel.add(messagePanel, BorderLayout.EAST);

        JFrame frame = new JFrame("Chess game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(1000, 800));

        addButtonToPanel(buttonPanel);

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
        presenter.onBoxClick(toggleButton.getPositionX(), toggleButton.getPositionY());
    }


    private void loadIcons() {
        String[] PIECES = {"Rook.png", "Knight.png", "Bishop.png", "Queen.png", "King.png", "Bishop.png", "Knight.png", "Rook.png"};
        java.net.URL imgURL;

        for (int i = 0; i < 8; i++) {
            org.example.presentation.Button button = buttonMatrix[0][i];
            imgURL = getClass().getResource("/blackPieces/" + PIECES[button.getPositionY()]);
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0; i < 8; i++) {
            org.example.presentation.Button button = buttonMatrix[1][i];
            imgURL = getClass().getResource("/blackPieces/Pawn.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0; i < 8; i++) {
            org.example.presentation.Button button = buttonMatrix[6][i];
            imgURL = getClass().getResource("/whitePieces/Pawn.png");
            button.setIcon(new ImageIcon(imgURL));
        }
        for (int i = 0; i < 8; i++) {
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
        for (PositionDto positionDto : buttonsPositions) {
            buttonMatrix[positionDto.getX()][positionDto.getY()].setSelected(true);
        }
       buttonPanel.repaint();
    }

    @Override
    public void unPaintButtons(List<PositionDto> ButtonsPositions) {
        for (PositionDto positionDto : ButtonsPositions) {
            buttonMatrix[positionDto.getX()][positionDto.getY()].setSelected(false);
        }
        buttonPanel.repaint();
    }

    @Override
    public void updateBoard(Piece[][] pieceMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieceMatrix[i][j] == null && buttonMatrix[i][j].getIcon() != null) {
                    buttonMatrix[i][j].setIcon(null);
                } else if (pieceMatrix[i][j] != null && buttonMatrix[i][j].getIcon() == null) {
                    setIconToBoard(pieceMatrix, i, j);
                } else if (pieceMatrix[i][j] != null && buttonMatrix[i][j].getIcon() != null) {
                    String desc = ((ImageIcon) buttonMatrix[i][j].getIcon()).getDescription();
                    if (!desc.contains(pieceMatrix[i][j].getColor().toString().toLowerCase())) {
                        setIconToBoard(pieceMatrix, i, j);
                    }
                }
            }
        }
        buttonPanel.repaint();
    }

    private void setIconToBoard(Piece pieceMatrix[][], int i, int j) {
        java.net.URL imgURL;
        if (pieceMatrix[i][j].getColor() == PieceColor.BLACK)
            imgURL = getClass().getResource("/blackPieces/" + pieceMatrix[i][j].getClass().getSimpleName() + ".png");
        else imgURL = getClass().getResource("/whitePieces/" + pieceMatrix[i][j].getClass().getSimpleName() + ".png");
        buttonMatrix[i][j].setIcon(new ImageIcon(imgURL));
    }

    public void displayMessage(String firstMessage, String secondMessage) {
        this.movementStatusLabel.setText(firstMessage);
        this.kingInCheckLabel.setText(secondMessage);
    }

    public void displayTurn(String turn) {
        turnLabel.setText("TURN : " + turn);
    }


}




