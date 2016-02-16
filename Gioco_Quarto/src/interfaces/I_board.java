package interfaces;

import java.io.IOException;

import Exception.BoardConfigurationException;
import classes.Piece;

public interface I_board {
	void loadBoard(String path) throws IOException, BoardConfigurationException;
	void saveBoard(String path) throws IOException;
	int gameSituation(); //tells whether the match is ended with a victory, a defeat or if it's not ended yet.
	boolean isPlaced(Piece p);
	
	//utili, poi possiamo anche levarli, ne dobbiamo parlare
	int size();//get size of board(for example 16 elements)
	boolean isFree(int position);//ask if the position is free. se invio 9, indico la prima cella della terza riga
	void putPieceAtPosition(Piece piece, int position); //la posizione funziona come sopra
	Piece removePieceAtPosition(int position);//posizioni gestite sempre allo stesso modo, restituisci indietro l'oggetto e metti null	
}
