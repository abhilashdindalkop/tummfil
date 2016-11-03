package authentication;

import com.ecommerce.models.sql.VendorSession;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyFailureResponse;
import utils.MyResponse;

public class VendorAuthenticator extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		try {
			String accessToken = ctx.request().getHeader(APIRequestKeys.VENDOR_TOKEN);
			if (accessToken == null) {
				return null;
			}
			VendorSession uToken = VendorSession.findByToken(accessToken);
			if (uToken == null) {
				return null;
			}
			String vendorId = uToken.getEncryptedVendorId();
			ctx.args.put(APIRequestKeys.ENCRYPTED_VENDOR_ID, vendorId);
			ctx.args.put(APIRequestKeys.VENDOR_TOKEN, accessToken);

			return vendorId.toString();
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