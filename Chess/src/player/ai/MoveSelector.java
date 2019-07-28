package player.ai;

import chess.Board;
import chess.Move;
import evaluators.BoardEvaluator;
import pieces.Alliance;

/**
 * @author Borna Sadeghi
 * @version Jul 27, 2019
 */
public class MoveSelector {

	private BoardEvaluator evaluator;
	private int searchDepth;

	public MoveSelector (int searchDepth, BoardEvaluator evaluator) {
		this.searchDepth = searchDepth;
		this.evaluator = evaluator;
	}

	public Move bestMove (Board board) {
		long currentTime = System.currentTimeMillis();
		
		Move bestMove = null;
		
		int curValue;

		if (board.getCurrentPlayer() == Alliance.WHITE) {
			
			int highest = Integer.MIN_VALUE;
			for (Move move : board.getLegalMoves(Alliance.WHITE)) {
				curValue = this.min(move.execute(), this.searchDepth-1);
				if (curValue > highest) {
					highest = curValue;
					bestMove = move;
				}
			}
		} else {
			
			int lowest = Integer.MAX_VALUE;
			for (Move move : board.getLegalMoves(Alliance.BLACK)) {
				curValue = this.max(move.execute(), this.searchDepth-1);
				if (curValue < lowest) {
					lowest = curValue;
					bestMove = move;
				}
			}
		}
		
		System.out.println(System.currentTimeMillis() - currentTime);
		
		return bestMove;
	}

	// Minimax

	private int min (Board board, int depth) {
		if (depth <= 0 || board.gameOver()) { // Base case
			return this.evaluator.evaluate(board);
		}

		int lowest = Integer.MAX_VALUE;

		for (Move move : board.getLegalMoves(board.getCurrentPlayer())) {
			int curValue = this.max(move.execute(), depth - 1);

			if (curValue < lowest) {
				lowest = curValue;
			}
		}
		
//		System.out.format("Min returned %d at depth %d.\n", lowest, depth);

		return lowest;
	}

	private int max (Board board, int depth) {
		if (depth <= 0 || board.gameOver()) { // Base case
			return this.evaluator.evaluate(board);
		}

		int highest = Integer.MIN_VALUE;

		for (Move move : board.getLegalMoves(board.getCurrentPlayer())) {
			int curValue = this.min(move.execute(), depth - 1);

			if (curValue > highest) {
				highest = curValue;
			}
		}

//		System.out.format("Max returned %d at depth %d.\n", highest, depth);
		
		return highest;
	}
}
