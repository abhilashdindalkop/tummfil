package com.ecommerce.sns;

import java.io.File;
import java.io.IOException;

import javax.persistence.PersistenceException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.ecommerce.sns.PushNotificationMessageGenerator.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;

import play.Logger;
import play.Play;
import utils.MyConstants.OsType;
import utils.MessageReaderFactory;

public class PushNotificationSender {

	private static AmazonSNSClientWrapper snsClientWrapper;
	private static AmazonSNS sns;
	private static String snsEndPoint;
	private static boolean isProduction;
	private static final String AWS_CREDENTIAL_FILE_PATH = Play.application().path().getAbsolutePath()
			+ "/conf/resources/awsSNSCredentials.properties";
	private static String apnsSandboxArn;
	private static String apnsProductionArn;

	static {
		try {
			sns = new AmazonSNSClient(new PropertiesCredentials(new File(AWS_CREDENTIAL_FILE_PATH)));
			snsEndPoint = MessageReaderFactory.getPropertyValue("SNS_ENDPOINT").trim();
			apnsSandboxArn = MessageReaderFactory.getPropertyValue("APNS_SANDBOX_ARN").trim();
			apnsProductionArn = MessageReaderFactory.getPropertyValue("APNS_PRODUCTION_ARN").trim();
			isProduction = Boolean
					.valueOf(MessageReaderFactory.getEnvironmentProperties().getProperty("is_apns_production").trim());
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sns.setEndpoint(snsEndPoint);
		snsClientWrapper = new AmazonSNSClientWrapper(sns);
	}

	/**
	 * Register your device creates Endpoint ARN
	 * 
	 * @param deviceToken
	 * @param osType
	 * @return Endpoint ARN associated to your device and your app
	 */
	public static String registerDevice(String deviceToken, int platform) {

		String endPointArn = null;
		switch (platform) {

		case OsType.IOS:
			if (isProduction) {
				endPointArn = registerAppleDevice(deviceToken);
			} else {
				endPointArn = registerAppleSandboxDevice(deviceToken);
			}
			break;

		case OsType.ANDROID:
			endPointArn = registerAndroidDevice(deviceToken);
			break;
		default:
			Logger.info("platform " + platform + " not supported");

		}

		return endPointArn;
	}

	/**
	 * Sends push notification to the given devices
	 * 
	 * @param endPointArn
	 * @param message
	 * @param platform
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws PersistenceException
	 * @throws PSQLException
	 * @throws JsonProcessingException
	 */
	public void sendPushNotification(String endPointArn, String message, int platform) {
		try {
			switch (platform) {
			case OsType.IOS:
				if (isProduction) {
					snsClientWrapper.sendPushNotification(Platform.APNS, endPointArn, message);
				} else {
					snsClientWrapper.sendPushNotification(Platform.APNS_SANDBOX, endPointArn, message);
				}
				break;
			case OsType.ANDROID:
				snsClientWrapper.sendPushNotification(Platform.GCM, endPointArn, message);
				break;
			default:
				Logger.info("platform " + platform + " not supported");
			}

		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
	}

	/**
	 * Unregister device Deletes Endpoint ARN
	 * 
	 * @param endPointArn
	 */
	public static void unRegisterDevice(String endPointArn) {
		snsClientWrapper.deleteEndpointArn(endPointArn);
	}

	/**
	 * Register Android device
	 * 
	 * @param registrationId
	 * @return
	 */
	public static String registerAndroidDevice(String registrationId) {
		String platformApplicationArn = Play.application().configuration().getString("arn.gcm");
		return snsClientWrapper.registerDevice(Platform.GCM, registrationId, platformApplicationArn);
	}

	/**
	 * Register Apple device
	 * 
	 * @param deviceToken
	 * @return
	 */
	public static String registerAppleDevice(String deviceToken) {
		return snsClientWrapper.registerDevice(Platform.APNS, deviceToken, apnsProductionArn);
	}

	/**
	 * Register Apple Sandbox device
	 * 
	 * @param deviceToken
	 * @return
	 */
	public static String registerAppleSandboxDevice(String deviceToken) {
		return snsClientWrapper.registerDevice(Platform.APNS_SANDBOX, deviceToken, apnsSandboxArn);
	}

}
