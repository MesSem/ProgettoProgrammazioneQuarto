package interfaces;

import java.util.ArrayList;

import classes.Board;
import classes.Piece;

public interface I_table {
	void setEnemyPiece(Piece piece); //Sets on a var the chosen pieces.
	void savePieces(String path); //saves all the left pieces on the file, the chosen piece is the first one at the top.
	Board getACopyOfTheBoard();
	Board getBoard();
	ArrayList<Piece> getACopyOfPieceNotUsed();
	ArrayList<Piece> getPieceNotUsed();
	Piece getPieceToPosition();
}
