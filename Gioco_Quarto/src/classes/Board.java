package classes;

import interfaces.I_board;

public class Board implements I_board {
	
	//public vettore[16]/matrice[4x4] 

	@Override
	public void loadBoard(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveBoard(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public int gameSituation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFree(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putPieceAtPosition(Piece piece, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Piece removePieceAtPosition(int position) {
		// TODO Auto-generated method stub
		return null;
	}

}
