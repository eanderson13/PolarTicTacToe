Included files:
src/polartictactoe/PolarTicTacToe.java - Creates the user interface for playing Polar Tic Tac Toe. Run this file to execute the program.
src/polartictactoe/Player.java - Abstract class that all Polar Tic Tac Toe agents must implement.
src/polartictactoe/User.java - Implements the controls to allow the user to play Polar Tic Tac Toe.
src/polartictactoe/Minimax.java - Implements a minimax search algorithm to play Polar Tic Tac Toe.
src/polartictactoe/Heuristic.java - Abstract class that all Polar Tic Tac Toe heuristic functions must implement.
src/polartictactoe/NeighborHeuristic.java - Implements a function that calculates a heuristic based on open win paths.
src/polartictactoe/Classifier.java - Implements a nearest neighbor classifier to evaluate the board state.
src/polartictactoe/NeuralNet.java - Implements a temporal difference learning neural network to evaluate the board state.
src/polartictactoe/Move.java - A class that extends java.awt.Point in order to print moves in the desired format.

data/p1Results_Function_AB.csv - Raw output data for player 1 from playing the heuristic function against itself with alpha-beta pruning.
data/p2Results_Function_AB.csv - Raw output data for player 2 from playing the heuristic function against itself with alpha-beta pruning.
data/p1Results_Function_No_AB.csv - Raw output data for player 1 from playing the heuristic function against itself without alpha-beta pruning.
data/p2Results_Function_No_AB.csv - Raw output data for player 2 from playing the heuristic function against itself without alpha-beta pruning.
data/Analysis_Function.xlsx - Analysis of data from playing the heuristic function against itself.

Program Function:
Search depths greater than 6 with alpha-beta pruning and greater than 5 without alpha-beta pruning cause excessive runtime, on the order of several minutes per move.
No other known issues.