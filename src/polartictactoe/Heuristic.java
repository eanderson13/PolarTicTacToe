/**	File Name: Heuristic.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Abstract class that all Polar Tic Tac Toe heuristic functions must implement.
 *
 *	Author: Erik Anderson
 *
 *	Date: 10/25/14
 */
package polartictactoe;

import java.awt.Point;
import java.util.Set;

public abstract class Heuristic {

	/**
	 * Returns a value between -1 and 1 representing the quality of the current
	 * state. -1 corresponds to an expected loss, 1 corresponds to an expected
	 * win.
	 * 
	 * @param selfMoves
	 *            The set of moves made by the agent
	 * @param opponentMoves
	 *            The set of moves made by the opponent
	 * @return The value of the current state
	 */
	public abstract double getValue(Set<Point> selfMoves,
			Set<Point> opponentMoves);

}
