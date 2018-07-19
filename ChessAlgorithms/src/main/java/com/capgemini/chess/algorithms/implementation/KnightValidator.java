package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class KnightValidator extends Validator {

	public KnightValidator(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		super(board, present, next);
	}

	public KnightValidator(Board board, Coordinate present) throws InvalidMoveException {
		super(board, present);
	}

	public boolean checkPossibleMoves() {
		possibleMoves = new ArrayList<>();
		for (int x = pX - 2; x < pX + 3; x = x + 4) {
			for (int y = pY - 1; y < pY + 2; y = y + 2) {
				Coordinate c = new Coordinate(x, y);
				if (c.isOnBoard()) {
					possibleMoves.add(c);
				}
			}
		}
		for (int y = pY - 2; y < pY + 3; y = y + 4) {
			for (int x = pX - 1; x < pX + 2; x = x + 2) {
				Coordinate c = new Coordinate(x, y);
				if (c.isOnBoard()) {
					possibleMoves.add(c);
				}
			}
		}
		return true;

	}

}
