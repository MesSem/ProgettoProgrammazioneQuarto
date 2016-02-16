package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

import Exception.BoardConfigurationException;
import Exception.NotUsedPieceConfigurationException;
import Exception.PieceConfigurationException;
import interfaces.I_piece;
import interfaces.I_table;

public class Table implements I_table {

	ArrayList<Piece> pieceNotUsed;
	Piece pieceToPosition;

	Board board;

	/**
	 * Constructor of the Table class
	 * 
	 * @param pathBoardFile
	 *            path of the file that contain the configuration of the Board
	 * @param pathNotUsedPiecesFile
	 *            path of the file that contain the list of the piece not used
	 * @throws NotUsedPieceConfigurationException
	 *             If there are some mistakes inside the Piece not used file
	 * @throws BoardConfigurationException
	 *             If there are some mistakes inside the Board file
	 * @throws IOException
	 *             If an input or output exception occurred inside loadBoard or
	 *             loadNotUsedPiece
	 */
	public Table(String pathBoardFile, String pathNotUsedPiecesFile)
			throws NotUsedPieceConfigurationException, BoardConfigurationException, IOException {
		// Preparation of the board
		board = new Board();
		board.loadBoard(pathBoardFile);

		// Preparation of free piece
		pieceNotUsed = new ArrayList<>();
		loadNotUsedPieces(pathNotUsedPiecesFile);
	}

	/**
	 * Load the Piece not used yet in the Table
	 * 
	 * @param path	of the file where is the data about the piece not used yet
	 * @throws PieceConfigurationException	if there is some error inside the file
	 * @throws IOException	if there are some I/O exceptions
	 */
	public void loadNotUsedPieces(String path) throws NotUsedPieceConfigurationException, IOException {
		String line = "";
		Piece tmp;
		BufferedReader in = null;
		FileReader fr;
		try {
			fr = new FileReader(path);

			in = new BufferedReader(fr);
			String firstPiece = in.readLine();
			if (firstPiece != null) {
				try {
					tmp = Piece.checkAndCreate(firstPiece, false);
				} catch (PieceConfigurationException er) {
					throw new NotUsedPieceConfigurationException(er.getMessage());
				}
				if (!(board.isPlaced(tmp)))
					pieceToPosition = tmp;
				else
					throw new NotUsedPieceConfigurationException("This piece " + line + " is already in the board ");
			} else
				throw new NotUsedPieceConfigurationException("No piece on file " + path);
			while ((line = in.readLine()) != null) {
				try {
					tmp = Piece.checkAndCreate(line, false);
				} catch (PieceConfigurationException er) {
					throw new NotUsedPieceConfigurationException(er.getMessage());
				}
				if (!board.isPlaced(tmp) && !isPlaced(tmp))
					pieceNotUsed.add(tmp);
				else
					throw new NotUsedPieceConfigurationException("This piece " + line + " is already used ");
			}
			in.close();
		} catch (IOException e) {
			try {
				in.close();
			} catch (Exception er) {
			} finally {
			}
			throw e;
		}
	}

	/**
	 * Set the piece to position for the enemy
	 * 
	 * @param piece
	 *            piece to position for the enemy
	 */
	@Override
	public void setEnemyPiece(Piece piece) {
		pieceToPosition = piece;
	}

	/**
	 * Save the pieces in the file specified by the path
	 * 
	 * @param path
	 *            of the file where you want to save the pieces
	 * 
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	@Override
	public void savePieces(String path) throws IOException {
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(path);
			out = new BufferedWriter(fw);
			out.write(pieceToPosition.toString());
			String s = pieceToPosition.toString();
			for (Piece piece : pieceNotUsed) {
				out.newLine();
				out.write(piece.toString());
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			try {
				out.flush();
				out.close();
			} catch (Exception er) {
			}
			throw e;
		}
	}

	/**
	 * Get the piece choose by the enemy to position by this program
	 */
	@Override
	public Piece getPieceToPosition() {
		return pieceToPosition;
	}

	/**
	 * Get the board of the Table.
	 * 
	 * @return the board as a Board object
	 */
	@Override
	public Board getBoard() {
		return board;
	}

	/**
	 * Get the piece not used of the Table.
	 * 
	 * @return the pieceNotUsed as ArrayList of Piece
	 */
	@Override
	public ArrayList<Piece> getPieceNotUsed() {
		return pieceNotUsed;
	}

	/**
	 * Save the board of this table. It use the method inside the Board object
	 * 
	 * @param path
	 *            the path where you want to save the board
	 * @exception IOException
	 *                If an input or output exception occurred inside
	 *                Board.saveBoard(String path);
	 */
	@Override
	public void saveBoard(String path) throws IOException {
		board.saveBoard(path);
	}

	/**
	 * Remove a piece in the board in the specified position. The position is
	 * calculated as the matrix is like a vector.
	 * 
	 * @param p
	 *            piece to position
	 * @param position
	 *            index of the position where the method have to remove the
	 *            piece
	 */
	@Override
	public void removePieceNotUsedAtPosition(int position) {
		pieceNotUsed.remove(position);

	}

	/**
	 * Insert a piece in the board in the specified position. The position is
	 * calculated as the matrix is like a vector.//PER ENRICO, DENTRO BOARD
	 * METTERE QUALCOSA PER SPIEGARE LA TRASFORMAZIONE DA VETTORE A MATRICE.
	 * cHIEDI A SIMONE PER CAHIRIMENTI SU COSA INTENDO
	 * 
	 * @param p
	 *            piece to position
	 * @param position
	 *            index of the position where the method have to put the piece
	 */
	@Override
	public void insertPieceInBoardAtPosition(Piece p, int position) {
		board.putPieceAtPosition(p, position);

	}

	/**
	 * Get the game of the situation in the current instant
	 * 
	 * @return the game's situation of the board
	 */
	@Override
	public int getGameSituation() {
		return board.gameSituation();
	}

	/**
	 * Check if a piece is already in pieceNotUsed or is in pieceToPosition
	 * 
	 * @param piece
	 *            that you want to check
	 * @return true if the piece is already placed, false otherwise
	 */
	private boolean isPlaced(Piece p) {
		if (p.isEqualTo(pieceToPosition)) {
			return true;
		} else {
			for (Piece piece : pieceNotUsed) {
				if (p.isEqualTo(piece))
					return true;
			}
		}
		return false;
	}

}
