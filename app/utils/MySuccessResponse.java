package utils;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MySuccessResponse extends MyResponse {

	public MySuccessResponse(String successMessage) {
		result.put(error, 0);
		result.put(messageKey, getResponseMessage(successMessage));
	}

	public MySuccessResponse(ObjectNode successData, Integer... args) {
		if (successData.has(messageKey)) {
			String successMessage = successData.get(messageKey).asText();
			successData.put(messageKey, getResponseMessage(successMessage));
		}

		if (args.length == 0) {
			result.put(error, 0);
		} else {
			result.put(error, args[0]);
		}

		ArrayList<JsonNode> list = new ArrayList<JsonNode>();
		list.add(successData);
		result.set(data, successData);
	}
}
