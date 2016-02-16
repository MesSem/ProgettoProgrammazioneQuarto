package interfaces;

import java.io.IOException;

import Exception.BoardConfigurationException;
import classes.Piece;

public interface I_board {
	void loadBoard(String path) throws IOException, BoardConfigurationException;
	void saveBoard(String path) throws IOException;
	int gameSituation(); 
	boolean isPlaced(Piece p);
	
	int size();
	boolean isFree(int position);
	void putPieceAtPosition(Piece piece, int position); 
	void removePieceAtPosition(int position);
}
