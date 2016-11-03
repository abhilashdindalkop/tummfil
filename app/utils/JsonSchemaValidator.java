package utils;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.MyConstants.JsonSchemaKeys;
import utils.ValidateJsonSchema.ValidateJson;

public class JsonSchemaValidator extends Action<ValidateJson> {
	private final static String WORKING_DIRECTORY = Play.application().path().getAbsolutePath();

	/**
	 * Delegates the call further if its valid input
	 */
	@Override
	public Promise<Result> call(Http.Context context) throws Throwable {
		Promise<Result> result = validateJson();
		if (result == null) {
			result = delegate.call(context);
		}
		return result;
	}

	private Promise<Result> validateJson() {

		MyResponse response = null;

		try {
			ObjectNode inputJson = (ObjectNode) Http.Context.current().request().body().asJson();

			if (inputJson == null) {
				throw new MyException(MyConstants.FailureMessages.INVALID_JSON.toString());
			}
			String filePath = WORKING_DIRECTORY + "/conf" + configuration.value();
			// /jsonSchemas/feedDetails.json

			JsonNode jsonSchema = CustomJsonFactory.getjsonSchemaNode(filePath);

			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonSchema schema = factory.getJsonSchema(jsonSchema);
			ProcessingReport report = schema.validate(inputJson);
			Logger.info("== error ==" + report);

			if (report.isSuccess()) {
				return null;
			}
			ProcessingMessage message;
			Iterator<ProcessingMessage> itr = report.iterator();

			while (itr.hasNext()) {
				message = itr.next();
				String levelKey = JsonSchemaResponseKeys.JSONSCHEMA_LEVEL_KEY.toString();
				String level = message.asJson().get(levelKey).asText();
				if (level == "error") {
					throw new MyException(getFailureMessage(message.asJson()));
				}
			}
		}

		catch (JsonParseException e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(MyConstants.FailureMessages.TECHNICAL_ERROR.toString(),
					MyConstants.FailureMessages.JSON_PARSE_ERROR.toString());
		}

		catch (JsonMappingException e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(MyConstants.FailureMessages.TECHNICAL_ERROR.toString(),
					MyConstants.FailureMessages.JSON_MAPPER_ERROR.toString());
		}

		catch (IOException e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(MyConstants.FailureMessages.TECHNICAL_ERROR.toString(),
					MyConstants.FailureMessages.IO_ERROR.toString());
		}

		catch (ProcessingException e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(MyConstants.FailureMessages.TECHNICAL_ERROR.toString(),
					MyConstants.FailureMessages.JSON_MAPPER_ERROR.toString());
		}

		catch (MyException e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(e.getMessage());
		}

		catch (Exception e) {
			Logger.error(e.getMessage());
			response = new MyFailureResponse(MyConstants.FailureMessages.TECHNICAL_ERROR.toString(),
					e.getMessage());
		}

		return response.getPromise();
	}

	private static String getFailureMessage(JsonNode json) {
		String failureMessage = null, missingResponseKey, unwantedResponseKey, instanceResponseKey, pointerResponseKey,
				minLengthKey, maxLengthKey;

		missingResponseKey = JsonSchemaKeys.JSONSCHEMA_MISSING_KEY;
		unwantedResponseKey = JsonSchemaKeys.JSONSCHEMA_UNWANTED_KEY;
		instanceResponseKey = JsonSchemaKeys.JSONSCHEMA_INSTANCE_KEY;
		pointerResponseKey = JsonSchemaKeys.JSONSCHEMA_POINTER_KEY;
		minLengthKey = JsonSchemaKeys.MIN_LENGTH_KEY;
		maxLengthKey = JsonSchemaKeys.MAX_LENGTH_KEY;
		String instanceKey = JsonSchemaKeys.INSTANCE_KEY;
		String pointerKey = JsonSchemaKeys.POINTER_KEY;
		String keywordKey = JsonSchemaKeys.KEYWORD_KEY;
		String expectedKey = JsonSchemaKeys.EXPECTED_KEY;

		if (json.get(missingResponseKey) != null) {
			String missingField = json.get(missingResponseKey).get(0).asText();
			failureMessage = formatString(missingField) + " is missing";
		} else if (json.has(minLengthKey)) {
			String field = json.get(instanceKey).get(pointerKey).asText();
			int minLengthValue = json.get(minLengthKey).asInt();
			failureMessage = formatString(field) + " - length should be atleast " + minLengthValue + " character(s)";
		} else if (json.has(maxLengthKey)) {
			String field = json.get(instanceKey).get(pointerKey).asText();
			int maxLengthValue = json.get(maxLengthKey).asInt();
			failureMessage = formatString(field) + " - length should be maximum of " + maxLengthValue + " character(s)";
		} else if (json.has(keywordKey) && json.get(keywordKey).asText().equals("type")) {
			String field = json.get(instanceKey).get(pointerKey).asText();
			String type = json.get(expectedKey).get(0).asText();
			failureMessage = "Type mismatch. " + formatString(field) + " should be of type " + type;
		} else if (json.has("minItems")) {
			String field = json.get(instanceKey).get(pointerKey).asText();
			failureMessage = formatString(field) + " field should contain atleast one element";
		} else if (json.get(unwantedResponseKey) != null) {
			failureMessage = JsonSchemaKeys.INVALID_INPUT;
		} else {
			String invalidField = json.get(instanceResponseKey).get(pointerResponseKey).asText();
			failureMessage = formatString(invalidField) + " is invalid";
		}

		return failureMessage;
	}

	private static String formatString(String str) {
		if (str.charAt(0) == '/') {
			str = str.substring(1, 2).toUpperCase() + str.substring(2);
		} else {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		}

		if (str.indexOf('/') >= 0) {
			int occurrences = StringUtils.countMatches(str, '/');
			str = str.split("/")[occurrences];
		}
		return str;
	}

}
