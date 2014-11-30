/**	File Name: PolarTicTacToe.java
 *	
 *	Project: Polar Tic Tac Toe
 *	
 *	Description: Creates the user interface for playing Polar Tic Tac Toe.
 *
 *	Author: Erik Anderson
 *
 *	Date: 10/25/14
 */
package polartictactoe;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PolarTicTacToe extends Application {

	/** Location of the center of the game board */
	final int center = 140;
	/** Spacing between circles on the game board */
	final int r0 = 25;
	/** Radii of the circles on the game board */
	final int[] r = { 2 * r0, 3 * r0, 4 * r0, 5 * r0 };

	/** List of agent types */
	ObservableList<String> choices = FXCollections.observableArrayList("User",
			"Minimax");
	/** Selection box for player 1 agent type */
	ComboBox<String> boxp1 = new ComboBox<>(choices);
	/** Selection box for player 2 agent type */
	ComboBox<String> boxp2 = new ComboBox<>(choices);
	/** Container for drawing the game board */
	Group board = new Group();
	/** Button to start game */
	Button play = new Button("Play");
	/** Label to report game outcome */
	Label message = new Label();

	/** Player 1 agent */
	Player player1;
	/** Player 2 agent */
	Player player2;

	/** Set of legal moves */
	Set<Point> legalMoves = new HashSet<>();

	/**
	 * Starts the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Polar Tic Tac Toe");

		// Quit program on UI close
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {
				System.exit(0);
			}

		});

		// Create UI content
		Label labelp1 = new Label("Player 1 (X):");
		Label labelp2 = new Label("Player 2 (O):");

		// Lay out UI content
		GridPane layout = new GridPane();
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(10));
		layout.setPrefSize(475, 350);
		layout.add(board, 0, 0, 1, 6);
		layout.addRow(0, labelp1, boxp1);
		layout.addRow(2, labelp2, boxp2);
		layout.add(message, 1, 4, 2, 1);
		layout.add(play, 2, 5);

		// Play button handler
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Create new player 1 agent
				layout.getChildren().remove(player1);
				switch (boxp1.getSelectionModel().getSelectedIndex()) {
				case 0:
					player1 = new User();
					break;
				case 1:
					player1 = new Minimax();
					break;
				}
				layout.add(player1, 1, 1, 2, 1);

				// Create new player 2 agent
				layout.getChildren().remove(player2);
				switch (boxp2.getSelectionModel().getSelectedIndex()) {
				case 0:
					player2 = new User();
					break;
				case 1:
					player2 = new Minimax();
					break;
				}
				layout.add(player2, 1, 3, 2, 1);

				// Disable agent selection controls
				player2.setDisable(true);
				boxp1.setDisable(true);
				boxp2.setDisable(true);
				play.setVisible(false);
				message.setText("");

				// Refresh game board
				layout.getChildren().remove(board);
				drawBoard();
				layout.add(board, 0, 0, 1, 6);

				// Initialize legal moves for first move
				Set<Point> firstMoves = new HashSet<>();
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 12; j++) {
						firstMoves.add(new Point(i, j));
					}
				}
				// Start game
				play(player1, player2, firstMoves);
			}
		});
		// Ensure an agent type is selected
		boxp1.getSelectionModel().selectFirst();
		boxp2.getSelectionModel().selectFirst();

		// Display UI
		primaryStage.setScene(new Scene(layout));
		primaryStage.show();
	}

	/**
	 * Draws the game board in the UI
	 */
	private void drawBoard() {
		// Initialize game board
		board = new Group();

		// Draw circles
		Circle circle1 = new Circle(center, center, r[0]);
		circle1.setFill(null);
		circle1.setStroke(Color.BLACK);
		Circle circle2 = new Circle(center, center, r[1]);
		circle2.setFill(null);
		circle2.setStroke(Color.BLACK);
		Circle circle3 = new Circle(center, center, r[2]);
		circle3.setFill(null);
		circle3.setStroke(Color.BLACK);
		Circle circle4 = new Circle(center, center, r[3]);
		circle4.setFill(null);
		circle4.setStroke(Color.BLACK);

		// Line endpoints relative to 0
		final int[] t = { 130, 112, 65 };
		// Line endpoints relative to center
		final int[] y = { center + t[0], center + t[1], center + t[2], center,
				center - t[2], center - t[1], center - t[0] };

		// Draw lines
		Line line1 = new Line(y[0], y[3], y[6], y[3]);
		Line line2 = new Line(y[1], y[4], y[5], y[2]);
		Line line3 = new Line(y[2], y[5], y[4], y[1]);
		Line line4 = new Line(y[3], y[6], y[3], y[0]);
		Line line5 = new Line(y[4], y[5], y[2], y[1]);
		Line line6 = new Line(y[5], y[4], y[1], y[2]);

		// Label offset
		final int offset = 10;

		// Draw radial labels
		Text text1 = new Text(y[0], y[3], "1");
		Text text2 = new Text(y[1], y[4], "2");
		Text text3 = new Text(y[2], y[5], "3");
		Text text4 = new Text(y[3], y[6], "4");
		Text text5 = new Text(y[4] - offset, y[5], "5");
		Text text6 = new Text(y[5] - offset, y[4], "6");
		Text text7 = new Text(y[6] - offset, y[3], "7");
		Text text8 = new Text(y[5] - offset, y[2] + offset, "8");
		Text text9 = new Text(y[4] - offset, y[1] + offset, "9");
		Text text10 = new Text(y[3] - offset, y[0] + offset, "10");
		Text text11 = new Text(y[2], y[1] + offset, "11");
		Text text12 = new Text(y[1], y[2] + offset, "12");

		// Label coordinates
		final int[] x = { center + r[0], center + r[1], center + r[2],
				center + r[3] };

		// Draw x labels
		Text textx1 = new Text(x[0] - offset, center + offset, "1");
		Text textx2 = new Text(x[1] - offset, center + offset, "2");
		Text textx3 = new Text(x[2] - offset, center + offset, "3");
		Text textx4 = new Text(x[3] - offset, center + offset, "4");

		// Draw board
		board.getChildren().addAll(circle1, circle2, circle3, circle4, line1,
				line2, line3, line4, line5, line6, text1, text2, text3, text4,
				text5, text6, text7, text8, text9, text10, text11, text12,
				textx1, textx2, textx3, textx4);
	}

	/**
	 * Executes the move made by the current player.
	 * 
	 * @param player
	 *            The current player
	 * @param next
	 *            The next player
	 * @param legal
	 *            The set of legal moves
	 */
	private void play(Player player, Player next, Set<Point> legal) {
		// Create a task to get the next move
		Task<Point> moveTask = new Task<Point>() {
			@Override
			protected Point call() {
				return player.getMove(legal, next.getMoves());
			}
		};

		// Wait until the move is returned
		moveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent e) {
				// Get move
				Point move = moveTask.getValue();
				player.addMove(move);

				// Update legal moves
				legalMoves = addNeighbors(move, legalMoves, player1.getMoves(),
						player2.getMoves());

				// Update game board
				drawMove(move, player);
				player.setDisable(true);

				// Check for winning move
				if (win(player.getMoves(), move)) {
					if (player == player1) {
						message.setText("Game Over! Player 1 wins.");
					} else {
						message.setText("Game Over! Player 2 wins.");
					}
					// Check for tie game
				} else if (legalMoves.isEmpty()) {
					message.setText("Game Over! Tie game.");
				} else {
					// Play next turn
					next.setDisable(false);
					play(next, player, legalMoves);
				}
				// Reset the game board
				boxp1.setDisable(false);
				boxp2.setDisable(false);
				play.setVisible(true);
				play.setText("Play Again");
			}
		});
		// Start task in a new thread
		new Thread(moveTask).start();
	}

	/**
	 * Draws the current move on the board
	 * 
	 * @param move
	 *            The current move
	 * @param player
	 *            The current player
	 */
	private void drawMove(Point move, Player player) {
		// Initialize settings
		Text symbol = new Text();
		symbol.setFont(Font.font("System", FontWeight.BOLD, 20));

		// Check which player is the current player
		if (player == player1) {
			symbol.setText("X");
			symbol.setFill(Color.RED);
		} else {
			symbol.setText("O");
			symbol.setFill(Color.BLUE);
		}
		// Calculate the location of the move on the game board
		int radius = r[(int) move.getX()];
		double angle = (12 - move.getY()) * Math.PI / 6;
		symbol.setX(center + radius * Math.cos(angle) - 7);
		symbol.setY(center + radius * Math.sin(angle) + 7);

		// Update the game board
		board.getChildren().add(symbol);
	}

	/**
	 * Updates the set of legal moves after a move is made.
	 * 
	 * @param move
	 *            The current move
	 * @param currentMoves
	 *            The current set of legal moves
	 * @param p1Moves
	 *            The set of moves made by player 1
	 * @param p2Moves
	 *            The set of moves made by player 2
	 * @return The new set of legal moves
	 */
	public static Set<Point> addNeighbors(Point move, Set<Point> currentMoves,
			Set<Point> p1Moves, Set<Point> p2Moves) {
		// Copy move set
		Set<Point> moves = new HashSet<>(currentMoves);

		// Add moves on same circle
		moves.add(new Point((int) move.getX(), (int) (move.getY() + 11) % 12));
		moves.add(new Point((int) move.getX(), (int) (move.getY() + 1) % 12));

		// Add moves on inward circle
		if (move.getX() > 0) {
			moves.add(new Point((int) move.getX() - 1, (int) move.getY()));
			moves.add(new Point((int) move.getX() - 1,
					(int) (move.getY() + 11) % 12));
			moves.add(new Point((int) move.getX() - 1,
					(int) (move.getY() + 1) % 12));
		}
		// Add moves on outward circle
		if (move.getX() < 3) {
			moves.add(new Point((int) move.getX() + 1, (int) move.getY()));
			moves.add(new Point((int) move.getX() + 1,
					(int) (move.getY() + 11) % 12));
			moves.add(new Point((int) move.getX() + 1,
					(int) (move.getY() + 1) % 12));
		}
		// Remove moves already made
		moves.removeAll(p1Moves);
		moves.removeAll(p2Moves);

		// Return moves
		return moves;
	}

	/**
	 * Checks if the current move results in a win
	 * 
	 * @param moves
	 *            The set of moves made by the player
	 * @param currentMove
	 *            The current move
	 * @return true if the current move results in a win
	 * @return false otherwise
	 */
	public static boolean win(Set<Point> moves, Point currentMove) {
		// Ensure the current move is in the move set
		moves.add(currentMove);

		// Check moves along a line
		if (moves.contains(new Point(0, (int) currentMove.getY()))
				&& moves.contains(new Point(1, (int) currentMove.getY()))
				&& moves.contains(new Point(2, (int) currentMove.getY()))
				&& moves.contains(new Point(3, (int) currentMove.getY()))) {
			return true;
		}
		// No win found
		return false;
	}

}
