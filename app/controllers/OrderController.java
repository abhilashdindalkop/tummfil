package controllers;

import javax.inject.Inject;

import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Orders;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.DeliveryBoyAuthenticator;
import authentication.VendorAuthenticator;
import authentication.UserAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.OrderService;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.SuccessMessages;
import utils.CorsComposition;
import utils.MyException;
import utils.MySuccessResponse;

public class OrderController extends ParentController {

	OrderService orderService;

	@Inject
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result getVendorOrders(int status, int page, int limit) {
		try {

			ObjectNode resultNode = orderService.getVendorOrders(status, page, limit);
			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result updateOrderStatus() {
		try {
			JsonNode inputJson = request().body().asJson();

			if (!inputJson.has(APIRequestKeys.ORDER_ID)) {
				throw new MyException(FailureMessages.ORDER_ID_NOT_FOUND);
			}
			String orderId = inputJson.findValue(APIRequestKeys.ORDER_ID).asText();
			Orders order = Orders.findById(orderId);

			String encryptedVendorId = VendorSession.getVendorEncryptedIdByContext();
			if (!order.getVendor().getEncryptedVendorId().equals(encryptedVendorId)) {
				throw new MyException(FailureMessages.ORDER_DOESNT_BELONG_TO_VENDOR);
			}

			orderService.updateOrderStatus(inputJson, order);

			response = new MySuccessResponse(SuccessMessages.ORDER_STATUS_UPDATE_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result createOrder() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = orderService.createOrder(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(UserAuthenticator.class)
	public Result getUserOrders(int page, int limit) {
		try {

			ObjectNode resultNode = orderService.getUserOrders(page, limit);
			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(DeliveryBoyAuthenticator.class)
	public Result updateOrderStatusDeliveryBoy() {
		try {
			JsonNode inputJson = request().body().asJson();

			if (!inputJson.has(APIRequestKeys.ORDER_ID)) {
				throw new MyException(FailureMessages.ORDER_ID_NOT_FOUND);
			}
			String orderId = inputJson.findValue(APIRequestKeys.ORDER_ID).asText();
			Orders order = Orders.findById(orderId);

			/* TODO Query for assigned orderId */
			// if (!) {
			// throw new
			// VendorException(FailureMessages.ORDER_DOESNT_BELONG_TO_DELIVERY_BOY);
			// }

			orderService.updateOrderStatus(inputJson, order);

			response = new MySuccessResponse(SuccessMessages.ORDER_STATUS_UPDATE_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}

// {
// "name": "Abhilash",
// "cityId": 12,
// "orderType": 1,
// "paymentType": 1,
// "address": "indiranagar, bangalore",
// "phoneNo": 0,
// "latitude": 1.234234342,
// "longitude": 30.324324324,
// "description": 1
//
// }
