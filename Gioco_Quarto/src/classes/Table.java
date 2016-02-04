package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import interfaces.I_piece;
import interfaces.I_table;

public class Table implements I_table {

	ArrayList<Piece> pieceNotUsed;
	Piece pieceToPosition;

	Board board;

	public Table(String pathBoardFile, String pathNotUsedPiecesFile) throws PieceConfigurationException, IOException {
		// Preparation of the board
		board = new Board();
		board.loadBoard(pathBoardFile);

		// Preparation of free piece
		pieceNotUsed = new ArrayList<>();
		loadNotUsedPieces(pathNotUsedPiecesFile);
	}

	/**
	 * #TODO: It must be checked #TODO: Add check if piece is already in list
	 * piecenotused
	 * 
	 * @param path
	 * @throws PieceConfigurationException
	 * @throws IOException
	 */
	public void loadNotUsedPieces(String path) throws PieceConfigurationException, IOException {
		String line = "";
		Piece tmp;

		FileReader fr = new FileReader(path);

		BufferedReader in = new BufferedReader(fr);
		String firstPiece = in.readLine();
		if (firstPiece != null) {
			tmp = Piece.checkAndCreate(firstPiece);
			if (!(board.isPlaced(tmp)))
				pieceToPosition=tmp;
			else
				throw new PieceConfigurationException("This piece " + line + " is already in the board ");
		} else
			throw new PieceConfigurationException("No piece on file " + path);
		while ((line = in.readLine()) != null) {
			tmp = Piece.checkAndCreate(line);
			if (!(board.isPlaced(tmp)))
				pieceNotUsed.add(tmp);
			else
				throw new PieceConfigurationException("This piece " + line + " is already in the board ");
		}

		in.close();
	}

	@Override
	public void setEnemyPiece(Piece piece) {
		pieceToPosition = piece;
	}

	/**
	 * #TODO: It must be checked #TODO: Add check if piece is already in list
	 * piecenotused
	 * @throws IOException 
	 */
	@Override
	public void savePieces(String path) throws IOException {
		FileWriter fw=new FileWriter(path);
		BufferedWriter out=new BufferedWriter(fw);
		out.write(pieceToPosition.toString());
		String s=pieceToPosition.toString();
		for (Piece piece : pieceNotUsed) {
			out.newLine();
			out.write(piece.toString());
		}
		out.flush();
		out.close();
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

	@Override
	public void saveBoard(String path) throws IOException {
		board.saveBoard(path);

	}

	@Override
	public void removePieceNotUsedAtPosition(int position) {
		pieceNotUsed.remove(position);

	}

	@Override
	public void insertPieceInBoardAtPosition(Piece p, int position) {
		board.putPieceAtPosition(p, position);

	}

	@Override
	public int getGameSituation() {
		return board.gameSituation();
	}

}
