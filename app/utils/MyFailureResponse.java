package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

public class MyFailureResponse extends MyResponse {

	public MyFailureResponse(String errorMessage, Integer... args) {
		result.put(error, 100);
		result.put(count, 0);
		result.put(messageKey, getResponseMessage(errorMessage));
		result.set(resultKey, Json.toJson(new ArrayList<JsonNode>()));
	}

	public MyFailureResponse(String errorMessage, String systemMessage) {
		result.put(error, 100);
		result.put(count, 0);
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

		result.put(messageKey, getResponseMessage(errorMessage));
		result.put(systemMessageKey, systemMessage);
	}

}
