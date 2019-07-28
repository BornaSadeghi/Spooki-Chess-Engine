package chess;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version Jul 16, 2019
 */
public class Position {

	// The actual values that correspond to the file and rank.
	private int x, y;

	/**
	 * Create a new Position object.
	 * 
	 * @param file - x-coordinate
	 * @param rank - y-coordinate
	 */
	public Position (int file, int rank) {
		this.x = fileToX(file);
		this.y = rankToY(rank);

//		if (!isValid(this))
//			System.out.format("Warning: Invalid position initialized: %s\n", this);
	}

	public int getFile () {
		return xToFile(this.x);
	}

	public int getX () {
		return x;
	}

	public int getRank () {
		return yToRank(this.y);
	}

	public int getY () {
		return y;
	}

	/**
	 * Convert a file to the actual index in the piece array.
	 * @param file
	 * @return
	 */
	public static int fileToX (int file) {
		return file-1;
	}

	/**
	 * Convert a piece array index to a file.
	 * @param x
	 * @return
	 */
	public static int xToFile (int x) {
		return x+1;
	}

	/**
	 * Convert a rank to the actual index in the piece array.
	 * @param rank
	 * @return
	 */
	public static int rankToY (int rank) {
		return PieceGrid.NUM_RANKS-rank;
	}
	
	/**
	 * Convert a piece array index to a rank.
	 * @param y
	 * @return
	 */
	public static int yToRank (int y) {
		return PieceGrid.NUM_RANKS-y;
	}

	public boolean isValid () {
		return 1 <= this.getFile() && this.getFile() <= 8 && 1 <= this.getRank() && this.getRank() <= 8;
	}
	
	public static boolean isValid (Position pos) {
		return 1 <= pos.getFile() && pos.getFile() <= 8 && 1 <= pos.getRank() && pos.getRank() <= 8;
	}
	
	public static boolean isValid (int file, int rank) {
		return 1 <= file && file <= PieceGrid.NUM_FILES && 1 <= rank && rank <= PieceGrid.NUM_RANKS;
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj == null) return false;
		Position other = (Position) obj;
		return this.x == other.x && this.y == other.y;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString () {
//		return String.format("(file %d, rank %d)", this.getFile(), this.getRank());
		return Notation.toAlphanumeric(this);
	}
}
