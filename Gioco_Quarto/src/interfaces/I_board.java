package interfaces;

public interface I_board {
	void loadBoard(String path);
	void saveBoard(String path);
	int gameSituation(); //tells whether the match is ended with a victory, a defeat or if it's not ended yet.
}
