package classes;

public class PieceConfigurationException extends Exception {
	public PieceConfigurationException() {
		super();
	}

	public PieceConfigurationException(String message) {
		super(message);
	}

	public PieceConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PieceConfigurationException(Throwable cause) {
		super(cause);
	}
}
