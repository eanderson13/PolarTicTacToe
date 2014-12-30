package polartictactoe;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class NeuralNet extends Heuristic {
	
	private ArrayList<Double> weights = new ArrayList<Double>();
	private static NeuralNet INSTANCE = new NeuralNet();
	private static ArrayList<Move> previousGames = new ArrayList<Move>();
	private static ArrayList[] table = new ArrayList[5000];
	private static double[][] inputs = new double[4][12];
	
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
			int iterator = 0;
			while (sc.hasNext()) {
				String s = sc.next();
				if (s.equals("X") || s.equals("O")) {
					table[iterator].add(previousGames);
					previousGames.removeAll(previousGames);
					iterator++;
				}
				else {
					int a = Integer.parseInt(s);
					int b = Integer.parseInt(sc.next());
					previousGames.add(new Move(a, b));
				}
			}
			sc.close();
		}
		catch (Exception e) {
		}
	}
	
	@Override
	public double getValue(Set<Move> selfMoves, Set<Move> opponentMoves) {
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
	
	private void trainNeuralNet() {
		readGames("testGames.txt");
		do {
			for (double d:weights) {
				d = Math.random(); // assign each weight to be a small random number
			}
			// For each node in the input layer, assign appropriate input
			for (int i = 0; i < table[0].size(); i++) {
				Move m = (Move)table[0].get(i);
				
				// Alternate moves by looking at odd/even iterator values
				if (i % 2 == 0) {
					// X move
					inputs[(int)m.getX() - 1][(int)m.getY()] = 1;
				}
				else {
					// O Move
					inputs[(int)m.getX() - 1][(int)m.getY()] = -1;
				}
			}
			// For l = 2 to L do
			int sumInj = 0;
			int aj = 1;
			for (int l = 2; l < table.length; l++) {
				for(int i = 0; i < table[l].size(); i++) {
					//sumInj += weights.get(index)
				}
			}
			
		} while (true);
	}
}

