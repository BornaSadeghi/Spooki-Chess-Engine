package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Move;
import chess.Position;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 16, 2019
 */
public abstract class Piece {

	private Position position;
	private ArrayList<Move> legalMoves;
	private Alliance alliance;

	private boolean hasMoved = false;


	/**
	 * Instantiate a new Piece object.
	 * 
	 * @param position - Initial position on the board.
	 * @param alliance - White or black.
	 */
	public Piece (Position position, Alliance alliance) {
		this.position = position;
		this.alliance = alliance;
	}

	/**
	 * Instantiate a new Piece object.
	 * 
	 * @param file - Initial file on the board.
	 * @param rank - Initial rank on the board.
	 * @param alliance - White or black.
	 */
	public Piece (int file, int rank, Alliance alliance) {
		this(new Position(file, rank), alliance);
	}
	
	public Piece (Piece pieceToCopy) {
		this.position = pieceToCopy.position;
		this.legalMoves = pieceToCopy.legalMoves;
		this.alliance = pieceToCopy.alliance;
		this.hasMoved = pieceToCopy.hasMoved;
	}

	/**
	 * @return The Position object of this piece.
	 */
	public Position getPosition () {
		return this.position;
	}

	/**
	 * Update this piece's position object.
	 * 
	 * @param position - The new Position.
	 */
	public void setPosition (Position position) {
		this.position = position;
	}

	/**
	 * Update this piece's position object.
	 * 
	 * @param file - The new file.
	 * @param rank - The new rank.
	 */
	public void setPosition (int file, int rank) {
		this.setPosition(new Position(file, rank));
	}

	/**
	 * @return The file this piece is on.
	 */
	public int getFile () {
		return this.position.getFile();
	}

	/**
	 * @return The rank this piece is on.
	 */
	public int getRank () {
		return this.position.getRank();
	}

	/**
	 * @return The alliance of this piece, white or black.
	 */
	public Alliance getAlliance () {
		return this.alliance;
	}
	
	public Alliance getOpponentAlliance () {
		return this.alliance.opponent();
	}

	public void setHasMoved () {
		this.hasMoved = true;
	}

	public boolean hasMoved () {
		return this.hasMoved;
	}

	/**
	 * @return True if this piece is white.
	 */
	public boolean isWhite() {
		return this.alliance == Alliance.WHITE;
	}
	
	/**
	 * @return True if this piece is black.
	 */
	public boolean isBlack() {
		return this.alliance == Alliance.BLACK;
	}

	/**
	 * @param move - The move to check for validity.
	 * @return True if the piece can legally land on the destination position on the given board.
	 */
	public boolean isValidDestination (Board board, Position dest) {
		return Position.isValid(dest) && board.getPieceAt(dest).alliance != this.alliance;
	}
	
	public boolean isValidCapture (Board board, Position dest) {
		return Position.isValid(dest) && board.getPieceAt(dest).alliance == this.alliance.opponent();
	}

	public boolean canMoveTo (Board board, Position dest) {
		for (Move move : this.getLegalMoves(board)) {
			if (move.getDestPos().equals(dest)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return The already-calculated list of legal moves.
	 */
	public ArrayList<Move> getLegalMoves (Board board) {
		return this.legalMoves == null ? this.calculateLegalMoves(board) : this.legalMoves;
	}

	/**
	 * Update this piece's list of legal moves.
	 * 
	 * @param board - The board to find valid moves on for this piece.
	 */
	public void updateLegalMoves (Board board) {
		this.legalMoves = this.calculateLegalMoves(board);
	}
	
	public int getValue () {
		return this.getPieceType().getValue();
	}

	/**
	 * Calculates legal moves for the piece.
	 * 
	 * @param board - The Board to calculate moves on.
	 */
	public abstract ArrayList<Move> calculateLegalMoves (Board board);

	/**
	 * 
	 * @return An ArrayList of the squares that this piece attacks.
	 */
	public abstract ArrayList<Position> getAttackedPositions (Board board);

	/**
	 * @return The type of piece this is.
	 */
	public abstract PieceType getPieceType ();
}
