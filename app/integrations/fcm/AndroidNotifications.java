package integrations.fcm;

import java.util.HashMap;

import javax.inject.Inject;

import integrations.fcm.NotificationConstants.FCMNavigationType;
import integrations.fcm.NotificationConstants.NotificationMessages;
import integrations.fcm.NotificationConstants.NotificationType;

public class AndroidNotifications {

	FCMConstructNotification fcmConstructNotification;

	@Inject
	public AndroidNotifications(FCMConstructNotification fcmConstructNotification) {
		this.fcmConstructNotification = fcmConstructNotification;
	}

	public FCMNotification generateUserNotification(int notificationType, HashMap<String, Object> params) {

		FCMNotification myNotification = null;
		String title = null;
		String body = null;
		String navigationInfo = null;
		FCMNavigationType navType = null;

		switch (notificationType) {

		case NotificationType.WELCOME:
			title = NotificationMessages.UserMessages.WELCOME;
			body = NotificationMessages.UserMessages.WELCOME;
			navigationInfo = NotificationMessages.UserMessages.WELCOME;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_CONFIRMED:
			title = NotificationMessages.UserMessages.ORDER_CONFIRMED;
			body = NotificationMessages.UserMessages.ORDER_CONFIRMED;
			navigationInfo = NotificationMessages.UserMessages.ORDER_CONFIRMED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_OUT_FOR_DELIVERY:
			title = NotificationMessages.UserMessages.ORDER_OUT_FOR_DELIVERY;
			body = NotificationMessages.UserMessages.ORDER_OUT_FOR_DELIVERY;
			navigationInfo = NotificationMessages.UserMessages.ORDER_OUT_FOR_DELIVERY;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_DELIVERED:
			title = NotificationMessages.UserMessages.ORDER_DELIVERED;
			body = NotificationMessages.UserMessages.ORDER_DELIVERED;
			navigationInfo = NotificationMessages.UserMessages.ORDER_DELIVERED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_DECLINED:
			title = NotificationMessages.UserMessages.ORDER_DECLINED;
			body = NotificationMessages.UserMessages.ORDER_DECLINED;
			navigationInfo = NotificationMessages.UserMessages.ORDER_DECLINED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_CANCELLED:
			title = NotificationMessages.UserMessages.ORDER_CANCELLED;
			body = NotificationMessages.UserMessages.ORDER_CANCELLED;
			navigationInfo = NotificationMessages.UserMessages.ORDER_CANCELLED;
			navType = FCMNavigationType.MESSAGE;
			break;

		default:
			title = "Please define notification";
			body = "Please define notification";
			navigationInfo = "Please define notification";
			navType = FCMNavigationType.MESSAGE;
			break;
		}

		myNotification = fcmConstructNotification.simpleNotification(title, body, navigationInfo, navType, null, false);
		return myNotification;
	}

	public FCMNotification generateVendorNotification(int notificationType, HashMap<String, Object> params) {

		FCMNotification myNotification = null;
		String title = null;
		String body = null;
		String navigationInfo = null;
		FCMNavigationType navType = null;

		switch (notificationType) {

		case NotificationType.WELCOME:
			title = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			body = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_CONFIRMED:
			title = NotificationMessages.VendorMessages.ORDER_CONFIRMED;
			body = NotificationMessages.VendorMessages.ORDER_CONFIRMED;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_CONFIRMED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_OUT_FOR_DELIVERY:
			title = NotificationMessages.VendorMessages.ORDER_OUT_FOR_DELIVERY;
			body = NotificationMessages.VendorMessages.ORDER_OUT_FOR_DELIVERY;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_OUT_FOR_DELIVERY;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_DELIVERED:
			title = NotificationMessages.VendorMessages.ORDER_DELIVERED;
			body = NotificationMessages.VendorMessages.ORDER_DELIVERED;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_DELIVERED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_DECLINED:
			title = NotificationMessages.VendorMessages.ORDER_DECLINED;
			body = NotificationMessages.VendorMessages.ORDER_DECLINED;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_DECLINED;
			navType = FCMNavigationType.MESSAGE;
			break;

		case NotificationType.ORDER_CANCELLED:
			title = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			body = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			navigationInfo = NotificationMessages.VendorMessages.ORDER_CANCELLED;
			navType = FCMNavigationType.MESSAGE;
			break;

		default:
			title = "Please define notification";
			body = "Please define notification";
			navigationInfo = "Please define notification";
			navType = FCMNavigationType.MESSAGE;
			break;
		}

		myNotification = fcmConstructNotification.simpleNotification(title, body, navigationInfo, navType, null, false);
		return myNotification;
	}
}
