package org.example.presentation.interfaces;

public interface IPresenter {
    void onBoxClick(int originX,int originY);
    void setView(IWindowBoard view);
}
