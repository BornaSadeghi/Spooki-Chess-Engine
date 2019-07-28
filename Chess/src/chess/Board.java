package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pieces.*;
import player.BlackPlayer;
import player.WhitePlayer;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 16, 2019
 */
public class Board extends PieceGrid {

	// The FEN notation for the default starting layout of the board.
	public static final String DEFAULT_START_LAYOUT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

	// True if it's white's turn, false if black's.
	private boolean whiteTurn = true;

	// After any pawn moves two squares, this is that pawn.
	private Piece enPassantPawn;

	// True when an en passant move can be played which captures the en passant pawn.
	private boolean enPassantPossible;

	/**
	 * Create a new empty board.
	 */
	public Board () {
		super();
	}

	/**
	 * Create a new board with a given layout.
	 * 
	 * @param layout - The FEN layout to initialize the pieces with.
	 */
	public Board (String layout) {
		super(layout);
	}

	/**
	 * Creates a new board which is a copy of another board.
	 * 
	 * @param board - The board to instantiate a copy of.
	 */
	public Board (Board board) {
		super(board);
		this.whiteTurn = board.whiteTurn;
	}

	/**
	 * Execute a move and return a copy of the new board.
	 * 
	 * @param move - The move to execute.
	 * 
	 * @return new Board after move is executed.
	 */
	public Board makeMove (Move move) {
		if (!move.isValid())
			throw new RuntimeException(String.format("Invalid move processed: %s", move.toString()));

		return move.execute();
	}

	/**
	 * 
	 * @param alliance - The alliance to count the attacks for.
	 * @param pos - The position to count the alliance's attacks on.
	 * 
	 * @return The number of attacks that the given alliance has on the given position. 
	 */
	public int countAttacksOnPosition (Alliance alliance, Position pos) {
		int numAttacks = 0;

		ArrayList<Move> legalMoves = this.getLegalMoves(alliance);

		for (Move move : legalMoves) {
			if (move.getDestPos().equals(pos)) {
				numAttacks++;
			}
		}

		return numAttacks;
	}

	public boolean gameOver () {
		return this.getLegalMoves(this.getCurrentPlayer()).isEmpty();
	}
	
	/**
	 * 
	 * @param alliance
	 * @return An ArrayList of the legal moves for the given alliance.
	 */
	public ArrayList<Move> getLegalMoves (Alliance alliance) {
		Set<Move> moves = new HashSet<Move>();

		for (Piece piece : this.getPiecesOfAlliance(alliance)) {
			for (Move move : piece.calculateLegalMoves(this)) {
				moves.add(move);
			}
		}

		return new ArrayList<Move>(moves);
	}

	/**
	 * 
	 * @param alliance
	 * @param pos
	 * @return True if the alliance has an attack on the given position.
	 */
	public boolean attackOnPosition (Alliance alliance, Position pos) {
		return this.getAttackedPositions(alliance).contains(pos);
	}

	/**
	 * 
	 * @return The Position behind the en passant pawn
	 */
	public Position getEnPassantPosition() {
		if (this.enPassantPawn != null)
			return new Position (this.enPassantPawn.getFile(), this.enPassantPawn.getAlliance().enPassantRank());
		return null;
	}

	/**
	 * 
	 * @param pawn - The pawn to set the en passant pawn to.
	 */
	public void setEnPassantPawn (Piece pawn) {
		this.enPassantPawn = pawn;
	}

	/**
	 * 
	 * @return The current en passant pawn.
	 */
	public Piece getEnPassantPawn () {
		return this.enPassantPawn;
	}

	/**
	 * 
	 * @return True if there is an en passant move that can be performed on the board.
	 */
	public boolean enPassantPossible () {
		return this.enPassantPossible;
	}

	/**
	 * 
	 * @param possible - Whether or not an en passant move should be possible.
	 */
	public void setEnPassantPossible (boolean possible) {
		this.enPassantPossible = possible;
	}


	/**
	 * 
	 * @param alliance
	 * @return An ArrayList of the positions that the alliance has control over.
	 */
	public ArrayList<Position> getAttackedPositions (Alliance alliance) {
		Set<Position> positions = new HashSet<Position>();

		for (Piece piece : this.getPiecesOfAlliance(alliance)) {
			for (Position pos : piece.getAttackedPositions(this)) {
				positions.add(pos);
			}
		}

		return new ArrayList<Position> (positions);
	}

	/**
	 * 
	 * @param alliance
	 * @return True if the given alliance is in check.
	 */
	public boolean inCheck (Alliance alliance) {
		Piece playerKing = alliance == Alliance.WHITE ? this.getWhiteKing() : this.getBlackKing();

		if (playerKing == null)
			throw new RuntimeException ("Board is missing a king.");

		return this.getAttackedPositions(playerKing.getOpponentAlliance()).contains(playerKing.getPosition());
	}

	/**
	 *  
	 * @return The king Piece of the active player.
	 */
	public Piece getCurrentPlayerKing () {
		return this.getCurrentPlayer() == Alliance.WHITE ? this.getWhiteKing() : this.getBlackKing(); 
	}
	
	/**
	 * Changes the current active player to the opponent player.
	 */
	public void endTurn() {
		this.whiteTurn = !this.whiteTurn;
	}

	/**
	 * 
	 * @return True if it is currently white's turn, false if black's.
	 */
	public boolean isWhiteTurn () {
		return this.whiteTurn;
	}

	/**
	 * 
	 * @return The Alliance of the current player.
	 */
	public Alliance getCurrentPlayer () {
		return this.whiteTurn ? Alliance.WHITE : Alliance.BLACK;
	}

	@Override
	public String toString () {
		Alliance currentPlayer = this.getCurrentPlayer();
		return String.format("%s to move\nIn check: %s\n", currentPlayer, this.inCheck(currentPlayer) ? currentPlayer : "None") + super.toString();
		//		return String.format("%s to move\n", currentPlayer) + super.toString();
	}
}
