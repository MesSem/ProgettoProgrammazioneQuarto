package Exception;

/**
 * This exception is for error inside the file with board's data
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
