package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.Position;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 18, 2019
 */
public class Knight extends Piece {


	/**
	 * @param pos
	 * @param alliance
	 */
	public Knight(Position position, Alliance alliance) {
		super(position, alliance);
	}

	public Knight (int file, int rank, Alliance alliance) {
		super(new Position(file, rank), alliance);
	}

	/**
	 * @param pieceToCopy
	 */
	public Knight (Piece pieceToCopy) {
		// TODO Auto-generated constructor stub
		super(pieceToCopy);
	}

	/**
	 * Calculate legal moves for the knight.
	 * 
	 * @param board
	 * @return An arraylist of the knight's current legal moves. 
	 */
	public ArrayList<Move> calculateLegalMoves(Board board) {
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

		Position dest = new Position (file-2, rank-1);
		if (this.isValidDestination(board, dest))
			dests.add(dest);

		dest = new Position (file-2, rank+1);
		if (this.isValidDestination(board, dest))
			dests.add(dest);

		dest = new Position (file-1, rank-2);
		if (this.isValidDestination(board, dest))
			dests.add(dest);

		dest = new Position (file-1, rank+2);
		if (this.isValidDestination(board, dest))
			dests.add(dest);


		dest = new Position (file+1, rank-2);
		if (this.isValidDestination(board, dest))
			dests.add(dest);


		dest = new Position (file+1, rank+2);
		if (this.isValidDestination(board, dest))
			dests.add(dest);


		dest = new Position (file+2, rank-1);
		if (this.isValidDestination(board, dest))
			dests.add(dest);


		dest = new Position (file+2, rank+1);
		if (this.isValidDestination(board, dest))
			dests.add(dest);

		return dests;
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.KNIGHT;
	}

	@Override
	public String toString () {
		return this.getAlliance() == Alliance.WHITE ? "N" : "n";
	}

}
