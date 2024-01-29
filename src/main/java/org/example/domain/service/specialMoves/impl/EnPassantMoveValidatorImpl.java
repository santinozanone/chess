package org.example.domain.service.specialMoves.impl;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.EnPassantMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Peon;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.specialMoves.interfaces.EnPassantMoveValidator;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public class EnPassantMoveValidatorImpl implements EnPassantMoveValidator {
    public  EnPassantMove getEnPassantMoveIfPossible(List<Move> moves, DomainBoard board, PositionDto actualPosition, PieceColor turn, MoveDto moveToMake) {
        /*
         * if the last movement was a pawn move of two squares that ended next to a pawn of the other player
         * this player can move to square that its diagonally behind the other opponent piece
         * */
        int x = 0;
        if (turn == PieceColor.WHITE) x = -1;
        else x = 1;
        if ((!(moveToMake.getDestinationX() == actualPosition.getX() + x && Math.abs(actualPosition.getY() - moveToMake.getDestinationY()) == 1)) || moveToMake.getDestinationX() == 0) {
            return null;
        }
        if (!(board.getPiece(actualPosition.getX(),actualPosition.getY()) instanceof Peon)) {
            return null;
        }
        EnPassantMove enPassantMove = null;
        if (moves.isEmpty()) return null;
        Move move = moves.get(moves.size() - 1); // we get the last movement
        Piece lastPieceMoved = board.getPiece(move.getMoveDto().getDestinationX(),move.getMoveDto().getDestinationY());
        if (lastPieceMoved != null && !lastPieceMoved.getClass().getSimpleName().equals("Peon")) return null;

        int positionsMoved = Math.abs(move.getMoveDto().getOriginX() - move.getMoveDto().getDestinationX());
        if (positionsMoved != 2) return null; // if it didnt move 2 positions we return

        Piece actualPiece = board.getPiece(actualPosition.getX(),actualPosition.getY());  // WE GET OUR ACTUAL POSITION
        if (actualPiece != null && actualPiece.getColor() == turn) {
            Piece positionToGo = board.getPiece(moveToMake.getDestinationX(),moveToMake.getDestinationY());
            Piece enemyPawn = board.getPiece(moveToMake.getDestinationX() - (x),moveToMake.getDestinationY());
            if (enemyPawn != null && enemyPawn.getClass().getSimpleName().equals("Peon") && enemyPawn.getColor() != turn && positionToGo == null) {
                enPassantMove = new EnPassantMove(moveToMake, enemyPawn, new PositionDto(actualPosition.getX(), moveToMake.getDestinationY()));
            }

        }
        return enPassantMove;
    }
}
