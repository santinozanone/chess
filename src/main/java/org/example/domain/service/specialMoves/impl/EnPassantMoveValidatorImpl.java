package org.example.domain.service.specialMoves.impl;

import org.example.domain.board.DomainBoard;
import org.example.domain.board.movements.EnPassantMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Pawn;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.specialMoves.interfaces.EnPassantMoveValidator;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.List;

public class EnPassantMoveValidatorImpl implements EnPassantMoveValidator {
    public EnPassantMove getEnPassantMoveIfPossible(List<Move> moves, DomainBoard board, PieceColor turn, MoveDto moveToMake) {
        /*
         * if the last movement was a pawn move of two squares that ended next to a pawn of the other player
         * this player can move to square that its diagonally behind the other opponent piece
         * */
        int x = 0;
        if (turn == PieceColor.WHITE) x = -1;
        else x = 1;
        if (!isEnPassantValid(board, moves, moveToMake, x, turn)) {
            return null;
        }
        EnPassantMove enPassantMove = null;
        Piece positionToGo = board.getPiece(moveToMake.getDestinationX(), moveToMake.getDestinationY());
        Piece enemyPawn = board.getPiece(moveToMake.getDestinationX() - (x), moveToMake.getDestinationY());
        if (enemyPawn instanceof Pawn && enemyPawn.getColor() != turn && positionToGo == null) {
            enPassantMove = new EnPassantMove(moveToMake, enemyPawn, new PositionDto(moveToMake.getOriginX(),  moveToMake.getDestinationY()));
        }
        return enPassantMove;
    }


    private boolean isEnPassantValid(DomainBoard board, List<Move> moves, MoveDto moveToMake, int x, PieceColor turn) {
        if (!isEnPassantDestinationPositionValid(moveToMake, x)) {
            return false;
        }
        if (!(board.getPiece(moveToMake.getOriginX(), moveToMake.getOriginY()) instanceof Pawn)) {
            return false;
        }
        if (moves.isEmpty()) return false;
        Move move = moves.get(moves.size() - 1); // we get the last movement
        Piece lastPieceMoved = board.getPiece(move.getMoveDto().getDestinationX(), move.getMoveDto().getDestinationY());
        if (lastPieceMoved != null && !(lastPieceMoved instanceof Pawn)) return false;

        int positionsMoved = Math.abs(move.getMoveDto().getOriginX() - move.getMoveDto().getDestinationX());
        if (positionsMoved != 2) return false; // if it didnt move 2 positions we return

        Piece actualPiece = board.getPiece(moveToMake.getOriginX(), moveToMake.getOriginY());  // WE GET OUR ACTUAL POSITION
        if (actualPiece == null || actualPiece.getColor() != turn) {
            return false;
        }
        return true;
    }


    private boolean isEnPassantDestinationPositionValid(MoveDto moveToMake, int x) {
        return !(!((moveToMake.getDestinationX() == moveToMake.getOriginX() + x) && (Math.abs(moveToMake.getOriginY() - moveToMake.getDestinationY())) == 1) || moveToMake.getDestinationX() == 0);
    }


}



