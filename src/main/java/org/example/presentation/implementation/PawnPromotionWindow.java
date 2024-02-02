package org.example.presentation.implementation;

import org.example.application.Game;
import org.example.domain.board.movements.Move;
import org.example.domain.board.movements.PawnPromotion;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class PawnPromotionWindow implements ActionListener {
    private PieceColor turn;
    private Game game;

    private Move move;

    private JDialog dialog;
    public PawnPromotionWindow(PieceColor turn, Game game,Move move) {
        this.turn = turn;
        this.game = game;
        this.move = move;
        displayWindow();
    }

    public void displayWindow(){
        dialog = new JDialog();
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(500,500));
        dialog.setLayout(new GridLayout(1,4));
        loadButtons(dialog);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void loadButtons(JDialog jDialog){
        String[] PIECES = {"Rook.png", "Knight.png", "Bishop.png", "Queen.png"};
        java.net.URL imgURL;
        String colorUrl= "/whitePieces/";
        if (turn == PieceColor.BLACK) colorUrl = "/blackPieces/";
        for (int i = 0;i<4;i++){
            JButton imageButton = new JButton();
            imageButton.setName(String.valueOf(i));
            imageButton.addActionListener(this);
            imgURL = getClass().getResource(colorUrl + PIECES[i]);
            imageButton.setIcon(new ImageIcon(imgURL));
            jDialog.add(imageButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] PIECES = {"Rook", "Knight", "Bishop", "Queen"};
        JButton button = (JButton) e.getSource();
        button.getName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName("org.example.domain.board.piece."+PIECES[Integer.parseInt(button.getName())]);
            Piece promotedPiece = (Piece) clazz.getConstructor(PieceColor.class).newInstance(turn);
            ((PawnPromotion) move).setPromotedPiece(promotedPiece);
            game.makeMove(move);
            dialog.dispose();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
}
