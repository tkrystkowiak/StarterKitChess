package com.capgemini.chess.algorithms.implementation;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedPathException;
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

	public Move validate(Board board, Coordinate present, Coordinate next)
			throws OutOfPieceRangeException, OccupiedCoordinatesException, OccupiedPathException {
		this.board = board;
		piece = board.getPieceAt(present);
		move.setFrom(present);
		move.setTo(next);
		move.setMovedPiece(piece);
		switch (piece.getType()) {
		case PAWN:
			validatePawn();
			break;
		case KNIGHT:
			validateKnight();
			break;
		case ROOK:
			validateRook();
			break;
		case KING:
			validateKing();
			break;
			
		}

		return move;
	}

	private boolean validatePawn() throws OutOfPieceRangeException, OccupiedCoordinatesException {
		boolean isFirstMove = false;

		if (nY <= pY || abs(nX - pX) > 1 || nY - pY > 2) {
			throw new OutOfPieceRangeException();
		}
		if (pY == 1 && piece.getColor() == Color.WHITE || pY == 6 && piece.getColor() == Color.BLACK) {
			isFirstMove = true;
		}
		if (board.getPieceAt(new Coordinate(pX, pY + 1)) != null) {
			throw new OccupiedCoordinatesException();
		}
		if (nY - pY == 2) {
			if (!isFirstMove) {
				throw new OutOfPieceRangeException();
			}
			if (Math.abs(nX - pX) > 0) {
				throw new OutOfPieceRangeException();
			}
			if (board.getPieceAt(next) != null) {
				// Tu jest problem bo moze byc pionek przeciwnika
				throw new OccupiedCoordinatesException();
			}
		}
		if ((nX - pX) == 1) {

			if (!isOccupied(next)) {
				throw new OutOfPieceRangeException();
			}
			if (board.getPieceAt(next).getColor() != board.getPieceAt(present).getColor()) {
				throw new OutOfPieceRangeException();
			}
			move.setType(MoveType.CAPTURE);
			return true;
		}
		if (isOccupied(next)) {
			throw new OccupiedCoordinatesException();
		}
		move.setType(MoveType.ATTACK);
		return true;
	}

	private boolean validateKnight() throws OutOfPieceRangeException, OccupiedCoordinatesException {
		if (abs(nX - pX) == 2 && abs(nY - pY) == 1 || abs(nX - pX) == 1 && abs(nY - pY) == 2) {
			if (isOccupied(next)) {
				if (isOccupiedByEnemy(next)) {
					move.setType(MoveType.CAPTURE);
					return true;
				}
				throw new OccupiedCoordinatesException();
			}
			move.setType(MoveType.ATTACK);
			return true;
		}
		throw new OutOfPieceRangeException();
	}

	private boolean validateRook() throws OccupiedPathException, OccupiedCoordinatesException {
		if ((abs(nX - pX) > 0 && nY == pY || (abs(nY - pY) > 0) && nX == pX)) {
			if (nY == pY) {
				if (pX < nX) {
					for (int i = pX; i < nX; i++) {
						if (isOccupied(new Coordinate(i, nY))) {
							throw new OccupiedPathException();
						}
					}
					if(isOccupied(next)){
						if(isOccupiedByEnemy(next)){
							move.setType(MoveType.CAPTURE);
							return true;
						}
						move.setType(MoveType.ATTACK);
						return true;
					}
					throw new OccupiedCoordinatesException();
				} else {
					for (int i = pX; i > nX; i--) {
						if(isOccupied(new Coordinate(i, nY))){
							throw new OccupiedPathException();
						}
					}
				}
				if(isOccupied(next)){
					if(isOccupiedByEnemy(next)){
						move.setType(MoveType.CAPTURE);
						return true;
					}
					move.setType(MoveType.ATTACK);
					return true;
				}
				throw new OccupiedCoordinatesException();
			}
		}
		return false;
	}
	
	private boolean validateKing() throws OccupiedCoordinatesException{
		if(abs(pX-nX)<=1 && abs(pX-nX)<=1){
			if(isOccupied(next)){
				if(isOccupiedByEnemy(next)){
					move.setType(MoveType.CAPTURE);
					return true;
				}
				move.setType(MoveType.ATTACK);
				return true;
			}
			throw new OccupiedCoordinatesException();
		}
		return false;
	}
	
	private boolean validateBishop() throws OccupiedCoordinatesException{
		List<Coordinate> possibleMoves = new ArrayList();
		if(abs(pX-nX)==abs(pY-nY)){
			if(isOccupied(next)){
				if(isOccupiedByEnemy(next)){
					move.setType(MoveType.CAPTURE);
					return true;
				}
				move.setType(MoveType.ATTACK);
				return true;
			}
			throw new OccupiedCoordinatesException();
		}
		return false;
	}

	private boolean isOccupied(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		return true;
	}

	private boolean isOccupiedByEnemy(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p.getColor() == piece.getColor()) {
			return false;
		}
		return true;
	}

}