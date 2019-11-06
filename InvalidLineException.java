
public class InvalidLineException extends Exception {

	public InvalidLineException(String errorMessage) {
		error = errorMessage;
	}

	public String	getError() {
		return (error);
	}

	private String	error;
}
