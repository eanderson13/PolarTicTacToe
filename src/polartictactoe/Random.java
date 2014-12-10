/**
 * 
 */
package polartictactoe;

import java.io.PrintWriter;
import java.util.Set;

/**
 * @author Dell
 *
 */
public class Random extends Player {

	/** PrintWriter to output results */
	PrintWriter results;

	public Random(PrintWriter result) {
		results = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polartictactoe.Player#getMove(java.util.Set, java.util.Set)
	 */
	@Override
	public Move getMove(Set<Move> legalMoves, Set<Move> opponentMoves) {
		Move move = (Move) legalMoves.toArray()[(int) (Math.random()
				* legalMoves.size())];
		results.printf("%d %d ", (int) move.getX(), (int) move.getY());
		return move;
	}

}
