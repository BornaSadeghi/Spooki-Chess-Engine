package chess;

import java.util.ArrayList;
import java.util.Scanner;

import evaluators.Evaluator_1;
import pieces.*;
import player.ai.MoveSelector;

// TODO:
// Clean up moveNotation for each kind of move. (Subclass Move for more move types?)
// Clean up code and improve efficiency where possible.
// Begin work on board evaluator, minimax algorithm.

/**
 * 
 * 
 * @author Borna Sadeghi
 * @version May 16, 2019
 */
public class Game {

	public static void main(String[] args) {
		Scanner input = new Scanner (System.in);
//		Board board = new Board(Board.DEFAULT_START_LAYOUT);
		Board board = new Board("k7/5Q2/8/8/8/8/8/7K");
		
		boolean whiteIsComputer = false;
		boolean blackIsComputer = true;
		
		MoveSelector engine = new MoveSelector (0, new Evaluator_1());
		
		String in = "";
		
		while (!board.gameOver()) {
			if (board.getCurrentPlayer() == Alliance.WHITE && whiteIsComputer || 
					board.getCurrentPlayer() == Alliance.BLACK && blackIsComputer) {
				Move computerMove = engine.bestMove(board);
				System.out.format("%s chooses to play %s.\n", board.getCurrentPlayer(), computerMove);
				board = board.makeMove(computerMove);
				continue;
			}
			ArrayList<Move> legalMoves = board.getLegalMoves(board.getCurrentPlayer());
			
			System.out.println(board);
//			System.out.println(legalMoves);
			
			System.out.print("Enter move: ");
			
			in = input.nextLine();
			
			if (in.equals("0")) break;
			
			boolean moveMade = false;
			for (Move move : legalMoves) {
				if (in.equals(move.toString())) {
					board = board.makeMove(move);
					moveMade = true;
					break;
				}
			}
			
			if (!moveMade)
				System.out.format("### INVALID MOVE ENTERED: \"%s\" ###\n", in);
//				throw new RuntimeException("Invalid move entered: " + in);
			System.out.println();
		}
		
		input.close();
	}

}
