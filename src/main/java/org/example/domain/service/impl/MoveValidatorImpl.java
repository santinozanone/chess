package org.example.domain.service.impl;

import org.example.domain.service.interfaces.CheckMovementValidator;
import org.example.domain.service.interfaces.MoveValidator;
import org.example.domain.service.interfaces.PositionValidator;
import org.example.dto.MovementStatus;
import org.example.domain.board.DomainBoard;
import org.example.domain.board.MovementState;
import org.example.domain.board.movements.EnPassantMove;
import org.example.domain.board.movements.Move;
import org.example.domain.board.movements.PawnPromotion;
import org.example.domain.board.movements.StandardMove;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.service.specialMoves.impl.CastlingMoveValidatorImpl;
import org.example.domain.service.specialMoves.impl.EnPassantMoveValidatorImpl;
import org.example.domain.service.specialMoves.impl.PawnPromotionValidatorImpl;
import org.example.domain.service.specialMoves.interfaces.CastlingMoveValidator;
import org.example.domain.service.specialMoves.interfaces.EnPassantMoveValidator;
import org.example.domain.service.specialMoves.interfaces.PawnPromotionValidator;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class MoveValidatorImpl implements MoveValidator {

    private PositionValidator positionHandler;
    private CheckMovementValidator checkMovementHandler;


    public MoveValidatorImpl(PositionValidator positionHandler, CheckMovementValidator checkMovementHandler) {
        this.positionHandler = positionHandler;
        this.checkMovementHandler = checkMovementHandler;
    }

    public List<PositionDto> getPossibleMovesOfPiece(DomainBoard domainBoard, List<Move> moves, PositionDto positionDto, PieceColor turno) {
        List<PositionDto> possibleMoves = new ArrayList<>();
        MovementStatus movementStatus;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                movementStatus = isSingleMovePossible(domainBoard, moves, new MoveDto(positionDto.getX(), positionDto.getY(), i, j), turno);
                if (movementStatus.isMovementPossible()) {
                    possibleMoves.add(new PositionDto(i, j));
                }
            }
        }
        return possibleMoves;
    }

    public boolean doPossibleMovementExists(DomainBoard domainBoard, List<Move> moves,PieceColor turno){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getPossibleMovesOfPiece(domainBoard, moves, new PositionDto(i, j), turno).size()>0) return true;
                }
            }
        return false;
    }

    public MovementStatus isSingleMovePossible(DomainBoard domainBoard, List<Move> moves, MoveDto move, PieceColor turno) {
        MovementStatus movementStatus = getMovementStatus(domainBoard, move, turno, moves);
        if (!movementStatus.isMovementPossible()) return movementStatus;
        return isMoveValid(domainBoard, movementStatus.getMove(), movementStatus.getState(), turno);
    }

    private boolean isPieceMovementValid(DomainBoard board, MoveDto move, PieceColor turn) {
        int originX = move.getOriginX();
        int originY = move.getOriginY();
        int destinationX = move.getDestinationX();
        int destinationY = move.getDestinationY();
        if (board.getPiece( originX,originY) == null ||board.getPiece( originX,originY) == null && board.getPiece( originX,originY) == null) {
            return false;
        }
        Piece originPiece = board.getPiece( originX,originY);
        Piece destinationPiece = board.getPiece( destinationX,destinationY);

        boolean movementValid = false;

        if (turn != originPiece.getColor()) {
            return false;
        }

        if (destinationPiece != null && originPiece.getColor().equals(destinationPiece.getColor())) {
            return false;
        }

        if (destinationPiece != null) {
            movementValid = originPiece.isEatingMovementPossible(originX, originY, destinationX, destinationY);
        }

        if (destinationPiece == null) {
            movementValid = originPiece.isMovementPossible(originX, originY, destinationX, destinationY);
        }
        return movementValid;
    }


    private MovementStatus getMovementStatus(DomainBoard domainBoard, MoveDto move, PieceColor turno, List<Move> moves){
        MovementStatus movementStatus = checkSpecialMoves(domainBoard,moves,move, turno);
        if (movementStatus == null ) {
            if (!isPieceMovementValid(domainBoard, move, turno) || positionHandler.arePiecesInBetween(domainBoard, move))  {
                return new MovementStatus(false, MovementState.NOT_POSSIBLE, null);
            }
            movementStatus = checkPawnPromotion(domainBoard, move, turno, new PawnPromotionValidatorImpl());
            if (movementStatus == null) {
                movementStatus = new MovementStatus(true, MovementState.POSSIBLE, new StandardMove(move, domainBoard.getPiece(move.getDestinationX(),move.getDestinationY())));
            }
        }
        return movementStatus;
    }


    private MovementStatus checkSpecialMoves(DomainBoard domainBoard,List<Move> moves,MoveDto move,PieceColor turno){
        MovementStatus specialMove = checkCastling(domainBoard, moves, move, turno,new CastlingMoveValidatorImpl());
        if (specialMove != null){
            return specialMove;
        }
        specialMove = checkEnPassant(domainBoard, moves, move, turno, new EnPassantMoveValidatorImpl());
        return specialMove;
    }

    private MovementStatus checkPawnPromotion(DomainBoard domainBoard , MoveDto move, PieceColor turno, PawnPromotionValidator pawnPromotionValidator){
        boolean isPawnPromotionPossible = pawnPromotionValidator.isPawnPromotionPossible(domainBoard, move, turno);
        if (isPawnPromotionPossible) {
            return new MovementStatus(true, MovementState.PAWN_PROMOTION,new PawnPromotion(move,domainBoard.getPiece(move.getOriginX(),move.getOriginY()),domainBoard.getPiece(move.getDestinationX(),move.getDestinationY()),null )) ;
        }
        return null;
    }
    private MovementStatus checkCastling(DomainBoard domainBoard, List<Move> moves, MoveDto move, PieceColor turno, CastlingMoveValidator castlingMoveValidator){
        Move castlingMove = castlingMoveValidator.getCastlingMoveIfPossible(domainBoard, new PositionDto(move.getOriginX(), move.getOriginY()), move, turno, moves, positionHandler, checkMovementHandler);
        if (castlingMove != null) {
            return  new  MovementStatus(true, MovementState.POSSIBLE, castlingMove);
        }
        return null;
    }
    private MovementStatus checkEnPassant(DomainBoard domainBoard, List<Move> moves, MoveDto move, PieceColor turno, EnPassantMoveValidator enPassantMoveValidator){
        EnPassantMove enPassantMove = enPassantMoveValidator.getEnPassantMoveIfPossible(moves, domainBoard, new PositionDto(move.getOriginX(), move.getOriginY()), turno, move);
        if (enPassantMove != null) {
            return new MovementStatus(true,MovementState.POSSIBLE,enPassantMove);
        }
        return null;
    }

    private MovementStatus isMoveValid(DomainBoard domainBoard, Move moveToBeMade, MovementState movementState, PieceColor turno){
        domainBoard.makeMove(moveToBeMade);
        domainBoard.logMove(moveToBeMade);  // we need to log the move so its added to the list of moves, that way we can undo it later
        if (!checkMovementHandler.isKingCheck(domainBoard, turno)) {
            domainBoard.undoMove();
            return new MovementStatus(true, movementState, moveToBeMade);
        } else {
            domainBoard.undoMove();
            return new MovementStatus(false, MovementState.NOT_POSSIBLE, null);
        }
    }
}
