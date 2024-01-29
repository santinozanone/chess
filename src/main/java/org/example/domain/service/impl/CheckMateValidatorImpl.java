    package org.example.domain.service.impl;

    import org.example.domain.service.interfaces.CheckMovementValidator;
    import org.example.domain.service.interfaces.MoveValidator;
    import org.example.domain.service.interfaces.PositionValidator;
    import org.example.dto.MovementStatus;
    import org.example.domain.board.DomainBoard;
    import org.example.domain.board.movements.Move;
    import org.example.domain.board.piece.Piece;
    import org.example.domain.board.piece.PieceColor;
    import org.example.domain.board.piece.Rey;
    import org.example.domain.service.interfaces.CheckMateValidator;
    import org.example.domain.strategy.implementation.NotFilterStrategy;
    import org.example.dto.MoveDto;
    import org.example.dto.PositionDto;

    import java.util.List;

    public class CheckMateValidatorImpl implements CheckMateValidator {
        private PositionValidator positionHandler;
        private MoveValidator moveHandler;

        private CheckMovementValidator checkMovementHandler;

        public CheckMateValidatorImpl(PositionValidator positionHandler, MoveValidator moveHandler, CheckMovementValidator checkMovementHandler) {
            this.positionHandler = positionHandler;
            this.moveHandler = moveHandler;
            this.checkMovementHandler = checkMovementHandler;
        }

        public boolean isCheckMate(DomainBoard board, List<Move> moves, PieceColor turno) {
            if (checkMovementHandler.isKingCheck(board, turno)) {
                int piecesThatSaveCheck = 0;
                PositionDto positionDto = positionHandler.getPiecePosition(board, Rey.class, turno);
                int kingX = positionDto.getX();
                int kingY = positionDto.getY();
               // DomainBoard board1 = new DomainBoard();
               // board1.setBoard(board);
                List<PositionDto> possibleKingPositions = moveHandler.getPossibleMovesOfPiece(board, moves, new PositionDto(kingX, kingY), turno);
                List<PositionDto> pieceCheckingKingPositions = checkMovementHandler.getPiecesCheckingKing(board, turno);
                List<PositionDto> intermediatePositions = positionHandler.getPositionsInBetween(board, new NotFilterStrategy(), kingX, kingY, pieceCheckingKingPositions.get(0).getX(), pieceCheckingKingPositions.get(0).getY());

                piecesThatSaveCheck = getNumberOfPiecesSavingCheck(pieceCheckingKingPositions, board, moves, turno, intermediatePositions);

                if (possibleKingPositions.size() == 0 && piecesThatSaveCheck < pieceCheckingKingPositions.size()) {
                    return true;
                }
            }
            return false;
        }


        private int getNumberOfPiecesSavingCheck(List<PositionDto> pieceCheckingKing,DomainBoard board, List<Move> moves, PieceColor turno,List<PositionDto> intermediatePositions) {
            int piecesThatSaveCheck = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (canSaveCheckByInterceptingCheckersPieces(intermediatePositions, i,j, board, moves,turno) || canSaveCheckByEatingCheckersPieces(pieceCheckingKing, i,j, board, moves,turno)){
                        piecesThatSaveCheck++;
                        return piecesThatSaveCheck;
                    }
                }
            }
            return piecesThatSaveCheck;
        }


        private boolean canSaveCheckByInterceptingCheckersPieces(List<PositionDto> intermediatePositions, int i, int j, DomainBoard board, List<Move> moves, PieceColor turno){
            for (PositionDto intermediatePosition : intermediatePositions) { // we loop to find if there is a position on the board that can get in the middle of the checkers piece
                MoveDto move = new MoveDto(i, j, intermediatePosition.getX(), intermediatePosition.getY());
                if ((!(board.getPiece(i, j) instanceof Rey)) && moveHandler.isSingleMovePossible(board, moves, move, turno).isMovementPossible()) {
                    return true;
                }
            }
            return false;
        }

        private boolean canSaveCheckByEatingCheckersPieces(List<PositionDto> pieceCheckingKing, int i, int j, DomainBoard board, List<Move> moves, PieceColor turno){
            for (PositionDto checker : pieceCheckingKing) {
                MoveDto move = new MoveDto(i, j, checker.getX(), checker.getY()); // we loop to find if there is a position on the board that can "eat" the checkers piece
                MovementStatus movementStatus = moveHandler.isSingleMovePossible(board, moves, move, turno);
                if (movementStatus.isMovementPossible()) {
                    return true;
                }
            }
            return false;
        }





    }