package Exception;

/**
 * This exception is for error inside the file about piece not used
 * 
 * @author Morettini and Candelaresi
 *
 */
public class NotUsedPieceConfigurationException extends Exception {
	public NotUsedPieceConfigurationException() {
		super();
	}

	public NotUsedPieceConfigurationException(String message) {
		super(message);
	}

}
