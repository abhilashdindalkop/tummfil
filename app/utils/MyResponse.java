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
	protected String data = APIResponseKeys.DATA;

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
