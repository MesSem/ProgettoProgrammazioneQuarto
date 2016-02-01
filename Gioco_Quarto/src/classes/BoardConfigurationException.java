package classes;

public class BoardConfigurationException extends Exception {
	public BoardConfigurationException() {
		super();
	}

	public BoardConfigurationException(String message) {
		super(message);
	}

	public BoardConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BoardConfigurationException(Throwable cause) {
		super(cause);
	}
}
