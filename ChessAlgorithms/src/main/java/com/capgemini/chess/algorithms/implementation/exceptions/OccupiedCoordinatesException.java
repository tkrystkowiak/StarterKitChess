package com.capgemini.chess.algorithms.implementation.exceptions;

public class OccupiedCoordinatesException extends InvalidMoveException {
	
	public OccupiedCoordinatesException() {
		super("This coordinates are occupied by the same colored piece!");
	}
	
}
