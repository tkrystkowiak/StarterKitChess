package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class PawnValidator extends Validator {

	public PawnValidator(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		super(board, present, next);
	}

	public PawnValidator(Board board, Coordinate present) throws InvalidMoveException {
		super(board, present);
	}

	@Override
	public boolean checkPossibleMoves() {
		possibleMoves = new ArrayList<>();
		boolean isFirstMove = false;

		if (pY == 1 && piece.getColor() == Color.WHITE || pY == 6 && piece.getColor() == Color.BLACK) {
			isFirstMove = true;
		}

		if (piece.getColor() == Color.BLACK) {
			Coordinate a = new Coordinate(pX, pY - 1);
			if (a.isOnBoard() && board.getPieceAt(a) == null) {
				possibleMoves.add(a);
				Coordinate b = new Coordinate(pX, pY - 2);
				if (isFirstMove && board.getPieceAt(b) == null) {
					possibleMoves.add(b);
				}
			}
			Coordinate c = new Coordinate(pX - 1, pY - 1);
			if (c.isOnBoard() && board.isOccupiedByEnemy(c, piece)) {
				possibleMoves.add(c);
			}
			Coordinate d = new Coordinate(pX + 1, pY - 1);
			if (d.isOnBoard() && board.isOccupiedByEnemy(d, piece)) {
				possibleMoves.add(d);
			}
		}
		if (piece.getColor() == Color.WHITE) {
			Coordinate a = new Coordinate(pX, pY + 1);
			if (a.isOnBoard() && board.getPieceAt(a) == null) {
				possibleMoves.add(a);
				Coordinate b = new Coordinate(pX, pY + 2);
				if (isFirstMove && board.getPieceAt(b) == null) {
					possibleMoves.add(b);
				}
			}
			Coordinate c = new Coordinate(pX - 1, pY + 1);
			if (c.isOnBoard() && board.isOccupiedByEnemy(c, piece)) {
				possibleMoves.add(c);
			}
			Coordinate d = new Coordinate(pX + 1, pY + 1);
			if (d.isOnBoard() && board.isOccupiedByEnemy(d, piece)) {
				possibleMoves.add(d);
			}
		}
		List<Move> moveHistory = board.getMoveHistory();
		if (!moveHistory.isEmpty()) {
			Move lastMove = moveHistory.get(moveHistory.size() - 1);
			if (lastMove.getMovedPiece().getType() == PieceType.PAWN) {
				int lpX = lastMove.getFrom().getX();
				int lpY = lastMove.getFrom().getY();
				int lnX = lastMove.getTo().getX();
				int lnY = lastMove.getTo().getY();
				if (piece.getColor() == Color.WHITE) {
					if (lpX == pX + 1 && lpY == pY + 2) {
						if (lnX == pX + 1 && lnY == pY) {
							possibleMoves.add(new Coordinate(pX + 1, pY + 1));
						}
					}
					if (lpX == pX - 1 && lpY == pY + 2) {
						if (lnX == pX - 1 && lnY == pY) {
							possibleMoves.add(new Coordinate(pX - 1, pY + 1));
						}
					}
				}
				if (piece.getColor() == Color.BLACK) {
					if (lpX == pX + 1 && lpY == pY - 2) {
						if (lnX == pX + 1 && lnY == pY) {
							possibleMoves.add(new Coordinate(pX + 1, pY + 1));
						}
					}
					if (lpX == pX - 1 && lpY == pY - 2) {
						if (lnX == pX - 1 && lnY == pY) {
							possibleMoves.add(new Coordinate(pX - 1, pY + 1));
						}
					}
				}

			}
		}

		return false;
	}

	@Override
	public boolean checkConditions() throws InvalidMoveException {
		if (!isInRange(possibleMoves)) {
			throw new OutOfPieceRangeException();
		}
		if (board.isOccupiedByOwn(next, piece) || board.isOccupiedByEnemy(next, piece) && pX == nX) {
			throw new OccupiedCoordinatesException();
		}
		if (board.isOccupiedByEnemy(next, piece)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		if (pX != nX) {
			move.setType(MoveType.EN_PASSANT);
			return true;
		}
		move.setType(MoveType.ATTACK);
		return true;
	}

}
