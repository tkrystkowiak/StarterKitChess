package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class ValidatorTest {

	@Test
	public void testIsFactoryCreatingProperValidators() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		ValidatorFactory validatorFactory = new ValidatorFactory();

		// when
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(0, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 1));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(0, 2));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(0, 3));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(0, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(0, 5));

		Validator validatorPawn = validatorFactory.getValidator(board, new Coordinate(0, 0));
		Validator validatorRook = validatorFactory.getValidator(board, new Coordinate(0, 1));
		Validator validatorKnight = validatorFactory.getValidator(board, new Coordinate(0, 2));
		Validator validatorBishop = validatorFactory.getValidator(board, new Coordinate(0, 3));
		Validator validatorQueen = validatorFactory.getValidator(board, new Coordinate(0, 4));
		Validator validatorKing = validatorFactory.getValidator(board, new Coordinate(0, 5));

		// then
		assertTrue(validatorPawn instanceof PawnValidator);
		assertTrue(validatorRook instanceof RookValidator);
		assertTrue(validatorKnight instanceof KnightValidator);
		assertTrue(validatorBishop instanceof BishopValidator);
		assertTrue(validatorQueen instanceof QueenValidator);
		assertTrue(validatorKing instanceof KingValidator);

	}

	@Test
	public void testPawnValidatorShouldShowPossibleMoves() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		ValidatorFactory validatorFactory = new ValidatorFactory();

		// when
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(0, 1));
		Validator validator = validatorFactory.getValidator(board, new Coordinate(0, 1));

		// then
		assertTrue(validator.isAnyMovePossible());
	}

	@Test
	public void testPawnValidatorShouldNotShowAnyPossibleMoves() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		ValidatorFactory validatorFactory = new ValidatorFactory();

		// when
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(0, 1));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(0, 2));
		Validator validator = validatorFactory.getValidator(board, new Coordinate(0, 1));

		// then
		assertFalse(validator.isAnyMovePossible());
	}

}
