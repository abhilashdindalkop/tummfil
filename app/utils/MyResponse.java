package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.i18n.Messages;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;

public class MyResponse extends Results {
	protected ObjectNode result = Json.newObject();
	protected String messageCodeKey = APIResponseKeys.MESSAGE_CODE;
	protected String messageKey = APIResponseKeys.MESSAGE;
	protected String systemMessageKey = APIResponseKeys.SYSTEM_MESSAGE;
	protected String stackTrace;
	protected boolean notifyByEmail;
	protected boolean isNullPointerException;
	protected boolean isUnauthorized = false;
	protected boolean isConflict = false;
	protected String error = APIResponseKeys.ERROR;
	protected String count = APIResponseKeys.COUNT;
	protected String resultKey = APIResponseKeys.RESULT_KEY;

	protected String getResponseMessage(String message, Object... args) {
		if (message.equals(FailureMessages.SESSION_INVALID)) {
			isUnauthorized = true;
		}

		if (message.equals(FailureMessages.SIGNOUT_CONFLICT)) {
			isConflict = true;
		}

		return Messages.get(message, args);
	}

	public Result getResult() {

		// boolean notifyErrrorsByEmail =
		// Play.application().configuration().getBoolean("errors.notifyByEmail",
		// false);
		//
		// if(this instanceof AgntFailureResponse && this.notifyByEmail &&
		// notifyErrrorsByEmail) {
		// Request req = Http.Context.current().request();
		// String token = req.getHeader(APIRequestKeys.TOKEN_HEADER);
		// String reqMethod = req.method();
		// String contentType =
		// req.getHeader(APIRequestKeys.CONTENT_TYPE_HEADER);
		// String message = new String();
		//
		// ObjectNode newResult = Json.newObject();
		// newResult.put(APIResponseKeys.MESSAGE,
		// result.get(APIResponseKeys.MESSAGE).asText());
		//
		// if(isNullPointerException) {
		// message += "Exception Type: Null Pointer \n<br/>";
		// }
		//
		// message += "Host: "+req.host()+"\n<br/>";
		// message += "Action name: "+req.uri()+"\n<br/>";
		// message += "IP: "+req.remoteAddress()+"\n<br/>";
		// message += "Token: "+token+"\n<br/>";
		// message += "Method: "+req.method()+"\n<br/>";
		//
		// if(reqMethod.equals("POST") &&
		// contentType.equals(RequestContentType.APPLICATION_JSON)) {
		// String inputString = req.body().asJson().toString();
		// message += "Request Data: "+inputString+"\n<br/>";
		// }
		//
		// message += "Response Data: "+newResult.toString()+"\n<br/>";
		//
		// if(this.stackTrace != null) {
		// message += "Stack Trace:\n<br/>"+
		// this.stackTrace.replaceAll("(\r\n|\n)", "<br/>");
		// }
		//
		// //TODO make it asynchronous
		// LatchMailer.sendMail("Latch API Error report: "+req.uri(), message,
		// AgntConstants.developerEmails);
		// }

		if (this instanceof MySuccessResponse) {
			return ok(result);
		} else if (isUnauthorized) {
			return unauthorized(result);
		} else if (isConflict) {
			return status(MyConstants.CONFLICT_HTTP_CODE, result);
		} else {
			return badRequest(result);
		}
	}

	public ObjectNode getResultJson() {
		return result;
	}

	public Promise<Result> getPromise() {

		return Promise.promise(new Function0<Result>() {

			@Override
			public Result apply() throws Throwable {
				return getResult();
			}
		});

	}
}
