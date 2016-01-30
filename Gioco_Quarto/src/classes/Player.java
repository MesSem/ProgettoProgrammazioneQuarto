package classes;

import java.awt.List;
import java.util.ArrayList;

public class Player {

	static Table t;
	String pathBoardFile = "pedine.txt";
	String pathNotUsedpiece = "Dautilizzare.txt";

	public static void main(String[] args) {

		// t=new Table(pathBoardFile,pathNotUsedpiece);
		t = new Table();// Ci vuole quello sopra, questo è solo per ora per non
						// avere errori in giro
		Board board = t.getACopyOfTheBoard();// Take from the table a board
												// where the algorithm do is
												// study to choose the better
												// move
		ArrayList<Piece> pieceNotUsed = t
				.getACopyOfPieceNotUsed(); /* new ArrayList<Piece>(); */
		Piece toPosition = t.getPieceToPosition();

		int risultato = getNextMove(board, pieceNotUsed, toPosition, true);

	}

	/*
	 * public static void main(String[] args) { int[] m = new int[16]; int[]
	 * disponibili = new int[10]; int risultato = cervello(m, disponibili, true,
	 * true); }
	 */

	static int indicevittoria = -1;

	/**
	 * 
	 * @param board
	 * @param freePiece
	 * @param turno
	 *            true= computer, false avversario
	 */
	static int getNextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turno) {
		int alfa, beta_N;// a=computer, b=nemico
		if (turno)
			alfa = -2;
		else
			alfa = 2;
		int situazione = 0;
		if (toPosition != null) {
			for (int i = 0; i < board.size(); i++) {
				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);

					situazione = controlla();
					if (situazione == 1000) {
						if (turno) {
							alfa = Math.max(alfa, getNextMove(board, freePiece, null, turno));
						} else {
							alfa = Math.min(alfa, getNextMove(board, freePiece, null, turno));
						}
					} else {
						if (turno) {
							alfa= Math.max(alfa, situazione);
						} else {
							alfa = Math.min(alfa, situazione * -1);
						}

						/*
						 * if (!turno && alfa_C>(situazione*(-1))) alfa_C =
						 * situazione * -1; if (turno && alfa_C<situazione){
						 * alfa_C=situazione;
						 */

						// indicevittoria=i;//lo faccio solo se sto sul giro più
						// alto. dovrei fare uguale per la pedina da consegnare.
						// creo un vettore globale con al configurazione
						// vincente
						// }
					}
					board.removePieceAtPosition(i);
				}
			}
			return alfa;
			/*
			 * for (int j = 0; j < disponibili.length; j++) { m[i] =
			 * disponibili[j]; situazione = controlla(); if (situazione == -1) {
			 * cervello(m, disponibili, !turno, !posiziona); } }
			 */

		} else {
			// tolgo il primo elemento dal vettore
			for (int k = 0; k < freePiece.size(); k++) {
				Piece pieceForEnemy = freePiece.get(k);
				freePiece.remove(k);
				situazione = getNextMove(board, freePiece, pieceForEnemy, !turno);
				if (situazione != 1000) {
					freePiece.add(k, pieceForEnemy);
					return situazione;
				}
				freePiece.add(k, pieceForEnemy);
			}

		}
		return situazione;
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