package clueGame;

public class BadConfigFormatException extends Exception {
	private String errorMsg;
	
	public BadConfigFormatException() {
		setErrorMsg("Error: Bad Config Format");
	}
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
