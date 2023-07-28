package org.example.dto;

import org.example.domain.board.PieceColor;

public class MovementStatus {
    private boolean isMovementPossible,isKingChecked,isCheckMate;

    public MovementStatus() {
    }

    public void setMovementPossible(boolean movementPossible) {
        isMovementPossible = movementPossible;
    }

    public void setKingChecked(boolean kingChecked) {
        isKingChecked = kingChecked;
    }

    public void setCheckMate(boolean checkMate) {
        isCheckMate = checkMate;
    }

    public boolean isMovementPossible() {
        return isMovementPossible;
    }

    public boolean isKingChecked() {
        return isKingChecked;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

}
