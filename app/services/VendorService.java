package services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.ecommerce.dao.VendorLocationDAO;
import com.ecommerce.models.sql.Category;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Products;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderType;
import utils.MyConstants.ProductType;
import utils.MyConstants.UnitType;
import utils.MyException;

public class VendorService {

	VendorLocationDAO vendorLocationDAO;

	@Inject
	public VendorService(VendorLocationDAO vendorLocationDAO) {
		this.vendorLocationDAO = vendorLocationDAO;
	}

	public ObjectNode signUpUsingPhoneNo(JsonNode inputJson) throws MyException, NoSuchAlgorithmException, IOException {

		/* Check if user already exists */
		String phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
		if (Vendors.isPhoneNoExists(phoneNo)) {
			throw new MyException(FailureMessages.PHONE_NO_ALREADY_EXISTS);
		}
		if (inputJson.has(APIRequestKeys.EMAIL)) {
			String email = inputJson.findValue(APIRequestKeys.EMAIL).asText();
			if (Vendors.isEmailExists(email)) {
				throw new MyException(FailureMessages.EMAIL_ALREADY_EXISTS);
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Vendors newVendor = mapper.convertValue(inputJson, Vendors.class);

		long cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asLong();
		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();
		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
		String deviceId = inputJson.findValue(APIRequestKeys.DEVICE_ID).asText();
		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		ObjectNode result = Vendors.createVendorUsingPhoneNo(newVendor, deviceToken, deviceId, deviceTypeId, password,
				cityId);

		return result;
	}

	public ObjectNode vendorSignIn(JsonNode inputJson) throws MyException, NoSuchAlgorithmException, IOException {
		Vendors vendor = null;
		if (inputJson.has(APIRequestKeys.EMAIL)) {
			String email = inputJson.findValue(APIRequestKeys.EMAIL).asText();
			/* Get by Email */
			vendor = Vendors.findByEmail(email);
		} else if (inputJson.has(APIRequestKeys.PHONE_NO)) {
			String phoneNo = inputJson.findValue(APIRequestKeys.PHONE_NO).asText();
			/* Get by Phone No */
			vendor = Vendors.findByPhoneNo(phoneNo);
		} else {
			throw new MyException(FailureMessages.EMAIL_PHONE_NOT_FOUND);
		}
		if (vendor == null) {
			throw new MyException(FailureMessages.INCORRECT_EMAIL_PHONE);
		} else {
			// TODO Check if verified
			// if (!vendor.getIsVendorVerified()) {
			// throw new CakeException(FailureMessages.VENDOR_NOT_VERIFIED);
			// }
		}
		String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
		if (!Vendors.checkPassword(vendor, password)) {
			throw new MyException(FailureMessages.INCORRECT_PASSWORD);
		}

		int deviceTypeId = inputJson.findValue(APIRequestKeys.DEVICE_TYPE).asInt();
		String deviceId = inputJson.findValue(APIRequestKeys.DEVICE_ID).asText();
		String deviceToken = (inputJson.has(APIRequestKeys.DEVICE_TOKEN))
				? inputJson.findValue(APIRequestKeys.DEVICE_TOKEN).asText() : null;

		VendorSession session = VendorSession.create(vendor, deviceToken, deviceId, deviceTypeId);

		ObjectNode resultNode = Json.newObject();
		resultNode = CreateResponseJson.constructVendorResponseJson(vendor);
		resultNode.put(APIResponseKeys.EMAIL, vendor.getEmail());
		resultNode.put(APIResponseKeys.PHONE_NO, vendor.getPhoneNo());
		resultNode.put(APIResponseKeys.TOKEN, session.getToken());
		if (vendor.getUpdatedTime() == null) {
			resultNode.put(APIResponseKeys.IS_NEW_USER, true);
		}

		return resultNode;
	}

	public void updateVendor(JsonNode inputJson) throws MyException {
		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		if (inputJson.has(APIResponseKeys.VENDOR_NAME)) {
			vendor.setVendorName(inputJson.findValue(APIRequestKeys.VENDOR_NAME).asText());
		}
		if (inputJson.has(APIResponseKeys.VENDOR_ADDRESS)) {
			vendor.setVendorAddress(inputJson.findValue(APIRequestKeys.VENDOR_ADDRESS).asText());
		}
		if (inputJson.has(APIRequestKeys.NAME)) {
			vendor.setName(inputJson.findValue(APIRequestKeys.NAME).asText());
		}
		if (inputJson.has(APIRequestKeys.CITY_ID)) {
			long cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asLong();
			Cities city = Cities.findById(cityId);
			vendor.setCity(city);
		}
		if (inputJson.has(APIRequestKeys.DESCRIPTION)) {
			vendor.setDescription(inputJson.findValue(APIRequestKeys.DESCRIPTION).asText());
		}
		if (inputJson.has(APIRequestKeys.LATITUDE) && inputJson.has(APIRequestKeys.LONGITUDE)) {
			double longitude = inputJson.findValue(APIRequestKeys.LONGITUDE).asDouble();
			double latitude = inputJson.findValue(APIRequestKeys.LATITUDE).asDouble();
			vendor.setLongitude(longitude);
			vendor.setLatitude(latitude);

			vendorLocationDAO.updateLocation(vendor.getId(), latitude, longitude);
		}
		if (inputJson.has(APIRequestKeys.SHIPPING_DISTANCE)) {
			double shippingDistance = inputJson.findValue(APIRequestKeys.SHIPPING_DISTANCE).asDouble();
			vendorLocationDAO.updateShippingDistance(vendor.getId(), shippingDistance);
		}
		vendor.updateVendor();
	}

	public ObjectNode getVendorPrerequisites(long updatedTime) {
		// String prerequisiteToken =
		// request().getHeader(CakeConstants.PREREQUISITE_TOKEN);
		// if (!prerequisiteToken.equals(CakeConstants.PREREQUISITE_KEY)) {
		// throw new CakeException(FailureMessages.INVALID_API_CALL);
		// }

		List<Category> categoryList = Category.findAllCategories();
		List<Cities> cityList = Cities.findAllCities();

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.CATEGORY_LIST, Json.toJson(categoryList));
		resultNode.set(APIResponseKeys.CITY_LIST, Json.toJson(cityList));

		HashMap<String, Integer> productTypeHM = new HashMap<String, Integer>();
		productTypeHM.put(ProductType.VEG_STR, ProductType.VEG);
		productTypeHM.put(ProductType.NON_VEG_STR, ProductType.NON_VEG);

		HashMap<String, Integer> orderTypeHM = new HashMap<String, Integer>();
		orderTypeHM.put(OrderType.NORMAL_STR, OrderType.NORMAL);
		orderTypeHM.put(OrderType.GIFT_WRAP_STR, OrderType.GIFT_WRAP);
		orderTypeHM.put(OrderType.PRE_ORDER_STR, OrderType.PRE_ORDER);

		HashMap<String, Integer> unitTypesHM = new HashMap<String, Integer>();
		unitTypesHM.put(UnitType.KG_STR, UnitType.KG);
		unitTypesHM.put(UnitType.LITRE_STR, UnitType.LITRE);
		unitTypesHM.put(UnitType.NONE_STR, UnitType.NONE);

		resultNode.set(APIResponseKeys.PRODUCT_TYPES, Json.toJson(productTypeHM));
		resultNode.set(APIResponseKeys.ORDER_TYPES, Json.toJson(orderTypeHM));
		resultNode.set(APIResponseKeys.UNIT_TYPES, Json.toJson(unitTypesHM));

		Date updatedDate = new Date(updatedTime);
		boolean isUpdate = false;
		if (Category.isCategoriesUpdated(updatedDate) || Cities.isCitiesUpdated(updatedDate)) {
			isUpdate = true;
		}
		resultNode.put(APIResponseKeys.IS_UPDATED, isUpdate);
		return resultNode;
	}

	public ObjectNode getVendorDetails() throws IOException, MyException {
		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		ObjectNode vendorNode = Json.newObject();
		vendorNode = CreateResponseJson.constructVendorResponseJson(vendor);
		vendorNode.put(APIResponseKeys.EMAIL, vendor.getEmail());
		vendorNode.put(APIResponseKeys.PHONE_NO, vendor.getPhoneNo());

		return vendorNode;
	}

	public void vendorLogout() throws IOException {
		VendorSession.deleteSession(VendorSession.findByContext());
	}

	public ObjectNode getVendorCategories() throws IOException, MyException {
		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		List<HashMap<String, Object>> categoryList = CreateResponseJson
				.constructCategoriesResponse(Products.getVendorCategories(vendor));

		ObjectNode categoriesNode = Json.newObject();
		categoriesNode.set(APIResponseKeys.CATEGORY_LIST, Json.toJson(categoryList));

		return categoriesNode;
	}

}
