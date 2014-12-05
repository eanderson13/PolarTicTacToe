package polartictactoe;

import java.util.Set;
/**
 * 
 * @author Rob Lewis
 *	Class implements singleton method and calculates
 *  a heuristic 
 */
public class NeighborHeuristic extends Heuristic {
	private  NeighborHeuristic() { } // Empty private constructor
	private static final NeighborHeuristic INSTANCE = new NeighborHeuristic();
	public static NeighborHeuristic getInstance() {
		return INSTANCE;
	}
	/**
	 * Returns a value between -1 and 1 representing the quality of the current
	 * state. -1 corresponds to an expected loss, 1 corresponds to an expected
	 * win.
	 * 
	 * This heuristic calculates a value based on the number of 'outs' or game
	 * winning move sequences possible. For each point on the board, there is
	 * a single out based upon radial moves along the circle, two outs based
	 * upon diagonal moves radially and along the circle, and four outs based
	 * upon moves that circumnavigate the level of the circle where the point
	 * is located.
	 * 
	 * 
	 * @param selfMoves
	 *            The set of moves made by the agent
	 * @param opponentMoves
	 *            The set of moves made by the opponent
	 * @return The value of the current state
	 */
	@Override
	public double getValue(Set<Move> selfMoves,
			Set<Move> opponentMoves) {
		double heuristic = oneSidedValue(selfMoves, opponentMoves) - oneSidedValue(opponentMoves, selfMoves);
		return heuristic;
	}
	/**
	 * 
	 * @param posMoves The set of moves that a positive board value can be attributed to
	 * @param negMoves The set of moves that decrement the positive board value
	 * @return A value between 0 and 1 that represents the 'offensive' quality of the
	 * 		   current posmoves set.
	 */
	private double oneSidedValue(Set<Move> posMoves, Set<Move> negMoves) {
		double value = 0.0;
		for (Move p:posMoves) {
			double val1 = value; //ROBL
			// Check moves along radial line
			int y = (int) p.getY();
			double lineValue = 0.0; // Heuristic evaluation for the radial line
			for (int xVal = 1; xVal <= 4; xVal++) {
				Move curPoint = new Move(xVal, y); // The point to be tested for belonging in either set
				if (pathEvaluator(posMoves, negMoves, curPoint) == -1.0) {
					lineValue = 0.0;
					break;
				}
				else {
					lineValue += pathEvaluator(posMoves, negMoves, curPoint);
				}
			}
			value += lineValue;		// Add radial component to overall value of heuristic

			// Check moves along counter-clockwise spiral
			double ccSpiralValue = 0.0; // Heuristic evaluation for the counter clock wise spiral
			y = (int) (p.getY() - p.getX() + 13) % 12;
			for (int xVal = 1; xVal <= 4; xVal++) {
				Move curPoint = new Move(xVal, (y + xVal - 1) % 12);
				if (pathEvaluator(posMoves, negMoves, curPoint) == -1.0) {
					ccSpiralValue = 0.0;
					break;
				}
				else {
					ccSpiralValue += pathEvaluator(posMoves, negMoves, curPoint);
				}
			}
			value += ccSpiralValue;

			// Check moves along clockwise spiral
			y = (int) (p.getY() + p.getX() - 1) % 12;
			double cSpiralValue = 0.0; // Heuristic evaluation for the counter clock wise spiral
			for (int xVal = 1; xVal <= 4; xVal++) {
				Move curPoint = new Move(xVal, (y + 13 - xVal) % 12);
				if (pathEvaluator(posMoves, negMoves, curPoint) == -1.0) {
					cSpiralValue = 0.0;
					break;
				}
				else {
					cSpiralValue += pathEvaluator(posMoves, negMoves, curPoint);
				}
			}
			value += cSpiralValue;

			// Check moves along circle
			int x = (int) p.getX();
			// Unify with all 4 terms of inference rule
			for (int i = 0; i < 4; i++) {
				y = (int) (p.getY() - i + 12) % 12;
				Move point1 = new Move(x, y);
				Move point2 = new Move(x, (y + 1) % 12);
				Move point3 = new Move(x, (y + 2) % 12);
				Move point4 = new Move(x, (y + 3) % 12);
				if ( !(negMoves.contains(point1)
						|| negMoves.contains(point2)		// if the points are not contained
						|| negMoves.contains(point3)		// in the opponents set... 
						|| negMoves.contains(point4))) {
					value += pathEvaluator(posMoves, negMoves, point1);
					value += pathEvaluator(posMoves, negMoves, point2);
					value += pathEvaluator(posMoves, negMoves, point3);
					value += pathEvaluator(posMoves, negMoves, point4);
				}
			}
		}
		if (posMoves.size() > 0) {
			return value / posMoves.size();
		}
		return value;
	}
	/**
	 * Evaluates for any given point, its value toward winning the game.
	 * @param posMoves The set of moves that a positive board value can be attributed to
	 * @param negMoves The set of moves that decrement the positive board value
	 * @param curPoint The point to be tested
	 * @return A value between -1 and 0.33 representing the value of the point. -1 implies
	 * that the point has no value toward winning the game.
	 */
	private double pathEvaluator(Set<Move> posMoves, Set<Move> negMoves, Move curPoint) {
		double lineValue = 0.0;
		if (negMoves.contains(curPoint)) {
			return -1; // win impossible along current path
		}
		if (posMoves.contains(curPoint)) {
			lineValue += 0.333; // Increment the value
		}
		return lineValue;
	}
}