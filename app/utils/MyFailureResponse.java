package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyFailureResponse extends MyResponse {

	public MyFailureResponse(String errorMessage) {
		result.put(error, 100);
		result.put(messageKey, getResponseMessage(errorMessage));
	}

	public MyFailureResponse(String errorMessage, String systemMessage) {
		result.put(error, 100);
		result.put(messageKey, getResponseMessage(errorMessage));
		result.put(systemMessageKey, systemMessage);
	}

	public MyFailureResponse(String errorMessage, Exception e) {

		String systemMessage = e.getMessage();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		String stackTraceString = sw.toString();
		this.stackTrace += stackTraceString;
		systemMessage += "\n Stack Trace: \n " + this.stackTrace;

		// Mark this API response (with exception message) to be notified by
		// email to developers.
		this.notifyByEmail = true;

		if (e instanceof NullPointerException) {
			this.isNullPointerException = true;
		}

		result.put(error, 100);
		result.put(messageKey, getResponseMessage(errorMessage));
		result.put(systemMessageKey, systemMessage);
	}

}
