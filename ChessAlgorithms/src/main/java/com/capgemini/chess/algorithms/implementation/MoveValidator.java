package com.capgemini.chess.algorithms.implementation;

import static java.lang.Math.*;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.EmptyFieldException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedPathException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfBoardException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class MoveValidator {

	private Board board;
	private Coordinate present;
	private Coordinate next;
	private int pX;
	private int pY;
	private int nX;
	private int nY;
	private Piece piece;
	private Move move;

	public Move validate(Board board, Coordinate present, Coordinate next)
			throws InvalidMoveException {
		this.board = board;
		this.present = present;
		this.next = next;
		move = new Move();
		isCoordinateOnBoard(present);
		isCoordinateOnBoard(next);
		piece = board.getPieceAt(present);
		if(piece==null){
			throw new EmptyFieldException();
		}
		move.setFrom(present);
		move.setTo(next);
		move.setMovedPiece(piece);
		pX = present.getX();
		pY = present.getY();
		nX = next.getX();
		nY = next.getY();	
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
		case BISHOP:
			validateBishop();
			break;
		case QUEEN:
			validateQueen();
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
		int test = abs(nX - pX);
		if (abs(nX - pX) > 0 && nY == pY || abs(nY - pY) > 0 && nX == pX) {
			if(isPathOccupiedInStraightLine()){
				throw new OccupiedPathException();
			}
			if (!isOccupied(next)) {
				move.setType(MoveType.ATTACK);
				return true;
			}
		}
		return false;
	}

	private boolean validateKing() throws OccupiedCoordinatesException, OutOfPieceRangeException {
		if (abs(pX - nX) <= 1 && abs(pY - nY) <= 1) {
			if (!isOccupied(next)) {
				move.setType(MoveType.ATTACK);
				return true;
			}
			throw new OccupiedCoordinatesException();
		}
		throw new OutOfPieceRangeException();
	}

	private boolean validateBishop() throws InvalidMoveException {
		if (abs(pX - nX) == abs(pY - nY)) {
			if(isPathOccupiedInDiagonalLine()){
				throw new OccupiedPathException();
			}
			if (!isOccupied(next)) {
				move.setType(MoveType.ATTACK);
				return true;
			}
		}
		throw new OutOfPieceRangeException();
	}

	private boolean validateQueen() throws OccupiedCoordinatesException, OccupiedPathException {
		if (abs(pX - nX) == abs(pY - nY)) {
			if(isPathOccupiedInDiagonalLine()){
				throw new OccupiedPathException();
			}
			if (!isOccupied(next)) {
				move.setType(MoveType.ATTACK);
				return true;
			}
		}
		if(nX - pX == 0 || nY - pY == 0) {
			if(isPathOccupiedInStraightLine()){
				throw new OccupiedPathException();
			}
			if (!isOccupied(next)) {
				move.setType(MoveType.ATTACK);
				return true;
			}
		}
		return false;
	}
	
	private boolean isOccupied(Coordinate c) throws OccupiedCoordinatesException {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (isOccupiedByEnemy(c)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		throw new OccupiedCoordinatesException();
	}

	private boolean isOccupiedByEnemy(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p.getColor() == piece.getColor()) {
			return false;
		}
		return true;
	}
	
	private boolean isCoordinateOnBoard(Coordinate c) throws InvalidMoveException{
		if(c.getX()>7||c.getX()<0||c.getY()>7||c.getY()<0){
			throw new OutOfBoardException();
		}
		return true;
	}

	private boolean isPathOccupiedInStraightLine() {
		if (pX == nX) {
			if (nY > pY) {
				for (int i = pY + 1; i < nY; i++) {
					if (board.getPieceAt(new Coordinate(pX, i)) != null) {
						return true;
					}
				}
			}
			for (int i = pY - 1; i > nY; i--) {
				if (board.getPieceAt(new Coordinate(pX, i)) != null) {
					return true;
				}
			}
		}
		if (pY == nY) {
			if (nX > pX) {
				for (int i = pX + 1; i < nX; i++) {
					if (board.getPieceAt(new Coordinate(i, pY)) != null) {
						return true;
					}
				}
			}
			for (int i = pX - 1; i > nX; i--) {
				if (board.getPieceAt(new Coordinate(i, pY)) != null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isPathOccupiedInDiagonalLine() {
		//Right-up quarter
		if (pX - nX < 0 && pY - nY < 0) {
			for (int i = 1; pX + i < nX && pY + i < nY; i++) {
				if (board.getPieceAt(new Coordinate(pX + i, pY + i)) != null) {
					return true;
				}
			}
		}
		//Right-down quarter
		if (pX - nX < 0 && pY - nY > 0) {
			for (int i = 1; pX - i > nX && pY + i < nY; i++) {
				if (board.getPieceAt(new Coordinate(pX + i, pY - i)) != null) {
					return true;
				}
			}
		}
		//Left-up quarter
		if (pX - nX > 0 && pY - nY < 0) {
			for (int i = 1; pX - i < nX && pY + i < nY; i++) {
				if (board.getPieceAt(new Coordinate(pX - i, pY + i)) != null) {
					return true;
				}
			}
		}
		//Left-down quarter
		if (pX - nX < 0 && pY - nY < 0) {
			for (int i = 1; pX - i < nX && pY - i < nY; i++) {
				if (board.getPieceAt(new Coordinate(pX - i, pY - i)) != null) {
					return true;
				}
			}
		}
		return false;
	}

}