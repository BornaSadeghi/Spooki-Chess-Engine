package evaluators;

import chess.Board;
import pieces.Alliance;
import pieces.Piece;

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version Jul 27, 2019
 */
public class Evaluator_1 implements BoardEvaluator {

	/**
	 * 
	 */
	@Override
	public int evaluate (Board board) {
		return this.scorePlayer (board, Alliance.WHITE) - this.scorePlayer (board, Alliance.BLACK);
	}

	/**
	 * @param board
	 * @param white
	 * @return
	 */
	private int scorePlayer (Board board, Alliance alliance) {
		return calculatePieceValues(board, alliance) +
				calculateMobility(board, alliance);
	}
	
	private static int calculatePieceValues (Board board, Alliance alliance) {
		int score = 0;
		for (Piece piece : board.getPiecesOfAlliance(alliance)) {
			score += piece.getValue();
		}
		return score;
	}
	
	private static int calculateMobility (Board board, Alliance alliance) {
		return board.getLegalMoves(alliance).size();
	}

}
