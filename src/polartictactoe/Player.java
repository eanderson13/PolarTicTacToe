/**	File Name: Player.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Abstract class that all Polar Tic Tac Toe agents must implement.
 *
 *	Author: Erik Anderson
 *
 *	Date: 10/25/14
 */
package polartictactoe;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.GridPane;

public abstract class Player extends GridPane {

	/** Set of moves made by the agent */
	private Set<Move> moves = new HashSet<>();

	/**
	 * Returns the move selected by the agent
	 * 
	 * @param legalMoves
	 *            The set of legal moves
	 * @param opponentMoves
	 *            The set of moves made by the opponent
	 * @return The selected move
	 */
	public abstract Move getMove(Set<Move> legalMoves, Set<Move> opponentMoves);

	/**
	 * Returns the set of moves made by the agent
	 * 
	 * @return The set of moves
	 */
	public Set<Move> getMoves() {
		return moves;
	}

	/**
	 * Adds a move to the set of moves made by the agent
	 * 
	 * @param move
	 *            The move to add
	 */
	public void addMove(Move move) {
		moves.add(move);
	}

}
