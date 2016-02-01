package classes;

import java.awt.List;
import java.util.ArrayList;

public class Player {

	static Table t;
	static String pathBoardFile = "pedine.txt";
	static String pathNotUsedPiece = "Dautilizzare.txt";
	static int indexBestPosition = 0;// index about position where Pc must put
										// his piece
	static int indexBestPieceForEnemy = 0;// index about piece to choose for
											// enemy

	public static void main(String[] args) {

		t=new Table(pathBoardFile,pathNotUsedPiece);
		Board board = t.getACopyOfTheBoard();// Take from the table a board
												// where the algorithm do is
												// study to choose the better
												// move
		ArrayList<Piece> pieceNotUsed = t
				.getACopyOfPieceNotUsed(); /* new ArrayList<Piece>(); */
		Piece toPosition = t.getPieceToPosition();

		int result = nextMove(board, pieceNotUsed, toPosition, true, 0, -999, +999);
		//manca di inserire il pezzo in tastiera
		//mettere il pezzo scelto per il nemico
		//salvare i files
		//mandare messaggi di segnale per indicare vittoria perdita o altro.
		//#TODO mettere che si ferma dopo certa profondità

	}

	static int nextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turno, int depth,
			int alpha, int beta) {

		int result = 1000;
		if (toPosition != null) {
			for (int i = 0; i < board.size(); i++) {
				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);

					result = controlla();
					if (result == 1000) {
						if (turno) {
							// solo al livello 0, sennò non mi interessa
							if (result >= alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1)
									return result;// dato che questo ramo da 1,
													// cioè vittoria non ha
													// senso andare oltre, mi
													// salvo l'indice della
													// posizione e chiudo
							}
							alpha = Math.max(alpha, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (beta <= alpha)
								break;
						} else {
							beta = Math.min(beta, nextMove(board, freePiece, null, turno, depth, alpha, beta));
							if (alpha <= beta)
								break;
						}
					} else {
						if (turno) {
							// controllare
							// solo al livello 0, sennò non mi interessa
							if (result >= alpha && depth == 0) {
								indexBestPosition = i;
								if (result == 1)
									return result;// dato che questo ramo da 1,
													// cioè vittoria non ha
													// senso andare oltre, mi
													// salvo l'indice della
													// posizione e chiudo
								//non ha senso testare gli altri rami figli con il for
							}
							alpha = Math.max(alpha, result);
						} else {
							beta = Math.min(beta, result * -1);
						}
					}
					board.removePieceAtPosition(i);
				}
			}
		} else {
			for (int k = 0; k < freePiece.size(); k++) {
				Piece pieceForEnemy = freePiece.get(k);
				freePiece.remove(k);
				result = nextMove(board, freePiece, pieceForEnemy, !turno, depth++, alpha, beta);

				if (turno) {
					if (alpha >= result && depth == 0) {// solo se sono al primo
														// livello, sennò non mi
														// interessa
						indexBestPieceForEnemy = k;
						if (alpha == 1)
							return alpha;
					}
					alpha = Math.max(alpha, result);
					if (beta <= alpha)
						break;
				} else {
					beta = Math.min(beta, result);
					if (alpha <= beta)
						break;
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