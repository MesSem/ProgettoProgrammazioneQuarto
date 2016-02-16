package classes;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialArray;

import Exception.BoardConfigurationException;
import Exception.NotUsedPieceConfigurationException;
import Exception.PieceConfigurationException;

public class Player {

	static Table t;
	static String pathBoardFile = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/board.txt";
	static String pathNotUsedPiece = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/pieces.txt";
	// index about position where Pc must put his piece
	static int indexBestPosition = 0;
	// index about piece to choose for enemy
	static int indexBestPieceForEnemy = 0;
	// max depth of the graph study by the method nextMove
	static int maxDepth;

	public static void main(String[] args) {

		int messageNumber = 0;
		String message = "";
		int result;

		try {
			t = new Table(pathBoardFile, pathNotUsedPiece);

			// before starts the study, the algorithm check if the game is
			// already ended
			if (t.getGameSituation() == 1000) {
				// Take from the table a board where the algorithm do is study
				// to choose the better move
				Board board = t.getBoard();

				// Take from the table a list of Piece not already used where
				// the algorithm do is study to choose the better move
				ArrayList<Piece> pieceNotUsed = t.getPieceNotUsed();

				// Take from the piece that the player ahve to position
				Piece toPosition = t.getPieceToPosition();

				// If there are a lot of piece to place the algorithm cannot
				// analyze all the possible combination,
				// it could employ too much time. So with the switch I set the
				// maximum attempt;
				switch (pieceNotUsed.size()) {
				case 15:
				case 14:
					maxDepth = 2;
					break;
				case 13:
				case 12:
				case 11:
					maxDepth = 5;
					break;
				default:
					maxDepth = 100;
					break;
				}
				// Invocation of the brain of the Player. The value in result
				// show the maximum result that current player could achieve at
				// the end of the game from the current situation
				result = nextMove(board, pieceNotUsed, toPosition, true, 0, -999, +999);

				// Now all the changes area apply at the structure in the table
				// and all data will being saved
				t.insertPieceInBoardAtPosition(toPosition, indexBestPosition);

				t.setEnemyPiece(pieceNotUsed.get(indexBestPieceForEnemy));
				t.removePieceNotUsedAtPosition(indexBestPieceForEnemy);

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
			// Error not expected
			message = e.getMessage();
			messageNumber = 999;
		}

		System.out.println(message);

		System.exit(messageNumber);

	}

	/**
	 * This method calculate the best position for the player. It use an
	 * algorithm similar the Minimax. It is a recursive method that create
	 * dynamically a tree to explore some possible combinations. It stop after
	 * some depth because otherwise the execution time will be too long. At the
	 * end of the execution of the method, in indexBestPosition will be the
	 * position where the algorithm choose to put the piece and in
	 * indexBestPieceForEnemy will be the index of the piece for enemy. Each
	 * index refer respectively to the board object and to the list of
	 * pieceNotUsed. The program aim for maximum points, the enemy aim for the
	 * minimum points. Alpha contains the maximum points that program could
	 * achieve, otherwise beta contains the minimum points that the enemy could
	 * achieve
	 * 
	 * @param board
	 *            the board where the algorithm do his analysis
	 * @param freePiece
	 *            list of piece not used
	 * @param toPosition
	 *            piece to position
	 * @param turn
	 *            it's a boolean variable, if is true, is the Computer turn,
	 *            otherwise is the turn of the enemy
	 * @param depth
	 *            depth of this iteration
	 * @param alpha
	 *            maximum result for the player in the father branch
	 * @param beta
	 *            minimum result for the enemy in the father branch
	 * @return the best result possible for the Player
	 */
	static int nextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turn, int depth, int alpha,
			int beta) {
		// Checks if it's overtake the maxDepth
		if (depth > maxDepth)
			if (turn)
				return +100;
			else
				return -100;
		// 100 is the default value, it means that the game isn't ended
		int result = 1000;
		// Check if the current iteration is the one where the algorithm have to
		// put one piece o to choose one for the enemy.
		if (toPosition != null) {
			// The for scan all the empty position and the algorithm try to put
			// the piece in every position. Than it call himself with the new
			// configuration.
			for (int i = 0; i < board.size(); i++) {
				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);
					result = board.gameSituation();
					// The if check if with the positioning the game is ended
					if (result == 1000) {
						if (turn) {
							//#TODO:per simone, controllare i successivi commenti
							int res=nextMove(board, freePiece, null, turn, depth, alpha, beta);
							// If the algorithm is at the top level, the turn is
							// of the program and the result in this branch is
							// better than the result of other branch, it save
							// the index of the position
							if (res > alpha && depth == 0) {
								indexBestPosition = i;
								// Since this branch give win and it's at the
								// top level, the algorithm could stop now.
								// Before the return it remove the piece added
								// to transform the board as the beginning
								if (res == 1) {
									board.removePieceAtPosition(i);
									return res;
								}
							}
							// If the algorithm isn't at the top level and the
							// turn is of the program, the algorithm put in
							// alpha the maximum score between the score archive
							// in the other branch of this level and the score
							// that it will achieve in below level
							alpha = Math.max(alpha,res );
							if (beta <= alpha) {
								board.removePieceAtPosition(i);
								// This node will return alpha, but since in the
								// higher level there is a branch with a lower
								// points, the enemy will choose the other
								// branch, it's useless continue the study of
								// this branch
								break;
							}
						} else {
							// If the algorithm isn't at the top level and the
							// turn isn't of the program, the algorithm put in
							// beta the minimum score between the score archive
							// in the other branch of this level and the score
							// that it will achieve in below level
							beta = Math.min(beta, nextMove(board, freePiece, null, turn, depth, alpha, beta));
							if (alpha >= beta) {
								// This node will return beta, but since in the
								// higher level there is a branch with a higher
								// points, the program will choose the other
								// branch, it's useless continue the study of
								// this branch
								board.removePieceAtPosition(i);
								break;
							}
						}
					} else {
						if (turn) {
							// Like previously, if it's a top level, and there
							// is a good result, the position index will being
							// saved
							if (result > alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1) {
									board.removePieceAtPosition(i);
									// If this branch give a victory, it's
									// useless continue
									return result;
								}
							}
							alpha = Math.max(alpha, result);
						} else {
							beta = Math.min(beta, (result * -1));
						}
					}
					// All sons of this branch is checked, the board will be
					// restore as at the beginning
					board.removePieceAtPosition(i);
				}
			}
		} else {
			// The program goes here when is the turn to choose the piece for
			// the other player. The for choose all not used piece and choose
			// each piece for the other.
			for (int k = 0; k < freePiece.size(); k++) {
				Piece pieceForEnemy = freePiece.get(k);
				freePiece.remove(k);
				result = nextMove(board, freePiece, pieceForEnemy, !turn, depth + 1, alpha, beta);

				if (turn) {
					// If it's the top level and there is a good result, the
					// index it's saved
					if (alpha < result && depth == 0) {
						indexBestPieceForEnemy = k;
						if (result == 1) {
							freePiece.add(k, pieceForEnemy);
							return result;
						}
					}
					alpha = Math.max(alpha, result);
					if (beta <= alpha) {
						freePiece.add(k, pieceForEnemy);
						break;
					}
				} else {
					beta = Math.min(beta, result);
					if (alpha >= beta) {
						freePiece.add(k, pieceForEnemy);
						break;
					}
				}
				freePiece.add(k, pieceForEnemy);
			}
		}
		// If in this level is the turn of the player, the node take the value
		// of alpha because it will choose the maximum score, otherwise for the
		// enemy.
		if (turn)
			return alpha;
		else
			return beta;
	}

}