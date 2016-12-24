package integrations.fcm;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.ecommerce.models.sql.UserPushNotifications;
import com.ecommerce.models.sql.UserSession;
import com.ecommerce.models.sql.Users;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;

import play.Logger;
import utils.MyConstants.AccountType;
import utils.MyConstants.OsType;

public class NotificationHandler {

	FCMHandler fcmHandler;
	AndroidNotifications androidNotifications;

	@Inject
	public NotificationHandler(FCMHandler fcmHandler, AndroidNotifications androidNotifications) {
		this.fcmHandler = fcmHandler;
		this.androidNotifications = androidNotifications;
	}

	public void sendUserPushNotification(Users receiver, int notificationType, HashMap<String, Object> params) {
		try {
			Users user = receiver;
			if (user == null) {
				return;
			}

			String receiverId = user.getEncryptedUserId();
			// TODO Check user notification settings and enable or disable
			// notifications

			// TODO Compute batch count
			int badgeCount = 0;

			List<UserSession> sessionList = UserSession.findByEncryptedUserId(receiverId);
			for (UserSession session : sessionList) {
				if (session.getDeviceToken() != null) {
					int platform = session.getDeviceType().getId();
					UserPushNotifications notificationObj = null;
					FCMNotification notification;
					/* Android or iOS Notifications */
					switch (platform) {
					case OsType.ANDROID:
						notification = androidNotifications.generateUserNotification(notificationType, params);
						notificationObj = fcmHandler.sendNotification(receiverId, AccountType.USER, notification,
								session.getDeviceToken(), false);
						break;
					case OsType.IOS:
						break;
					}

					/*
					 * update the badge count after sending push notification
					 */
					if (notificationObj != null) {
						session.updateBadgeCount(badgeCount);
						notificationObj.updateNotificationStatus();
					}
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
	}

	public void sendVendorPushNotification(Vendors receiver, int notificationType, HashMap<String, Object> params) {
		try {

			Vendors vendor = receiver;
			if (vendor == null) {
				Logger.info("--------------Vendor Not Found------------");
				return;
			}

			String receiverId = vendor.getEncryptedVendorId();
			// TODO Check user notification settings and enable or disable
			// notifications

			// TODO Compute batch count
			int badgeCount = 0;

			List<VendorSession> sessionList = VendorSession.findByEncryptedVendorId(receiverId);
			for (VendorSession session : sessionList) {
				if (session.getDeviceToken() != null) {
					int platform = session.getDeviceType().getId();

					UserPushNotifications notificationObj = null;
					FCMNotification notification;
					/* Android or iOS Notifications */
					switch (platform) {
					case OsType.ANDROID:
						notification = androidNotifications.generateVendorNotification(notificationType, params);
						notificationObj = fcmHandler.sendNotification(receiverId, AccountType.VENDOR, notification,
								session.getDeviceToken(), false);
						break;
					case OsType.IOS:
						break;
					}

					/*
					 * update the badge count after sending push notification
					 */
					if (notificationObj != null) {
						session.updateBadgeCount(badgeCount);
						notificationObj.updateNotificationStatus();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info(e.getMessage());
		}
	}

}
