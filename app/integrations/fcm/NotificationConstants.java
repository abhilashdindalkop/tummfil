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
		public static final int ORDER_PLACED = 2;
		public static final int ORDER_CONFIRMED = 3;
		public static final int ORDER_OUT_FOR_DELIVERY = 4;
		public static final int ORDER_DELIVERED = 5;
		public static final int ORDER_DECLINED = 6;
		public static final int ORDER_CANCELLED = 7;
		public static final int PAYMENT_SUCCESS = 8;
		public static final int PAYMENT_FAILED = 9;
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
			public static final String WELCOME = "Welcome to Tummfil!";
			public static final String ORDER_CONFIRMED = "Your order got confirmed!";
			public static final String ORDER_DECLINED = "Sorry for inconvinience, your order got declined";
			public static final String ORDER_CANCELLED = "Sorry for inconvinience, your order got cancelled";
			public static final String ORDER_OUT_FOR_DELIVERY = "Your order will reach you in sometime. It's already out for delivery!";
			public static final String ORDER_DELIVERED = "Thanks for ordering from Tummfil, have a great meal!";
		}

		class VendorMessages {
			/*
			 * Vendors
			 */
			public static final String WELCOME = "Welcome to Tummfil!";
			public static final String ORDER_CONFIRMED = "You got an order!";
			public static final String ORDER_DECLINED = "Order declined successful";
			public static final String ORDER_CANCELLED = "Order cancelled successful";
			public static final String ORDER_OUT_FOR_DELIVERY = "Order out for delivery";
			public static final String ORDER_DELIVERED = "Order delivered!";
		}
	}

}
