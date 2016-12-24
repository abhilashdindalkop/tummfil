package integrations.fcm;

import javax.inject.Inject;

import com.ecommerce.models.sql.Orders;

import integrations.fcm.NotificationConstants.NotificationType;

public class TownNotificationMessages {

	NotificationHandler notificationHandler;

	@Inject
	public TownNotificationMessages(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}

	public void confirmOrderMessage(Orders order) {

		notificationHandler.sendUserPushNotification(order.getUser(), NotificationType.ORDER_CONFIRMED, null);
		notificationHandler.sendVendorPushNotification(order.getVendor(), NotificationType.ORDER_CONFIRMED, null);

	}

	public void outForDeliveryOrderMessage(Orders order) {

		notificationHandler.sendUserPushNotification(order.getUser(), NotificationType.ORDER_OUT_FOR_DELIVERY, null);
		notificationHandler.sendVendorPushNotification(order.getVendor(), NotificationType.ORDER_OUT_FOR_DELIVERY,
				null);

	}

	public void deliveredOrderMessage(Orders order) {

		notificationHandler.sendUserPushNotification(order.getUser(), NotificationType.ORDER_DELIVERED, null);
		notificationHandler.sendVendorPushNotification(order.getVendor(), NotificationType.ORDER_DELIVERED, null);

	}

	public void declinedOrderMessage(Orders order) {

		notificationHandler.sendUserPushNotification(order.getUser(), NotificationType.ORDER_DECLINED, null);
		notificationHandler.sendVendorPushNotification(order.getVendor(), NotificationType.ORDER_DECLINED, null);

	}

	public void cancelledOrderMessage(Orders order) {

		notificationHandler.sendUserPushNotification(order.getUser(), NotificationType.ORDER_CANCELLED, null);
		notificationHandler.sendVendorPushNotification(order.getVendor(), NotificationType.ORDER_CANCELLED, null);

	}

}
