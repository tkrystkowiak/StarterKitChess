package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.AnotherPlayerTurnException;
import com.capgemini.chess.algorithms.implementation.exceptions.DestinationNotDefinedException;
import com.capgemini.chess.algorithms.implementation.exceptions.EmptyFieldException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfBoardException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class ExceptionsTest {

	@Test
	public void testIsWrongTurnExceptionThrown() {
		// given
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		List<Move> moveList = board.getMoveHistory();
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(0, 6), new Coordinate(0, 4));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof AnotherPlayerTurnException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsEmptyFieldExceptionThrown() {
		// given
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		List<Move> moveList = board.getMoveHistory();
		boolean isThrown = false;

		// when
		try {
			boardManager.performMove(new Coordinate(0, 3), new Coordinate(0, 4));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof EmptyFieldException;
		}

		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsOccupiedCoordinatesExceptionThrown() {
		// given
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		List<Move> moveList = board.getMoveHistory();
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(0, 3), new Coordinate(0, 4));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof EmptyFieldException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsOutOfPieceRangeThrownIfPathIsBlocked() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);

		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(0, 4));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 7));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof OutOfPieceRangeException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsOutOfPieceRangeThrownIfPieceCantMoveThatWay() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);

		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(7, 7));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof OutOfPieceRangeException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsOutOfBoardExceptionThrownIfStartingCoordinatesAreInvalid() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(-5, 0), new Coordinate(7, 7));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof OutOfBoardException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testIsOutOfBoardExceptionThrownIfFinalCoordinatesAreInvalid() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		boolean isThrown = false;
		// when
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 8));
		} catch (InvalidMoveException e) {
			isThrown = e instanceof OutOfBoardException;
		}
		// then
		assertTrue(isThrown);
	}

	@Test
	public void testShouldThrowDestinationIsNotDefinedForValidator() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		boolean isThrown = false;
		ValidatorFactory validatorFactory = new ValidatorFactory();
		Validator validator = validatorFactory.getValidator(board, new Coordinate(0, 0));
		// when
		try {
			validator.validate();
		} catch (InvalidMoveException e) {
			isThrown = e instanceof DestinationNotDefinedException;
		}
		// then
		assertTrue(isThrown);
	}

}
