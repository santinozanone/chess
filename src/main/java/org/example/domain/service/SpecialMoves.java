package org.example.domain.service;

import org.example.domain.board.Piece;
import org.example.domain.board.PieceColor;
import org.example.domain.board.logger.LoggerMove;
import org.example.dto.PositionDto;

import java.util.ArrayList;
import java.util.List;

public class SpecialMoves {

    public void isCastingPossible(){

    }

    public void isPawnPromotionPossible(){

    }

    public List<PositionDto> isEnPassantPossible(List<LoggerMove> moves, Piece[][] matrix, PieceColor turn){
        /*
        * if the last movement was a pawn move of two squares that ended next to a pawn of the other player
        * this player can move to square that its diagonally behind the other opponent piece
        * */
        List<PositionDto> positions = new ArrayList<>();
        if (moves.isEmpty()) return positions;
        LoggerMove move = moves.get(moves.size()-1); // we get the last movement
        if (!move.getOriginPiece().equals("Peon"))return positions;
        int positionsMoved = Math.abs(move.getMoveDto().getOriginX() - move.getMoveDto().getDestinationX());
        if (positionsMoved != 2 )return positions; // if it didnt move 2 positions we return

        int Yposition = 1;
        // now we check if there is any pawn on the left or right
        for (int i = 0; i<2;i++){
            if (move.getMoveDto().getDestinationY()==7) Yposition = -1;
            if (move.getMoveDto().getDestinationY()==0) Yposition = 1;
            Piece piece = matrix[move.getMoveDto().getDestinationX()][move.getMoveDto().getDestinationY()+Yposition];
            if (piece != null && piece.getClass().getSimpleName().equals("Peon") && piece.getColor() == turn){
                // there is a pawn at the left or right, so  there is a possibility for the  pawn to eat
                PositionDto positionDto = null;
                if (piece.getColor().equals(PieceColor.BLACK )) positionDto = new PositionDto(move.getMoveDto().getDestinationX()+1,move.getMoveDto().getDestinationY());
                else positionDto = new PositionDto(move.getMoveDto().getDestinationX()-1,move.getMoveDto().getDestinationY()); // this is the position where the opponent pawn can go
                positions.add(positionDto);
                System.out.println(positionDto);
            }
            Yposition = -1;
        }
        return positions;
    }



}
