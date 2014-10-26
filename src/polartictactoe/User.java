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

import java.awt.Point;
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

	Point selectedPoint = null;
	Label selectLabel = new Label("Select a move:");
	Button select = new Button("Select");

	public User() {
		Label xLabel = new Label("X (from center):");
		ComboBox<String> xBox = new ComboBox<>(
				FXCollections.observableArrayList("1", "2", "3", "4"));
		xBox.getSelectionModel().selectFirst();

		Label yLabel = new Label("Y (around circle):");
		ComboBox<String> yBox = new ComboBox<>(
				FXCollections.observableArrayList("1", "2", "3", "4", "5", "6",
						"7", "8", "9", "10", "11", "12"));
		yBox.getSelectionModel().selectFirst();

		setHgap(10);
		setVgap(10);
		addRow(0, selectLabel, select);
		addRow(1, xLabel, xBox);
		addRow(2, yLabel, yBox);

		select.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				selectLabel.setTextFill(Color.BLACK);
				selectLabel.setText("Select a move:");
				selectedPoint = new Point(xBox.getSelectionModel()
						.getSelectedIndex(), yBox.getSelectionModel()
						.getSelectedIndex());
				synchronized (select) {
					select.notify();
				}
			}
		});
	}

	@Override
	public Point getMove(Set<Point> legalMoves) {
		while (true) {
			try {
				synchronized (select) {
					select.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
			if (legalMoves.contains(selectedPoint)) {
				return selectedPoint;
			} else {
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
