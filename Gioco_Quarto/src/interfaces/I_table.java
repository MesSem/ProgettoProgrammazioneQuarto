package interfaces;

import java.io.IOException;
import java.util.ArrayList;

import classes.Board;
import classes.Piece;

public interface I_table {
	void setEnemyPiece(Piece piece); //Sets on a var the chosen pieces.
	void savePieces(String path) throws IOException; //saves all the left pieces on the file, the chosen piece is the first one at the top.
	void saveBoard(String path) throws IOException; //saves all the left pieces on the file, the chosen piece is the first one at the top.
	
	Board getBoard();
	ArrayList<Piece> getPieceNotUsed();
	Piece getPieceToPosition();
	void removePieceNotUsedAtPosition(int position);
	void insertPieceInBoardAtPosition(Piece p,int position);
	
	int getGameSituation();
}
