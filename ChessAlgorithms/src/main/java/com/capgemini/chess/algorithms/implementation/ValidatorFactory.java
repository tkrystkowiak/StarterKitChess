package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class ValidatorFactory {
	
	public Validator getValidator(Board board, Coordinate from, Coordinate to) throws InvalidMoveException{
		Piece piece = board.getPieceAt(from);
		Validator validator = null;
		switch (piece.getType()) {
		case PAWN:
			validator = new PawnValidator(board,from,to);
			break;
		case KNIGHT:
			validator = new KnightValidator(board,from,to);
			break;
		case ROOK:
			validator = new RookValidator(board,from,to);
			break;
		case KING:
			validator = new KingValidator(board,from,to);
			break;
		case BISHOP:
			validator = new BishopValidator(board,from,to);
			break;
		case QUEEN:
			validator = new QueenValidator(board,from,to);
			break;
		}
		return validator;
	}
	
	public Validator getValidator(Board board, Coordinate from) throws InvalidMoveException{
		Piece piece = board.getPieceAt(from);
		Validator validator = null;
		switch (piece.getType()) {
		case PAWN:
			validator = new PawnValidator(board,from);
			break;
		case KNIGHT:
			validator = new KnightValidator(board,from);
			break;
		case ROOK:
			validator = new RookValidator(board,from);
			break;
		case KING:
			validator = new KingValidator(board,from);
			break;
		case BISHOP:
			validator = new BishopValidator(board,from);
			break;
		case QUEEN:
			validator = new QueenValidator(board,from);
			break;
		}
		return validator;
	}
	
}
