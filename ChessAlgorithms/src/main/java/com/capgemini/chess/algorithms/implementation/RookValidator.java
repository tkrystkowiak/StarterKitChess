package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class RookValidator extends Validator {

	public RookValidator(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		super(board, present, next);
	}
	
	public RookValidator(Board board, Coordinate present) throws InvalidMoveException {
		super(board, present);
	}

	public boolean checkPossibleMoves() {
		possibleMoves = new ArrayList<>();
		for (int i = pY + 1; i < 8; i++) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pY - 1; i > -1; i--) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX + 1; i < 8; i++) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX - 1; i > -1; i--) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		return true;

	}

}
