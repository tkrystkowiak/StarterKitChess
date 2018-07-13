package com.capgemini.chess.algorithms.implementation.exceptions;

public class OutOfPieceRangeException extends InvalidMoveException {
	
	public OutOfPieceRangeException() {
		super("This move is out of piece range!");
		
	}

}
