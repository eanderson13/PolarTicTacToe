/**	File Name: User.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Implements the controls to allow the user to play Polar Tic Tac Toe.
 *
 *	Author: Erik Anderson
 *
 *	Date: 10/25/14
 */
package polartictactoe;

import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class User extends Player {

	/** Move selected by the user */
	Move selectedPoint = null;
	/** Label to prompt user to select a move */
	Label selectLabel = new Label("Select a move:");
	/** Button to submit move */
	Button select = new Button("Select");

	/**
	 * Sets up the UI to get moves from the user
	 */
	public User() {
		// Create UI content
		Label xLabel = new Label("X (from center):");
		final ComboBox<Integer> xBox = new ComboBox<>(
				FXCollections.observableArrayList(1, 2, 3, 4));
		xBox.getSelectionModel().selectFirst();

		Label yLabel = new Label("Y (around circle):");
		final ComboBox<Integer> yBox = new ComboBox<>(
				FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
						10, 11));
		yBox.getSelectionModel().selectFirst();

		// Update UI
		setHgap(10);
		setVgap(10);
		addRow(0, selectLabel, select);
		addRow(1, xLabel, xBox);
		addRow(2, yLabel, yBox);

		// Select button handler
		select.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Reset UI for later turns
				selectLabel.setTextFill(Color.BLACK);
				selectLabel.setText("Select a move:");

				// Get selected move
				selectedPoint = new Move(xBox.getSelectionModel()
						.getSelectedItem(), yBox.getSelectionModel()
						.getSelectedItem());

				// Signal selection
				synchronized (select) {
					select.notify();
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
	public Move getMove(Set<Move> legalMoves, Set<Move> opponentMoves) {
		while (true) {
			// Wait for move to be submitted
			try {
				synchronized (select) {
					select.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
			// Check for legal move
			if (legalMoves.contains(selectedPoint)) {
				return selectedPoint;
			} else {
				// Notify user of illegal move
				Platform.runLater(new Thread() {
					@Override
					public void run() {
						selectLabel.setTextFill(Color.RED);
						selectLabel.setText("Not a legal move!");
					}
				});
			}
		}
	}

}
