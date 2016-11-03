package com.ecommerce.sns;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.ecommerce.sns.PushNotificationMessageGenerator.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;

import play.Logger;

public class AmazonSNSClientWrapper {

	private final AmazonSNS snsClient;

	public AmazonSNSClientWrapper(AmazonSNS client) {
		this.snsClient = client;
	}

	/**
	 * Delete Endpoint ARN
	 * 
	 * @param endPointArn
	 */
	public void deleteEndpointArn(String endPointArn) {
		DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
		deleteEndpointRequest.setEndpointArn(endPointArn);
		snsClient.deleteEndpoint(deleteEndpointRequest);
	}

	/**
	 * Publish a push notification to an Endpoint.
	 * 
	 * @param platform
	 * @param endPointArn
	 * @param message
	 * @throws PersistenceException
	 * @throws JsonProcessingException
	 */
	public void sendPushNotification(Platform platform, String endPointArn, String message)
			throws JsonProcessingException {

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessageStructure("json");

		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(platform.name(), message);
		message = PushNotificationMessageGenerator.jsonify(messageMap);
		publishRequest.setTargetArn(endPointArn);
		Logger.info("{Message Body: " + message + "}");

		// publishing message
		publishRequest.setMessage(message);
		PublishResult publishResult = snsClient.publish(publishRequest);
		Logger.info("Notification sent successfully!!! \nMessageId:" + publishResult.getMessageId());
	}

	/**
	 * Create an Endpoint. This corresponds to an app on a device.
	 * 
	 * @param platform
	 * @param platformToken
	 * @param platformApplicationArn
	 * @return endpointArn
	 */
	public String registerDevice(Platform platform, String platformToken, String platformApplicationArn) {

		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setToken(platformToken);
		platformEndpointRequest.setPlatformApplicationArn(platformApplicationArn);
		CreatePlatformEndpointResult platformEndpointResult = snsClient.createPlatformEndpoint(platformEndpointRequest);
		return platformEndpointResult.getEndpointArn();
	}

}
