package classes;

import java.awt.List;
import java.util.ArrayList;

public class Player {

	static Table t;
	String pathBoardFile = "pedine.txt";
	String pathNotUsedpiece = "Dautilizzare.txt";
	static int indexBestPosition=0;//index about position where Pc must put his piece
	static int indexBestPieceForEnemy=0;//index about piece to choose for enemy

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

		int risultato = getNextMove(board, pieceNotUsed, toPosition, true,-1);

	}

	static int indicevittoria = -1;
	
	/**
	 * 
	 * @param board
	 * @param freePiece
	 * @param turno
	 *            true= computer, false avversario
	 */
	
	//meglio rinominarlo
	static int getNextMove(Board board, ArrayList<Piece> freePiece, Piece toPosition, Boolean turno, int depth) {
		int alfa, beta_N;// a=computer, b=nemico
		if (turno)
			alfa = -2;
		else
			alfa = 2;
		int result = 0;
		if (toPosition != null) {
			for (int i = 0; i < board.size(); i++) {
				if (board.isFree(i)) {
					board.putPieceAtPosition(toPosition, i);

					result = controlla();
					if (result == 1000) {
						if (turno) {
							alfa = Math.max(alfa, getNextMove(board, freePiece, null, turno, depth));
						} else {
							alfa = Math.min(alfa, getNextMove(board, freePiece, null, turno, depth));
						}
					} else {
						if (turno) {
							if(alfa>=result && depth==0){//solo se sono al primo livello, sennò non mi interessa
								indexBestPosition=i;
								if(alfa==1 )
									return alfa;//dato che questo ramo da 1, cioè vittoria non ha senso andare oltre, mi salvo l'indice della posizione e chiudo
							}
							alfa= Math.max(alfa, result);
						} else {
							alfa = Math.min(alfa, result * -1);
						}						
						/*
						 * if (!turno && alfa_C>(situazione*(-1))) alfa_C =
						 * situazione * -1; if (turno && alfa_C<situazione){
						 * alfa_C=situazione;
						 */
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
			for (int k = 0; k < freePiece.size(); k++) {
				Piece pieceForEnemy = freePiece.get(k);
				freePiece.remove(k);
				result = getNextMove(board, freePiece, pieceForEnemy, !turno, depth++);
				
				if (turno) {
					if(alfa>=result && depth==0){//solo se sono al primo livello, sennò non mi interessa
						indexBestPieceForEnemy=k;
						if(alfa==1)
							return alfa;
					}
					alfa = Math.max(alfa, result);
				} else {
					alfa = Math.min(alfa, result);
				}
				
				freePiece.add(k, pieceForEnemy);
			}

		}
		return alfa;
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