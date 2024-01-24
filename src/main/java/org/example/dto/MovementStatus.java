package org.example.dto;

import org.example.domain.board.MovementState;
import org.example.domain.board.movements.Move;

public class MovementStatus {
    private boolean isMovementPossible;
    private MovementState movementState;

    private Move move;


    public MovementStatus(boolean isMovementPossible, MovementState movementState, Move move) {
            this.isMovementPossible = isMovementPossible;
            this.movementState = movementState;
            this.move = move;
    }

    public boolean isMovementPossible() {
        return isMovementPossible;
    }

    public MovementState getState() {
        return movementState;
    }

    public Move getMove() {
        return move;
    }
}
