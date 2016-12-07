package services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.ecommerce.dao.BoyAssignedOrdersDAO;
import com.ecommerce.dao.DeliveryBoySessionDAO;
import com.ecommerce.dao.DeliveryBoysDAO;
import com.ecommerce.models.sql.BoyAssignedOrders;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.DeliveryBoySession;
import com.ecommerce.models.sql.DeliveryBoys;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.PasswordEncryptDecrypt;
import play.libs.Json;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;
import utils.MyException;

public class DeliveryBoyService {

	DeliveryBoysDAO deliveryBoysDAO;
	BoyAssignedOrdersDAO boyAssignedOrdersDAO;
	DeliveryBoySessionDAO deliveryBoySessionDAO;
	OrderService orderService;

	@Inject
	public DeliveryBoyService(DeliveryBoysDAO deliveryBoysDAO, BoyAssignedOrdersDAO boyAssignedOrdersDAO,
			DeliveryBoySessionDAO deliveryBoySessionDAO, OrderService orderService) {

		this.deliveryBoysDAO = deliveryBoysDAO;
		this.deliveryBoySessionDAO = deliveryBoySessionDAO;
		this.boyAssignedOrdersDAO = boyAssignedOrdersDAO;
		this.orderService = orderService;
	}

	public ObjectNode addDeliveryBoy(Vendors vendor, JsonNode inputJson) throws MyException {

		/* Check if user already exists */
		String phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
		if (deliveryBoysDAO.isPhoneNoExists(phoneNo)) {
			throw new MyException(FailureMessages.PHONE_NO_ALREADY_EXISTS);
		}

		Cities city = null;
		if (inputJson.has(APIRequestKeys.CITY_ID)) {
			int cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asInt();
			city = Cities.findById(cityId);
		}

		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();

		/* Create Delivery Boy */
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DeliveryBoys newBoy = mapper.convertValue(inputJson, DeliveryBoys.class);

		newBoy = deliveryBoysDAO.add(newBoy, vendor, phoneNo, password, city);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.DELIVERY_BOY_ID, newBoy.getEncryptedId());
		resultNode.put(APIResponseKeys.NAME, newBoy.getName());
		resultNode.put(APIResponseKeys.PHONE_NO, newBoy.getPhoneNo());
		return resultNode;

	}

	public ObjectNode signIn(JsonNode inputJson) throws MyException, NoSuchAlgorithmException, IOException {
		DeliveryBoys boy = null;
		if (inputJson.has(APIRequestKeys.PHONE_NO)) {
			String phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
			/* Get by Phone No */
			boy = deliveryBoysDAO.findByPhoneNo(phoneNo);
		} else {
			throw new MyException(FailureMessages.EMAIL_PHONE_NOT_FOUND);
		}
		if (boy == null) {
			throw new MyException(FailureMessages.INCORRECT_EMAIL_PHONE);
		}
		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
		if (!PasswordEncryptDecrypt.isPasswordSame(password, boy.getPassword())) {
			throw new MyException(FailureMessages.INCORRECT_PASSWORD);
		}

		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();
		String deviceId = inputJson.findValue(APIRequestKeys.DEVICE_ID).asText();
		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		DeliveryBoySession session = deliveryBoySessionDAO.create(boy, deviceToken, deviceId, deviceTypeId);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.DELIVERY_BOY_ID, boy.getEncryptedId());
		resultNode.put(APIResponseKeys.NAME, boy.getName());
		resultNode.put(APIResponseKeys.PHONE_NO, boy.getPhoneNo());
		resultNode.put(APIResponseKeys.ADDRESS, boy.getAddress());
		Vendors vendor = boy.getVendor();
		if (vendor != null) {
			resultNode.put(APIResponseKeys.VENDOR_NAME, vendor.getVendorName());
		}
		resultNode.set(APIResponseKeys.CITY, Json.toJson(boy.getCity()));
		resultNode.put(APIResponseKeys.TOKEN, session.getToken());
		return resultNode;
	}

	// public void updateDeliveryBoy(JsonNode inputJson) throws VendorException
	// {
	//
	// String encryptedUserId = deliveryBoySessionDAO.getEncryptedIdByContext();
	// DeliveryBoys boy = deliveryBoysDAO.findById(encryptedUserId);
	//
	// if (boy == null) {
	// throw new VendorException(FailureMessages.USER_DOESNT_EXIST);
	// }
	//
	// // boy.updateDeliveryBoy(inputJson);
	//
	// ObjectNode resultNode = Json.newObject();
	// resultNode.put(APIResponseKeys.USER_ID, boy.getEncryptedId());
	// resultNode.put(APIResponseKeys.NAME, boy.getName());
	// resultNode.put(APIResponseKeys.PHONE_NO, boy.getPhoneNo());
	// resultNode.put(APIResponseKeys.ADDRESS, boy.getAddress());
	// resultNode.set(APIResponseKeys.CITY, Json.toJson(boy.getCity()));
	//
	// }

	public void deliveryBoyLogout() throws IOException {
		deliveryBoySessionDAO.deleteSession(deliveryBoySessionDAO.findByContext());
	}

	public void updateBoyOrderStatus(JsonNode inputJson, Orders order) throws MyException {

		/* Query for assigned orderId */
		String deliveryBoyId = deliveryBoySessionDAO.getEncryptedIdByContext();
		BoyAssignedOrders assignedOrder = boyAssignedOrdersDAO.findByOrderId(order);
		if (!assignedOrder.getDeliveryBoy().getEncryptedId().equals(deliveryBoyId)) {
			throw new MyException(FailureMessages.ORDER_DOESNT_BELONG_TO_DELIVERY_BOY);
		}

		orderService.updateOrderStatus(inputJson, order);
	}

	public ObjectNode getBoyOrders(int status, int page, int limit) throws MyException, IOException {

		if (!MyConstants.orderStatusList.contains(status)) {
			throw new MyException(FailureMessages.SEND_VALID_ORDER_STATUS);
		}
		String deliveryBoyId = deliveryBoySessionDAO.getEncryptedIdByContext();
		DeliveryBoys deliveryBoy = deliveryBoysDAO.findById(deliveryBoyId);

		List<BoyAssignedOrders> assignedOrders = boyAssignedOrdersDAO.getAssignedOrders(deliveryBoy, status);

		List<Orders> orderList = new ArrayList<Orders>();
		for (BoyAssignedOrders assignedOrder : assignedOrders) {
			orderList.add(assignedOrder.getOrder());
		}

		List<ObjectNode> orderJsonList = CreateResponseJson.getOrdersJsonResult(orderList, true);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ORDERS, Json.toJson(orderJsonList));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, assignedOrders.size());
		return resultNode;
	}

	public void assignOrderToBoy(JsonNode inputJson) throws MyException, IOException {

		if (!inputJson.has(APIRequestKeys.ORDER_ID)) {
			throw new MyException(FailureMessages.ORDER_ID_NOT_FOUND);
		}
		if (!inputJson.has(APIRequestKeys.DELIVERY_BOY_ID)) {
			throw new MyException(FailureMessages.DELIVERY_BOY_ID_NOT_FOUND);
		}
		String orderId = inputJson.findValue(APIRequestKeys.ORDER_ID).asText();
		String deliveryBoyId = inputJson.findValue(APIRequestKeys.DELIVERY_BOY_ID).asText();

		Orders order = Orders.findById(orderId);

		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		if (!order.getVendor().getEncryptedVendorId().equals(vendorId)) {
			throw new MyException(FailureMessages.ORDER_DOESNT_BELONG_TO_VENDOR);
		}
		DeliveryBoys deliveryBoy = deliveryBoysDAO.findById(deliveryBoyId);
		try {
			Ebean.beginTransaction();
			order.updateStatus(OrderStatus.OUT_FOR_DELIVERY);

			boyAssignedOrdersDAO.add(deliveryBoy, order);
			Ebean.commitTransaction();
		} finally {
			if (Ebean.currentTransaction() != null) {
				Ebean.currentTransaction().rollback();
			}
		}
	}
}
