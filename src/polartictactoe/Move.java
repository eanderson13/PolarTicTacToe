/**	File Name: Move.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: A class that extends java.awt.Point in order to print moves in the
 *				 desired format.
 *
 *	Author: Erik Anderson
 *
 *	Date: 12/4/14
 */
package polartictactoe;

import java.awt.Point;

public class Move extends Point {

	/** Version ID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a move with the specified coordinates
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public Move(int x, int y) {
		super(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Point#toString()
	 */
	public String toString() {
		return "(" + (int) getX() + "," + (int) getY() + ")";
	}

}
