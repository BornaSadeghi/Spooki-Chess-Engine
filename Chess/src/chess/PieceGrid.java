package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pieces.*;

/**
 * @author Borna Sadeghi
 * @version Jul 16, 2019
 */
public class PieceGrid {

	public static final int NUM_FILES = 8;
	public static final int NUM_RANKS = 8;
	private Piece[][] pieces = new Piece[NUM_FILES][NUM_RANKS];

	private ArrayList<Piece> whitePieces = new ArrayList<Piece>(), blackPieces = new ArrayList<Piece>();

	private Piece whiteKing, blackKing;

	/**
	 * Initialize an empty PieceGrid full of NoPieces.
	 */
	public PieceGrid () {
		for (int y = 0; y < NUM_RANKS; y++) {
			for (int x = 0; x < NUM_FILES; x++) {
				this.pieces[x][y] = new NoPiece(Position.xToFile(x), Position.yToRank(y));
			}
		}
	}

	/**
	 * Initialize a grid of pieces with a given FEN layout.
	 * 
	 * @param layout - The FEN layout to initialize the pieces with.
	 */
	public PieceGrid (String layout) {
		String[] ranks = layout.split("/");
		if (ranks.length != PieceGrid.NUM_RANKS) {
			throw new RuntimeException(
					"Invalid FEN layout used to initialize piece grid: Board layout must have 8 ranks.");
		}

		// x = file-1, y = rank-1
		// For each rank
		for (int y = 0; y < ranks.length; y++) {
			String rankString = ranks[y];

			// For each file
			int x = 0;
			for (char c : rankString.toCharArray()) {
				Position pos = new Position(Position.xToFile(x), Position.yToRank(y));

				// Digits represent empty space
				if (Character.isDigit(c)) {
					for (int j = 0; j < Character.getNumericValue(c); j++) {
						this.pieces[x][y] = new NoPiece(pos);
						x++;
					}
				} else {
					// Letters represent pieces
					switch (c) {
					case 'P':
						this.pieces[x][y] = new Pawn(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						break;
					case 'N':
						this.pieces[x][y] = new Knight(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						break;
					case 'B':
						this.pieces[x][y] = new Bishop(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						break;
					case 'R':
						this.pieces[x][y] = new Rook(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						break;
					case 'Q':
						this.pieces[x][y] = new Queen(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						break;
					case 'K':
						this.pieces[x][y] = new King(pos, Alliance.WHITE);
						this.whitePieces.add(this.pieces[x][y]);
						this.whiteKing = this.pieces[x][y];
						break;
					case 'p':
						this.pieces[x][y] = new Pawn(pos, Alliance.BLACK);
						this.blackPieces.add(this.pieces[x][y]);
						break;
					case 'n':
						this.pieces[x][y] = new Knight(pos, Alliance.BLACK);
						this.blackPieces.add(this.pieces[x][y]);
						break;
					case 'b':
						this.pieces[x][y] = new Bishop(pos, Alliance.BLACK);
						this.blackPieces.add(this.pieces[x][y]);
						break;
					case 'r':
						this.pieces[x][y] = new Rook(pos, Alliance.BLACK);
						this.blackPieces.add(this.pieces[x][y]);
						break;
					case 'k':
						this.pieces[x][y] = new King(pos, Alliance.BLACK);
						this.blackKing = this.pieces[x][y];
						break;
					case 'q':
						this.pieces[x][y] = new Queen(pos, Alliance.BLACK);
						this.blackPieces.add(this.pieces[x][y]);
						break;
					default:
						throw new RuntimeException(
								"Invalid FEN layout used to initialize board: Invalid character in FEN layout string.");
					}
					x++;
				}
			}
			if (x < NUM_FILES) {
				throw new RuntimeException(
						"Invalid FEN layout used to initialize board: Board layout must have 8 files per rank.");
			}
		}
	}

	/**
	 * Initialize a PieceGrid using a board to copy.
	 * 
	 * @param board - The board to copy the piece layout from.
	 */
	public PieceGrid (Board board) {
		// TODO: This method of copying the board (string layout) is slow. try a more
		// direct method.
		// Here's a better method: just copy the 2D Piece array instead. The more naive
		// method I was initially avoiding.
		for (int x = 0; x < NUM_FILES; x++) {
			for (int y = 0; y < NUM_RANKS; y++) {
				Piece pieceToCopy = board.getPieceAt(Position.xToFile(x), Position.yToRank(y));

				switch (pieceToCopy.getPieceType()) {
				case PAWN:
					this.pieces[x][y] = new Pawn(pieceToCopy);
					break;
				case KNIGHT:
					this.pieces[x][y] = new Knight(pieceToCopy);
					break;
				case BISHOP:
					this.pieces[x][y] = new Bishop(pieceToCopy);
					break;
				case ROOK:
					this.pieces[x][y] = new Rook(pieceToCopy);
					break;
				case QUEEN:
					this.pieces[x][y] = new Queen(pieceToCopy);
					break;
				case KING:
					this.pieces[x][y] = new King(pieceToCopy);
					if (this.pieces[x][y].isWhite())
						this.whiteKing = this.pieces[x][y];
					else
						this.blackKing = this.pieces[x][y];
					break;
				case NO_PIECE:
					this.pieces[x][y] = new NoPiece(pieceToCopy);
					break;
				}

				if (pieceToCopy.isWhite())
					this.whitePieces.add(this.pieces[x][y]);
				else if (pieceToCopy.isBlack())
					this.blackPieces.add(this.pieces[x][y]);
			}
		}

	}

	/**
	 * Initialize a PieceGrid with a 2D piece array.
	 * 
	 * @param grid
	 */
	public PieceGrid (Piece[][] grid) {
		this.pieces = grid;
	}

//	/**
//	 * @param alliance
//	 * @return An ArrayList containing all of the alliance's active pieces.
//	 */
//	public ArrayList<Piece> getPiecesOfAlliance (Alliance alliance) {
//		return alliance == Alliance.WHITE ? this.whitePieces : this.blackPieces;
//	}

	 /**
	 * @param alliance
	 * @return An ArrayList containing all of the alliance's active pieces.
	 */
	 public ArrayList<Piece> getPiecesOfAlliance (Alliance alliance) {
	 ArrayList<Piece> piecesOfAlliance = new ArrayList<Piece>();
	
	 for (int x = 0; x < NUM_FILES; x++) {
	 for (int y = 0; y < NUM_RANKS; y++) {
	 if (pieces[x][y].getAlliance() == alliance)
	 piecesOfAlliance.add(pieces[x][y]);
	 }
	 }
	
	 return piecesOfAlliance;
	 }

	/**
	 * @return The 2D piece array representing this PieceGrid.
	 */
	public Piece[][] get2dPieceArray () {
		return this.pieces;
	}

	/**
	 * @param pieces - The new 2D piece array.
	 */
	public void setPieces (Piece[][] pieces) {
		this.pieces = pieces;
	}

	/**
	 * Set the given piece down at the given position on the grid.
	 * 
	 * @param pos - The position to put the piece on.
	 * @param piece - The given piece.
	 */
	public void setPieceAt (Position pos, Piece piece) {
		this.pieces[pos.getX()][pos.getY()] = piece;
	}

	/**
	 * Set the given piece down at the given position on the grid.
	 * 
	 * @param file - The file to place the piece on.
	 * @param rank - The rank to place the piece on.
	 * @param piece - The given piece.
	 */
	public void setPieceAt (int file, int rank, Piece piece) {
		this.pieces[Position.fileToX(file)][Position.rankToY(rank)] = piece;
	}

	/**
	 * Set the given piece down at the given position on the grid.
	 * 
	 * @param alphanumeric - The alphanumeric representation of the position to put
	 *            the piece on.
	 * @param piece - The given piece.
	 */
	public void setPieceAt (String alphanumeric, Piece piece) {
		this.setPieceAt(Notation.toPosition(alphanumeric), piece);
	}

	/**
	 * Move a piece from one place on the board to another.
	 * 
	 * @param startPos - The current position of the piece to move.
	 * @param endPos - The position to move the piece to.
	 */
	public void movePiece (Position startPos, Position endPos) {
		Piece moved = this.getPieceAt(startPos);

		moved.setPosition(endPos);
		this.setPieceAt(endPos, moved);
		this.removePieceAt(startPos);

		moved.setHasMoved();
	}

	/**
	 * @param pos - The position to retrieve a piece from.
	 * @return The piece at the given position.
	 */
	public Piece getPieceAt (Position pos) {
		if (!Position.isValid(pos))
			throw new RuntimeException("getPieceAt called with invalid position.");
		return this.pieces[pos.getX()][pos.getY()];
	}

	/**
	 * @param file - The file to get a piece from.
	 * @param rank - The rank to get a piece from.
	 * @return The piece at the given position.
	 */
	public Piece getPieceAt (int file, int rank) {
		if (!Position.isValid(file, rank))
			throw new RuntimeException(
					String.format("getPieceAt called with invalid position: file %d, rank %d", file, rank));
		return this.pieces[Position.fileToX(file)][Position.rankToY(rank)];
	}

	/**
	 * @param alphanumeric - The alphanumeric representation of the position to
	 *            retrieve a piece from.
	 * @return The piece at the given position.
	 */
	public Piece getPieceAt (String alphanumeric) {
		return this.getPieceAt(Notation.toPosition(alphanumeric));
	}

	/**
	 * @param pos - The position to check for a piece.
	 * @return True if there is a piece at the given position.
	 */
	public boolean pieceExistsAt (Position pos) {
		return !(this.getPieceAt(pos) instanceof NoPiece);
	}

	/**
	 * @param pos - The position to check for a piece.
	 * @return True if there is a piece at pos.
	 */
	public boolean pieceExistsAt (int file, int rank) {
		return !(this.getPieceAt(file, rank) instanceof NoPiece);
	}

	/**
	 * @return The white player's king.
	 */
	public Piece getWhiteKing () {
		return this.whiteKing;
	}

	/**
	 * @return The black player's king.
	 */
	public Piece getBlackKing () {
		return this.blackKing;
	}

	/**
	 * Remove a Piece object from the piece grid.
	 * 
	 * @param piece - The piece to remove.
	 */
	public void removePiece (Piece piece) {
		this.removePieceAt(piece.getPosition());
	}

	/**
	 * Remove a piece at a certain position from the piece grid.
	 * 
	 * @param pos - The position to delete a piece at.
	 */
	public void removePieceAt (Position pos) {
		this.setPieceAt(pos, new NoPiece(pos));
	}

	/**
	 * Remove a piece at a certain position from the piece grid.
	 * 
	 * @param file - The file to delete a piece at.
	 * @param rank - The rank to delete a piece at.
	 */
	public void removePieceAt (int file, int rank) {
		this.setPieceAt(file, rank, new NoPiece(file, rank));
	}

	@Override
	public String toString () {
		String out = "";
		for (int rank = 0; rank < NUM_RANKS; rank++) {
			for (int file = 0; file < NUM_FILES; file++) {
				out += this.pieces[file][rank] + " ";
			}
			if (rank < NUM_RANKS - 1)
				out += "\n";
		}
		return out;
	}
}
