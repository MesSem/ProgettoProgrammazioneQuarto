package classes;

import Exception.PieceConfigurationException;
import interfaces.I_piece;

/**
 * This class stands for a single piece
 * 
 * @author Candelaresi
 *
 */
public class Piece implements I_piece {
	// These are the four characteristic about one piece
	private char size;
	private char colour;
	private char shape;
	private char volume;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            char array with the following characteristics, in this precise order:
	 *            size-color-shape-volume
	 */
	public Piece(char[] c) {
		size = c[0];
		colour = c[1];
		shape = c[2];
		volume = c[3];
	}

	/**
	 * Tries to create a new piece after some checks have been made. 
	 *We've made it 'static' because it allows us to check
	 *some parameters before we call the constructor method
	 *which is called inside this method.
	*
	 * NOTA PER ENRICO: COSA NE PENSI DELLA COSA DEL'ASTERISCO? MEGLIO METTERE
	 * UN CONTROLLO IN TABLE O LASCIARE COSÌ?
	 * 
	 * @param pieceDescription
	 *          contains data to create a new object
	 * @param asteriskAccepted
	 *            if the asterisk ("*") can be accepted(true) or not(false)
	 * @return the piece created
	 * @throws PieceConfigurationException
	 *             if  any error occurs in the String received to create the
	 *             piece
	 */
	static Piece checkAndCreate(String pieceDescription, boolean asteriskAccepted) throws PieceConfigurationException {
		char[] analyzedPiece = pieceDescription.toCharArray();
		// contains letters which compose the piece, after i've checked them
		String tmpPiece = "";
		int i = 0;
		if (analyzedPiece.length == 4 || analyzedPiece.length == 1) {
			for (char c : analyzedPiece) {
				switch (c) {
				case '*':
					if (asteriskAccepted)
						return null;
					else
						throw new PieceConfigurationException(
								"An error has occurred. The string ''*'' can not be accepted");
				case 'A':
				case 'B':
					// the if check if the index is zero, A or B could stay only
					// in the first position
					if (i == 0) {
						// i'm building a string which contains the letters, if
						// they're right.
						tmpPiece = tmpPiece + c;
					} else {
						// the analyzedPiece has some wrong parameter
						throw new PieceConfigurationException(
								"An error has occurred. First character should always be A or B.");
					}
					break;
				// in the other case the code do the same things for the other
				// letters
				case 'W':
				case 'N':
					if (i == 1) {
						tmpPiece = tmpPiece + c;
					} else {
						throw new PieceConfigurationException(
								"An error has occurred. Second character should always be W or N.");
					}
					break;
				case 'T':
				case 'Q':
					if (i == 2) {
						tmpPiece = tmpPiece + c;
					} else {
						throw new PieceConfigurationException(
								"An error has occurred. Third character should always be T or Q.");
					}
					break;
				case 'P':
				case 'F':
					if (i == 3) {
						tmpPiece = tmpPiece + c;
					} else {
						throw new PieceConfigurationException(
								"An error has occurred. Fourth character should always be P or F.");
					}
					break;
				default:
					throw new PieceConfigurationException(
							"An error has occurred. Character '" + c + "' can not be accepted");
				}
				i++;
			}
		} else {
			throw new PieceConfigurationException(
					"An error has occurred. File is compromised. --->There's a string longer or shorter than 4 chars.");
		}
		if (analyzedPiece.length == 4) {
			// If the program arrives here it means that the description of the
			// piece is okay, so the method create the piece.
			Piece p = new Piece(tmpPiece.toCharArray());
			return p;
		} else {
			// Null will be used in the board to identify empty box of the board
			return null;
		}
	}

	/**
	 * Checks if an input-given piece is equal to the current piece.
	 * 
	 * @param p
	 *            The piece you want to compare the current piece with
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean isEqualTo(Piece p) {
		if ((this.colour == p.colour) && (this.size == p.size) && (this.volume == p.volume) && (this.shape == p.shape))
			return true;
		else {
			return false;
		}
	}

	/**
	 * Gets the object characteristics as String
	 * 
	 * @return The object characteristic in String format
	 */
	@Override
	public String toString() {
		return new StringBuilder().append(size).append(colour).append(shape).append(volume).toString();
	}

	/**
	 * Checks if 4 input-given pieces have something in common
	 * 
	 * @param p0
	 *            Piece #one
	 * @param p1
	 *            Piece #two
	 * @param p2
	 *            Piece #three
	 * @param p3
	 *            Piece #four
	 * @return true if there are something in common, false otherwise
	 */
	static boolean victory(Piece p0, Piece p1, Piece p2, Piece p3) {

		if (((p0.colour == p1.colour) && (p0.colour == p2.colour) && (p0.colour == p3.colour))
				|| ((p0.size == p1.size) && (p0.size == p2.size) && (p0.size == p3.size))
				|| ((p0.volume == p1.volume) && (p0.volume == p2.volume) && (p0.volume == p3.volume))
				|| ((p0.shape == p1.shape) && (p0.shape == p2.shape) && (p0.shape == p3.shape)))
			return true;
		else
			return false;
	}
}
