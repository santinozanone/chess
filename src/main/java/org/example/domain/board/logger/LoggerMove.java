package org.example.domain.board.logger;

import org.example.dto.MoveDto;

public class LoggerMove {
    private MoveDto moveDto;
    private String originPiece;
    private String destinationPiece;

    public LoggerMove(MoveDto moveDto, String originPiece, String eatenPiece) {
        this.moveDto = moveDto;
        this.originPiece = originPiece;
        this.destinationPiece = eatenPiece;
    }

    public MoveDto getMoveDto() {
        return moveDto;
    }

    public String getOriginPiece() {
        return originPiece;
    }

    public String getEatenPiece() {
        return destinationPiece;
    }

    @Override
    public String toString() {
        return "LoggerMove{" +
                "moveDto=" + moveDto +
                ", originPiece='" + originPiece + '\'' +
                ", destinationPiece='" + destinationPiece + '\'' +
                '}';
    }
}
