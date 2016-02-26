package interfaces;

import java.io.IOException;
import java.util.ArrayList;

import classes.Board;
import classes.Piece;

public interface I_table {
	void setEnemyPiece(Piece piece); 
	void savePieces(String path) throws IOException; 
	void saveBoard(String path) throws IOException; 
	
	Board getBoard();
	ArrayList<Piece> getPieceNotUsed();
	Piece getPieceToPosition();
	void removePieceNotUsedAtPosition(int position);
	void insertPieceInBoardAtPosition(Piece p,int position);
	
	int getGameSituation();
}
