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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
import javafx.scene.control.TextField;
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
	ObservableList<String> choices = FXCollections.observableArrayList("Human",
			"Computer");
	/** Selection box for player 1 agent type */
	ComboBox<String> boxp1 = new ComboBox<>(choices);
	/** Selection box for player 2 agent type */
	ComboBox<String> boxp2 = new ComboBox<>(choices);
	/** Container for drawing the game board */
	Group board = new Group();
	/** Label for number of games to play */
	Label countLabel = new Label("Number of Games:");
	/** Text field for number of games to play */
	TextField countField = new TextField("1");
	/** Button to start game */
	Button play = new Button("Play");
	/** Label to report game outcome */
	Label message = new Label();

	/** The number of games to play */
	int count;

	/** Player 1 agent */
	Player player1;
	/** Player 2 agent */
	Player player2;
	/** PrintWriter to output player 1's results */
	PrintWriter p1Results;
	/** PrintWriter to output player 2's results */
	PrintWriter p2Results;

	/** Set of legal moves */
	Set<Move> legalMoves;

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
		// Initialization
		primaryStage.setTitle("Polar Tic Tac Toe");
		try {
			p1Results = new PrintWriter("data/p1Results.csv");
			p2Results = new PrintWriter("data/p2Results.csv");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Quit program on UI close
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {
				p1Results.flush();
				p1Results.close();
				p2Results.flush();
				p2Results.close();
				System.exit(0);
			}

		});

		// Create UI content
		Label labelp1 = new Label("Player 1 (X):");
		Label labelp2 = new Label("Player 2 (O):");

		// Lay out UI content
		final GridPane layout = new GridPane();
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(10));
		layout.setPrefSize(600, 400);
		layout.add(board, 0, 0, 1, 7);
		layout.addRow(0, labelp1, boxp1);
		layout.addRow(2, labelp2, boxp2);
		layout.add(message, 1, 4, 2, 1);
		layout.addRow(5, countLabel, countField);
		layout.add(play, 2, 6);

		// Play button handler
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Get number of games to play
				try {
					count = Integer.parseInt(countField.getText()) - 1;
				} catch (NumberFormatException e1) {
					return;
				}
				countField.setText(count + "");
				p1Results.println("Number of games left: " + count);
				p2Results.println("Number of games left: " + count);

				// Create new player 1 agent
				layout.getChildren().remove(player1);
				switch (boxp1.getSelectionModel().getSelectedIndex()) {
				case 0:
					player1 = new User();
					break;
				case 1:
					player1 = new Minimax(p1Results);
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
					player2 = new Minimax(p2Results);
					break;
				}
				layout.add(player2, 1, 3, 2, 1);

				// Disable agent selection controls
				player2.setDisable(true);
				boxp1.setDisable(true);
				boxp2.setDisable(true);
				countField.setDisable(true);
				play.setVisible(false);
				message.setText("");

				// Refresh game board
				layout.getChildren().remove(board);
				drawBoard();
				layout.add(board, 0, 0, 1, 6);

				// Initialize legal moves for first move
				legalMoves = new HashSet<>();
				Set<Move> firstMoves = new HashSet<>();
				for (int i = 1; i <= 4; i++) {
					for (int j = 0; j < 12; j++) {
						firstMoves.add(new Move(i, j));
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
		Text text1 = new Text(y[0], y[3], "0");
		Text text2 = new Text(y[1], y[4], "1");
		Text text3 = new Text(y[2], y[5], "2");
		Text text4 = new Text(y[3], y[6], "3");
		Text text5 = new Text(y[4] - offset, y[5], "4");
		Text text6 = new Text(y[5] - offset, y[4], "5");
		Text text7 = new Text(y[6] - offset, y[3], "6");
		Text text8 = new Text(y[5] - offset, y[2] + offset, "7");
		Text text9 = new Text(y[4] - offset, y[1] + offset, "8");
		Text text10 = new Text(y[3] - offset, y[0] + offset, "9");
		Text text11 = new Text(y[2], y[1] + offset, "10");
		Text text12 = new Text(y[1], y[2] + offset, "11");

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
	private void play(final Player player, final Player next,
			final Set<Move> legal) {
		// Create a task to get the next move
		final Task<Move> moveTask = new Task<Move>() {
			@Override
			protected Move call() {
				return player.getMove(legal, next.getMoves());
			}
		};

		// Wait until the move is returned
		moveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent e) {
				// Get move
				Move move = moveTask.getValue();
				player.addMove(move);

				// Update legal moves
				legalMoves = addNeighbors(move, legalMoves, player1.getMoves(),
						player2.getMoves());

				// Update game board
				drawMove(move, player);
				player.setDisable(true);

				// Check for winning move
				if (printWin(player.getMoves(), move)) {
					// Indicate winner
					if (player == player1) {
						message.setText("Game Over! Player 1 wins.");
						p1Results.println(",,,,,1");
						p2Results.println(",,,,,0");
					} else {
						message.setText("Game Over! Player 2 wins.");
						p1Results.println(",,,,,0");
						p2Results.println(",,,,,1");
					}
					// Reset board
					resetBoard();

					// Check for tie game
				} else if (legalMoves.isEmpty()) {
					// Indicate tie
					message.setText("Game Over! Tie game.");

					// Reset board
					resetBoard();
				} else {
					// Play next turn
					next.setDisable(false);
					System.out.println("Legal neighborhood: " + legalMoves);
					play(next, player, legalMoves);
				}
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
	private void drawMove(Move move, Player player) {
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
		int radius = r[(int) move.getX() - 1];
		double angle = (12 - move.getY()) * Math.PI / 6;
		symbol.setX(center + radius * Math.cos(angle) - 7);
		symbol.setY(center + radius * Math.sin(angle) + 7);

		// Update the game board
		board.getChildren().add(symbol);
	}

	private void resetBoard() {
		// Check if all games have been played
		if (count <= 0) {
			// Reset the game board
			boxp1.setDisable(false);
			boxp2.setDisable(false);
			countField.setDisable(false);
			countField.setText("1");
			play.setVisible(true);
			play.setText("Play Again");
		} else {
			// Play another game
			play.fire();
		}
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
	public static Set<Move> addNeighbors(Move move, Set<Move> currentMoves,
			Set<Move> p1Moves, Set<Move> p2Moves) {
		// Copy move set
		Set<Move> moves = new HashSet<>(currentMoves);

		// Add moves on same circle
		moves.add(new Move((int) move.getX(), (int) (move.getY() + 11) % 12));
		moves.add(new Move((int) move.getX(), (int) (move.getY() + 1) % 12));

		// Add moves on inward circle
		if (move.getX() > 1) {
			moves.add(new Move((int) move.getX() - 1, (int) move.getY()));
			moves.add(new Move((int) move.getX() - 1,
					(int) (move.getY() + 11) % 12));
			moves.add(new Move((int) move.getX() - 1,
					(int) (move.getY() + 1) % 12));
		}
		// Add moves on outward circle
		if (move.getX() < 4) {
			moves.add(new Move((int) move.getX() + 1, (int) move.getY()));
			moves.add(new Move((int) move.getX() + 1,
					(int) (move.getY() + 11) % 12));
			moves.add(new Move((int) move.getX() + 1,
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
	public static boolean win(Set<Move> moves, Move currentMove) {
		// Ensure the current move is in the move set
		moves.add(currentMove);

		// Check moves along line
		int y = (int) currentMove.getY();
		if (moves.contains(new Move(1, y)) && moves.contains(new Move(2, y))
				&& moves.contains(new Move(3, y))
				&& moves.contains(new Move(4, y))) {
			return true;
		}
		// Check moves along counter-clockwise spiral
		y = (int) (currentMove.getY() - currentMove.getX() + 12) % 12;
		if (moves.contains(new Move(1, y))
				&& moves.contains(new Move(2, (y + 1) % 12))
				&& moves.contains(new Move(3, (y + 2) % 12))
				&& moves.contains(new Move(4, (y + 3) % 12))) {
			return true;
		}
		// Check moves along clockwise spiral
		y = (int) (currentMove.getY() + currentMove.getX()) % 12;
		if (moves.contains(new Move(1, y))
				&& moves.contains(new Move(2, (y + 11) % 12))
				&& moves.contains(new Move(3, (y + 10) % 12))
				&& moves.contains(new Move(4, (y + 9) % 12))) {
			return true;
		}
		// Check moves along circle
		int x = (int) currentMove.getX();
		// Unify with all 4 terms of inference rule
		for (int i = 0; i < 4; i++) {
			y = (int) (currentMove.getY() - i + 12) % 12;
			if (moves.contains(new Move(x, y))
					&& moves.contains(new Move(x, (y + 1) % 12))
					&& moves.contains(new Move(x, (y + 2) % 12))
					&& moves.contains(new Move(x, (y + 3) % 12))) {
				return true;
			}
		}
		// No win found
		return false;
	}

	/**
	 * Duplicate win checker that prints the resolution process
	 * 
	 * @param moves
	 *            The set of moves made by the player
	 * @param currentMove
	 *            The current move
	 * @return true if the current move results in a win
	 * @return false otherwise
	 */
	public static boolean printWin(Set<Move> moves, Move currentMove) {
		// Ensure the current move is in the move set
		moves.add(currentMove);

		// Check moves along line
		System.out
				.println("Resolving move(1,y) ^ move(2,y) ^ move(3,y) ^ move(4,y) -> win");
		int y = (int) currentMove.getY();
		System.out.println("Unifying move" + currentMove + " with move("
				+ (int) currentMove.getX() + ",y): {y/" + y + "}");
		if (moves.contains(new Move(1, y)) && moves.contains(new Move(2, y))
				&& moves.contains(new Move(3, y))
				&& moves.contains(new Move(4, y))) {
			System.out.println("Resolution successful using facts: move(1," + y
					+ "), move(2," + y + "), move(3," + y + "), move(4," + y
					+ ")");
			return true;
		}
		System.out.println("Resolution failed");
		// Check moves along counter-clockwise spiral
		System.out
				.println("Resolving move(1,y) ^ move(2,y+1) ^ move(3,y+2) ^ move(4,y+3) -> win");
		y = (int) (currentMove.getY() - currentMove.getX() + 13) % 12;
		System.out.println("Unifying move" + currentMove + " with move("
				+ (int) currentMove.getX() + ",y+"
				+ (int) (currentMove.getX() - 1) + "): {y/" + y + "}");
		if (moves.contains(new Move(1, y))
				&& moves.contains(new Move(2, (y + 1) % 12))
				&& moves.contains(new Move(3, (y + 2) % 12))
				&& moves.contains(new Move(4, (y + 3) % 12))) {
			System.out.println("Resolution successful using facts: move(1," + y
					+ "), move(2," + (y + 1) % 12 + "), move(3," + (y + 2) % 12
					+ "), move(4," + (y + 3) % 12 + ")");
			return true;
		}
		System.out.println("Resolution failed");
		// Check moves along clockwise spiral
		System.out
				.println("Resolving move(1,y) ^ move(2,y-1) ^ move(3,y-2) ^ move(4,y-3) -> win");
		y = (int) (currentMove.getY() + currentMove.getX() - 1) % 12;
		System.out.println("Unifying move" + currentMove + " with move("
				+ (int) currentMove.getX() + ",y-"
				+ (int) (currentMove.getX() - 1) + "): {y/" + y + "}");
		if (moves.contains(new Move(1, y))
				&& moves.contains(new Move(2, (y + 11) % 12))
				&& moves.contains(new Move(3, (y + 10) % 12))
				&& moves.contains(new Move(4, (y + 9) % 12))) {
			System.out.println("Resolution successful using facts: move(1," + y
					+ "), move(2," + (y + 11) % 12 + "), move(3," + (y + 10)
					% 12 + "), move(4," + (y + 9) % 12 + ")");
			return true;
		}
		System.out.println("Resolution failed");
		// Check moves along circle
		System.out
				.println("Resolving move(x,y) ^ move(x,y+1) ^ move(x,y+2) ^ move(x,y+3) -> win");
		int x = (int) currentMove.getX();
		// Unify with all 4 terms of inference rule
		for (int i = 0; i < 4; i++) {
			y = (int) (currentMove.getY() - i + 12) % 12;
			System.out.println("Unifying " + currentMove + " with move(x,y+"
					+ i + "): {x/" + x + ", y/" + y + "}");
			if (moves.contains(new Move(x, y))
					&& moves.contains(new Move(x, (y + 1) % 12))
					&& moves.contains(new Move(x, (y + 2) % 12))
					&& moves.contains(new Move(x, (y + 3) % 12))) {
				System.out.println("Resolution successful using facts: move("
						+ x + "," + y + "), move(" + x + "," + (y + 1) % 12
						+ "), move(" + x + "," + (y + 2) % 12 + "), move(" + x
						+ "," + (y + 3) % 12 + ")");
				return true;
			}
			System.out.println("Resolution failed");
		}
		// No win found
		System.out.println("No win found");
		return false;
	}

}
