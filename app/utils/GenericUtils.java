package utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.Vendors;

import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;

public class GenericUtils {

	public static double computeProductTotalPrice(Products product, double quantity) {
		double price = product.getPrice() * quantity;
		return price;
	}

	public static String generateReferralCode(String uuid) {
		StringBuilder referralCode = createRandomCode(6);
		referralCode.append(uuid.substring(5, 7));
		return referralCode.toString().toUpperCase();
	}

	public static String generateTransactionId(String orderId) {
		StringBuilder transactionId = createRandomCode(4);
		transactionId.append(new Date().getTime() / 100000000);
		transactionId.append(orderId.substring(5, 7));
		return transactionId.toString().toUpperCase();
	}

	public static String generateOrderId(long userId, long vendorId) {
		StringBuilder orderId = new StringBuilder();
		orderId.append("T-");
		orderId.append(createRandomCode(4));
		orderId.append(userId);
		orderId.append(createRandomCode(2));
		orderId.append(vendorId);
		orderId.append(new Date().getTime() / 100000000);
		return orderId.toString().toUpperCase();
	}

	public static double computeExtraFee(Vendors vendor) {
		// TODO Get delivery fee based on vendor
		return vendor.getShippingFee();
	}

	public static StringBuilder createRandomCode(int codeLength) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < codeLength; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb;
	}

	public static void orderStatusValidation(int prevOrderStatus, int orderStatus) throws MyException {

		String failureMessage = null;
		switch (orderStatus) {
		case OrderStatus.CONFIRMED:
			if (prevOrderStatus == OrderStatus.CONFIRMED) {
				failureMessage = FailureMessages.ALREADY_CONFIRMED;
			}
			if (prevOrderStatus == OrderStatus.OUT_FOR_DELIVERY) {
				failureMessage = FailureMessages.ALREADY_OUT_FOR_DELIVERY;
			}
			if (prevOrderStatus == OrderStatus.CANCELLED) {
				failureMessage = FailureMessages.ALREADY_CANCELLED;
			}
			if (prevOrderStatus == OrderStatus.DELIVERED) {
				failureMessage = FailureMessages.ALREADY_DELIVERED;
			}
			if (prevOrderStatus == OrderStatus.DECLINED) {
				failureMessage = FailureMessages.ALREADY_DECLINED;
			}
		case OrderStatus.OUT_FOR_DELIVERY:
			if (prevOrderStatus == OrderStatus.OUT_FOR_DELIVERY) {
				failureMessage = FailureMessages.ALREADY_OUT_FOR_DELIVERY;
			}
			if (prevOrderStatus == OrderStatus.CANCELLED) {
				failureMessage = FailureMessages.ALREADY_CANCELLED;
			}
			if (prevOrderStatus == OrderStatus.DELIVERED) {
				failureMessage = FailureMessages.ALREADY_DELIVERED;
			}
			if (prevOrderStatus == OrderStatus.DECLINED) {
				failureMessage = FailureMessages.ALREADY_DECLINED;
			}
			break;
		case OrderStatus.DECLINED:
			if (prevOrderStatus == OrderStatus.OUT_FOR_DELIVERY) {
				failureMessage = FailureMessages.ALREADY_OUT_FOR_DELIVERY;
			}
			if (prevOrderStatus == OrderStatus.CANCELLED) {
				failureMessage = FailureMessages.ALREADY_CANCELLED;
			}
			if (prevOrderStatus == OrderStatus.DELIVERED) {
				failureMessage = FailureMessages.ALREADY_DELIVERED;
			}
			if (prevOrderStatus == OrderStatus.DECLINED) {
				failureMessage = FailureMessages.ALREADY_DECLINED;
			}
			break;
		case OrderStatus.CANCELLED:
			if (prevOrderStatus == OrderStatus.CONFIRMED) {
				failureMessage = FailureMessages.CANNOT_CANCEL_CONFIRMED_ORDER;
			}
			if (prevOrderStatus == OrderStatus.PENDING) {
				failureMessage = FailureMessages.CANNOT_CANCEL_PENDING_ORDER;
			}
			if (prevOrderStatus == OrderStatus.CANCELLED) {
				failureMessage = FailureMessages.ALREADY_CANCELLED;
			}
			if (prevOrderStatus == OrderStatus.DELIVERED) {
				failureMessage = FailureMessages.ALREADY_DELIVERED;
			}
			if (prevOrderStatus == OrderStatus.DECLINED) {
				failureMessage = FailureMessages.ALREADY_DECLINED;
			}
			break;
		case OrderStatus.DELIVERED:
			if (prevOrderStatus == OrderStatus.PENDING) {
				failureMessage = FailureMessages.CONFIRM_PENDING_ORDER_FIRST;
			}
			if (prevOrderStatus == OrderStatus.CANCELLED) {
				failureMessage = FailureMessages.ALREADY_CANCELLED;
			}
			if (prevOrderStatus == OrderStatus.DELIVERED) {
				failureMessage = FailureMessages.ALREADY_DELIVERED;
			}
			if (prevOrderStatus == OrderStatus.DECLINED) {
				failureMessage = FailureMessages.ALREADY_DECLINED;
			}
			break;
		default:
			throw new MyException(FailureMessages.INVALID_ORDER_STATUS);
		}

		if (failureMessage != null) {
			throw new MyException(failureMessage);
		}

	}

}
