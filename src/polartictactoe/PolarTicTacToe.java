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

	final int center = 140;
	final int r0 = 25;
	final int[] r = { 2 * r0, 3 * r0, 4 * r0, 5 * r0 };

	ObservableList<String> choices = FXCollections.observableArrayList("User");
	ComboBox<String> boxp1 = new ComboBox<>(choices);
	ComboBox<String> boxp2 = new ComboBox<>(choices);
	Group board = new Group();
	Button play = new Button("Play");
	Label message = new Label();
	Player player1, player2;
	Set<Point> legalMoves = new HashSet<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Polar Tic Tac Toe");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {
				System.exit(0);
			}

		});

		Label labelp1 = new Label("Player 1 (X):");
		boxp1.getSelectionModel().selectFirst();
		Label labelp2 = new Label("Player 2 (O):");
		boxp2.getSelectionModel().selectFirst();

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

		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				layout.getChildren().remove(player1);
				switch (boxp1.getSelectionModel().getSelectedIndex()) {
				case 0:
					player1 = new User();
					break;
				}
				layout.add(player1, 1, 1, 2, 1);
				layout.getChildren().remove(player2);
				switch (boxp2.getSelectionModel().getSelectedIndex()) {
				case 0:
					player2 = new User();
					break;
				}
				layout.add(player2, 1, 3, 2, 1);
				player2.setDisable(true);
				boxp1.setVisible(false);
				boxp2.setVisible(false);
				play.setVisible(false);
				message.setText("");
				layout.getChildren().remove(board);
				drawBoard();
				layout.add(board, 0, 0, 1, 6);
				Set<Point> firstMoves = new HashSet<>();
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 12; j++) {
						firstMoves.add(new Point(i, j));
					}
				}
				play(player1, player2, firstMoves);
			}
		});

		primaryStage.setScene(new Scene(layout));
		primaryStage.show();
	}
	
	private void drawBoard() {
		board = new Group();
		
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

		final int[] x = { center + r[0], center + r[1], center + r[2],
				center + r[3] };
		final int[] t = { 130, 112, 65 };
		final int[] y = { center + t[0], center + t[1], center + t[2], center,
				center - t[2], center - t[1], center - t[0] };
		final int offset = 10;

		Line line1 = new Line(y[0], y[3], y[6], y[3]);
		Line line2 = new Line(y[1], y[4], y[5], y[2]);
		Line line3 = new Line(y[2], y[5], y[4], y[1]);
		Line line4 = new Line(y[3], y[6], y[3], y[0]);
		Line line5 = new Line(y[4], y[5], y[2], y[1]);
		Line line6 = new Line(y[5], y[4], y[1], y[2]);

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

		Text textx1 = new Text(x[0] - offset, center + offset, "1");
		Text textx2 = new Text(x[1] - offset, center + offset, "2");
		Text textx3 = new Text(x[2] - offset, center + offset, "3");
		Text textx4 = new Text(x[3] - offset, center + offset, "4");

		board.getChildren().addAll(circle1, circle2, circle3, circle4, line1,
				line2, line3, line4, line5, line6, text1, text2, text3, text4,
				text5, text6, text7, text8, text9, text10, text11, text12,
				textx1, textx2, textx3, textx4);
	}

	private void play(Player player, Player next, Set<Point> legal) {
		Task<Point> moveTask = new Task<Point>() {
			@Override
			protected Point call() {
				return player.getMove(legal);
			}
		};

		moveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent e) {
				Point move = moveTask.getValue();
				player.addMove(move);
				addNeighbors(move);
				legalMoves.remove(move);
				drawMove(move, player);
				player.setDisable(true);
				if (!legalMoves.isEmpty()) {
					next.setDisable(false);
					play(next, player, legalMoves);
				} else {
					message.setText("Game Over! Tie game.");
					boxp1.setVisible(true);
					boxp2.setVisible(true);
					play.setVisible(true);
					play.setText("Play Again");
				}
			}
		});

		new Thread(moveTask).start();
	}

	private void addNeighbors(Point move) {
		legalMoves.add(new Point((int) move.getX(),
				(int) (move.getY() + 11) % 12));
		legalMoves.add(new Point((int) move.getX(),
				(int) (move.getY() + 1) % 12));
		if (move.getX() > 0) {
			legalMoves.add(new Point((int) move.getX() - 1, (int) move.getY()));
		}
		if (move.getX() < 3) {
			legalMoves.add(new Point((int) move.getX() + 1, (int) move.getY()));
		}
		legalMoves.removeAll(player1.getMoves());
		legalMoves.removeAll(player2.getMoves());
	}

	private void drawMove(Point move, Player player) {
		Text symbol = new Text();
		symbol.setFont(Font.font("System", FontWeight.BOLD, 20));
		if (player == player1) {
			symbol.setText("X");
			symbol.setFill(Color.RED);
		} else {
			symbol.setText("O");
			symbol.setFill(Color.BLUE);
		}
		int radius = r[(int) move.getX()];
		double angle = (12 - move.getY()) * Math.PI / 6;
		symbol.setX(center + radius * Math.cos(angle) - 7);
		symbol.setY(center + radius * Math.sin(angle) + 7);
		board.getChildren().add(symbol);
	}
}
