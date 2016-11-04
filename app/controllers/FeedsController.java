package controllers;

import javax.inject.Inject;

import com.ecommerce.models.sql.Products;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.FeedService;
import utils.MyConstants.JsonSchemaFilePath;
import utils.MySuccessResponse;
import utils.ValidateJsonSchema.ValidateJson;

public class FeedsController extends ParentController {

	private FeedService feedService;

	@Inject
	public FeedsController(FeedService feedService) {
		this.feedService = feedService;
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.VENDOR_FEEDS)
	public Result vendorFeeds() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = feedService.getVendorFeeds(inputJson);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(JsonSchemaFilePath.PRODUCT_FEEDS)
	public Result productFeeds() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = feedService.productFeeds(inputJson);

			// send 5 most ordered products of each category
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	public Result getPromotions() {
		try {
			ObjectNode resultNode = feedService.getPromotions();
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	public Result getFeaturedProducts(int page, int limit) {
		try {
			ObjectNode resultNode = Json.newObject();

			resultNode = feedService.findFeaturedProductsList(page, limit);
			// send 5 most ordered products of each category
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}
}
