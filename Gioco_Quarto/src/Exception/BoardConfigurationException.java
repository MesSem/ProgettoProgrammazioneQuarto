package Exception;

/**
 * This exception will be thrown if an error occurs into the Board's file
 * 
 * @author Morettini and Candelaresi
 *
 */
public class BoardConfigurationException extends Exception {
	public BoardConfigurationException() {
		super();
	}

	public BoardConfigurationException(String message) {
		super(message);
	}

}
