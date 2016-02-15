package classes;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialArray;

public class Player {

	// tra i controlli bisogna controllare che il gioco non sia già finito

	static Table t;
	static String pathBoardFile = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/board.txt";
	static String pathNotUsedPiece = "/home/morettini/git/ProgettoProgrammazioneQuarto/Pedine & Scacchiera/pieces.txt";
	static int indexBestPosition = 0;// index about position where Pc must put
										// his piece
	static int indexBestPieceForEnemy = 0;// index about piece to choose for
											// enemy

	public static void main(String[] args) {

		int messageNumber = 0;
		String message = "";

		try {
			t = new Table(pathBoardFile, pathNotUsedPiece);

			Board board = t.getBoard();// Take from the table a board
										// where the algorithm do is
										// study to choose the better
										// move
			ArrayList<Piece> pieceNotUsed = t.getPieceNotUsed();
			Piece toPosition = t.getPieceToPosition();
			int result = nextMove(board, pieceNotUsed, toPosition, true, 0, -999, +999);

			t.insertPieceInBoardAtPosition(toPosition, indexBestPosition);

			t.setEnemyPiece(pieceNotUsed.get(indexBestPieceForEnemy));
			t.removePieceNotUsedAtPosition(indexBestPieceForEnemy);

			t.savePieces(pathNotUsedPiece);
			t.saveBoard(pathBoardFile);

			switch (t.getGameSituation()) {
			case 0:
				message="pareggio pos"+indexBestPosition;
				messageNumber = 2;
				break;
			case 1:
				message="vinto pos"+ indexBestPosition;
				messageNumber = 1;
				break;
			case 1000:
				message="continuare pos"+indexBestPosition;
				messageNumber = 0;
				break;
				default:
					throw new Exception("Error not atteso");
					
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
			message = e.getMessage();
			messageNumber = 999;// errore non atteso
		}

		System.out.println(message);
		
		System.exit(messageNumber);

		// #TODO mettere che si ferma dopo certa profondità POSSIBILE CODICE:
		// if (depth >10)
		// if(turno)
		// return -1;
		// else
		// return 1;
		// forse viceversa.

	}

	static int nextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turno, int depth, int alpha,
			int beta) {
		if (depth >2)
			 if(turno)
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
								if (result == 1){
									board.removePieceAtPosition(i);
									return result;
								}// dato che questo ramo da 1,
													// cioè vittoria non ha
													// senso andare oltre, mi
													// salvo l'indice della
													// posizione e chiudo
							}
							alpha = Math.max(alpha, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (beta <= alpha){
								board.removePieceAtPosition(i);
								break;
							}
						} else {
							beta = Math.min(beta, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (alpha <= beta){
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
								if (result == 1){
									board.removePieceAtPosition(i);
									return result;
								}// dato che questo ramo da 1,
													// cioè vittoria non ha
													// senso andare oltre, mi
													// salvo l'indice della
													// posizione e chiudo
								// non ha senso testare gli altri rami figli con
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
				result = nextMove(board, freePiece, pieceForEnemy, !turno, depth+1, alpha, beta);

				if (turno) {
					if (alpha <=result && depth == 0) {// solo se sono al primo
														// livello, sennò non mi
														// interessa
						indexBestPieceForEnemy = k;
						if (result == 1){
							freePiece.add(k, pieceForEnemy);
							return result;
							}
					}
					alpha = Math.max(alpha, result);
					if (beta <= alpha){
						freePiece.add(k, pieceForEnemy);
						break;
					}
				} else {
					beta = Math.min(beta, result);
					if (alpha <= beta){
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
	 * 
	 * @return 1 vittoria, 0 patta, 1000 casella ancora libera. meglio usare una
	 *         enum
	 */
	static int controlla() {
		return 0;
	}
}