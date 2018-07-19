package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.EmptyFieldException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public abstract class Validator {

	public Board board;
	public Coordinate checked;
	public Coordinate present;
	public Coordinate next;
	public int pX;
	public int pY;
	public int nX;
	public int nY;
	public Piece piece;
	public Move move;
	public ArrayList<Coordinate> possibleMoves;

	public Validator(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		this.board = board;
		this.present = present;
		this.next = next;
		piece = board.getPieceAt(present);
		pX = present.getX();
		pY = present.getY();
		nX = next.getX();
		nY = next.getY();
	}

	public Validator(Board board, Coordinate present) {
		this.board = board;
		this.present = present;
		piece = board.getPieceAt(present);
		pX = present.getX();
		pY = present.getY();
	}

	public Move validate() throws InvalidMoveException {
		move = new Move();

		if (piece == null) {
			throw new EmptyFieldException();
		}
		move.setFrom(present);
		move.setTo(next);
		move.setMovedPiece(piece);
		checkPossibleMoves();
		checkConditions();
		return move;
	}

	public boolean checkPossibleMoves() {
		return false;
	}

	public ArrayList<Coordinate> getPossibleMoves() {
		checkPossibleMoves();
		ArrayList<Coordinate> toReturn = (ArrayList<Coordinate>) possibleMoves.clone();
		return toReturn;
	}

	public boolean isAnyMovePossible() {
		checkPossibleMoves();
		Iterator itr = possibleMoves.iterator();
		List<Coordinate> toRemove = new ArrayList<Coordinate>();
		while (itr.hasNext()) {
			Coordinate c = (Coordinate) itr.next();
			if (isOccupiedByOwn(c)) {
				toRemove.add(c);
			}
		}
		possibleMoves.removeAll(toRemove);
		if (possibleMoves.size() > 0) {
			return true;
		}
		return false;

	}

	public boolean checkConditions() throws InvalidMoveException {
		if (!isInRange(possibleMoves)) {
			throw new OutOfPieceRangeException();
		}
		if (isOccupiedByOwn(next)) {
			throw new OccupiedCoordinatesException();
		}
		if (isOccupiedByEnemy(next)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		move.setType(MoveType.ATTACK);
		return true;
	}

	public boolean isOccupiedByOwn(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (p.getColor() == piece.getColor()) {
			return true;
		}
		return false;

	}

	public boolean isOccupied(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		return true;
	}

	public boolean isOccupiedByEnemy(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (p.getColor() == piece.getColor()) {
			return false;
		}
		return true;
	}

	public boolean isCoordinateOnBoard(Coordinate c) {
		if (c.getX() > 7 || c.getX() < 0 || c.getY() > 7 || c.getY() < 0) {
			return false;
		}
		return true;
	}

	public boolean isInRange(ArrayList<Coordinate> moves) {
		Iterator itr = moves.iterator();
		while (itr.hasNext()) {
			Coordinate c = (Coordinate) itr.next();
			if (c.getX() == nX && c.getY() == nY) {
				return true;
			}
		}
		return false;
	}

}
