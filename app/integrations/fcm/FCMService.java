package integrations.fcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import play.Configuration;
import play.Logger;
import play.libs.Json;
import utils.ObjectMapperUtil;

@Singleton
public class FCMService {

	@Inject
	private Configuration configuration;

	public static String FCM_SEND_NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";

	public void sendNotification(String to, FCMNotification notification) throws UnirestException, IOException {

		String authKey = "key=" + getAuthKey();

		Map<String, Object> outSide = new HashMap<String, Object>();
		outSide.put("data", notification);

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", authKey);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("to", to);
		map.put("data", outSide);

		JsonNode params = ObjectMapperUtil.getInstance().convertValue(Json.toJson(map).toString(), JsonNode.class);
		Logger.info("FCM Params : " + params);

		HttpResponse<String> response = Unirest.post(FCM_SEND_NOTIFICATION_URL).headers(headers).body(params)
				.asString();

		Logger.info("Notification status Response: " + response.getBody());

	}

	private String getAuthKey() {
		return configuration.getString("FCM_SERVER_AUTH_KEY");
	}

}
