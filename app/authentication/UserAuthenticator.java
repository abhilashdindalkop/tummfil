package authentication;

import com.ecommerce.models.sql.UserSession;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyFailureResponse;
import utils.MyResponse;

public class UserAuthenticator extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		try {
			String accessToken = ctx.request().getHeader(APIRequestKeys.USER_TOKEN_HEADER);
			if (accessToken == null) {
				return null;
			}
			UserSession uToken = UserSession.findByToken(accessToken);
			if (uToken == null) {
				return null;
			}
			String userId = uToken.getEncryptedUserId();
			ctx.args.put(APIRequestKeys.ENCRYPTED_USER_ID, userId);
			ctx.args.put(APIRequestKeys.USER_TOKEN_HEADER, accessToken);

			return userId.toString();
		} catch (Exception e) {
			Logger.error("Exception :: " + e.getMessage());
			return null;
		}

	}

	@Override
	public Result onUnauthorized(Context ctx) {
		MyResponse response = new MyFailureResponse(FailureMessages.SESSION_INVALID);
		return response.getResult();
	}
}