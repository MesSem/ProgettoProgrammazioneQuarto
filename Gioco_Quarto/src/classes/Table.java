package classes;

import java.util.ArrayList;

import interfaces.I_piece;
import interfaces.I_table;

public class Table implements I_table {
	
	ArrayList<Piece> pieceNotUsed;
	Piece pieceToPosition;

	Board board;
	
	public Table(String pathBoardFile, String pathNotUsedPiecesFile){
		
		//Preparation of free piece
		pieceNotUsed=new ArrayList<>();
		loadNotUsedPieces(pathNotUsedPiecesFile);
		
		//preparation of the board
		board=new Board();
		board.loadBoard(pathBoardFile);
	}

	public void loadNotUsedPieces(String path) {
		//prendo dati dal file
		//controllo qualcosina
		//uso metodo di Piece per controllare e caricare
		
		//pieceNotUsed.add()// TODO Auto-generated method stub

	}

	@Override
	public void setEnemyPiece(Piece piece) {
		pieceToPosition=piece;
	}

	@Override
	public void savePieces(String path) {
		//salvare la lista di pedine, ma in prima riga salvare toPosition

	}

	/**
	 * Better than getBoard() but now not work
	 */
	@Override
	public Board getACopyOfTheBoard() {
		
		return null;
	}

	/**
	 * Better than getPieceNotUsed() but now not work
	 */
	@Override
	public ArrayList<Piece> getACopyOfPieceNotUsed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece getPieceToPosition() {
		return pieceToPosition;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public ArrayList<Piece> getPieceNotUsed() {
		return pieceNotUsed;
	}

}
