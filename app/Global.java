import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.notFound;

import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import utils.MyConstants.FailureMessages;
import utils.MyFailureResponse;
import utils.MyResponse;

public class Global extends GlobalSettings {

	@Override
	public Promise<Result> onBadRequest(RequestHeader request, String error) {
		MyResponse response = new MyFailureResponse(FailureMessages.BAD_REQUEST, error);
		return Promise.<Result> pure(badRequest(response.getResultJson()));
	}

	@Override
	public Promise<Result> onHandlerNotFound(RequestHeader request) {
		MyResponse response = new MyFailureResponse(FailureMessages.INVALID_API_CALL);
		return Promise.<Result> pure(notFound(response.getResultJson()));
	}

	@Override
	public Promise<Result> onError(RequestHeader request, Throwable t) {
		MyResponse response = new MyFailureResponse(FailureMessages.TECHNICAL_ERROR);
		return Promise.<Result> pure(internalServerError(response.getResultJson()));
	}

}
