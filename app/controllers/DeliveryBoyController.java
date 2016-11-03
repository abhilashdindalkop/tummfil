
package controllers;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

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
import utils.MyConstants.FailureMessages;
import utils.MyConstants.SuccessMessages;
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

			deliveryBoyService.userLogout();

			response = new MySuccessResponse(SuccessMessages.LOGOUT_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
