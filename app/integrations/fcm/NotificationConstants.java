package integrations.fcm;

public class NotificationConstants {

	public enum FCMNavigationType {
		/**
		 * INETERNAL - This will open some activity or screen in the application
		 * EXTERNAL - This will open some URL in browser API - This will hit
		 * some GET URL in the background
		 */
		INTERNAL, API, SYNC, MAPS, CALL, MESSAGE, BROWSER, STORE, SHARE;
	}

	public enum FCMActionType {
		/**
		 * POSITIVE, NEGATIVE
		 */
		POSITIVE, NEGATIVE, NEUTRAL;
	}

	public class NotificationType {

		/*
		 * Common
		 */
		public static final int WELCOME = 1;
		public static final int ORDER_CONFIRMED = 2;
		public static final int ORDER_OUT_FOR_DELIVERY = 3;
		public static final int ORDER_DELIVERED = 4;
		public static final int ORDER_DECLINED = 5;
		public static final int ORDER_CANCELLED = 6;
		public static final int PAYMENT_SUCCESS = 7;
		public static final int PAYMENT_FAILED = 8;
		/*
		 * Users
		 */

		/*
		 * Vendors
		 */
	}

	public class NotificationMessages {

		class UserMessages {
			/*
			 * Users
			 */
			public static final String WELCOME = "Welcome!";
			public static final String CREATE_ORDER_USER = "Welcome!";
			public static final String ORDER_CONFIRMED = "Order confirmed";
			public static final String ORDER_DECLINED = "Order declined";
			public static final String ORDER_CANCELLED = "Order cancelled";
			public static final String ORDER_OUT_FOR_DELIVERY = "Order out for delivery";
			public static final String ORDER_DELIVERED = "Order delivered";
		}

		class VendorMessages {
			/*
			 * Vendors
			 */
			public static final String WELCOME = "Welcome!";
			public static final String CREATE_ORDER_USER = "Welcome!";
			public static final String ORDER_CONFIRMED = "Order confirmed";
			public static final String ORDER_DECLINED = "Order declined";
			public static final String ORDER_CANCELLED = "Order cancelled";
			public static final String ORDER_OUT_FOR_DELIVERY = "Order out for delivery";
			public static final String ORDER_DELIVERED = "Order delivered";
		}
	}

}
