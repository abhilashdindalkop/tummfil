package services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.ecommerce.dao.TransactionsDAO;
import com.ecommerce.dto.request.CreateOrderRequestDTO;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.UserSession;
import com.ecommerce.models.sql.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.TransactionStatus;
import utils.MyException;
import utils.ObjectMapperUtil;

public class OrderService {
	
	TransactionsDAO transactionsDAO;
	
	@Inject
	public OrderService(TransactionsDAO transactionsDAO) {
		this.transactionsDAO = transactionsDAO;
	}

	public ObjectNode getVendorOrders(int status, int page, int limit) throws MyException, IOException {

		if (!MyConstants.orderStatusList.contains(status)) {
			throw new MyException(FailureMessages.SEND_VALID_ORDER_STATUS);
		}
		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		HashMap<String, Object> orderMap = Orders.findVendorOrders(vendor, status, page, limit);

		@SuppressWarnings("unchecked")
		List<Orders> orderList = (List<Orders>) orderMap.get("result");
		int totalCount = (int) orderMap.get("totalCount");

		List<ObjectNode> orderJsonList = CreateResponseJson.getOrdersJsonResult(orderList, false);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ORDERS, Json.toJson(orderJsonList));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, totalCount);
		return resultNode;
	}

	public void updateOrderStatus(JsonNode inputJson, Orders order) throws MyException {
		if (!inputJson.has(APIRequestKeys.ORDER_STATUS)) {
			throw new MyException(FailureMessages.ORDER_STATUS_NOT_FOUND);
		}
		int orderStatus = inputJson.findValue(APIRequestKeys.ORDER_STATUS).asInt();

		order.updateStatus(orderStatus);
	}

	public ObjectNode createOrder(JsonNode inputJson) throws MyException {
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		ObjectMapper mapper = ObjectMapperUtil.getInstance();
		CreateOrderRequestDTO requestDTO = mapper.convertValue(inputJson, CreateOrderRequestDTO.class);

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

}
