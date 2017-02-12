package services.cms;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.ecommerce.dao.VendorLocationDAO;
import com.ecommerce.models.mongo.VendorLocations;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.PasswordEncryptDecrypt;
import play.libs.Json;
import services.CreateResponseJson;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyException;

public class VendorCmsService {

	VendorLocationDAO vendorLocationDAO;

	@Inject
	public VendorCmsService(VendorLocationDAO vendorLocationDAO) {
		this.vendorLocationDAO = vendorLocationDAO;
	}

	public void updateVendor(JsonNode inputJson) throws MyException {
		String vendorId;
		if (!inputJson.has(APIResponseKeys.VENDOR_ID)) {
			throw new MyException(FailureMessages.VENDOR_ID_NOT_FOUND);
		}

		vendorId = inputJson.findValue(APIRequestKeys.VENDOR_ID).asText();
		Vendors vendor = Vendors.findById(vendorId);

		if (inputJson.has(APIResponseKeys.VENDOR_NAME)) {
			vendor.setVendorName(inputJson.findValue(APIRequestKeys.VENDOR_NAME).asText());
		}
		if (inputJson.has(APIRequestKeys.NAME)) {
			vendor.setName(inputJson.findValue(APIRequestKeys.NAME).asText());
		}
		if (inputJson.has(APIRequestKeys.CITY_ID)) {
			long cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asLong();
			Cities city = Cities.findById(cityId);
			vendor.setCity(city);
		}
		if (inputJson.has(APIRequestKeys.VENDOR_TYPE)) {
			int vendorType = inputJson.findValue(APIRequestKeys.VENDOR_TYPE).asInt();
			vendor.setVendorType(vendorType);

		}
		if (inputJson.has(APIRequestKeys.IS_AVAILABLE)) {
			boolean isAvailable = inputJson.findValue(APIRequestKeys.IS_AVAILABLE).asBoolean();
			vendor.setIsVendorAvailable(isAvailable);
		}
		if (inputJson.has(APIRequestKeys.IS_VERIFIED)) {
			boolean isVerify = inputJson.findValue(APIRequestKeys.IS_VERIFIED).asBoolean();
			vendor.setIsVendorVerified(isVerify);
		}
		if (inputJson.has(APIRequestKeys.DESCRIPTION)) {
			vendor.setDescription(inputJson.findValue(APIRequestKeys.DESCRIPTION).asText());
		}
		if (inputJson.has(APIRequestKeys.PASSWORD)) {
			String password = inputJson.findValue(APIRequestKeys.PASSWORD).asText();
			vendor.setPassword(PasswordEncryptDecrypt.generatePasswordHash(password));
		}
		if (inputJson.has(APIRequestKeys.SHIPPING_DISTANCE)) {
			long shippingDistance = inputJson.findValue(APIRequestKeys.SHIPPING_DISTANCE).asLong();
			vendorLocationDAO.updateShippingDistance(vendor.getId(), shippingDistance);
		}
		vendor.updateVendor();
	}

	public ObjectNode getVendorDetails(String vendorId) throws IOException, MyException {
		Vendors vendor = Vendors.findById(vendorId);

		ObjectNode vendorNode = Json.newObject();
		vendorNode = CreateResponseJson.constructVendorResponseJson(vendor);
		VendorLocations vendorLoc = new VendorLocationDAO().findById(vendor.getId());
		List<Double> location = vendorLoc.getLocation();
		vendorNode.put(APIResponseKeys.LATITUDE, location.get(1));
		vendorNode.put(APIResponseKeys.LONGITUDE, location.get(0));
		vendorNode.put(APIResponseKeys.EMAIL, vendor.getEmail());
		vendorNode.put(APIResponseKeys.PHONE_NO, vendor.getPhoneNo());

		return vendorNode;
	}

	public ObjectNode getVendorsCmsList(JsonNode inputJson) throws MyException, IOException {

		int page = 0, limit = MyConstants.DEFAULT_PAGINATION_LIMIT;
		JsonNode filterNode = Json.newObject();

		if (inputJson.has(APIRequestKeys.PAGE)) {
			page = inputJson.findValue(APIRequestKeys.PAGE).asInt();
		}
		if (inputJson.has(APIRequestKeys.LIMIT)) {
			limit = inputJson.findValue(APIRequestKeys.LIMIT).asInt();
		}

		if (inputJson.has(APIRequestKeys.FILTERS)) {
			filterNode = inputJson.findValue(APIRequestKeys.FILTERS);
		}

		ObjectNode resultNode = Json.newObject();

		resultNode = Vendors.getVendorCmsList(filterNode, page, limit);

		return resultNode;
	}

}
