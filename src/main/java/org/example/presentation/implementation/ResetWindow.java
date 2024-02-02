package org.example.presentation.implementation;

import org.example.application.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetWindow implements ActionListener {

    private Game game;
    private  JDialog dialog;
    public ResetWindow(Game game){
        this.game = game;
        initializeBoard();
    }


    private void initializeBoard(){
        dialog = new JDialog();
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(500, 500));
        JButton button = new JButton();
        button.setText("RESET GAME");
        button.addActionListener(this);
        button.setBounds(100,100,200, 200);
        dialog.add(button);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        game.resetGame();
        dialog.dispose();
    }
}
