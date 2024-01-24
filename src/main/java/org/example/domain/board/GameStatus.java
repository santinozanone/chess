package org.example.domain.board;

public enum GameStatus {
    KING_IN_CHECK("King is in Check"),CHECKMATE("Checkmate"),ACTIVE("Active"),STALEMATE("DRAW");
    private String message;

    GameStatus(String message) {
        this.message = message;
    }

    public String getValue() {
        return message;
    }
}
