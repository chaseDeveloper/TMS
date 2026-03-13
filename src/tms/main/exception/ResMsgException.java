package tms.main.exception;

public class ResMsgException extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg;
	private String uri;


	public String getMsg() {
		return msg;
	}
	

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getUri() {
		return uri;
	}


	public ResMsgException() {
	}
	
	public ResMsgException(String msg) {
		this.msg = msg;		
	}
	
	public ResMsgException(String msg, String uri) {
		this.msg = msg;
		this.uri = uri;
	}
}
