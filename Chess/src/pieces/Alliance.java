package pieces;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 19, 2019
 */
public enum Alliance {
	WHITE {
		@Override
		public Alliance opponent () {
			return BLACK;
		}

		@Override
		public int homeRank () {
			return 1;
		}
		
		@Override
		public int pawnStartingRank () {
			return 2;
		}

		@Override
		public int enPassantRank () {
			return 3;
		}
	},
	BLACK {
		@Override
		public Alliance opponent () {
			return WHITE;
		}

		@Override
		public int homeRank () {
			return 8;
		}
		
		@Override
		public int pawnStartingRank () {
			return 7;
		}

		@Override
		public int enPassantRank () {
			return 6;
		}
	},
	NO_ALLIANCE {
		@Override
		public Alliance opponent () {
			return NO_ALLIANCE;
		}

		@Override
		public int homeRank () {
			return -1;
		}
		
		@Override
		public int pawnStartingRank () {
			return -1;
		}

		@Override
		public int enPassantRank () {
			return -1;
		}
	};
	
	public abstract Alliance opponent ();
	public abstract int pawnStartingRank ();
	public abstract int homeRank ();
	public abstract int enPassantRank(); // The rank behind a pawn after it moves forward two spaces.
	
}
