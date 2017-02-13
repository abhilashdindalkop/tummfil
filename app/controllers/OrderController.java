package controllers;

import javax.inject.Inject;

import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.VendorSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.UserAuthenticator;
import authentication.VendorAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.DeliveryBoyService;
import services.OrderService;
import utils.CorsComposition;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.SuccessMessages;
import utils.MyException;
import utils.MySuccessResponse;

public class OrderController extends ParentController {

	OrderService orderService;
	DeliveryBoyService deliveryBoyService;

	@Inject
	public OrderController(OrderService orderService, DeliveryBoyService deliveryBoyService) {
		this.orderService = orderService;
		this.deliveryBoyService = deliveryBoyService;
	}

	/*
	 * Vendor
	 */
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
	@Security.Authenticated(VendorAuthenticator.class)
	public Result assignOrderToBoy() {
		try {
			JsonNode inputJson = request().body().asJson();

			deliveryBoyService.assignOrderToBoy(inputJson);
			response = new MySuccessResponse(SuccessMessages.ORDER_ASSIGN_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	/*
	 * CUSTOMER
	 */
	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result createOrder() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = orderService.createOrder(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			e.printStackTrace();
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
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

	@CorsComposition.Cors
	@Security.Authenticated(UserAuthenticator.class)
	public Result getUserOrderById(String orderId) {
		try {

			ObjectNode resultNode = orderService.getByOrderId(orderId);
			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
