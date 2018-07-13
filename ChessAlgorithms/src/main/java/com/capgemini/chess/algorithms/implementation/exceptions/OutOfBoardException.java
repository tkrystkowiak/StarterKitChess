package com.capgemini.chess.algorithms.implementation.exceptions;

public class OutOfBoardException extends InvalidMoveException {

	public OutOfBoardException() {
		super("This coordinates are out of board!");
	}
	
}
