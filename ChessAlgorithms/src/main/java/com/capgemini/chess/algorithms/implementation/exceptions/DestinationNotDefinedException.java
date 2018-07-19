package com.capgemini.chess.algorithms.implementation.exceptions;

public class DestinationNotDefinedException extends InvalidMoveException {

	public DestinationNotDefinedException() {
		super("Destination is not Defined");
	}

}
