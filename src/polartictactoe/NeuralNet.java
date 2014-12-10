package polartictactoe;

import java.io.File;

import java.util.ArrayList;

import java.util.Scanner;

import java.util.Set;

public class NeuralNet extends Heuristic {

	private ArrayList<Double> weights = new ArrayList<Double>();

	private static NeuralNet INSTANCE = new NeuralNet();

	private NeuralNet() {

		for (int i = 0; i < 58; i++) {

			weights.add(Math.random());

		}

	}

	public static NeuralNet getInstance() {

		return INSTANCE;

	}

	private static void readGames(String fname) {

		try {

			Scanner sc = new Scanner(new File(fname));

			StringBuffer gameMoves = new StringBuffer();

			while (sc.hasNext()) {

				String s = sc.next();

				if (s.equals("X")) {

					gameMoves.append(s);

				}

				else if (s.equals("O")) {

				}

				else {

				}

			}

			sc.close();

		}

		catch (Exception e) {
		}

	}

	@Override
	public double getValue(Set<Move> selfMoves, Set<Move> opponentMoves) {

		double[][] inputs = new double[4][12];
		for (int x = 0; x < 4; x++) {

			for (int y = 0; y < 12; y++) {

				inputs[x][y] = 0.0;

			}

		}

		for (Move m : selfMoves) {

			inputs[(int) m.getX() - 1][(int) m.getY()] = 1.0;

		}

		for (Move m : opponentMoves) {

			inputs[(int) m.getX() - 1][(int) m.getY()] = -1.0;

		}

		ArrayList<Double> hiddenLayer = new ArrayList<Double>();
		int iterator = 0;

		for (int x = 0; x < 4; x++) {

			double sum = 0;

			for (int y = 0; y < 12; y++) {

				sum += inputs[x][y] * weights.get(iterator);

				iterator++;

			}

			hiddenLayer.add(sum);
			
		}

		double sum = 0;

		for (int i = 0; i < 4; i++) {

			sum += hiddenLayer.get(i) * weights.get(iterator);

			iterator++;

		}

		return sum;

	}

}