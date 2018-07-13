package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class MoveValidator {
	
	private Board board;
	private Coordinate present;
	private Coordinate next;
	private int pX = present.getX();
	private int pY = present.getY();
	private int nX = present.getX();
	private int nY = present.getY();
	private Piece piece;
	private Move move;
	
	public Move validate(Board board, Coordinate present, Coordinate next) throws OutOfPieceRangeException, OccupiedCoordinatesException{
		this.board = board;
		piece = board.getPieceAt(present);
		move.setFrom(present);
		move.setTo(next);
		move.setMovedPiece(piece);
		validatePawn();
		return move;
	}
	
	private boolean validatePawn() throws OutOfPieceRangeException, OccupiedCoordinatesException{
		Piece piece = board.getPieceAt(present);
		boolean isFirstMove = false;
		
		
		if(nY <= pY||Math.abs(nX-pX)>1||nY-pY>2){
			throw new OutOfPieceRangeException();
		}
		if(pY == 1 && piece.getColor()== Color.WHITE||pY == 6 && piece.getColor()== Color.BLACK){
			isFirstMove = true;
		}
		if(board.getPieceAt(new Coordinate(pX,pY+1))!=null){
			throw new OccupiedCoordinatesException();
		}
		if(nY-pY==2){
			if(!isFirstMove){
			throw new OutOfPieceRangeException();
			}
			if(Math.abs(nX-pX)>0){
			throw new OutOfPieceRangeException();
			}
			if(board.getPieceAt(next)!=null){
			//Tu jest problem bo moze byc pionek przeciwnika
			throw new OccupiedCoordinatesException();
			}
		}
		if((nX-pX)==1){
			
			if(board.getPieceAt(next)==null){
				throw new OutOfPieceRangeException();
			}
			if(board.getPieceAt(next).getColor()!=board.getPieceAt(present).getColor()){
				throw new OutOfPieceRangeException();
			}
			move.setType(MoveType.CAPTURE);
			return true;
		}
		if(board.getPieceAt(next)!=null){
			throw new OccupiedCoordinatesException();
		}
		move.setType(MoveType.ATTACK);
		return true;
	}
	
}