package controllers;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.DeliveryBoyAuthenticator;
import authentication.VendorAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.DeliveryBoyService;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.SuccessMessages;
import utils.MyException;
import utils.MyFailureResponse;
import utils.MySuccessResponse;

public class DeliveryBoyController extends ParentController {

	DeliveryBoyService deliveryBoyService;

	@Inject
	public DeliveryBoyController(DeliveryBoyService deliveryBoyService) {
		this.deliveryBoyService = deliveryBoyService;
	}

	/* To Sign Up a new user */
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result addDeliveryBoy() {
		try {
			JsonNode inputJson = request().body().asJson();

			Vendors vendor = VendorSession.findByContext().getVendor();

			ObjectNode resultNode = deliveryBoyService.addDeliveryBoy(vendor, inputJson);

			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result signIn() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = deliveryBoyService.signIn(inputJson);

			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	// @BodyParser.Of(BodyParser.Json.class)
	// @Security.Authenticated(VendorAuthenticator.class)
	// public Result updateDeliveryBoy() {
	// try {
	// JsonNode inputJson = request().body().asJson();
	//
	// // deliveryBoyService.updateDeliveryBoy();
	//
	// response = new VendorSuccessResponse(resultNode);
	// } catch (PersistenceException e) {
	// response = new
	// VendorFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
	// } catch (Exception e) {
	// response = createFailureResponse(e);
	// }
	// return response.getResult();
	// }

	@Security.Authenticated(DeliveryBoyAuthenticator.class)
	public Result deliveryBoyLogout() {
		try {

			deliveryBoyService.deliveryBoyLogout();

			response = new MySuccessResponse(SuccessMessages.LOGOUT_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	/*
	 * Delivery Boy - Check on routes
	 */
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

			deliveryBoyService.updateBoyOrderStatus(inputJson, order);

			response = new MySuccessResponse(SuccessMessages.ORDER_STATUS_UPDATE_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(DeliveryBoyAuthenticator.class)
	public Result getBoyOrders(int status, int page, int limit) {
		try {
			if (status == 0) {
				throw new MyException(FailureMessages.ORDER_STATUS_NOT_FOUND);
			}
			ObjectNode resultNode = deliveryBoyService.getBoyOrders(status, page, limit);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			e.printStackTrace();
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
