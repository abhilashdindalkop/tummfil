package services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import com.ecommerce.dao.DeliveryBoySessionDAO;
import com.ecommerce.dao.DeliveryBoysDAO;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.DeliveryBoySession;
import com.ecommerce.models.sql.DeliveryBoys;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.PasswordEncryptDecrypt;
import play.libs.Json;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyException;

public class DeliveryBoyService {

	DeliveryBoysDAO deliveryBoysDAO;
	DeliveryBoySessionDAO deliveryBoySessionDAO;

	@Inject
	public DeliveryBoyService(DeliveryBoysDAO deliveryBoysDAO, DeliveryBoySessionDAO deliveryBoySessionDAO) {

		this.deliveryBoysDAO = deliveryBoysDAO;
		this.deliveryBoySessionDAO = deliveryBoySessionDAO;
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
		Vendors vendor = boy.getVendor() ;
		if(vendor!= null){
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

}
