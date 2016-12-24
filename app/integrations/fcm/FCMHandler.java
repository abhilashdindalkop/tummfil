package integrations.fcm;

import java.io.IOException;

import javax.inject.Inject;

import com.ecommerce.models.sql.UserPushNotifications;
import com.mashape.unirest.http.exceptions.UnirestException;

import play.Logger;
import play.libs.Json;
import utils.MyConstants.OsType;

public class FCMHandler {

	FCMService service;

	@Inject
	public FCMHandler(FCMService service) {
		this.service = service;
	}

	public UserPushNotifications sendNotification(String receiverId, int accountType, FCMNotification fcmNotification,
			String deviceToken, boolean isTopic) throws UnirestException, IOException {

		/* add notification messages in table */
		UserPushNotifications notificationObj = new UserPushNotifications();

		String fcmStringify = Json.toJson(fcmNotification).toString();
		notificationObj.create(receiverId, fcmStringify, deviceToken, accountType, OsType.ANDROID);

		if (isTopic) {
			service.sendNotification("/topics/users", fcmNotification);
		} else if (deviceToken != null) {
			service.sendNotification(deviceToken, fcmNotification);
		}

		Logger.info("FCM notification sent successfully");
		return notificationObj;
	}

	/*
	 * Subscriptions
	 */
	// public Result subscribeToTopic(Users user, String topic, String platform)
	// {
	//
	// Subscriptions fcmSubscription = subscriptionDao.findByUserId(user);
	// if (fcmSubscription != null) {
	// // topic is already subscribed, hence ignore
	// // FCM Token Subscribed successfully
	// } else {
	// fcmSubscription = new Subscriptions();
	// fcmSubscription.setUser(user);
	// fcmSubscription.setTopic(topic);
	// subscriptionDao.create(fcmSubscription);
	// // FCM Token Subscribed successfully
	// }
	//
	// }
	//
	// public void unsubscribeToTopic(Users user, String topic) {
	//
	// Subscriptions fcmSubscription = subscriptionDao.findBy(user, topic);
	// if (fcmSubscription != null) {
	// subscriptionDao.softDelete(fcmSubscription);
	// // FCM Token unsubscribed successfully
	// }
	// }

}
