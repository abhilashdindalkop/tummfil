package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.ecommerce.dao.BoyAssignedOrdersDAO;
import com.ecommerce.dao.TransactionsDAO;
import com.ecommerce.dto.request.CreateOrderRequestDTO;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.UserSession;
import com.ecommerce.models.sql.Users;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import integrations.fcm.TummfilNotificationMessages;
import play.libs.Json;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;
import utils.MyConstants.PaymentStatus;
import utils.MyConstants.PaymentType;
import utils.MyConstants.TransactionStatus;
import utils.MyException;
import utils.ObjectMapperUtil;

public class OrderService {

	TransactionsDAO transactionsDAO;
	BoyAssignedOrdersDAO boyAssignedOrdersDAO;
	TummfilNotificationMessages tummfilNotificationMessages;

	@Inject
	public OrderService(TransactionsDAO transactionsDAO, BoyAssignedOrdersDAO boyAssignedOrdersDAO,
			TummfilNotificationMessages tummfilNotificationMessages) {
		this.transactionsDAO = transactionsDAO;
		this.boyAssignedOrdersDAO = boyAssignedOrdersDAO;
		this.tummfilNotificationMessages = tummfilNotificationMessages;
	}

	public ObjectNode getVendorOrders(String status, int page, int limit) throws MyException, IOException {

		List<Integer> statusList = new ArrayList<Integer>();
		String[] statusStrList = status.split(",");
		for (String statusStr : statusStrList) {
			int statusInt = Integer.parseInt(statusStr);
			if (!MyConstants.orderStatusList.contains(statusInt)) {
				throw new MyException(FailureMessages.SEND_VALID_ORDER_STATUS);
			}
			statusList.add(statusInt);
		}
		if (statusList.isEmpty()) {
			throw new MyException(FailureMessages.SEND_VALID_ORDER_STATUS);
		}

		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		HashMap<String, Object> orderMap = Orders.findVendorOrders(vendor, statusList, page, limit);

		@SuppressWarnings("unchecked")
		List<Orders> orderList = (List<Orders>) orderMap.get("result");
		int totalCount = (int) orderMap.get("totalCount");

		List<ObjectNode> orderJsonList = CreateResponseJson.getOrdersJsonResult(orderList, false);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ORDERS, Json.toJson(orderJsonList));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, totalCount);
		return resultNode;
	}

	public ObjectNode createOrder(JsonNode inputJson) throws MyException {
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		ObjectMapper mapper = ObjectMapperUtil.getInstance();
		CreateOrderRequestDTO requestDTO = mapper.convertValue(inputJson, CreateOrderRequestDTO.class);

		if (!(requestDTO.latitude != null && requestDTO.longitude != null)
				|| (requestDTO.pincode != null && requestDTO.cityId != null)) {
			throw new MyException(FailureMessages.INVALID_ADDRESS_INFO);
		}

		Users user = Users.findById(encryptedUserId);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ORDER, Orders.createOrder(requestDTO, user));

		return resultNode;
	}

	public ObjectNode getUserOrders(int page, int limit) throws MyException, IOException {

		String userId = UserSession.getUserEncryptedIdByContext();
		Users user = Users.findById(userId);

		HashMap<String, Object> orderMap = Orders.findUserOrders(user, page, limit);

		@SuppressWarnings("unchecked")
		List<Orders> orderList = (List<Orders>) orderMap.get("result");
		int totalCount = (int) orderMap.get("totalCount");

		List<ObjectNode> orderJsonList = CreateResponseJson.getOrdersJsonResult(orderList, true);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ORDERS, Json.toJson(orderJsonList));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, totalCount);
		return resultNode;
	}

	public ObjectNode getByOrderId(String orderId) throws MyException, IOException {

		String userId = UserSession.getUserEncryptedIdByContext();
		Users user = Users.findById(userId);

		Orders curOrder = Orders.findById(orderId);

		if (!user.getId().equals(curOrder.getUser().getId())) {
			throw new MyException(FailureMessages.ORDER_DOESNT_BELONG_TO_USER);
		}

		ObjectNode orderJson = CreateResponseJson.getOrderJson(curOrder, true);
		transactionsDAO.findByOrderId(curOrder, TransactionStatus.SUCCESS);

		return orderJson;
	}

	public void updateOrderStatus(JsonNode inputJson, Orders order) throws MyException {
		if (!inputJson.has(APIRequestKeys.ORDER_STATUS)) {
			throw new MyException(FailureMessages.ORDER_STATUS_NOT_FOUND);
		}
		int orderStatus = inputJson.findValue(APIRequestKeys.ORDER_STATUS).asInt();

		try {
			Ebean.beginTransaction();
			order.updateStatus(orderStatus);

			if (orderStatus == OrderStatus.DELIVERED) {
				if (order.getPaymentType() == PaymentType.COD) {
					order.updatePaymentTypeAndStatus(PaymentType.COD, PaymentStatus.SUCCESS);
					System.out.println("Orders ----------------- ");
				}
				boyAssignedOrdersDAO.updateOrderStatus(order, OrderStatus.DELIVERED);
			}
			Ebean.commitTransaction();

			switch (orderStatus) {
			case OrderStatus.CONFIRMED:
				tummfilNotificationMessages.confirmOrderMessage(order);
				break;
			case OrderStatus.OUT_FOR_DELIVERY:
				tummfilNotificationMessages.outForDeliveryOrderMessage(order);
				break;
			case OrderStatus.DELIVERED:
				tummfilNotificationMessages.deliveredOrderMessage(order);
				break;
			case OrderStatus.CANCELLED:
				tummfilNotificationMessages.cancelledOrderMessage(order);
				break;
			case OrderStatus.DECLINED:
				tummfilNotificationMessages.declinedOrderMessage(order);
				break;
			}

		} finally {
			if (Ebean.currentTransaction() != null) {
				Ebean.currentTransaction().rollback();
			}
		}
	}

	public ObjectNode getVendorOrderStats(long startTime, long endTime) throws MyException, IOException {

		Vendors vendor = VendorSession.findByContext().getVendor();

		List<SqlRow> orderList = Orders.findVendorOrderStats(vendor, new Date(startTime), new Date(endTime));

		int status;
		long total;
		double totalAmount;

		double successTotalAmount = 0, pendingTotalAmount = 0, failedTotalAmount = 0;
		long successTotal = 0, failedTotal = 0, pendingTotal = 0;

		for (SqlRow order : orderList) {
			status = order.getInteger("status");
			total = order.getLong("total");
			totalAmount = order.getDouble("total_amount");

			System.out.println(" status: " + status);
			if (status == 2 || status == 3 || status == 4) {
				pendingTotal += total;
				pendingTotalAmount += totalAmount;
			} else if (status == 5 || status == 6) {
				failedTotal += total;
				failedTotalAmount += totalAmount;
			} else if (status == 7) {
				successTotal += total;
				successTotalAmount += totalAmount;
			}
		}

		ObjectNode successDetails = Json.newObject();
		successDetails.put("totalAmount", successTotalAmount);
		successDetails.put("orderCount", successTotal);

		ObjectNode pendingDetails = Json.newObject();
		pendingDetails.put("totalAmount", pendingTotalAmount);
		pendingDetails.put("orderCount", pendingTotal);

		ObjectNode failedDetails = Json.newObject();
		failedDetails.put("totalAmount", failedTotalAmount);
		failedDetails.put("orderCount", failedTotal);

		ObjectNode resultNode = Json.newObject();
		resultNode.set("successStats", successDetails);
		resultNode.set("failedStats", failedDetails);
		resultNode.set("pendingStats", pendingDetails);

		return resultNode;
	}

}
