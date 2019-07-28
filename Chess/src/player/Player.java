package player;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import pieces.King;
import pieces.Piece;
import pieces.PieceType;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 20, 2019
 */
public abstract class Player {
	
	Board board;
	private ArrayList<Move> playerMoves;
	
	private King playerKing;
	
	private boolean human;
	
	/**
	 * 
	 * @param board
	 */
	public Player (Board board) {
		
		this.board = board;
		this.playerKing = this.findPlayerKing();
		
	}

	/**
	 * Init player King by finding him in the player's pieces.
	 * @return
	 */
	private King findPlayerKing () {
		for (Piece p: this.getActivePieces()) {
			if (p.getPieceType() == PieceType.KING) {
				return (King) p;
			}
		}
		throw new RuntimeException ();
	}
	
	/**
	 * @return
	 */
	public King getPlayerKing() {
		return this.playerKing;
	}

	/**
	 * @return
	 */
	public abstract ArrayList<Piece> getActivePieces();
	
}
