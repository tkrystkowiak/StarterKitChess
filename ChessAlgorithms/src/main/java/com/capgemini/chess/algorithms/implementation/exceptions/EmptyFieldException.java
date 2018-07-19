package com.capgemini.chess.algorithms.implementation.exceptions;

public class EmptyFieldException extends InvalidMoveException {
	
	public EmptyFieldException() {
		super("This field is empty!");
	}
	
}
