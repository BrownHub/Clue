package clueGame;

public class BadConfigFormatException extends Exception {
	private String errorMsg;
	
	public BadConfigFormatException() {
		errorMsg = "Error: Bad Config Format";
	}
	public BadConfigFormatException(String message) {
		errorMsg = message;
	}
}
