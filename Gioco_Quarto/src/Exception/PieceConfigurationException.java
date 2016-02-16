package Exception;

/**
 * This exception is for error inside the data for the creation of a single piece
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
