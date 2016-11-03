package com.ecommerce.sns;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PushNotificationMessageGenerator {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// GCM => Google Cloud Messaging
	// APNS => Apple Push Notification Service
	// APNS_SANDBOX => Sandbox version of Apple Push Notification Service
	public static enum Platform {
		APNS, APNS_SANDBOX, GCM
	}

	public static String jsonify(Object message) throws JsonProcessingException {
		return objectMapper.writeValueAsString(message);
	}

	public static String getAppleMessage(String message, int badgeCount, Map<String, Object> extraParams)
			throws JsonProcessingException {
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		message = StringEscapeUtils.unescapeJava(message);
		appMessageMap.put("alert", message);
		appMessageMap.put("badge", badgeCount);
		appMessageMap.put("sound", "default");
		extraParams.put("aps", appMessageMap);
		return jsonify(extraParams);
	}

	public static String getAndroidMessage(String message, int badgeCount, Map<String, Object> extraParams)
			throws JsonProcessingException {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		extraParams.put("message", message);
		androidMessageMap.put("collapse_key", "Welcome");
		androidMessageMap.put("data", extraParams);
		androidMessageMap.put("delay_while_idle", true);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);
		return jsonify(androidMessageMap);
	}
}
