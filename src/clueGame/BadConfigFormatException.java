package clueGame;

public class BadConfigFormatException extends Exception {
	private String errorMsg;
	
	// constructor which prints generic error message
	public BadConfigFormatException() {
		setErrorMsg("Error: Bad Config Format");
	}
	
	// constructor which prints specific error message
	public BadConfigFormatException(String message) {
		setErrorMsg(message);
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
