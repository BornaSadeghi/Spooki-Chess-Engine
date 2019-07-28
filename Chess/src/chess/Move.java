package chess;

import pieces.*;

/**
 * @author Borna Sadeghi
 * @version May 18, 2019
 */
public class Move {

	private final Board board;

	private Piece movedPiece;
	private Position startPos;
	private Position destPos;

	/**
	 * @param board - The current board.
	 * @param moved - The piece being moved.
	 * @param dest - The destination position of the piece.
	 */
	public Move (Board board, Piece moved, Position dest) {
		this.board = board;
		this.movedPiece = moved;
		this.destPos = dest;

		this.startPos = moved.getPosition();

		if (board == null) {
			throw new RuntimeException("Move object was instantiated on a null board.");
		} else if (moved == null || moved instanceof NoPiece) {
			throw new RuntimeException("Move object was instantiated with a null piece.");
		}
	}

	/**
	 * @param board - The current board.
	 * @param start - The position to move a piece from.
	 * @param dest - The destination position of the piece.
	 */
	public Move (Board board, Position start, Position dest) {
		this(board, board.getPieceAt(start), dest);

		if (this.getMovedPiece() instanceof NoPiece) {
			throw new RuntimeException("No piece specified for Move.");
		}
	}

	/**
	 * @param board - The current board.
	 * @param moved - The piece being moved.
	 * @param alphanumeric - The alphanumeric representation of the destination
	 *            position.
	 */
	public Move (Board board, Piece moved, String alphanumeric) {
		this(board, moved, Notation.toPosition(alphanumeric));
	}

	/**
	 * @param board - The current board.
	 * @param alphanumericStart - The alphanumeric representation of the start
	 *            position.
	 * @param alphanumericDest - The alphanumeric representation of the destination
	 *            position.
	 */
	public Move (Board board, String alphanumericStart, String alphanumericDest) {
		this(board, Notation.toPosition(alphanumericStart), Notation.toPosition(alphanumericDest));
	}

	/**
	 * @return The piece being moved.
	 */
	public Piece getMovedPiece () {
		return this.movedPiece;
	}

	/**
	 * @return The starting position of the moving piece.
	 */
	public Position getStartPos () {
		return this.startPos;
	}

	/**
	 * @return The destination position of the moving piece
	 */
	public Position getDestPos () {
		return this.destPos;
	}

	/**
	 * @return True if this move is a capture move.
	 */
	public boolean isCapture () {
		return this.board.pieceExistsAt(this.destPos);
	}

	/**
	 * @return True if this move is valid.
	 */
	public boolean isValid () {
		return this.movedPiece.getLegalMoves(this.board).contains(this);
	}

	/**
	 * Try this move on the board whether it's valid or not.
	 * 
	 * @return The board after this move is executed.
	 */
	public Board execute () {
		Board updatedBoard = new Board(this.board);

		updatedBoard.movePiece(this.startPos, this.destPos);
		updatedBoard.endTurn(); // Change current player after turn.

		return updatedBoard;
	}

	/**
	 * @return True if this move leaves the opponent in check.
	 */
	public boolean leavesOpponentInCheck () {
		// TODO: Find a better way without calling execute() again
		// Check if the piece's new attacked positions contains the opponent king
		// position? (What about discovered checks?)
		return this.execute().inCheck(this.movedPiece.getAlliance().opponent());
	}
	
	public boolean leavesPlayerInCheck (Alliance playerAlliance) {
		return this.execute().inCheck (playerAlliance);
	}

	public Board getBoard () {
		return this.board;
	}

	@Override
	public boolean equals (Object obj) {
		Move other = (Move) obj;
		if (this.startPos.equals(other.startPos) && this.destPos.equals(other.destPos)
				&& this.board.equals(other.board)) {
			return true;
		}
		return false;
	}

	public String toString () {
		// return String.format("(Move: %s on %s %s %s)", this.movedPiece.toString(),
		// Notation.toAlphanumeric(this.startPos),
		// this.isCapture() ? "takes" : "to", Notation.toAlphanumeric(this.destPos));
		return Notation.moveNotation(this);
	}

	public static abstract class CastleMove extends Move {

		private Piece rook;
		private Position rookStartPos;
		private Position rookDestPos;

		/**
		 * @param board
		 * @param moved
		 * @param dest
		 */
		public CastleMove (Board board, Piece king, Position dest, Piece rook, Position rookDest) {
			super(board, king, dest);
			this.rook = rook;
			this.rookStartPos = rook.getPosition();
			this.rookDestPos = rookDest;
		}

		/**
		 * Perform the king move first, and then move the rook.
		 */
		@Override
		public Board execute () {
			Board updatedBoard = super.execute();
			
			updatedBoard.movePiece(this.rook.getPosition(), this.rookDestPos);

			return updatedBoard;
		}
	}
	
	public static class KingsideCastleMove extends CastleMove {

		/**
		 * @param board
		 * @param king
		 * @param dest
		 * @param rook
		 * @param rookDest
		 */
		public KingsideCastleMove (Board board, Piece king, Position dest, Piece rook, Position rookDest) {
			super(board, king, dest, rook, rookDest);
		}
		
		@Override
		public String toString () {
			return "O-O";
		}
	}
	
	public static class QueensideCastleMove extends CastleMove {

		/**
		 * @param board
		 * @param king
		 * @param dest
		 * @param rook
		 * @param rookDest
		 */
		public QueensideCastleMove (Board board, Piece king, Position dest, Piece rook, Position rookDest) {
			super(board, king, dest, rook, rookDest);
		}
		
		@Override
		public String toString () {
			return "O-O-O";
		}
	}

	public static class PawnJumpMove extends Move {

		/**
		 * The Move for a pawn moving two spaces.
		 * 
		 * @param board
		 * @param moved
		 * @param dest
		 */
		public PawnJumpMove (Board board, Piece moved, Position dest) {
			super(board, moved, dest);
		}

		@Override
		public Board execute() {
			Board updatedBoard = super.execute();
			
			updatedBoard.setEnPassantPawn(updatedBoard.getPieceAt(this.getDestPos()));
			
			return updatedBoard;
		}
	}

	public static class PawnPromotionMove extends Move {

		private PieceType promotionType;
		
		/**
		 * @param board
		 * @param moved
		 * @param dest
		 */
		public PawnPromotionMove (Board board, Piece moved, Position dest, PieceType promotionType) {
			super(board, moved, dest);
			this.promotionType = promotionType;
		}
		
		@Override
		public Board execute() {
			Board updatedBoard = super.execute();
			
			switch (promotionType) {
			case KNIGHT:
				updatedBoard.setPieceAt (this.getDestPos(), new Knight(this.getDestPos(), this.getMovedPiece().getAlliance()));
				break;
			case BISHOP:
				updatedBoard.setPieceAt (this.getDestPos(), new Bishop(this.getDestPos(), this.getMovedPiece().getAlliance()));
				break;
			case ROOK:
				updatedBoard.setPieceAt (this.getDestPos(), new Rook(this.getDestPos(), this.getMovedPiece().getAlliance()));
				break;
			case QUEEN:
				updatedBoard.setPieceAt (this.getDestPos(), new Queen(this.getDestPos(), this.getMovedPiece().getAlliance()));
				break;
			default:
				throw new RuntimeException ("Invalid piece type for pawn promotion.");
			}
			
			return updatedBoard;
		}
		
		@Override
		public String toString () {
			return super.toString().replace("+", "") + "=" + this.promotionType + (this.leavesOpponentInCheck() ? "+" : ""); 
		}
		
	}
	
	public static class EnPassantMove extends Move {

		/**
		 * @param board
		 * @param moved
		 * @param dest
		 */
		public EnPassantMove (Board board, Piece moved, Position dest) {
			super(board, moved, dest);
//			System.out.println(Notation.moveNotation(this));
		}
		
		@Override
		public Board execute () {
			Board updatedBoard = super.execute();
			
			updatedBoard.removePieceAt(this.getBoard().getEnPassantPawn().getPosition());
			
			return updatedBoard;
		}

		@Override
		public String toString () {
			return Notation.toFileCharacter(this.getStartPos().getFile()) + "x" + super.toString();
		}
	}
}
