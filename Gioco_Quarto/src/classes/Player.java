package classes;

import java.io.IOException;
import java.util.ArrayList;

import Exception.BoardConfigurationException;
import Exception.NotUsedPieceConfigurationException;

/**
 * This class represent the player. It contains the main method and the
 * intelligent of the program
 * 
 * @author Morettini
 *
 */
public class Player {

	static Table t;
	static String pathBoardFile = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/board.txt";
	static String pathNotUsedPiece = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/pieces.txt";
	// Stands for the best position where PC should put his piece
	static int indexBestPosition = 0;
	// Stands for the best piece to give to the adversary
	static int indexBestPieceForEnemy = 0;
	// max depth of the graph which is studied by the method nextMove
	static int maxDepth;

	public static void main(String[] args) {

		int messageNumber = 0;
		String message = "";
		int result;

		try {
			t = new Table(pathBoardFile, pathNotUsedPiece);

			// The algorithm checks if the game is already ended
			// before start to study the graph.
			if (t.getGameSituation() == 1000) {
				// Takes a board from the table. The board is then analyzed by
				// the algorithm
				// to choose the best move
				Board board = t.getBoard();

				// Takes from the table a list of Pieces which aren't already
				// used
				// which are then studied by the algorithm
				ArrayList<Piece> notUsedPieces = t.getPieceNotUsed();

				// Takes the piece that the player has to place
				Piece toPosition = t.getPieceToPosition();

				// If there are too many pieces to be placed the algorithm
				// cannot
				// analyze all the possible combinations.
				// it would take too much time. So with this switch I set the
				// maximum number of attempts;
				switch (notUsedPieces.size()) {
				case 15:
				case 14:
					maxDepth = 2;
					break;
				case 13:
				case 12:
				case 11:
				case 10:
				case 9:
					maxDepth = 5;
					break;
				default:
					maxDepth = 100;
					break;
				}
				// Invocation Player's brain. The value in result
				// shows the maximum result that the current player could
				// achieve at the end of the game at the current situation
				result = nextMove(board, notUsedPieces, toPosition, true, 0, -999, +999);

				// Now all the changes are applied at the structure in the table
				// and all data are going to be saved
				t.insertPieceInBoardAtPosition(toPosition, indexBestPosition);

				if (notUsedPieces.size() > 0) {
					t.setEnemyPiece(notUsedPieces.get(indexBestPieceForEnemy));
					t.removePieceNotUsedAtPosition(indexBestPieceForEnemy);
				} else {
					t.setEnemyPiece(null);
				}

				t.savePieces(pathNotUsedPiece);
				t.saveBoard(pathBoardFile);
			} else {
				throw new BoardConfigurationException("The game is already ended");
			}

			switch (t.getGameSituation()) {
			case 0:
				message = "pareggio pos" + indexBestPosition;
				messageNumber = 2;
				break;
			case 1:
				message = "vinto pos" + indexBestPosition;
				messageNumber = 1;
				break;
			case 1000:
				message = "continuare pos" + indexBestPosition + " " + result;
				messageNumber = 0;
				break;
			default:
				throw new Exception("Errore non atteso");

			}
		} catch (IOException e) {
			message = e.getMessage();
			messageNumber = 11;
		} catch (NotUsedPieceConfigurationException e) {
			message = e.getMessage();
			messageNumber = 12;
		} catch (BoardConfigurationException e) {
			message = e.getMessage();
			messageNumber = 13;
		} catch (Exception e) {
			// Not expected error
			message = e.getMessage();
			messageNumber = 999;
		}

		System.out.println(message);

		System.exit(messageNumber);

	}

	/**
	 * This method calculates the best position for the player. It uses an
	 * algorithm similar to the Minimax. It is a recursive function which
	 * creates dynamically a tree which explores some possible combinations. It
	 * stops itself after some branches because otherwise the execution time
	 * would be too long. At the end of the execution of the method, in
	 * indexBestPosition will be contained the position that the algorithm has
	 * decided to be the best to put the piece in. Into indexBestPieceForEnemy
	 * there's going to be the index of the piece for enemy. Each index refers
	 * respectively to the board object and to the list of notUsedPieces. The
	 * program aims for maximum points, the adversary aims for the minimum
	 * points. Alpha contains the maximum points that program could achieve
	 * while beta contains the minimum points that the adversary could achieve
	 * 
	 * @param board
	 *            Current board where the game is played.
	 * @param freePieces
	 *            list of not used pieces
	 * @param toPosition
	 *            piece to place
	 * @param turn
	 *            it's a boolean variable, if it's true and it is Computer's
	 *            turn. otherwise it's adversary's turn.
	 * @param depth
	 *            depth of this iteration
	 * @param alpha
	 *            highest result for the player in the father branch
	 * @param beta
	 *            lower result for the enemy in the father branch
	 * @return the best result possible for the Player
	 */
	static int nextMove(Board board, ArrayList<Piece> freePieces, Piece toPosition, Boolean turn, int depth, int alpha,
			int beta) {
		// Checks if we're going out of the tree.
		if (depth > maxDepth) {
			if (turn)
				return +100;
			else
				return -100;
		}
		// 1000 is the default value, it means that the game hasn't ended
		int result = 1000;
		// Checks whether the current iteration is the one where the algorithm
		// has to
		// place a piece or whether it has to choose one for the enemy.
		if (toPosition != null) {
			// The following for cycle scans all the empty positions and the
			// algorithm tries to place
			// the piece in each position. Then it calls itself with the new
			// configuration.
			for (int i = 0; i < board.size(); i++) {
				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);
					result = board.gameSituation();
					// Checks whether the game has ended or no.
					if (result == 1000) {
						if (turn) {
							// It's call himself but passing null as toPosition
							int res = nextMove(board, freePieces, null, turn, depth, alpha, beta);
							// If the algorithm is at the top level, it's
							// program's turn
							// and the result of this branch is
							// better than the result of the other branch. So it
							// saves
							// the index of the position
							if (res > alpha && depth == 0) {
								indexBestPosition = i;
								// if this branch gives 'win' and it's at the
								// top level, the algorithm can stop now.
								// Before the return statement it removes the
								// added piece
								// to transform the board as it was at the
								// beginning
								if (res == 1) {
									board.removePieceAtPosition(i);
									return res;
								}
							}
							// If the algorithm isn't at the top level and
							// it's program's turn, the algorithm sets in
							// alpha the maximum score between the score
							// achieved
							// in the other branch of this level and the score
							// that will be achieved on the level below
							alpha = Math.max(alpha, res);
							if (beta <= alpha) {
								board.removePieceAtPosition(i);
								// This node will return alpha, but since in the
								// higher level there is a branch with a lower
								// score, the enemy would choose the other
								// branch so it's useless to continue the study
								// of
								// this branch
								break;
							}
						} else {
							// If the algorithm isn't at the top level and the
							// it isn't program's turn, the algorithm puts in
							// beta the minimum score between the score achieved
							// in the other branch of this level and the score
							// that it will be achieved in the level below.
							beta = Math.min(beta, nextMove(board, freePieces, null, turn, depth, alpha, beta));
							if (alpha >= beta) {
								// This node will return beta, but since in the
								// higher level there is a branch with a higher
								// score, the program will choose the other
								// branch, it's useless to continue to study
								// this branch
								board.removePieceAtPosition(i);
								break;
							}
						}
					} else {
						if (turn) {
							// Just like before, if it's a top level, and there
							// is a good result, the position index will be
							// saved
							if (result > alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1) {
									board.removePieceAtPosition(i);
									// If this branch gives a victory, it's
									// useless to continue
									return result;
								}
							}
							alpha = Math.max(alpha, result);
						} else {
							beta = Math.min(beta, (result * -1));
						}
					}
					// Every son of this branch has been checked, the board will
					// be
					// restored as it was at the beginning
					board.removePieceAtPosition(i);
				}
			}
		} else {
			// The program arrives here when "toPosition" is null
			// so it is time to choose the enemy's piece
			// This 'for' statement scans each not used piece
			// and chooses
			// the best piece to give to the other player.
			for (int k = 0; k < freePieces.size(); k++) {
				Piece pieceForEnemy = freePieces.get(k);
				freePieces.remove(k);
				result = nextMove(board, freePieces, pieceForEnemy, !turn, depth + 1, alpha, beta);

				if (turn) {
					// If it's the top level and there is a good result, the
					// index is saved
					if (alpha < result && depth == 0) {
						indexBestPieceForEnemy = k;
						if (result == 1) {
							freePieces.add(k, pieceForEnemy);
							return result;
						}
					}
					alpha = Math.max(alpha, result);
					if (beta <= alpha) {
						freePieces.add(k, pieceForEnemy);
						break;
					}
				} else {
					beta = Math.min(beta, result);
					if (alpha >= beta) {
						freePieces.add(k, pieceForEnemy);
						break;
					}
				}
				freePieces.add(k, pieceForEnemy);
			}
		}
		// If at this level it's the player's turn, the node takes the value
		// of alpha because it is going to contain the maximum score, otherwise
		// it will return
		// beta for the
		// enemy.
		if (turn)
			return alpha;
		else
			return beta;
	}

}