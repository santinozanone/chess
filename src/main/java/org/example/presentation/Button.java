package org.example.presentation;

import javax.swing.*;

public class Button extends JToggleButton {
    private int positionX,positionY;

    public Button(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }


}
