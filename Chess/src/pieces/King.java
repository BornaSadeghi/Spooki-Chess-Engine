package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.Move.KingsideCastleMove;
import chess.Move.QueensideCastleMove;
import chess.Position;

/**
 * @author Borna Sadeghi
 * @version May 19, 2019
 */
public class King extends Piece {

	/**
	 * @param pos
	 * @param alliance
	 */
	public King (Position position, Alliance alliance) {
		super(position, alliance);
	}

	public King (int file, int rank, Alliance alliance) {
		super(new Position(file, rank), alliance);
	}

	/**
	 * @param pieceToCopy
	 */
	public King (Piece pieceToCopy) {
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
				//			if (!board.attackOnPosition (this.getAlliance().opponent(), dest))
				moves.add(new Move (board, this, dest));
		}

		// Castling

		// Kingside
		if (this.canCastleKingside(board)) {
			int castleRank = this.getAlliance().homeRank();
			moves.add(new KingsideCastleMove(board, this, new Position(7, castleRank), board.getPieceAt(8, castleRank), new Position(6, castleRank)));
		}

		// Queenside
		if (this.canCastleQueenside(board)) {
			int castleRank = this.getAlliance().homeRank();
			moves.add(new QueensideCastleMove(board, this, new Position(3, castleRank), board.getPieceAt(1, castleRank), new Position(4, castleRank)));
		}

		return moves;
	}

	private boolean canCastleKingside (Board board) {
		int homeRank = this.getAlliance().homeRank();
		Piece kingsideRook = board.getPieceAt(8, homeRank);
		return !this.hasMoved() 
				&& !kingsideRook.hasMoved()
				&& kingsideRook.getPieceType() == PieceType.ROOK
				&& !board.pieceExistsAt(6, homeRank)
				&& !board.pieceExistsAt(7, homeRank)
				&& !board.inCheck(this.getAlliance())
				&& !board.attackOnPosition(this.getAlliance().opponent(), new Position (6, homeRank)) // TODO This may be slow, find more efficient method
				&& !board.attackOnPosition(this.getAlliance().opponent(), new Position (7, homeRank));


	}

	private boolean canCastleQueenside (Board board) {
		int homeRank = this.getAlliance().homeRank();
		Piece queensideRook = board.getPieceAt(1, homeRank);
		return !this.hasMoved() 
				&& !queensideRook.hasMoved()
				&& queensideRook.getPieceType() == PieceType.ROOK
				&& !board.pieceExistsAt(2, homeRank)
				&& !board.pieceExistsAt(3, homeRank)
				&& !board.pieceExistsAt(4, homeRank)
				&& !board.inCheck(this.getAlliance())
				&& !board.attackOnPosition(this.getAlliance().opponent(), new Position (2, homeRank)) // TODO This may be slow, find more efficient method
				&& !board.attackOnPosition(this.getAlliance().opponent(), new Position (3, homeRank));
		
	}

	/**
	 * @return An ArrayList of the squares that this piece attacks.
	 */
	@Override
	public ArrayList<Position> getAttackedPositions (Board board) {
		ArrayList<Position> dests = new ArrayList<Position>();

		int file = this.getFile(), rank = this.getRank();

		Position pos = new Position(file - 1, rank + 1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);
		pos = new Position(file, rank + 1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);
		pos = new Position(file + 1, rank + 1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);

		pos = new Position(file - 1, rank);
		if (this.isValidDestination(board, pos))
			dests.add(pos);
		pos = new Position(file + 1, rank);
		if (this.isValidDestination(board, pos))
			dests.add(pos);

		pos = new Position(file - 1, rank - 1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);
		pos = new Position(file, rank - 1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);
		pos = new Position(file + 1, rank - +1);
		if (this.isValidDestination(board, pos))
			dests.add(pos);

		return dests;
	}

	/**
	 * 
	 */
	@Override
	public PieceType getPieceType () {
		return PieceType.KING;
	}

	/**
	 * 
	 */
	@Override
	public String toString () {
		return this.getAlliance() == Alliance.WHITE ? "K" : "k";
	}
}
