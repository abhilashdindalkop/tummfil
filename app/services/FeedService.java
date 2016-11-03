package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.Promotions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyException;

public class FeedService {

	public ObjectNode getVendorFeeds(JsonNode inputJson) throws MyException, IOException {

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

		resultNode = Vendors.getVendorFeeds(filterNode, page, limit);

		return resultNode;
	}

	public ObjectNode productFeeds(JsonNode inputJson) throws MyException, IOException {

		JsonNode filterNode = Json.newObject();
		int page = 0, limit = MyConstants.DEFAULT_PAGINATION_LIMIT;

		if (inputJson.has(APIRequestKeys.PAGE)) {
			page = inputJson.findValue(APIRequestKeys.PAGE).asInt();
		}
		if (inputJson.has(APIRequestKeys.LIMIT)) {
			limit = inputJson.findValue(APIRequestKeys.LIMIT).asInt();
		}

		if (inputJson.has(APIRequestKeys.FILTERS)) {
			filterNode = inputJson.findValue(APIRequestKeys.FILTERS);
		}
		return Products.findProductsList(filterNode, page, limit);

	}

	public ObjectNode getPromotions() throws MyException {
		List<ObjectNode> promoJsonList = new ArrayList<ObjectNode>();

		List<Promotions> promotionList = Promotions.findAllPromotions();
		for (Promotions promotion : promotionList) {

			ObjectNode promotionNode = Json.newObject();
			boolean isClickable = false;

			promotionNode.put(APIRequestKeys.PROMOTION_ID, promotion.getId());
			promotionNode.put(APIRequestKeys.IMAGE_URL, promotion.getImageUrl());
			Vendors vendor = promotion.getVendor();
			if (vendor != null) {
				isClickable = true;
				promotionNode.put(APIRequestKeys.VENDOR_ID, vendor.getEncryptedVendorId());
			}
			promotionNode.put(APIRequestKeys.DESCRIPTION, promotion.getDescription());

			promotionNode.put(APIRequestKeys.IS_CLICK_ENABLED, isClickable);
			promoJsonList.add(promotionNode);
		}

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIRequestKeys.PROMOTIONS, Json.toJson(promoJsonList));

		return resultNode;
	}
}
