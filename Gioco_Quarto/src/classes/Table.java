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

/**
 * Table stands for the physical table/desk where the board and the
 * pieces are. It contains some methods to interact
 * with the board and with not already used pieces
 * 
 * @author Morettini
 *
 */
public class Table implements I_table {
	ArrayList<Piece> notUsedPieces;
	Piece pieceToBePlaced;

	Board board;

	/**
	 * Constructor of the Table class
	 * 
	 * @param pathBoardFile
	 *            file's path which contains board's configuration
	 * @param pathNotUsedPiecesFile
	 *            file's path which contains the list of not used pieces
	 * @throws NotUsedPieceConfigurationException
	 *             If there are any mistakes inside the Not Used Pieces' file
	 * @throws BoardConfigurationException
	 *           If there are any mistakes inside the Board's file
	 * @throws IOException
	 *             If an input or output exception occurs inside loadBoard or
	 *             loadNotUsedPiece
	 */
	public Table(String pathBoardFile, String pathNotUsedPiecesFile)
			throws NotUsedPieceConfigurationException, BoardConfigurationException, IOException {
		//Board preparation
		board = new Board();
		board.loadBoard(pathBoardFile);

		// Not Used pieces preparation
		notUsedPieces = new ArrayList<>();
		loadNotUsedPieces(pathNotUsedPiecesFile);
	}

	/**
	 * Load the Piece not used yet in the Table
	 * 
	 * @param path
	 *            File's path where data about the not used pieces are contained.
	 * @throws PieceConfigurationException
	 *             if there is any error inside the file
	 * @throws IOException
	 *             if there are any I/O exceptions
	 */
	public void loadNotUsedPieces(String path) throws NotUsedPieceConfigurationException, IOException {
		String line = "";
		Piece tmp;
		BufferedReader in = null;
		FileReader fr;
		try {
			fr = new FileReader(path);
			in = new BufferedReader(fr);
			// The first line of the file contains the data of the piece that
			// the player have to position, so I set its value in a special variable
			String firstPiece = in.readLine();
			if (firstPiece != null) {
				try {
					// The string which contains piece's data and the boolean value 'false'
					// are passed to the Piece's checkAndCreate function
					// because the piece can't be a null one.
					tmp = Piece.checkAndCreate(firstPiece, false);
				} catch (PieceConfigurationException er) {
					throw new NotUsedPieceConfigurationException(er.getMessage());
				}
				if (!(board.isPlaced(tmp)))
					pieceToBePlaced = tmp;
				else
					throw new NotUsedPieceConfigurationException("This piece " + firstPiece + " is already in the board ");
			} else
				throw new NotUsedPieceConfigurationException("No pieces on file " + path);
			// All other pieces are now stored into notUsedPieces
			while ((line = in.readLine()) != null) {
				try {
					tmp = Piece.checkAndCreate(line, false);
				} catch (PieceConfigurationException er) {
					throw new NotUsedPieceConfigurationException(er.getMessage());
				}
				// This 'if ' statement checks if the current piece has already been placed on the board or in any other
				// structure on the Table
				if (!board.isPlaced(tmp) && !isPlaced(tmp))
					notUsedPieces.add(tmp);
				else
					throw new NotUsedPieceConfigurationException("This piece " + line + " is already used ");
			}
			in.close();
			int cont=0;
			for(int i=0;i<board.size();i++){
				if(board.isFree(i))
					cont++;
			}
			if(cont!=notUsedPieces.size()+1){
				throw new NotUsedPieceConfigurationException("Pieces that haven't been placed exceed or are fewer than free positions on the board");
			}
		} catch (IOException e) {
			throw e;
		} catch (NotUsedPieceConfigurationException er) {
			throw er;
		} finally {
			try {
				in.close();
			} catch (Exception er) {
			}
		}
	}

	/**
	 * Set the piece to position for the enemy
	 * 
	 * @param piece
	 *           Piece to be placed by the adversary
	 */
	@Override
	public void setEnemyPiece(Piece piece) {
		pieceToBePlaced = piece;
	}

	/**
	 * Saves the pieces in the file specified by the path
	 * 
	 * @param path
	 *            file's path where you want the pieces to be saved
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
			for (Piece piece : notUsedPieces) {
				out.newLine();
				out.write(piece.toString());
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception er) {
			}
		}
	}

	/**
	 * Gets the piece that has to be place which has previously been chosen by the adversary.
	 */
	@Override
	public Piece getPieceToPosition() {
		return pieceToBePlaced;
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
	 * Gets the piece not used of the Table.
	 * 
	 * @return the pieceNotUsed as ArrayList of Piece
	 */
	@Override
	public ArrayList<Piece> getPieceNotUsed() {
		return notUsedPieces;
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
	 * Remove a piece in the list of pieces not used in the specified position.
	 * 
	 * @param position
	 *            index of the position where the method have to remove the
	 *            piece
	 */
	@Override
	public void removePieceNotUsedAtPosition(int position) {
		notUsedPieces.remove(position);
	}

	/**
	 * Insert a piece in the board in the specified position. The position is an
	 * index about a vector, Board will calculate the respective matrix index
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
		if (p.isEqualTo(pieceToBePlaced)) {
			return true;
		} else {
			for (Piece piece : notUsedPieces) {
				if (p.isEqualTo(piece))
					return true;
			}
		}
		return false;
	}

}
