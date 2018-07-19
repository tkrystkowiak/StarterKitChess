package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.DestinationNotDefinedException;
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
		if (next == null) {
			throw new DestinationNotDefinedException();
		}
		if (piece == null) {
			throw new EmptyFieldException();
		}
		move = new Move();
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
		Iterator<Coordinate> itr = possibleMoves.iterator();
		List<Coordinate> toRemove = new ArrayList<Coordinate>();
		while (itr.hasNext()) {
			Coordinate c = itr.next();
			if (board.isOccupiedByOwn(c, piece)) {
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
		if (board.isOccupiedByOwn(next, piece)) {
			throw new OccupiedCoordinatesException();
		}
		if (board.isOccupiedByEnemy(next, piece)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		move.setType(MoveType.ATTACK);
		return true;
	}

	public boolean isInRange(ArrayList<Coordinate> moves) {
		Iterator<Coordinate> itr = moves.iterator();
		while (itr.hasNext()) {
			Coordinate c = itr.next();
			if (c.getX() == nX && c.getY() == nY) {
				return true;
			}
		}
		return false;
	}

}
