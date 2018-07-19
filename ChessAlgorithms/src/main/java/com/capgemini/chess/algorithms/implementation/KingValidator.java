package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class KingValidator extends Validator {

	public KingValidator(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		super(board, present, next);
	}

	public KingValidator(Board board, Coordinate present) throws InvalidMoveException {
		super(board, present);
	}

	@Override
	public boolean checkPossibleMoves() {
		possibleMoves = new ArrayList<>();
		for (int x = pX - 1; x < pX + 2; x++) {
			for (int y = pY - 1; y < pY + 2; y++) {
				Coordinate c = new Coordinate(x, y);
				if (isCoordinateOnBoard(c) && (x != pX || y != pY)) {
					possibleMoves.add(c);
				}
			}
		}

		if (piece.getColor() == Color.WHITE && pX == 4 && pY == 0 && !wasPiecedMovedFromField(new Coordinate(pX, pY))) {
			Piece leftRook = board.getPieceAt(new Coordinate(0, 0));
			Piece rightRook = board.getPieceAt(new Coordinate(7, 0));
			if (leftRook != null && leftRook.getType() == PieceType.ROOK && leftRook.getColor() == Color.WHITE
					&& !wasPiecedMovedFromField(new Coordinate(0, 0))) {
				boolean isPathEmpty = true;
				for (int i = pX - 1; i > 0; i--) {
					if (isOccupied(new Coordinate(i, pY))) {
						isPathEmpty = false;
						break;
					}
				}
				if (isPathEmpty) {
					boolean isPathClear = true;
					for (int i = pX - 1; i < pX - 3; i--) {
						Coordinate castlingPath = new Coordinate(i, pY);
						if (isFieldUnderAttack(castlingPath)) {
							isPathClear = false;
							break;
						}
					}
					if (isPathClear) {
						possibleMoves.add(new Coordinate(pX - 2, pY));
					}
				}
			}
			if (rightRook != null && rightRook.getType() == PieceType.ROOK && rightRook.getColor() == Color.WHITE
					&& !wasPiecedMovedFromField(new Coordinate(0, 7))) {
				boolean isPathEmpty = true;
				for (int i = pX + 1; i < 7; i++) {
					if (isOccupied(new Coordinate(i, pY))) {
						isPathEmpty = false;
						break;
					}
				}
				if (isPathEmpty) {
					boolean isPathClear = true;
					for (int i = pX + 1; i < pX + 3; i++) {
						Coordinate castlingPath = new Coordinate(i, pY);
						if (isFieldUnderAttack(castlingPath)) {
							isPathClear = false;
							break;
						}
					}
					if (isPathClear) {
						possibleMoves.add(new Coordinate(pX + 2, pY));
					}
				}
			}
		}
		if (piece.getColor() == Color.BLACK && pX == 4 && pY == 7 && !wasPiecedMovedFromField(new Coordinate(pX, pY))) {
			Piece leftRook = board.getPieceAt(new Coordinate(0, 7));
			Piece rightRook = board.getPieceAt(new Coordinate(7, 7));
			if (leftRook != null && leftRook.getType() == PieceType.ROOK && leftRook.getColor() == Color.BLACK
					&& !wasPiecedMovedFromField(new Coordinate(0, 7))) {
				boolean isPathEmpty = true;
				for (int i = pX - 1; i > 0; i--) {
					if (isOccupied(new Coordinate(i, pY))) {
						isPathEmpty = false;
						break;
					}
				}
				if (isPathEmpty) {
					boolean isPathClear = true;
					for (int i = pX - 1; i < pX - 3; i--) {
						Coordinate castlingPath = new Coordinate(i, pY);
						if (isFieldUnderAttack(castlingPath)) {
							isPathClear = false;
							break;
						}
					}
					if (isPathClear) {
						possibleMoves.add(new Coordinate(pX - 2, pY));
					}
				}
			}
			if (rightRook != null && rightRook.getType() == PieceType.ROOK && rightRook.getColor() == Color.BLACK
					&& !wasPiecedMovedFromField(new Coordinate(7, 7))) {
				boolean isPathEmpty = true;
				for (int i = pX + 1; i < 7; i++) {
					if (isOccupied(new Coordinate(i, pY))) {
						isPathEmpty = false;
						break;
					}
				}
				if (isPathEmpty) {
					boolean isPathClear = true;
					for (int i = pX + 1; i < pX + 3; i++) {
						Coordinate castlingPath = new Coordinate(i, pY);
						if (isFieldUnderAttack(castlingPath)) {
							isPathClear = false;
							break;
						}
					}
					if (isPathClear) {
						possibleMoves.add(new Coordinate(pX + 2, pY));
					}
				}
			}
		}
		return true;
	}

	public boolean wasPiecedMovedFromField(Coordinate c) {
		List<Move> moveHistory = board.getMoveHistory();
		int fieldX = c.getX();
		int fieldY = c.getY();
		Iterator itr = moveHistory.iterator();
		while (itr.hasNext()) {
			Move move = (Move) itr.next();
			int moveX = move.getFrom().getX();
			int moveY = move.getFrom().getY();
			if (fieldX == moveX && fieldY == moveY) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkConditions() throws InvalidMoveException {
		if (!isInRange(possibleMoves)) {
			throw new OutOfPieceRangeException();
		}
		if (isOccupiedByOwn(next)) {
			throw new OccupiedCoordinatesException();
		}
		if (isOccupiedByEnemy(next)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		if (Math.abs(pX - nX) == 2) {
			move.setType(MoveType.CASTLING);
			return true;
		}
		move.setType(MoveType.ATTACK);
		return true;
	}

	private boolean isFieldUnderAttack(Coordinate field) {
		ValidatorFactory vF = new ValidatorFactory();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Coordinate c = new Coordinate(x, y);
				Piece attackingPiece = board.getPieceAt(c);
				if (attackingPiece != null) {
					if (attackingPiece.getColor() != piece.getColor()) {
						try {
							vF.getValidator(board, c, field).validate();
							return true;
						} catch (InvalidMoveException e) {
						}
					}
				}
			}
		}
		return false;
	}

}
