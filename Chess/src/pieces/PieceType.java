package pieces;

/**
 * @author Borna Sadeghi
 * @version May 20, 2019
 */
public enum PieceType {
	KING {
		@Override
		public String toString () {
			return"K";
		}

		@Override
		public int getValue () {
			return 10000;
		}
	},
	QUEEN {
		@Override
		public String toString () {
			return"Q";
		}

		@Override
		public int getValue () {
			return 900;
		}
	},
	ROOK {
		@Override
		public String toString () {
			return"R";
		}

		@Override
		public int getValue () {
			return 500;
		}
	},
	BISHOP {
		@Override
		public String toString () {
			return"B";
		}

		@Override
		public int getValue () {
			return 300;
		}
	},
	KNIGHT {
		@Override
		public String toString () {
			return "N";
		}

		@Override
		public int getValue () {
			// TODO Auto-generated method stub
			return 300;
		}
	},
	PAWN {
		@Override
		public String toString () {
			return "P";
		}

		@Override
		public int getValue () {
			// TODO Auto-generated method stub
			return 100;
		}

	},
	NO_PIECE {
		@Override
		public String toString () {
			return ".";
		}

		@Override
		public int getValue () {
			return 0;
		}

	};

	public abstract int getValue ();
	
	@Override
	public abstract String toString ();
}
