package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.Move.PawnJumpMove;
import chess.Move.EnPassantMove;
import chess.Move.PawnPromotionMove;
import chess.PieceGrid;
import chess.Position;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 19, 2019
 */
public class Pawn extends Piece {

	/**
	 * @param position
	 * @param alliance
	 */
	public Pawn (Position position, Alliance alliance) {
		super(position, alliance);
		if (this.getRank() != alliance.pawnStartingRank())
			this.setHasMoved();
	}

	public Pawn (int file, int rank, Alliance alliance) {
		super(new Position(file, rank), alliance);
		if (this.getRank() != alliance.pawnStartingRank())
			this.setHasMoved();
	}

	/**
	 * @param pieceToCopy
	 */
	public Pawn (Piece pieceToCopy) {
		super(pieceToCopy);
		if (this.getRank() != this.getAlliance().pawnStartingRank())
			this.setHasMoved();
	}

	/**
	 * @param board
	 * @return
	 */
	@Override
	public ArrayList<Move> calculateLegalMoves (Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		int file = this.getFile(), rank = this.getRank();

		// Regular movement.
		if (this.isWhite()) {
			// One space forward
			if (!board.pieceExistsAt(file, rank+1)) {

				// If currently at seventh rank add pawn promotion, otherwise add one space forward.
				Move move = new Move (board, this, new Position(file, rank+1));
				if (!move.leavesPlayerInCheck(this.getAlliance())) {
					if (this.getRank() == 7) {
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank+1), PieceType.KNIGHT));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank+1), PieceType.BISHOP));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank+1), PieceType.ROOK));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank+1), PieceType.QUEEN));
					} else {
						moves.add(move);
					}
				}

				// Two spaces forward
				move = new PawnJumpMove (board, this, new Position(file, rank+2));
				if (!this.hasMoved() && !board.pieceExistsAt(file, rank+2) && !move.leavesPlayerInCheck(this.getAlliance())) {
					moves.add(move);
				}
			}
		} else {
			// One space forward
			if (!board.pieceExistsAt(file, rank-1)) {

				// If currently at second rank add pawn promotion, otherwise add one space forward.
				Move move = new Move (board, this, new Position(file, rank-1));
				if (!move.leavesPlayerInCheck(this.getAlliance())) {
					if (this.getRank() == 2) {
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank-1), PieceType.KNIGHT));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank-1), PieceType.BISHOP));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank-1), PieceType.ROOK));
						moves.add(new PawnPromotionMove (board, this, new Position(file, rank-1), PieceType.QUEEN));
					} else {
						moves.add(move);
					}
				}

				// Two spaces forward
				move = new PawnJumpMove (board, this, new Position(file, rank-2));
				if (!this.hasMoved() && !board.pieceExistsAt(file, rank-2) && !move.leavesPlayerInCheck(this.getAlliance())) {
					moves.add(move);
				}
			}
		}

		// Captures including en passant.
		for (Position dest : this.getAttackedPositions(board)) {
			if (this.isValidCapture(board, dest)) {
				Move move = new PawnPromotionMove (board, this, dest, PieceType.QUEEN);
				if (!move.leavesPlayerInCheck(this.getAlliance())) {
					if (this.isWhite() && this.getRank() == 7) {
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.KNIGHT));
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.BISHOP));
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.ROOK));
						moves.add(move);
					} else if (this.isBlack() && this.getRank() == 2){
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.KNIGHT));
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.BISHOP));
						moves.add(new PawnPromotionMove (board, this, dest, PieceType.ROOK));
						moves.add(move);
					} else {
						moves.add(new Move (board, this, dest));
					}
				}

			} else if (this.isValidEnPassant(board, dest)) {
				Move move = new EnPassantMove (board, this, dest);
				if (move.leavesPlayerInCheck(this.getAlliance())) {
					moves.add(move);
					board.setEnPassantPossible(true);
				}
			}
		}

		return moves;
	}

	public boolean isValidEnPassant (Board board, Position dest) {
		return dest.equals(board.getEnPassantPosition());
	}

	/**
	 * 
	 * @return An ArrayList of the squares that this piece attacks.
	 */
	@Override
	public ArrayList<Position> getAttackedPositions(Board board) {
		ArrayList<Position> attackedSquares = new ArrayList<Position>();

		int file = this.getFile(), rank = this.getRank();

		if (this.isWhite()) {
			if (Position.isValid(file-1, rank+1))
				attackedSquares.add(new Position (file-1, rank+1));
			if (Position.isValid(file+1, rank+1))
				attackedSquares.add(new Position (file+1, rank+1));
		} else {
			if (Position.isValid(file-1, rank-1))
				attackedSquares.add(new Position (file-1, rank-1));
			if (Position.isValid(file+1, rank-1))
				attackedSquares.add(new Position (file+1, rank-1));
		}

		return attackedSquares;
	}

	/**
	 * 
	 */
	@Override
	public PieceType getPieceType() {
		return PieceType.PAWN;
	}

	/**
	 * 
	 */
	@Override
	public String toString () {
		return this.getAlliance() == Alliance.WHITE ? "P" : "p";
	}
}
