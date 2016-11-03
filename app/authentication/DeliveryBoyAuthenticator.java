package authentication;

import javax.inject.Inject;

import com.ecommerce.dao.DeliveryBoySessionDAO;
import com.ecommerce.models.sql.DeliveryBoySession;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyFailureResponse;
import utils.MyResponse;

public class DeliveryBoyAuthenticator extends Security.Authenticator {

	DeliveryBoySessionDAO deliveryBoySessionDAO;

	@Inject
	public DeliveryBoyAuthenticator(DeliveryBoySessionDAO deliveryBoySessionDAO) {

		this.deliveryBoySessionDAO = deliveryBoySessionDAO;
	}

	@Override
	public String getUsername(Context ctx) {
		try {
			String accessToken = ctx.request().getHeader(APIRequestKeys.DELIVERY_BOY_TOKEN);
			if (accessToken == null) {
				return null;
			}
			DeliveryBoySession uToken = deliveryBoySessionDAO.findByToken(accessToken);
			if (uToken == null) {
				return null;
			}
			String boyId = uToken.getEncryptedId();
			ctx.args.put(APIRequestKeys.ENCRYPTED_DELIVERY_BOY_ID, boyId);
			ctx.args.put(APIRequestKeys.DELIVERY_BOY_TOKEN, accessToken);

			return boyId.toString();
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