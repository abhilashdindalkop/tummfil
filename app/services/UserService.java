package services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.UserAddress;
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
import utils.MyException;
import utils.ObjectMapperUtil;

public class UserService {

	/* To Sign Up a new user */
	public ObjectNode signUpUsingPhoneNo(JsonNode inputJson) throws MyException, NoSuchAlgorithmException, IOException {

		/* Check if user already exists */
		String phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
		if (Users.isPhoneNoExists(phoneNo)) {
			throw new MyException(FailureMessages.PHONE_NO_ALREADY_EXISTS);
		}

		if (inputJson.has(APIRequestKeys.EMAIL)) {
			String email = inputJson.findValue(APIRequestKeys.EMAIL).asText();
			if (Users.isEmailExists(email)) {
				throw new MyException(FailureMessages.EMAIL_ALREADY_EXISTS);
			}
		}

		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();
		String deviceId = inputJson.findValue(APIRequestKeys.DEVICE_ID).asText();
		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		/* Create User */
		ObjectMapper mapper = ObjectMapperUtil.getInstance();
		Users newUser = mapper.convertValue(inputJson, Users.class);

		ObjectNode resultNode = Json.newObject();
		try {
			Ebean.beginTransaction();
			newUser.addUser(password);
			/* Create User Session */
			UserSession session = UserSession.create(newUser, deviceToken, deviceId, deviceTypeId);
			Ebean.commitTransaction();

			resultNode.put(APIResponseKeys.USER_ID, newUser.getEncryptedUserId());
			resultNode.put(APIResponseKeys.TOKEN, session.getToken());

		} finally {
			if (Ebean.currentTransaction() != null) {
				Ebean.currentTransaction().rollback();
				throw new MyException(FailureMessages.SIGN_UP_FAILED);
			}
		}
		return resultNode;
	}

	public ObjectNode userSignIn(JsonNode inputJson) throws MyException, NoSuchAlgorithmException, IOException {
		Users user = null;
		if (inputJson.has(APIRequestKeys.USERNAME)) {
			String username = inputJson.findValue(APIRequestKeys.USERNAME).asText();
			/* Get by Email */
			user = Users.findByUsername(username);
		} else {
			throw new MyException(FailureMessages.EMAIL_PHONE_NOT_FOUND);
		}

		if (user == null) {
			throw new MyException(FailureMessages.INCORRECT_EMAIL_PHONE);
		}

		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
		if (!Users.checkPassword(user, password)) {
			throw new MyException(FailureMessages.INCORRECT_PASSWORD);
		}

		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();
		String deviceId = inputJson.findValue(APIRequestKeys.DEVICE_ID).asText();
		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		UserSession session = UserSession.create(user, deviceToken, deviceId, deviceTypeId);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.USER_ID, user.getEncryptedUserId());
		resultNode.put(APIResponseKeys.NAME, user.getName());
		resultNode.put(APIResponseKeys.EMAIL, user.getEmail());
		resultNode.put(APIResponseKeys.PHONE_NO, user.getPhoneNo());
		resultNode.put(APIResponseKeys.TOKEN, session.getToken());
		if (user.getCreatedTime() == null) {
			user.updateCreatedTime();
			resultNode.put(APIResponseKeys.IS_NEW_USER, true);
		}

		// TODO - Get User Default Address

		return resultNode;
	}

	public ObjectNode userSync() throws MyException {

		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);

		if (user == null) {
			throw new MyException(FailureMessages.USER_DOESNT_EXIST);
		}

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.USER_ID, user.getEncryptedUserId());
		resultNode.put(APIResponseKeys.NAME, user.getName());
		resultNode.put(APIResponseKeys.EMAIL, user.getEmail());
		resultNode.put(APIResponseKeys.PHONE_NO, user.getPhoneNo());
		resultNode.put(APIResponseKeys.REFERRAL_CODE, user.getReferralCode());
		resultNode.put(APIResponseKeys.WALLET_AMOUNT, user.getWalletAmount());

		// TODO - Get User Default Address

		return resultNode;

	}

	public ObjectNode updateUser(JsonNode inputJson) throws MyException {

		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);

		if (user == null) {
			throw new MyException(FailureMessages.USER_DOESNT_EXIST);
		}

		user.updateUser(inputJson);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.USER_ID, user.getEncryptedUserId());
		resultNode.put(APIResponseKeys.NAME, user.getName());
		resultNode.put(APIResponseKeys.EMAIL, user.getEmail());
		resultNode.put(APIResponseKeys.PHONE_NO, user.getPhoneNo());

		// TODO - Get User Default Address

		return resultNode;
	}

	public ObjectNode signUpUsingFacebook(JsonNode inputJson)
			throws MyException, NoSuchAlgorithmException, IOException {
		String facebookId = inputJson.findValue(APIRequestKeys.FACEBOOK_ID).asText();
		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();

		// check whether Facebook user already exists
		Users user = Users.findByFacebookId(facebookId);

		if (user != null) {
			throw new MyException(FailureMessages.FACEBOOK_LINK_CONFLICT);
		}

		String accessToken = inputJson.findValue(APIRequestKeys.FACEBOOK_TOKEN).asText();

		// check whether valid fb credential
		if (!utils.SocialAccounts.validateFBCredentials(facebookId, accessToken)) {
			throw new MyException(FailureMessages.INVALID_FACEBOOK_CREDENTIALS);
		}

		String deviceId = (inputJson.has(APIRequestKeys.DEVICE_ID))
				? inputJson.findValue(APIRequestKeys.DEVICE_ID).asText() : null;

		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		String email = null;
		String phoneNo = null;
		if (inputJson.has(APIRequestKeys.EMAIL) && inputJson.findValue(APIRequestKeys.EMAIL).asText() != "") {
			email = inputJson.findValue(APIRequestKeys.EMAIL).asText();
		}
		if (inputJson.has(APIRequestKeys.PHONE_NO) && inputJson.findValue(APIRequestKeys.PHONE_NO).asText() != "") {
			phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
		}

		String name = inputJson.findValue(APIRequestKeys.NAME).asText();

		ObjectNode result = Users.createUserUsingFacebook(facebookId, phoneNo, email, name, deviceToken, deviceId,
				deviceTypeId);

		return result;
	}

	public ObjectNode signInUsingFacebook(JsonNode inputJson)
			throws MyException, NoSuchAlgorithmException, IOException {

		String facebookId = inputJson.findValue(APIRequestKeys.FACEBOOK_ID).asText();
		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();

		Users user = Users.findByFacebookId(facebookId);

		if (user == null) {
			throw new MyException(FailureMessages.FACEBOOK_LINK_ABSENT);
		}

		String accessToken = inputJson.findValue(APIRequestKeys.FACEBOOK_TOKEN).asText();

		if (!utils.SocialAccounts.validateFBCredentials(facebookId, accessToken)) {
			throw new MyException(FailureMessages.INVALID_FACEBOOK_CREDENTIALS);

		}

		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		String deviceId = (inputJson.has(APIRequestKeys.DEVICE_ID))
				? inputJson.findValue(APIRequestKeys.DEVICE_ID).asText() : null;

		UserSession session = UserSession.create(user, deviceToken, deviceId, deviceTypeId);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.USER_ID, user.getEncryptedUserId());
		resultNode.put(APIResponseKeys.TOKEN, session.getToken());

		return resultNode;

	}

	public void addUserAddress(JsonNode inputJson) throws MyException {

		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);
		if (user == null) {
			throw new MyException(FailureMessages.USER_DOESNT_EXIST);
		}

		if (!inputJson.has(APIRequestKeys.ADDRESS)) {
			throw new MyException(FailureMessages.ADDRESS_NOT_FOUND);
		}
		if (!inputJson.has(APIRequestKeys.CITY_ID)) {
			throw new MyException(FailureMessages.CITY_ID_DOESNT_EXIST);
		}

		if (!inputJson.has(APIRequestKeys.PINCODE)) {
			throw new MyException(FailureMessages.PINCODE_NOT_FOUND);
		}

		if (!inputJson.has(APIRequestKeys.ADDRESS_TYPE)) {
			throw new MyException(FailureMessages.ADDRESS_TYPE_NOT_FOUND);
		}
		int addressType = inputJson.findValue(APIRequestKeys.ADDRESS_TYPE).asInt();
		if (!MyConstants.addressTypeList.contains(addressType)) {
			throw new MyException(FailureMessages.INVALID_ADDRESS_TYPE);
		}
		long cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asLong();

		Cities city = Cities.findById(cityId);

		UserAddress oldAddress = UserAddress.findByUserIdAndAddressType(user, addressType);
		if (oldAddress != null) {
			oldAddress.deleteAddress();
		}

		UserAddress newAddress = new UserAddress();
		ObjectMapper mapper = ObjectMapperUtil.getInstance();
		newAddress = mapper.convertValue(inputJson, UserAddress.class);
		newAddress.add(user, city);

	}

	public void deleteUserAddress(int addressId) throws MyException {
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);

		UserAddress.deleteById(addressId);

	}

	public ObjectNode getUserAddress() throws MyException {

		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);
		if (user == null) {
			throw new MyException(FailureMessages.USER_DOESNT_EXIST);
		}

		List<UserAddress> addressList = UserAddress.findByUserId(user);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.ADDRESS_LIST, Json.toJson(addressList));

		return resultNode;
	}

	public void userLogout() throws IOException {

		UserSession.deleteSession(UserSession.findByContext());

	}

	public void registerUserFCM(UserSession session, String deviceToken) throws IOException {
		session.updateDeviceToken(deviceToken);
	}

}
