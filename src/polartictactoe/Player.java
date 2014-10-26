/**	File Name: Player.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Abstract class that all players must implement.
 *
 *	Author: Erik Anderson
 *
 *	Date: 10/25/14
 */
package polartictactoe;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.GridPane;

public abstract class Player extends GridPane {
	
	private Set<Point> moves = new HashSet<>();

	public abstract Point getMove(Set<Point> legalMoves);
	
	public Set<Point> getMoves() {
		return moves;
	}
	
	public void addMove(Point move) {
		moves.add(move);
	}

}
