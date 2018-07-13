package com.capgemini.chess.algorithms.implementation.exceptions;

public class OccupiedPathException extends InvalidMoveException {

	public OccupiedPathException() {
		super("The path to the destination is occupied!");
	}
}
