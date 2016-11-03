
package utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import play.Logger;

public class SocialAccounts {

	private static final String FACEBOOK_MY_PROFILE_END_POINT = "me?";

	private static final String FACEBOOK_BASE_END_POINT = "https://graph.facebook.com/v2.5/";

	private static final String ACCESS_TOKEN = "&access_token=";

	public static boolean validateFBCredentials(String fbId, String fbAccessToken) {
		try {
			HttpResponse<JsonNode> resp = Unirest.get(FACEBOOK_BASE_END_POINT.concat(FACEBOOK_MY_PROFILE_END_POINT)
					.concat(ACCESS_TOKEN).concat(fbAccessToken)).asJson();
			if (resp != null) {
				if (resp.getBody().getObject().get("id").equals(fbId)) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.info(e.fillInStackTrace().toString());
		}
		return false;
	}

}