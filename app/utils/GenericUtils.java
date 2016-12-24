package utils;

import java.util.UUID;

import com.ecommerce.models.sql.Products;

import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;

public class GenericUtils {

	public static double computeProductTotalPrice(Products product, double quantity) {
		double price = product.getPrice() * quantity;
		return price;
	}

	public static String generateReferralCode() {
		// TODO
		String referralCode = UUID.randomUUID().toString();
		return referralCode;
	}

	public static String generateTransactionId() {
		// TODO
		String transactionId = UUID.randomUUID().toString();
		return transactionId;
	}

	public static String generateOrderId() {
		// TODO
		String orderId = UUID.randomUUID().toString();
		return orderId;
	}

	public static double computeExtraFee() {
		// TODO Get delivery fee based on vendor
		return MyConstants.DELIVERY_FEE;
	}

	public static void orderStatusValidation(int prevOrderStatus, int orderStatus) throws MyException {

		String failureMessage = null;
		switch (orderStatus) {
		case OrderStatus.CONFIRMED:
			if (prevOrderStatus == OrderStatus.CONFIRMED) {
				failureMessage = FailureMessages.ALREADY_CONFIRMED;
			}
		case OrderStatus.UNDER_PROGRESS:
			if (prevOrderStatus == OrderStatus.UNDER_PROGRESS) {
				failureMessage = FailureMessages.ALREADY_UNDER_PROGRESS;
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
			break;
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
			if (prevOrderStatus == OrderStatus.CONFIRMED) {
				failureMessage = FailureMessages.ALREADY_CONFIRMED;
			}
			if (prevOrderStatus == OrderStatus.UNDER_PROGRESS) {
				failureMessage = FailureMessages.ALREADY_UNDER_PROGRESS;
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
			break;
		}

		if (failureMessage != null) {
			throw new MyException(failureMessage);
		}

	}

}
