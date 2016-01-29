package interfaces;

public interface I_table {
	void loadNotUsedPieces(String path);
	void setEnemyPiece(I_piece pedina); //Sets on a var the chosen pieces.
	void savePieces(String path); //saves all the left pieces on the file, the chosen piece is the first one at the top.

}
