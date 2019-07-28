package chess;

import pieces.*;

/**
 * @author Borna Sadeghi
 * @version Jul 14, 2019
 */
public class Notation {

	/**
	 * Convert an integer position into an alphanumeric position string (e.g. 18 =>
	 * "c6")
	 * 
	 * @param pos - The integer position to convert to alphanumeric.
	 * @return The alphanumeric expression of the position.
	 */
	public static String toAlphanumeric (Position pos) {
		char file = Notation.toFileCharacter(pos.getFile());
		char rank = Notation.toRankCharacter(pos.getRank());

		return file + "" + rank;
	}
	
	public static char toFileCharacter (int file) {
		return (char) (file + '`');
	}
	
	public static char toRankCharacter (int rank) {
		return (char) (rank + '0');
	}

	/**
	 * Convert an alphanumeric position string into an integer position (e.g. "f4"
	 * => 37)
	 * 
	 * @param alphanumeric - The alphanumeric string to convert to an integer
	 *            position.
	 * @return The integer expression of the position.
	 */
	public static Position toPosition (String alphanumeric) {
		int file = alphanumeric.charAt(0) - '`';
		int rank = alphanumeric.charAt(1) - '0';
		return new Position(file, rank);
	}

	public static String getLayout (PieceGrid grid) {
		Piece[][] pieces = grid.get2dPieceArray();
		String layout = "";

		for (int y = 0; y < PieceGrid.NUM_RANKS; y++) {

			int consecutiveEmptySquares = 0;

			for (int x = 0; x < PieceGrid.NUM_FILES; x++) {
				if (!grid.pieceExistsAt(Position.xToFile(x), Position.yToRank(y))) {
					consecutiveEmptySquares++;
				} else {
					if (consecutiveEmptySquares > 0) {
						layout += (char) (consecutiveEmptySquares + '0');
						consecutiveEmptySquares = 0;
					}
					Piece piece = pieces[x][y];
					switch (piece.getPieceType()) {
					case PAWN:
						layout += piece.isWhite() ? "P" : "p";
						break;
					case KNIGHT:
						layout += piece.isWhite() ? "N" : "n";
						break;
					case BISHOP:
						layout += piece.isWhite() ? "B" : "b";
						break;
					case ROOK:
						layout += piece.isWhite() ? "R" : "r";
						break;
					case QUEEN:
						layout += piece.isWhite() ? "Q" : "q";
						break;
					case KING:
						layout += piece.isWhite() ? "K" : "k";
						break;
					default:
						System.out.println("Layout generation has errors.");
						break;
					}
				}
			}
			if (consecutiveEmptySquares > 0)
				layout += (char) (consecutiveEmptySquares + '0');
			if (y < PieceGrid.NUM_RANKS - 1)
				layout += "/";
		}
		return layout;
	}

	public static Piece[][] to2dPieceArray (String layout) {
		String[] ranks = layout.split("/");

		Piece[][] pieces = new Piece[PieceGrid.NUM_FILES][PieceGrid.NUM_RANKS];

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
						pieces[x][y] = new NoPiece(pos);
						x++;
					}
				} else {
					// Letters represent pieces
					switch (c) {
					case 'P':
						pieces[x][y] = new Pawn(pos, Alliance.WHITE);
						break;
					case 'N':
						pieces[x][y] = new Knight(pos, Alliance.WHITE);
						break;
					case 'B':
						pieces[x][y] = new Bishop(pos, Alliance.WHITE);
						break;
					case 'R':
						pieces[x][y] = new Rook(pos, Alliance.WHITE);
						break;
					case 'Q':
						pieces[x][y] = new Queen(pos, Alliance.WHITE);
						break;
					case 'K':
						pieces[x][y] = new King(pos, Alliance.WHITE);
						break;
					case 'p':
						pieces[x][y] = new Pawn(pos, Alliance.BLACK);
						break;
					case 'n':
						pieces[x][y] = new Knight(pos, Alliance.BLACK);
						break;
					case 'b':
						pieces[x][y] = new Bishop(pos, Alliance.BLACK);
						break;
					case 'r':
						pieces[x][y] = new Rook(pos, Alliance.BLACK);
						break;
					case 'k':
						pieces[x][y] = new King(pos, Alliance.BLACK);
						break;
					case 'q':
						pieces[x][y] = new Queen(pos, Alliance.BLACK);
						break;
					default:
						throw new RuntimeException("Invalid FEN layout used to initialize board: Invalid character in FEN layout string.");
					}
					x++;
				}
			}
			if (x < PieceGrid.NUM_FILES) {
				throw new RuntimeException("Invalid FEN layout used to initialize board: Board layout must have 8 files per rank.");
			}
		}
		return pieces;
	}

	/**
	 * Convert a move into the notation for that move.
	 * 
	 * @param move
	 * @return
	 */
	public static String moveNotation (Move move) {
		Piece moved = move.getMovedPiece();
		String notation = "";

		if (moved.getPieceType() == PieceType.PAWN) {
			if (move.isCapture()) {
				notation = Notation.toFileCharacter(move.getStartPos().getFile()) + "x" + Notation.toAlphanumeric(move.getDestPos());
			} else {
				notation = Notation.toAlphanumeric(move.getDestPos());
			}
		} else {
			
			notation = move.getMovedPiece().toString().toUpperCase();
			
			Board board = move.getBoard();
			
			boolean specifyFile = false, specifyRank = false;
			
			// If any other pieces of the same type have the same legal move...
			for (Piece piece : board.getPiecesOfAlliance(moved.getAlliance())) {
				if (piece.getPieceType() == moved.getPieceType() && piece != moved) {
					for (Move otherMove : piece.getLegalMoves(board)) {
						if (otherMove.getDestPos().equals(move.getDestPos())) {
//							System.out.format("Other piece with same move at %s\n", piece.getPosition());
							
							// If other piece on different file and rank, specify file.
							// If the other piece is on different file, we can specify file.
							// If the other piece is on the same file, we can specify rank.
							if (piece.getFile() != moved.getFile()) {
								specifyFile = true;
							} else { // No need to check if rank is different. Another piece cannot occupy the same file and rank as the current one.
//							} else if (piece.getRank() != moved.getRank()){
								specifyRank = true;
							}
							
						}
					}
				}
			}
			
			if (specifyFile)
				notation += Notation.toFileCharacter(move.getStartPos().getFile());
			if (specifyRank)
				notation += Notation.toRankCharacter(move.getStartPos().getRank());

			if (move.isCapture())
				notation += "x";
			notation += Notation.toAlphanumeric(move.getDestPos());
		}

		if (move.leavesOpponentInCheck())
			notation += "+";
		
		return notation/* + "\nNotation generator is still incomplete."*/;
	}
}
