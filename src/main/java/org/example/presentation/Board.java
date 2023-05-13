package org.example.presentation;

import org.example.domain.DomainBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Board implements ActionListener {
    DomainBoard domainBoard = new DomainBoard();
    private Button buttonPressed1;
    private Button buttonPressed2;
    private int cont=0;

    private List<Button> buttons  = new ArrayList<>();

    public void initializateBoard(){
        JFrame frame = new JFrame("Chess game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel =  new JPanel();
        panel.setPreferredSize(new Dimension(1000,800));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);;
        frame.setLocationRelativeTo(null);  // PONE EL FRAME EN EL MEDIO DE LA PANTALLA
        frame.setMinimumSize(new Dimension(500,400));
        GridLayout g = new GridLayout(8,8);
        panel.setLayout(g);
        addButtonToPanel(panel);

    }

    private void addButtonToPanel(JPanel panel){
        Color color = new Color(102,51,0);
        for (int i = 0; i <8 ; i++) {
            for (int j = 0; j <8 ; j++) {
                Button b = new Button(i,j);
                if ((i%2)==0) {
                    if ((j % 2) == 0) b.setBackground(Color.WHITE);
                    else b.setBackground(color);
                }else {
                    if ((j % 2) == 0) b.setBackground(color);
                    else b.setBackground(Color.WHITE);
                }
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.addActionListener(this);
                buttons.add(b);
                panel.add(b);
            }
        }
        loadIcons();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Button toggleButton = (Button) e.getSource();
        if (cont==0){
            buttonPressed1 = toggleButton;
            buttonPressed1.setSelected(true);
            cont++;
        }
        else if (cont==1){
            buttonPressed2 = toggleButton;
            buttonPressed2.setSelected(true);
            cont=0;
            buttonPressed1.setSelected(false);
            buttonPressed2.setSelected(false);
            System.out.println("------------------------------------------------------------------------");
            System.out.println(buttonPressed1.getPositionX() + " " + buttonPressed1.getPositionY());
            System.out.println(buttonPressed2.getPositionX() + " " + buttonPressed2.getPositionY());




            boolean isPossible = domainBoard.isMovementPossible(buttonPressed1.getPositionX(),buttonPressed1.getPositionY(),buttonPressed2.getPositionX(),buttonPressed2.getPositionY());
            if (isPossible){
                buttonPressed2.setIcon(buttonPressed1.getIcon());
                buttonPressed1.setIcon(null);
            }
            else{
                System.err.println("moviemiento incapaz");
            }
        }
    }



    private void loadIcons(){
        String [] PIECES = {"Torre.png","Caballo.png","Alfil.png","Reina.png","Rey.png","Alfil.png","Caballo.png","Torre.png"};

        java.net.URL imgURL;
        for (Button b:buttons) {

            if (b.getPositionX() == 1){
                imgURL = getClass().getResource("/blackPieces/Peon.png");
                b.setIcon(new ImageIcon(imgURL));
            }
            else if(b.getPositionX() == 6){
                imgURL = getClass().getResource("/whitePieces/Peon.png");
                b.setIcon(new ImageIcon(imgURL));
            }

            else if (b.getPositionX() == 7){
                imgURL = getClass().getResource("/whitePieces/"+PIECES[b.getPositionY()]);
                b.setIcon(new ImageIcon(imgURL));
            }

            else if (b.getPositionX() == 0){
                imgURL = getClass().getResource("/blackPieces/"+PIECES[b.getPositionY()]);
                b.setIcon(new ImageIcon(imgURL));
            }
        }
    }





}




