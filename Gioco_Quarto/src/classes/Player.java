package classes;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialArray;

import Exception.BoardConfigurationException;
import Exception.NotUsedPieceConfigurationException;

public class Player {

	static Table t;
	static String pathBoardFile = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/board.txt";
	static String pathNotUsedPiece = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/pieces.txt";
	static int indexBestPosition = 0;// index about position where Pc must put
										// his piece
	static int indexBestPieceForEnemy = 0;// index about piece to choose for
											// enemy
	static int maxDepth;

	public static void main(String[] args) {

		int messageNumber = 0;
		String message = "";

		try {
			t = new Table(pathBoardFile, pathNotUsedPiece);

			Board board = t.getBoard();// Take from the table a board
										// where the algorithm do is
										// study to choose the better
										// move
			if (t.getGameSituation() == 1000) {
				ArrayList<Piece> pieceNotUsed = t.getPieceNotUsed();
				Piece toPosition = t.getPieceToPosition();
				// If there are a lot of piece to place the algorithm cannot
				// analyze all the possible combination,
				// it could employ too much time. So with the switch I set the
				// maximum attempt;
				switch (pieceNotUsed.size()) {
				case 15:
				case 14:
				case 13:
				case 12:
				case 11:
					maxDepth = 2;
					break;
				case 10:
				case 9:
				case 8:
					maxDepth = 5;
					break;
				default:
					maxDepth = 100;
					break;
				}
				int result = nextMove(board, pieceNotUsed, toPosition, true, 0, -999, +999);

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
				message = "continuare pos" + indexBestPosition;
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
	 * some depth because otherwise the execution time will be too long
	 * 
	 * @param board
	 *            the board where the algorithm do his analysis
	 * @param freePiece
	 *            list of piece not used
	 * @param toPosition
	 *            piece to position
	 * @param turno
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
	static int nextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turno, int depth, int alpha,
			int beta) {
		if (depth > maxDepth)
			if (turno)
				return +100;
			else
				return -100;

		int result = 1000;
		if (toPosition != null) {
			for (int i = 0; i < board.size(); i++) {

				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);

					result = board.gameSituation();
					if (result == 1000) {
						if (turno) {
							// solo al livello 0, sennò non mi interessa
							if (result >= alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1) {
									board.removePieceAtPosition(i);
									return result;
								} // dato che questo ramo da 1,
									// cioè vittoria non ha
									// senso andare oltre, mi
									// salvo l'indice della
									// posizione e chiudo
							}
							alpha = Math.max(alpha, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (beta <= alpha) {
								board.removePieceAtPosition(i);
								break;
							}
						} else {
							beta = Math.min(beta, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (alpha <= beta) {
								board.removePieceAtPosition(i);
								break;
							}
						}
					} else {
						if (turno) {
							// controllare
							// solo al livello 0, sennò non mi interessa
							if (result >= alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1) {
									board.removePieceAtPosition(i);
									return result;
								} // dato che questo ramo da 1,
									// cioè vittoria non ha
									// senso andare oltre, mi
									// salvo l'indice della
									// posizione e chiudo
									// non ha senso testare gli altri rami figli
									// con
									// il for
							}
							alpha = Math.max(alpha, result);
						} else {
							beta = Math.min(beta, (result * -1));
						}
					}
					board.removePieceAtPosition(i);
				}
			}
		} else {
			for (int k = 0; k < freePiece.size(); k++) {
				Piece pieceForEnemy = freePiece.get(k);
				freePiece.remove(k);
				result = nextMove(board, freePiece, pieceForEnemy, !turno, depth + 1, alpha, beta);

				if (turno) {
					if (alpha <= result && depth == 0) {// solo se sono al primo
														// livello, sennò non mi
														// interessa
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
					if (alpha <= beta) {
						freePiece.add(k, pieceForEnemy);
						break;
					}
				}
				freePiece.add(k, pieceForEnemy);
			}
		}
		if (turno)
			return alpha;
		else
			return beta;
	}

	/**
	 * @return 1 vittoria, 0 patta, 1000 casella ancora libera
	 */

}