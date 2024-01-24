package org.example.domain.board;

public enum MovementState {
    POSSIBLE("Movement Possible"),NOT_POSSIBLE("Movement Not Possible"),PAWN_PROMOTION("");
    private String message;

    MovementState(String message) {
        this.message = message;
    }

    public String getValue() {
        return message;
    }
}
