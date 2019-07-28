package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.PieceGrid;
import chess.Position;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 19, 2019
 */
public class Rook extends Piece {

	/**
	 * @param pos
	 * @param colour
	 */
	public Rook(Position position, Alliance alliance) {
		super(position, alliance);
	}

	public Rook (int file, int rank, Alliance alliance) {
		super(new Position(file, rank), alliance);
	}


	/**
	 * @param pieceToCopy
	 */
	public Rook (Piece pieceToCopy) {
		super(pieceToCopy);
	}

	/**
	 * @param board
	 * @return
	 */
	@Override
	public ArrayList<Move> calculateLegalMoves (Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		for (Position dest : this.getAttackedPositions(board)) {
			Move move = new Move (board, this, dest);
			if (!move.leavesPlayerInCheck(this.getAlliance()))
				moves.add(new Move (board, this, dest));
		}

		return moves;
	}

	/**
	 * 
	 * @return An ArrayList of the squares that this piece attacks.
	 */
	@Override
	public ArrayList<Position> getAttackedPositions(Board board) {
		ArrayList<Position> dests = new ArrayList<Position>();

		int file = this.getFile(), rank = this.getRank();

		// Left
		for (int i=file-1; i>=1; i--) {
			if (board.pieceExistsAt(i, rank)) {
				if (board.getPieceAt(i, rank).getAlliance() != this.getAlliance())
					dests.add(new Position(i, rank));
				break;
			} else {
				dests.add(new Position(i, rank));
			}
		}

		// Right
		for (int i=file+1; i<=PieceGrid.NUM_FILES; i++) {
			if (board.pieceExistsAt(i, rank)) {
				if (board.getPieceAt(i, rank).getAlliance() != this.getAlliance())
					dests.add(new Position(i, rank));
				break;
			} else {
				dests.add(new Position(i, rank));
			}
		}

		// Up
		for (int i=rank+1; i<=PieceGrid.NUM_RANKS; i++) {
			if (board.pieceExistsAt(file, i)) {
				if (board.getPieceAt(file, i).getAlliance() != this.getAlliance())
					dests.add(new Position(file, i));
				break;
			} else {
				dests.add(new Position(file, i));
			}
		}
		
		// Down
		for (int i=rank-1; i>=1; i--) {
			if (board.pieceExistsAt(file, i)) {
				if (board.getPieceAt(file, i).getAlliance() != this.getAlliance())
					dests.add(new Position(file, i));
				break;
			} else {
				dests.add(new Position(file, i));
			}
		}

		return dests;
	}
	
	/**
	 * 
	 */
	@Override
	public PieceType getPieceType() {
		return PieceType.ROOK;
	}

	/**
	 * 
	 */
	@Override
	public String toString () {
		return this.getAlliance() == Alliance.WHITE ? "R" : "r";
	}
}
