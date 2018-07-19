package com.capgemini.chess.algorithms.data.generated;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;

/**
 * Board representation. Board objects are generated based on move history.
 * 
 * @author Michal Bejm
 *
 */
public class Board {

	public static final int SIZE = 8;

	private Piece[][] pieces = new Piece[SIZE][SIZE];
	private List<Move> moveHistory = new ArrayList<>();
	private BoardState state;

	public Board() {
	}

	public Board(Board clonedBoard) {
		Piece[][] clonedPieces = clonedBoard.getPieces();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				this.pieces[x][y] = clonedPieces[x][y];
			}
		}
		List<Move> clonedHistory = clonedBoard.getMoveHistory();
		for (Move m : clonedHistory)
			moveHistory.add(m);
		this.state = clonedBoard.getState();
	}

	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public BoardState getState() {
		return state;
	}

	public void setState(BoardState state) {
		this.state = state;
	}

	public Coordinate findKing(Color kingColor) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Coordinate c = new Coordinate(x, y);
				Piece piece = getPieceAt(c);
				if (piece != null) {
					if (piece.getType() == PieceType.KING && piece.getColor() == kingColor) {
						return c;
					}
				}
			}
		}
		return null;
	}

	public boolean isOccupied(Coordinate c) {
		Piece p = getPieceAt(c);
		if (p == null) {
			return false;
		}
		return true;
	}

	public boolean isOccupiedByOwn(Coordinate c, Piece ownPiece) {
		if (!isOccupied(c)) {
			return false;
		}
		Piece p = getPieceAt(c);

		if (p.getColor() == ownPiece.getColor()) {
			return true;
		}
		return false;
	}

	public boolean isOccupiedByEnemy(Coordinate c, Piece ownPiece) {
		Piece p = getPieceAt(c);
		if (!isOccupied(c)) {
			return false;
		}
		if (p.getColor() == ownPiece.getColor()) {
			return false;
		}
		return true;
	}

	/**
	 * Sets chess piece on board based on given coordinates
	 * 
	 * @param piece
	 *            chess piece
	 * @param board
	 *            chess board
	 * @param coordinate
	 *            given coordinates
	 */
	public void setPieceAt(Piece piece, Coordinate coordinate) {
		pieces[coordinate.getX()][coordinate.getY()] = piece;
	}

	/**
	 * Gets chess piece from board based on given coordinates
	 * 
	 * @param coordinate
	 *            given coordinates
	 * @return chess piece
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return pieces[coordinate.getX()][coordinate.getY()];
	}
}
