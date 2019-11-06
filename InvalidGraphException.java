
public class InvalidGraphException extends Exception {

	public InvalidGraphException(String errorMessage) {
		error = errorMessage;
	}

	public String	getError() {
		return (error);
	}

	private String	error;
}
