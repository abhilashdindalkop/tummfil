package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.VendorAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.ProductService;
import utils.MyConstants.JsonSchemaFilePath;
import utils.MyConstants.SuccessMessages;
import utils.MySuccessResponse;
import utils.ValidateJsonSchema.ValidateJson;

public class ProductController extends ParentController {

	ProductService productService;

	@Inject
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	@ValidateJson(JsonSchemaFilePath.ADD_PRODUCT)
	public Result addProduct() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectNode resultNode = productService.addProduct(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	@ValidateJson(JsonSchemaFilePath.UPDATE_PRODUCT)
	public Result updateProduct() {
		try {
			JsonNode inputJson = request().body().asJson();

			productService.updateProduct(inputJson);
			response = new MySuccessResponse(SuccessMessages.PRODUCT_UPDATE_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result deleteProductByIds() {
		try {
			JsonNode inputJson = request().body().asJson();
			productService.deleteProducts(inputJson);

			response = new MySuccessResponse(SuccessMessages.PRODUCT_DELETE_SUCCESS);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result setProductAvailability() {
		try {
			JsonNode inputJson = request().body().asJson();
			productService.setProductAvailability(inputJson);

			response = new MySuccessResponse(SuccessMessages.PRODUCT_UPDATE_AVAILABILITY_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result getVendorProducts(int categoryId, Boolean isAvailable, int page, int limit) {
		try {

			ObjectNode resultNode = productService.getVendorProducts(categoryId, isAvailable, page, limit);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	/* Search products for Vendors */
	@Security.Authenticated(VendorAuthenticator.class)
	public Result searchVendorProducts(String searchText) {
		try {

			ObjectNode resultNode = productService.searchVendorProducts(searchText);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	/* Search products for Users */
	public Result searchProducts(String searchText, int page, int limit) {
		try {

			ObjectNode resultNode = productService.searchProducts(searchText, page, limit);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(VendorAuthenticator.class)
	public Result searchCategory(String searchText) {
		try {
			ObjectNode resultNode = productService.searchCategory(searchText);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
