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

/**
 * This class represent the board where players play to Quarto!
 * 
 * @author Candelaresi
 *
 */
public class Board implements I_board {

	// Is the board where the players put the pieces
	public Piece[][] board = new Piece[4][4];

	/**
	 * Loads placed pieces in the board.
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
		BufferedReader boardReader = null;
		try {
			// Creates file-reader variables.
			File boardfile = new File(path);
			if (boardfile.exists()) {
				// opens file, reading mode
				FileReader br = new FileReader(boardfile);
				boardReader = new BufferedReader(br);
				String app;
				int cont = 0;
				// while i'm actually reading something and if i've still not
				// read more than 4 lines i put the value of that line in app
				while (((app = boardReader.readLine()) != null) && cont < 5) {
					String[] row = app.split(" ");
					if(cont>=4){
						cont++;
						break;
					}
					if(row.length !=4){
						throw new BoardConfigurationException("The line "+ (cont+1)+" of the board file contains less or more than 4 box");
					}
					// scans each row's cells and checks its actual value, if
					// it's okay it becomes a piece on the board.
					for (int c = 0; c < 4; c++) {
						try {
							Piece p = Piece.checkAndCreate(row[c], true);

							if (p != null) {
								// check if the piece is already placed in the
								// board
								boolean placed = isPlaced(p);
								if (placed) {
									throw new BoardConfigurationException("This piece "+ row[c] +" is already placed!");
								} else {
									board[cont][c] = p;
								}
							}
						} catch (PieceConfigurationException e) {
							// The PieceConfigurationException is checked and it
							// throw a more specific exception
							throw new BoardConfigurationException(e.getMessage());
						}
					}
					cont++;
				}
				if(cont!=4){
					throw new BoardConfigurationException("The file of the board contains less or more than 4 rows");
				}
				boardReader.close();
			} else {
				throw new IOException("File doesn't exist.");
			}
		} catch (IOException e) {
			throw e;
		} catch (BoardConfigurationException er) {
			throw er;
		} finally {
			// If there was some exception the code try to close the Buffer
			// before to go forward
			try {
				boardReader.close();
			} catch (Exception err) {

			}
		}

	}

	/**
	 * It saves board's new configuration in a file
	 * 
	 * @param path
	 *            location and name of the file where you want to save the data
	 * @throws IOException
	 *             gets threw when an error occurs searching or trying to read
	 *             the board file.
	 */
	@Override
	public void saveBoard(String path) throws IOException {
		FileWriter fw;
		BufferedWriter out = null;

		try {
			fw = new FileWriter(path);
			out = new BufferedWriter(fw);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					// If it's null it's a void space.
					if (board[i][j] == null)
						out.write("* ");
					else
						out.write(board[i][j].toString() + " ");
				}
				out.newLine();
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} finally {
			// If there was some error or not, I try to close the output-stream 
			try {
				//the flush method is to force the writing of all data in the buffer
				out.flush();
				out.close();
			} catch (Exception ex) {

			}
		}

	}

	/**
	 * It checks if the game is ended or not. There are three possible
	 * situations: <br>
	 * 0= the game is ended with a tie <br>
	 * 1= the game has ended and the current player is the winner <br>
	 * 1000= the game hasn't ended <br>
	 * 
	 * @return The situation of the game
	 */
	@Override
	public int gameSituation() {
		// scans each row and column of the board, checking if there's any
		// victory .
		for (int i = 0; i < 4; i++) {
			// Checks if there actually are pieces in those positions, In the
			// first if, the index i is used to scan the different row #TODO:
			// ROW OR COLUMN?
			if ((board[i][0] != null) && (board[i][1] != null) && (board[i][2] != null) && (board[i][3] != null)) {
				// Checks if those pieces have something in common
				if (Piece.victory(board[i][0], board[i][1], board[i][2], board[i][3]))
					return 1; // if they all have something in common you win.
			}

			// Same procedure of the first if but using i for scan the different
			// column
			if ((board[0][i] != null) && (board[1][i] != null) && (board[2][i] != null) && (board[3][i] != null)) {
				if (Piece.victory(board[0][i], board[1][i], board[2][i], board[3][i]))
					return 1;
			}
		}
		// Checks diagonals
		if ((board[0][0] != null) && (board[1][1] != null) && (board[2][2] != null) && (board[3][3] != null))
			if (Piece.victory(board[0][0], board[1][1], board[2][2], board[3][3]))
				return 1;
		if ((board[0][3] != null) && (board[1][2] != null) && (board[2][1] != null) && (board[3][0] != null))
			if (Piece.victory(board[0][3], board[1][2], board[2][1], board[3][0]))
				return 1;

		// Checks if there are empty spaces
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[i][j] == null)
					return 1000;// There are some empty spaces, the game hasn't
								// ended
		// The game has  ended with a tie
		return 0;
	}

	/**
	 *Gets board's size: row*column
	 * 
	 * @return board's size
	 */
	@Override
	public int size() {
		return 16;
	}

	/**
	 * Checks if a position on the board is empty or not<br>
	 * IMPORTANT: Position is given as an integer which stands for an array index
	 * to simplify  Player class' code.<br>
	 * Matrix <br>
	 * 0__1__2__3<br>
	 * 4__5__6__7<br>
	 * 8__9__10_11<br>
	 * 12_13_14_15<br>
	 * 
	 * @param position
	 *            position to check
	 * @return true if the position is empty, false otherwise
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
	 * IMPORTANT: Position is given as an integer which stands for an array index
	 * to simplify  Player class' code.<br>
	 * Matrix <br>
	 * 0__1__2__3<br>
	 * 4__5__6__7<br>
	 * 8__9__10_11<br>
	 * 12_13_14_15<br>
	 * 
	 * @param piece
	 *            object to place
	 * @param position
	 *            array's index where you want to place the piece
	 */
	@Override
	public void putPieceAtPosition(Piece piece, int position) {
		int[] index = { position, -1 };
		convertIndex(index);
		board[index[0]][index[1]] = piece;

	}

	/**
	 * Assigns "null" as value to the input-given board's position<br>
	 * IMPORTANT: Position is given as an integer which stands for an array index
	 * to simplify  Player class' code.<br>
	 * Matrix <br>
	 * 0__1__2__3<br>
	 * 4__5__6__7<br>
	 * 8__9__10_11<br>
	 * 12_13_14_15<br>
	 * 
	 * @param position
	 *            where you want to remove the piece
	 */
	@Override
	public void removePieceAtPosition(int position) {
		int[] index = { position, -1 };
		convertIndex(index);
		board[index[0]][index[1]] = null;
	}

	/**
	 * Checks if a piece is already placed on the board<br>
	 * IMPORTANT: Position is given as an integer which stands for an array index
	 * to simplify  Player class' code.<br>
	 * Matrix <br>
	 * 0__1__2__3<br>
	 * 4__5__6__7<br>
	 * 8__9__10_11<br>
	 * 12_13_14_15<br>
	 * 
	 * @param p
	 *            The Piece you want to check
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
	 *            it's a vector which contains in the first position the array index
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
