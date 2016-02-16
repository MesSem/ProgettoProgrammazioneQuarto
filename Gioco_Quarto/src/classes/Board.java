package classes;

import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.stream.Stream;

import Exception.BoardConfigurationException;
import Exception.PieceConfigurationException;
import interfaces.I_board;

public class Board implements I_board {

	public Piece[][] board = new Piece[4][4];

	/**
	 * Loads placed pieces on the board.
	 * 
	 * @param path
	 *            contains board file's path.
	 * @throws BoardConfigurationException
	 *             Gets threw when an error occurs into board file.
	 * @throws IOException
	 *             Gets threw when an error occurs searching or trying to read
	 *             the board file.
	 */
	@Override
	public void loadBoard(String path) throws BoardConfigurationException, IOException {
		try {
			// Creates file-reader variables.
			File boardfile = new File(path);
			if (boardfile.exists()) {
				// opens file, reading mode
				FileReader br = new FileReader(boardfile);
				BufferedReader boardReader = new BufferedReader(br);
				String app;
				int cont = 0;
				// while i'm actually reading something and if i've still not
				// read more than 4 lines i put the value of that line in app
				while (((app = boardReader.readLine()) != null) && cont < 5) {
					String[] row = app.split(" ");
					// scans each row's cells and checks its actual value, if
					// it's okay it becomes a piece on the board.
					for (int c = 0; c < 4; c++) {
						try {
							Piece p = Piece.checkAndCreate(row[c], true);

							if (p != null) {
								boolean placed = isPlaced(p);
								if (placed) {
									throw new BoardConfigurationException("This piece is already placed!");
								} else {
									board[cont][c] = p;
								}
							}
						} catch (PieceConfigurationException e) {
							throw new BoardConfigurationException(e.getMessage());
						}
					}
					cont++;
				}
				boardReader.close();
			} else {
				// throws exception, file doesn't exist.
				throw new IOException("File doesn't exist.");
			}
		} catch (IOException e) {
			// Throws exception: input error
			throw new IOException(e.getMessage());
		}

	}

	/**
	 * It save the board in a file
	 * 
	 * @param path
	 *            location and name of the file where you want to save the data
	 */
	@Override
	public void saveBoard(String path)
			throws IOException { /** Saves board's new configuration */
		FileWriter fw;
		BufferedWriter out = null;

		try {
			fw = new FileWriter(path);
			out = new BufferedWriter(fw);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					// If it's null it's a void space.
					if (board[i][j] == null)
						out.write("**** ");
					else
						out.write(board[i][j].toString() + " ");
				}
				out.newLine();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// If there was some error I try to close the output-stream and then
			// I throw again the exception
			try {
				out.flush();
				out.close();
			} catch (Exception ex) {

			} finally {
				throw new IOException(e.getMessage());
			}

		}
	}

	/**
	 * It checks if the game is ended or not. There are three possible
	 * situation: <br>
	 * 0= the game is ended in parity <br>
	 * 1= the game is ended and the current player is the winner <br>
	 * 1000= the game isn't ended <br>
	 * 
	 * @return The situation of the game
	 */
	@Override
	public int gameSituation() {
		// scans each row and column of the board, checking if there's any
		// victory .
		for (int i = 0; i < 4; i++) {
			// Checks if there actually are pieces in those positions
			if ((board[i][0] != null) && (board[i][1] != null) && (board[i][2] != null) && (board[i][3] != null))
				// Checks if those pieces have something in common
				if (Piece.victory(board[i][0], board[i][1], board[i][2], board[i][3]))
					return 1; // if they all have something in common you win.

			// Checks if there actually are pieces in those positions
			if ((board[0][i] != null) && (board[1][i] != null) && (board[2][i] != null) && (board[3][i] != null))
				// Checks if those pieces have something in common
				if (Piece.victory(board[0][i], board[1][i], board[2][i], board[3][i]))
					return 1; // if they all have something in common you win.
		}
		// Checks diagonal
		if ((board[0][0] != null) && (board[1][1] != null) && (board[2][2] != null) && (board[3][3] != null))
			if (Piece.victory(board[0][0], board[1][1], board[2][2], board[3][3]))
				return 1; // win
		// Checks diagonal
		if ((board[0][3] != null) && (board[1][2] != null) && (board[2][1] != null) && (board[3][0] != null))
			if (Piece.victory(board[0][3], board[1][2], board[2][1], board[3][0]))
				return 1;
		// Checks if there are void space
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[i][j] == null)
					return 1000;// There are some void space, the game isn't
								// ended
		return 0;// The game is ended with parity
	}

	/**
	 * It get the size of the board
	 * 
	 * @return the size of the board
	 */
	@Override
	public int size() {
		return 16;
	}

	/**
	 * checks if a position on the board is empty or not<br>
	 * IMPORTANT: The position for simplify the code of the class Player is an
	 * integer that index the matrix like a vector <br>
	 * Matrix <br>
	 * 0 1 2 3<br>
	 * 4 5 6 7<br>
	 * 8 9 10 11<br>
	 * 12 13 14 15<br>
	 * 
	 * @param position
	 *            position to check
	 * @return true if the position is empty, false overwhise
	 */
	@Override
	public boolean isFree(int position) {
		int[] index = { position, -1 };
		// Calls a method which converts an array index in a couple of indexes
		// for our square matrix
		convertIndex(index);
		// It has an array as input because it's passed by reference so we'll
		// have 2 edited variables into the array as output.
		if (board[index[0]][index[1]] == null)
			return true;
		else
			return false;
	}

	/**
	 * Places a piece into a certain position into the board<br>
	 * IMPORTANT: The position for simplify the code of the class Player is an
	 * integer that index the matrix like a vector
	 * 
	 * @param piece
	 *            object to position
	 * @param position
	 *            array index where you want to position the piece
	 */
	@Override
	public void putPieceAtPosition(Piece piece, int position) {
		int[] index = { position, -1 };
		convertIndex(index);
		board[index[0]][index[1]] = piece;

	}

	/**
	 * Assigns "null" as value to the input-given board's position
	 * 
	 * @param position
	 *            where you want to remove the piece
	 * @return Piece removed
	 */
	@Override
	public Piece removePieceAtPosition(int position) {
		int[] index = { position, -1 };
		convertIndex(index);
		board[index[0]][index[1]] = null;

		return null;
	}

	/**
	 * Check if a piece is already placed in the board
	 * 
	 * @param p
	 *            Piece you want to check if is placed
	 * @return true if is already placed, false otherwise
	 */
	public boolean isPlaced(Piece p) {
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				if ((board[r][c] != null) && (p.isEqualTo(board[r][c])))
					return true;
			}
		}
		return false;
	}

	/**
	 * Converts an Array index into a couple of coordinates for a matrix. Uses
	 * an array because it's passed by references.
	 * 
	 * @param index
	 *            it's a vector contains in the first position the array index
	 *            and then it insert in the first position the x-index and in
	 *            the second position the y-index
	 */
	private void convertIndex(int[] index) {
		int position = index[0];
		int row = 0, column = 0;
		column = position % 4;
		row = (position - column) / 4;
		index[0] = row;
		index[1] = column;

	}
}
