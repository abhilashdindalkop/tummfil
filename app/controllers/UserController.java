
package controllers;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.ecommerce.models.sql.UserSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.UserAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.UserService;
import utils.CorsComposition;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.JsonSchemaFilePath;
import utils.MyConstants.SuccessMessages;
import utils.MyException;
import utils.MyFailureResponse;
import utils.MySuccessResponse;
import utils.ValidateJsonSchema.ValidateJson;

public class UserController extends ParentController {

	UserService userService;

	@Inject
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/* To Sign Up a new user */
	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.USER_SIGN_UP)
	public Result signUpUsingPhoneNo() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode result = userService.signUpUsingPhoneNo(inputJson);

			response = new MySuccessResponse(result);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.USER_SIGN_IN)
	public Result userSignIn() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = userService.userSignIn(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@Security.Authenticated(UserAuthenticator.class)
	public Result userSync() {
		try {

			ObjectNode resultNode = userService.userSync();

			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.UPDATE_USER)
	@Security.Authenticated(UserAuthenticator.class)
	public Result updateUser() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = userService.updateUser(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.USER_FB_SIGN_UP)
	public Result signUpUsingFacebook() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode result = userService.signUpUsingFacebook(inputJson);
			response = new MySuccessResponse(result);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}

		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.USER_FB_SIGN_IN)
	public Result signInUsingFacebook() {

		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = userService.signInUsingFacebook(inputJson);

			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}

		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result addUserAddress() {
		try {
			JsonNode inputJson = request().body().asJson();

			userService.addUserAddress(inputJson);
			response = new MySuccessResponse(SuccessMessages.ADDRESS_ADD_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@Security.Authenticated(UserAuthenticator.class)
	public Result deleteUserAddress(int addressId) {
		try {

			userService.deleteUserAddress(addressId);
			response = new MySuccessResponse(SuccessMessages.ADDRESS_DELETE_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@Security.Authenticated(UserAuthenticator.class)
	public Result getUserAddress() {
		try {

			ObjectNode resultNode = userService.getUserAddress();
			response = new MySuccessResponse(resultNode);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@Security.Authenticated(UserAuthenticator.class)
	public Result userLogout() {
		try {

			userService.userLogout();

			response = new MySuccessResponse(SuccessMessages.LOGOUT_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result registerFCMToken() {
		try {
			JsonNode inputJson = request().body().asJson();
			if (!inputJson.has(APIRequestKeys.DEVICE_TOKEN)) {
				throw new MyException(FailureMessages.DEVICE_TOKEN_NOT_FOUND);
			}
			String deviceToken = inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText();

			UserSession session = UserSession.findByContext();

			userService.registerUserFCM(session, deviceToken);

			response = new MySuccessResponse(SuccessMessages.FCM_REGISTER_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
