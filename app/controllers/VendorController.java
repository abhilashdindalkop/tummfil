package controllers;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.VendorAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.VendorService;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.JsonSchemaFilePath;
import utils.MyConstants.SuccessMessages;
import utils.MyFailureResponse;
import utils.MySuccessResponse;
import utils.ValidateJsonSchema.ValidateJson;

public class VendorController extends ParentController {

	VendorService vendorService;

	@Inject
	public VendorController(VendorService vendorService) {
		this.vendorService = vendorService;
	}

	/* To Sign Up a new user */
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.VENDOR_SIGN_UP)
	public Result signUpUsingPhoneNo() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode result = vendorService.signUpUsingPhoneNo(inputJson);

			response = new MySuccessResponse(result);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.VENDOR_SIGN_IN)
	public Result vendorSignIn() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = vendorService.vendorSignIn(inputJson);

			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.UPDATE_VENDOR)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result updateVendor() {
		try {
			JsonNode inputJson = request().body().asJson();

			vendorService.updateVendor(inputJson);

			response = new MySuccessResponse(SuccessMessages.VENDOR_UPDATE_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	public Result getVendorPrerequisites(long updatedTime) {
		try {

			ObjectNode resultNode = vendorService.getVendorPrerequisites(updatedTime);
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result getVendorDetails() {
		try {

			ObjectNode vendorNode = vendorService.getVendorDetails();

			response = new MySuccessResponse(vendorNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result getVendorCategories() {
		try {

			ObjectNode vendorNode = vendorService.getVendorCategories();

			response = new MySuccessResponse(vendorNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result vendorLogout() {
		try {

			vendorService.vendorLogout();

			response = new MySuccessResponse(SuccessMessages.LOGOUT_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}