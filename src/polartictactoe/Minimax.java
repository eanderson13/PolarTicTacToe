/**	File Name: Minimax.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Implements a minimax search algorithm to play Polar Tic Tac Toe.
 *
 *	Author: Erik Anderson
 *
 *	Date: 11/29/14
 */
package polartictactoe;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Minimax extends Player {

	/** Maximum search depth allowed */
	int searchDepth;
	/** Indicates whether alpha-beta pruning is enabled */
	boolean alphabeta;
	/** Heuristic to use */
	Heuristic heuristic;

	/** Maximum search depth reached for the current move */
	int maxDepth;
	/** Number of nodes searched for the current move */
	int nodeCount;
	/** Time elapsed for the current move */
	long time;

	/** Label to display maximum search depth reached */
	Label depthValue = new Label();
	/** Label to display number of nodes searched */
	Label nodeValue = new Label();
	/** Label to display time elapsed */
	Label timeValue = new Label();

	/** Button to submit user parameters */
	Button submit = new Button("Submit Parameters");

	/**
	 * Sets up the UI to get parameters from the user
	 */
	public Minimax() {
		// Create UI content
		Label heuristicLabel = new Label("Heuristic:");
		ComboBox<String> heuristicBox = new ComboBox<>(
				FXCollections.observableArrayList("Function", "Classifier",
						"Neural Net"));
		heuristicBox.getSelectionModel().selectFirst();

		Label depthLabel = new Label("Search depth (0 = unbounded):");
		TextField depthField = new TextField("0");
		depthField.setPrefWidth(0);

		Label abLabel = new Label("Use alpha-beta pruning?");
		ComboBox<String> abBox = new ComboBox<>(
				FXCollections.observableArrayList("Yes", "No"));
		abBox.getSelectionModel().selectFirst();

		// Update UI
		setHgap(10);
		setVgap(10);
		addRow(0, heuristicLabel, heuristicBox);
		addRow(1, depthLabel, depthField);
		addRow(2, abLabel, abBox);
		addRow(3, submit);

		// Submit button handler
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Update parameters
				searchDepth = Integer.parseInt(depthField.getText());
				alphabeta = (abBox.getSelectionModel().getSelectedIndex() == 0);
				switch (heuristicBox.getSelectionModel().getSelectedIndex()) {
				case 0:
					// heuristic = new Heuristic();
					break;
				case 1:
					// heuristic = new Heuristic();
					break;
				case 2:
					// heuristic = new Heuristic();
					break;
				}

				// Signal submission
				synchronized (submit) {
					submit.notify();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see polartictactoe.Player#getMove(java.util.Set, java.util.Set)
	 */
	@Override
	public Point getMove(Set<Point> legalMoves, Set<Point> opponentMoves) {
		// Copy move set
		Set<Point> legal = legalMoves;

		// Check if first move
		if (getMoves().size() == 0) {
			// Wait for parameters to be submitted
			while (true) {
				try {
					synchronized (submit) {
						submit.wait();
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
			}
			// Only need to check one row for first move, because of symmetry
			if (legal.size() == 48) {
				legal = new HashSet<>();
				legal.add(new Point(0, 0));
				legal.add(new Point(1, 0));
				legal.add(new Point(2, 0));
				legal.add(new Point(3, 0));
			}
			// Schedule task for UI thread
			Platform.runLater(new Thread() {
				@Override
				public void run() {
					// Create labels
					Label depthLabel = new Label("Maximum search depth:");
					Label nodeLabel = new Label("Number of nodes searched:");
					Label timeLabel = new Label("Time elapsed (ms):");

					// Update UI
					getChildren().clear();
					addRow(0, depthLabel, depthValue);
					addRow(1, nodeLabel, nodeValue);
					addRow(2, timeLabel, timeValue);
				}
			});
		}
		// Initialize values
		maxDepth = searchDepth - 1;
		nodeCount = 0;
		time = System.currentTimeMillis();

		// Get move
		Point move = maxMove(legal, opponentMoves);

		// Update UI
		Platform.runLater(new Thread() {
			@Override
			public void run() {
				depthValue.setText((searchDepth - maxDepth) + "");
				nodeValue.setText(nodeCount + "");
				timeValue.setText((System.currentTimeMillis() - time) + "");
			}
		});
		// Return move
		return move;
	}

	/**
	 * Returns the move selected by the agent
	 * 
	 * @param legal
	 *            The set of legal moves
	 * @param opponent
	 *            The set of moves made by the opponent
	 * @return The selected move
	 */
	private Point maxMove(Set<Point> legal, Set<Point> opponent) {
		// Initialize values
		double value = Double.NEGATIVE_INFINITY;
		double a = value;
		Point choice = null;

		// Check all legal moves
		for (Point move : legal) {
			// Copy move set
			Set<Point> selfMoves = new HashSet<>(getMoves());
			selfMoves.add(move);

			// Check for winning move
			if (PolarTicTacToe.win(selfMoves, move)) {
				return move;
			}
			// Calculate utility
			double utility = min(PolarTicTacToe.addNeighbors(move, legal,
					selfMoves, opponent), selfMoves, opponent, searchDepth - 1,
					a, Double.POSITIVE_INFINITY);

			// Check if best move so far
			if (utility > value) {
				value = utility;
				choice = move;
				a = Math.max(a, value);
			}
		}
		// Return move
		return choice;
	}

	/**
	 * Returns the optimal utility of the current node for the agent
	 * 
	 * @param legal
	 *            The set of legal moves
	 * @param self
	 *            The set of moves made by the agent
	 * @param opponent
	 *            The set of moves made by the opponent
	 * @param depth
	 *            The search depth reached
	 * @param a
	 *            The optimal utility found so far for the agent
	 * @param b
	 *            The optimal utility found so far for the opponent
	 * @return The optimal utility
	 */
	private double max(Set<Point> legal, Set<Point> self, Set<Point> opponent,
			int depth, double a, double b) {
		// Update status
		maxDepth = Math.min(maxDepth, depth);
		nodeCount++;

		// Check for terminate condition
		if (legal.size() == 0) {
			return 0;
		}
		if (depth == 0) {
			// return heuristic.getValue(self, opponent);
			return 0;
		}
		// Initialize value
		double value = Double.NEGATIVE_INFINITY;
		// Check all legal moves
		for (Point move : legal) {
			// Copy move set
			Set<Point> selfMoves = new HashSet<>(self);
			selfMoves.add(move);

			// Check for winning move
			if (PolarTicTacToe.win(selfMoves, move)) {
				return 1;
			}
			// Calculate utility
			value = Math.max(
					min(PolarTicTacToe.addNeighbors(move, legal, selfMoves,
							opponent), selfMoves, opponent, depth - 1, a, b),
					value);

			// Alpha-beta prune, if enabled
			if (alphabeta && value >= b) {
				return value;
			}
			a = Math.max(a, value);
		}
		// Return utility
		return value;
	}

	/**
	 * Returns the optimal utility of the current node for the opponent
	 * 
	 * @param legal
	 *            The set of legal moves
	 * @param self
	 *            The set of moves made by the agent
	 * @param opponent
	 *            The set of moves made by the opponent
	 * @param depth
	 *            The search depth reached
	 * @param a
	 *            The optimal utility found so far for the agent
	 * @param b
	 *            The optimal utility found so far for the opponent
	 * @return The optimal utility
	 */
	private double min(Set<Point> legal, Set<Point> self, Set<Point> opponent,
			int depth, double a, double b) {
		// Update status
		maxDepth = Math.min(maxDepth, depth);
		nodeCount++;

		// Check for terminate condition
		if (legal.size() == 0) {
			return 0;
		}
		if (depth == 0) {
			// return heuristic.getValue(self, opponent);
			return 0;
		}
		// Initialize value
		double value = Double.POSITIVE_INFINITY;
		// Check all legal moves
		for (Point move : legal) {
			// Copy move set
			Set<Point> opponentMoves = new HashSet<>(opponent);
			opponentMoves.add(move);

			// Check for winning move
			if (PolarTicTacToe.win(opponentMoves, move)) {
				return -1;
			}
			// Calculate utility
			value = Math.min(
					max(PolarTicTacToe.addNeighbors(move, legal, self,
							opponentMoves), self, opponentMoves, depth - 1, a,
							b), value);

			// Alpha-beta prune, if enabled
			if (alphabeta && value <= a) {
				return value;
			}
			b = Math.min(b, value);
		}
		// Return utility
		return value;
	}

}
