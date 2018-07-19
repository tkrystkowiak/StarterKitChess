package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class BoardTest {

	@Test
	public void testShouldReturnCorrectKingCoordinates() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate expected = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.WHITE_KING, expected);
		Coordinate actual = board.findKing(Color.WHITE);
		// then
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnNoCoordinates() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.BLACK_KING, c);
		Coordinate actual = board.findKing(Color.WHITE);
		// then
		assertEquals(null, actual);
	}

	@Test
	public void testShouldReturnFieldIsOccupied() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.BLACK_KING, c);
		// then
		assertTrue(board.isOccupied(c));
	}

	@Test
	public void testShouldReturnFieldIsNotOccupied() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);

		// when
		Coordinate c = new Coordinate(5, 5);

		// then
		assertFalse(board.isOccupied(c));
	}

	@Test
	public void testShouldReturnFieldIsOccupiedByOwn() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Piece p = Piece.WHITE_KING;
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.WHITE_PAWN, c);
		// then
		assertTrue(board.isOccupiedByOwn(c, p));
	}

	@Test
	public void testShouldReturnFieldIsNotOccupiedByOwn() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Piece p = Piece.WHITE_KING;
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.BLACK_PAWN, c);
		// then
		assertFalse(board.isOccupiedByOwn(c, p));
	}

	@Test
	public void testShouldReturnFieldIsOccupiedByEnemy() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Piece p = Piece.WHITE_KING;
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.BLACK_PAWN, c);
		// then
		assertTrue(board.isOccupiedByEnemy(c, p));
	}

	@Test
	public void testShouldReturnFieldIsNotOccupiedByEnemy() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Piece p = Piece.WHITE_KING;
		Coordinate c = new Coordinate(5, 5);

		// when
		board.setPieceAt(Piece.WHITE_PAWN, c);
		// then
		assertFalse(board.isOccupiedByEnemy(c, p));
	}

}
