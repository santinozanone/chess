package org.example.domain.service;

import org.example.domain.board.movements.Move;
import org.example.domain.board.piece.Piece;
import org.example.domain.board.piece.PieceColor;
import org.example.domain.board.logger.LoggerMove;
import org.example.domain.board.movements.EnPassantMove;
import org.example.dto.MoveDto;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class SpecialMoves {




    private void isCastingPossible(){

    }

    private void isPawnPromotionPossible(){

    }

    public static List<EnPassantMove> getEnPassantMoveIfPossible(List<Move> moves, Piece[][] matrix, PositionDto actualPosition, PieceColor turn){
        /*
        * if the last movement was a pawn move of two squares that ended next to a pawn of the other player
        * this player can move to square that its diagonally behind the other opponent piece
        * */
        List<EnPassantMove> positions = new ArrayList<>();
        if (moves.isEmpty()) return positions;
        Move move = moves.get(moves.size()-1); // we get the last movement
        Piece lastPieceMoved = matrix[move.getMoveDto().getOriginX()][move.getMoveDto().getOriginY()];
        if ( lastPieceMoved != null && !lastPieceMoved.getClass().getSimpleName().equals("Peon"))return positions;
        int positionsMoved = Math.abs(move.getMoveDto().getOriginX() - move.getMoveDto().getDestinationX());
        if (positionsMoved != 2 )return positions; // if it didnt move 2 positions we return

        int Yposition = 1;

        Piece piece = matrix[actualPosition.getX()][actualPosition.getY()];  // WE GET OUR ACTUAL POSITION
        if (piece != null && piece.getColor() == turn ){
            int x = 0,y=1;
            for (int i = 0;i<2;i++){

                if (turn == PieceColor.WHITE) x = -1;
                else x = 1;
                if (actualPosition.getY()==0){
                    y = 1;
                }
                if (actualPosition.getY() == 7) { // IF ITS IN THE BORDER WE CAN ONLY ASK FOR THE POSITION TO THE LEFT
                    y = -1;
                }
                Piece enemyPawn = matrix[actualPosition.getX()][actualPosition.getY()+y];
                if (enemyPawn != null && enemyPawn.getClass().getSimpleName().equals("Peon")  && enemyPawn.getColor() != turn && matrix[actualPosition.getX()+x][actualPosition.getY()+y] == null){ // IF THERE IS AN ENEMY PAWN WHO HAS A FREE SPACE BEHIND IT THEN WE HAVE EN PASSANT
                    PositionDto positionToGo = new PositionDto(actualPosition.getX()+x,actualPosition.getY()+y);
                    EnPassantMove enPassantMove = new EnPassantMove(new MoveDto(actualPosition.getX(), actualPosition.getY(),actualPosition.getX()+x,actualPosition.getY()+y),enemyPawn,new PositionDto(actualPosition.getX(),actualPosition.getY()+y));

                    positions.add(enPassantMove);
                }
                y = -1;
                // if our actual position isnt at any border we have to scan next to us, thats why the y starts en 1 and then it gets a -1
            }
        }
        return positions;
    }



}
