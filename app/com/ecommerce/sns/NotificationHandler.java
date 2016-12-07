package com.ecommerce.sns;

import java.util.List;
import java.util.Map;

import com.ecommerce.models.sql.UserPushNotifications;
import com.ecommerce.models.sql.UserSession;
import com.ecommerce.models.sql.Users;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;

import play.Logger;
import play.i18n.Messages;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.AccountType;
import utils.MyConstants.OsType;

public class NotificationHandler {

	private static PushNotificationSender snsPushObj;

	static {
		snsPushObj = new PushNotificationSender();
	}

	public static void sendUserPushNotification(String senderId, String receiverId, String message,
			Map<String, Object> extraParams) {
		try {
			int badgeCount = Integer.parseInt(extraParams.get(APIResponseKeys.UNREAD_MESSAGE_COUNT).toString());
			int notificationType = (int) extraParams.get(APIResponseKeys.NOTIFICATION_TYPE);
			Users user = Users.findById(receiverId);

			// TODO Check user notification settings and enable or disable
			// notifications
			if (user != null) {
				List<UserSession> sessionList = UserSession.findByEncryptedUserId(receiverId);

				for (UserSession session : sessionList) {
					int platform = session.getDeviceType().getId();
					if (session.getEndPointArn() != null) {

						message = Messages.get(message);
						String pushNotificationMessage = generatePushNotificationMessage(platform, message, badgeCount,
								extraParams);

						// add notification messages in table
						UserPushNotifications notificationObj = new UserPushNotifications();

						Long id = notificationObj.create(senderId, receiverId, pushNotificationMessage,
								session.getDeviceToken(), notificationType, AccountType.USER);

						snsPushObj.sendPushNotification(session.getEndPointArn(), pushNotificationMessage, platform);

						// update the badge count after sending push
						// notification
						session.updateBadgeCount(badgeCount);
						notificationObj.updateNotificationStatus(id);
					}
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}

	}

	public static void sendVendorPushNotification(String senderId, String receiverId, String message,
			Map<String, Object> extraParams) {
		try {
			int badgeCount = Integer.parseInt(extraParams.get(APIResponseKeys.UNREAD_MESSAGE_COUNT).toString());
			int notificationType = (int) extraParams.get(APIResponseKeys.NOTIFICATION_TYPE);
			Vendors vendor = Vendors.findById(receiverId);

			// TODO Check vendor notification settings and enable or disable
			// notifications
			if (vendor != null) {
				List<VendorSession> sessionList = VendorSession.findByEncryptedVendorId(receiverId);

				for (VendorSession session : sessionList) {
					int platform = session.getDeviceType().getId();
					if (session.getEndPointArn() != null) {

						message = Messages.get(message);
						String pushNotificationMessage = generatePushNotificationMessage(platform, message, badgeCount,
								extraParams);

						// add notification messages in table
						UserPushNotifications notificationObj = new UserPushNotifications();

						Long id = notificationObj.create(senderId, receiverId, pushNotificationMessage,
								session.getDeviceToken(), notificationType, AccountType.VENDOR);

						snsPushObj.sendPushNotification(session.getEndPointArn(), pushNotificationMessage, platform);

						// update the badge count after sending push
						// notification
						session.updateBadgeCount(badgeCount);
						notificationObj.updateNotificationStatus(id);
					}
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}

	}

	private static String generatePushNotificationMessage(int platform, String message, int badgeCount,
			Map<String, Object> extraParams) throws Exception {

		String pushNotificationMessage;

		switch (platform) {
		case OsType.IOS:
			pushNotificationMessage = PushNotificationMessageGenerator.getAppleMessage(message, badgeCount,
					extraParams);
			break;
		case OsType.ANDROID:
			pushNotificationMessage = PushNotificationMessageGenerator.getAndroidMessage(message, badgeCount,
					extraParams);
			break;
		default:
			throw new Exception("platform " + platform + " not supported");
		}

		return pushNotificationMessage;
	}

}
