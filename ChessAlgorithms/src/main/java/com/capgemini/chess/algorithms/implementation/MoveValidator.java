package com.capgemini.chess.algorithms.implementation;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Iterator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.AnotherPlayerTurnException;
import com.capgemini.chess.algorithms.implementation.exceptions.EmptyFieldException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.OccupiedCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfBoardException;
import com.capgemini.chess.algorithms.implementation.exceptions.OutOfPieceRangeException;

public class MoveValidator {

	private Board board;
	private Coordinate checked;
	private Coordinate present;
	private Coordinate next;
	private int pX;
	private int pY;
	private int nX;
	private int nY;
	private Piece piece;
	private Move move;
	private ArrayList<Coordinate> possibleMoves;
	
	public Move validate(Board board, Coordinate present, Coordinate next) throws InvalidMoveException {
		this.board = board;
		this.present = present;
		this.next = next;
		move = new Move();
		if(!isCoordinateOnBoard(present)){
			throw new OutOfBoardException();
		}
		if(!isCoordinateOnBoard(next)){
			throw new OutOfBoardException();
		}
		piece = board.getPieceAt(present);
		if (piece == null) {
			throw new EmptyFieldException();
		}
		if(!isThisRightTurn()){
			throw new AnotherPlayerTurnException();
		}
		move.setFrom(present);
		move.setTo(next);
		move.setMovedPiece(piece);
		pX = present.getX();
		pY = present.getY();
		nX = next.getX();
		nY = next.getY();
		switch (piece.getType()) {
		case PAWN:
			validatePawn();
			break;
		case KNIGHT:
			validateKnight();
			break;
		case ROOK:
			validateRook();
			break;
		case KING:
			validateKing();
			break;
		case BISHOP:
			validateBishop();
			break;
		case QUEEN:
			validateQueen();
			break;
		}
		return move;
	}

	private boolean validatePawn() throws OutOfPieceRangeException, OccupiedCoordinatesException {
		possibleMoves = new ArrayList<>();
		boolean isFirstMove = false;

		if (pY == 1 && piece.getColor() == Color.WHITE || pY == 6 && piece.getColor() == Color.BLACK) {
			isFirstMove = true;
		}

		if (piece.getColor() == Color.BLACK) {
			Coordinate a = new Coordinate(pX, pY - 1);
			if (board.getPieceAt(a) == null) {
				possibleMoves.add(a);
				Coordinate b = new Coordinate(pX, pY - 2);
				if (isFirstMove && board.getPieceAt(b) == null) {
					possibleMoves.add(b);
				}
			}
			Coordinate c = new Coordinate(pX - 1, pY - 1);
			if (isOccupiedByEnemy(c)) {
				possibleMoves.add(c);
			}
			Coordinate d = new Coordinate(pX + 1, pY - 1);
			if (isOccupiedByEnemy(d)) {
				possibleMoves.add(d);
			}

		}
		if (piece.getColor() == Color.WHITE) {
			Coordinate a = new Coordinate(pX, pY + 1);
			if (board.getPieceAt(a) == null) {
				possibleMoves.add(a);
				Coordinate b = new Coordinate(pX, pY + 2);
				if (isFirstMove && board.getPieceAt(b) == null) {
					possibleMoves.add(b);
				}
			}
			Coordinate c = new Coordinate(pX - 1, pY + 1);
			if (isOccupiedByEnemy(c)) {
				possibleMoves.add(c);
			}
			Coordinate d = new Coordinate(pX + 1, pY + 1);
			if (isOccupiedByEnemy(d)) {
				possibleMoves.add(d);
			}
		}
		if (!isInRange(possibleMoves)) {
			throw new OutOfPieceRangeException();
		}
		if (isOccupiedByOwn(next)||isOccupiedByEnemy(next)&&pX==nX) {
			throw new OccupiedCoordinatesException();
		}
		if (isOccupiedByEnemy(next)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		move.setType(MoveType.ATTACK);
		return true;

	}

	private boolean validateKnight() throws OutOfPieceRangeException, OccupiedCoordinatesException {
		possibleMoves = new ArrayList<>();
		for (int x = pX - 2; x < pX + 3; x = x + 4) {
			for (int y = pY - 1; y < pY + 2; y = y + 2) {
				Coordinate c = new Coordinate(x, y);
				if (isCoordinateOnBoard(c)) {
					possibleMoves.add(c);
				}
			}
		}
		for (int y = pY - 2; y < pY + 3; y = y + 4) {
			for (int x = pX - 1; x < pX + 2; x = x + 2) {
				Coordinate c = new Coordinate(x, y);
				if (isCoordinateOnBoard(c)) {
					possibleMoves.add(c);
				}
			}
		}
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
		move.setType(MoveType.ATTACK);
		return true;

	}

	private boolean validateRook() throws InvalidMoveException {
		possibleMoves = new ArrayList<>();
		for (int i = pY + 1; i < 8; i++) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pY - 1; i > -1; i--) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX + 1; i < 8; i++) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX - 1; i > -1; i--) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}

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
		move.setType(MoveType.ATTACK);
		return true;

	}

	private boolean validateKing() throws OccupiedCoordinatesException, OutOfPieceRangeException {
		possibleMoves = new ArrayList<>();
		for (int x = pX - 1; x < pX + 2; x++) {
			for (int y = pY - 1; y < pY + 2; y++) {
				Coordinate c = new Coordinate(x, y);
				if (isCoordinateOnBoard(c) && (x != pX || y != pY)) {
					possibleMoves.add(c);
				}
			}
		}
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
		move.setType(MoveType.ATTACK);
		return true;

	}

	private boolean validateBishop() throws InvalidMoveException {
		possibleMoves = new ArrayList<>();
		for (int i = 1; pX + i < 8 && pY + i < 8; i++) {
			Coordinate c = new Coordinate(pX + i, pY + i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX - i > -1 && pY + i < 8; i++) {
			Coordinate c = new Coordinate(pX - i, pY + i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX - i > -1 && pY - i > -1; i++) {
			Coordinate c = new Coordinate(pX - i, pY - i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX + i < 8 && pY - i > -1; i++) {
			Coordinate c = new Coordinate(pX + i, pY - i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}

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
		move.setType(MoveType.ATTACK);
		return true;

	}

	private boolean validateQueen() throws InvalidMoveException {
		possibleMoves = new ArrayList<>();
		for (int i = 1; pX + i < 8 && pY + i < 8; i++) {
			Coordinate c = new Coordinate(pX + i, pY + i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX - i > -1 && pY + i < 8; i++) {
			Coordinate c = new Coordinate(pX - i, pY + i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX - i > -1 && pY - i > -1; i++) {
			Coordinate c = new Coordinate(pX - i, pY - i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = 1; pX + i < 8 && pY - i > -1; i++) {
			Coordinate c = new Coordinate(pX + i, pY - i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pY + 1; i < 8; i++) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pY - 1; i > -1; i--) {
			Coordinate c = new Coordinate(pX, i);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX + 1; i < 8; i++) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}
		for (int i = pX - 1; i > -1; i--) {
			Coordinate c = new Coordinate(i, pY);
			if (board.getPieceAt(c) == null) {
				possibleMoves.add(c);
			} else {
				possibleMoves.add(c);
				break;
			}
		}

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
		move.setType(MoveType.ATTACK);
		return true;
	}

	private boolean isOccupiedByOwn(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (p.getColor() == piece.getColor()) {
			return true;
		}
		return false;

	}

	private boolean isOccupied(Coordinate c) throws OccupiedCoordinatesException {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (isOccupiedByEnemy(c)) {
			move.setType(MoveType.CAPTURE);
			return true;
		}
		throw new OccupiedCoordinatesException();
	}

	private boolean isOccupiedByEnemy(Coordinate c) {
		Piece p = board.getPieceAt(c);
		if (p == null) {
			return false;
		}
		if (p.getColor() == piece.getColor()) {
			return false;
		}
		return true;
	}

	private boolean isCoordinateOnBoard(Coordinate c) {
		if (c.getX() > 7 || c.getX() < 0 || c.getY() > 7 || c.getY() < 0) {
			return false;
		}
		return true;
	}

	private boolean isInRange(ArrayList<Coordinate> moves) {
		Iterator itr = moves.iterator();
		while (itr.hasNext()) {
			Coordinate c = (Coordinate) itr.next();
			if (c.getX() == nX && c.getY() == nY) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isThisRightTurn(){
		if(piece.getColor()==actualColor()){
			return true;
		}
		return false;
	}
	
	private Color actualColor(){
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

}