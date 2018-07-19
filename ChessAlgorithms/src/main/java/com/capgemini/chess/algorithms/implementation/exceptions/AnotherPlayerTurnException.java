package com.capgemini.chess.algorithms.implementation.exceptions;

public class AnotherPlayerTurnException extends InvalidMoveException {
	
	public AnotherPlayerTurnException(){
		super("This is not this player's turn!");
	}

}
