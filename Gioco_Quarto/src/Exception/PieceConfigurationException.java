package Exception;

/**
 * This exception will be thrown if Piece Not Used file's data are damaged,
 * corrupted, incomplete or wrong
 * 
 * @author Morettini and Candelaresi
 *
 */
public class PieceConfigurationException extends Exception {
	public PieceConfigurationException() {
		super();
	}

	public PieceConfigurationException(String message) {
		super(message);
	}

}
