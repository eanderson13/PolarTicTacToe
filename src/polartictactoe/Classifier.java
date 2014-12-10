package polartictactoe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

public class Classifier extends Heuristic {
	// Singleton implementation
	private static Classifier INSTANCE = new Classifier();

	private Classifier() {
	}

	// Singleton return
	public static Classifier getInstance() {
		// Set initial values to 0.
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 12; y++) {
				heuristicHash[x][y] = 0;
			}
		}
		readGames("fileOut.txt");
		double sum = 0; // ROBL
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 12; y++) {
				sum+=heuristicHash[x][y];
			}
		}
		return INSTANCE;
	}
	// Creates an int hash set the size of the board
	private static double[][] heuristicHash = new double[4][12];
	/**
	 * Opens the file containing previous games and stores the text in the file to a string
	 * buffer.
	 * @param fname The name of the file to be opened
	 */
	private static void readGames(String fname) {
		try {
			Scanner sc = new Scanner(new File(fname));
			StringBuffer gameMoves = new StringBuffer();
			while (sc.hasNext()) {
				String s = sc.next();
				if(s.equals("X") || s.equals("O")) {
					gameMoves.delete(0, gameMoves.length());
				}
				else {
					int a = Integer.parseInt(s);
					int b = Integer.parseInt(sc.next());
					heuristicHash[a - 1][b]++;
				}
			}
			sc.close();

			// Normalize scores
			double sum = 0;
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 12; y++) {
					sum += heuristicHash[x][y];
				}
			}
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 12; y++) {
					heuristicHash[x][y] /= sum;
				}
			}
			// Board should be set up for heuristic calculations.
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
		}
	}

	/**
	 * Returns value based on how common move was in previous games
	 */
	@Override
	public double getValue(Set<Move> selfMoves, Set<Move> opponentMoves) {
		double score = 0.0;
		for (Move m: selfMoves) {
			score += heuristicHash[(int)m.getX() - 1][(int)m.getY()];
		}
		for (Move m: opponentMoves) {
			score -= heuristicHash[(int)m.getX() - 1][(int)m.getY()];
		}
		return score;

	}


}
