package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.Position;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 20, 2019
 */
public class NoPiece extends Piece {

	/**
	 * @param position
	 * @param colour
	 */
	public NoPiece(Position position) {
		super(position, Alliance.NO_ALLIANCE);
	}
	
	public NoPiece(int file, int rank) {
		this(new Position(file, rank));
	}
	
	public NoPiece (Piece pieceToCopy) {
		super(pieceToCopy);
	}

	/* 
	 * 
	 */
	@Override
	public ArrayList<Move> calculateLegalMoves(Board board) {
		throw new RuntimeException ("NoPiece tried to calculate legal moves.");
//		return null;
	}

	/**
	 * 
	 * @return An ArrayList of the squares that this piece attacks.
	 */
	@Override
	public ArrayList<Position> getAttackedPositions(Board board) {
		throw new RuntimeException ("NoPiece tried to calculate attacked positions.");
//		return null;
	}
	
	@Override
	public boolean canMoveTo (Board board, Position dest) {
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public PieceType getPieceType() {
		return PieceType.NO_PIECE;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString () {
		return ".";
	}
}
