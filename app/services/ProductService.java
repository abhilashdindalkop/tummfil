package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;
import com.ecommerce.models.sql.Category;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
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

public class ProductService {

	public ObjectNode addProduct(JsonNode inputJson) throws MyException, IOException {
		String encryptedVendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(encryptedVendorId);

		int categoryId = inputJson.findValue(APIRequestKeys.CATEGORY_ID).asInt();

		Category category = Category.findById(categoryId);

		if (inputJson.has(APIRequestKeys.PRODUCT_TYPE)) {
			int productType = inputJson.findValue(APIRequestKeys.PRODUCT_TYPE).asInt();
			if (!MyConstants.productTypeList.contains(productType)) {
				throw new MyException(FailureMessages.INVALID_PRODUCT_TYPE);
			}
		}

		if (inputJson.has(APIRequestKeys.UNIT_TYPE)) {
			int unitType = inputJson.findValue(APIRequestKeys.UNIT_TYPE).asInt();
			if (!MyConstants.unitTypeList.contains(unitType)) {
				throw new MyException(FailureMessages.INVALID_UNIT_TYPE);
			}
		}

		ObjectMapper mapper = ObjectMapperUtil.getInstance();
		Products newProduct = mapper.convertValue(inputJson, Products.class);

		newProduct.setName(inputJson.findValue(APIRequestKeys.PRODUCT_NAME).asText());
		newProduct.add(vendor, category);

		ObjectNode resultNode = Json.newObject();
		resultNode.put(APIResponseKeys.PRODUCT_ID, newProduct.getProductId());
		return resultNode;
	}

	public void updateProduct(JsonNode inputJson) throws MyException, IOException {

		String encryptedVendorId = VendorSession.getVendorEncryptedIdByContext();

		String productId = inputJson.findValue(APIRequestKeys.PRODUCT_ID).asText();
		Products product = Products.findById(productId);
		if (!product.getVendor().getEncryptedVendorId().equals(encryptedVendorId)) {
			throw new MyException(FailureMessages.VENDOR_NOT_OWNER_OF_PRODUCT);
		}

		Products.updateProduct(inputJson, product);

	}

	public void deleteProducts(JsonNode inputJson) throws MyException, IOException {

		String encryptedVendorId = VendorSession.getVendorEncryptedIdByContext();
		JsonNode productIds = inputJson.findValue(APIRequestKeys.PRODUCT_ID_LIST);
		for (JsonNode productIdJson : productIds) {
			String productId = productIdJson.asText();
			Products product = Products.findById(productId);
			if (!product.getVendor().getEncryptedVendorId().equals(encryptedVendorId)) {
				throw new MyException(FailureMessages.VENDOR_NOT_OWNER_OF_PRODUCT);
			}
			Products.deleteProduct(product);
		}

	}

	public void setProductAvailability(JsonNode inputJson) throws MyException, IOException {

		String encryptedVendorId = VendorSession.getVendorEncryptedIdByContext();

		JsonNode availableProductIdsJson = inputJson.findValue(APIRequestKeys.AVAILABLE_PRODUCT_IDS);
		JsonNode unavailableProductIdsJson = inputJson.findValue(APIRequestKeys.UNAVAILABLE_PRODUCT_IDS);

		List<String> availableProdList = new ArrayList<String>();
		List<String> unavailableProdList = new ArrayList<String>();

		for (JsonNode productIdJson : availableProductIdsJson) {
			String productId = productIdJson.asText();
			Products product = Products.findById(productId);
			if (!product.getVendor().getEncryptedVendorId().equals(encryptedVendorId)) {
				throw new MyException(FailureMessages.VENDOR_NOT_OWNER_OF_PRODUCT);
			}
			availableProdList.add(productId);
		}
		for (JsonNode productIdJson : unavailableProductIdsJson) {
			String productId = productIdJson.asText();
			Products product = Products.findById(productId);
			if (!product.getVendor().getEncryptedVendorId().equals(encryptedVendorId)) {
				throw new MyException(FailureMessages.VENDOR_NOT_OWNER_OF_PRODUCT);
			}
			unavailableProdList.add(productId);
		}

		try {
			Ebean.beginTransaction();
			Products.setAvailability(availableProdList, true);
			Products.setAvailability(unavailableProdList, false);
			Ebean.commitTransaction();
		} catch (Exception e) {
			if (Ebean.currentTransaction() != null) {
				Ebean.rollbackTransaction();
			}
			throw e;
		}
	}

	public ObjectNode getVendorProducts(int categoryId, int page, int limit) throws MyException, IOException {
		String vendorId = VendorSession.getVendorEncryptedIdByContext();
		Vendors vendor = Vendors.findById(vendorId);

		Category category = null;
		if (categoryId != 0) {
			category = Category.findById(categoryId);
		}
		ObjectNode resultNode = Json.newObject();

		Query<Products> productsQuery = Products.findVendorProductsQuery(vendor, category);
		resultNode.put(APIResponseKeys.TOTAL_COUNT, productsQuery.findRowCount());

		List<Products> products = new ArrayList<Products>();
		products = productsQuery.findPagedList(page, limit).getList();

		resultNode.set(APIResponseKeys.PRODUCTS, Json.toJson(CreateResponseJson.getProductsListJson(products)));

		return resultNode;
	}

	public ObjectNode searchVendorProducts(String searchText) throws MyException, IOException {
		if (searchText == null) {
			throw new MyException(FailureMessages.INVALID_SEARCH_OPERATION);
		}
		if (searchText.length() < MyConstants.MIN_SEARCH_LENGTH) {
			throw new MyException(FailureMessages.SEARCH_TEXT_MIN_LENGTH_TWO);
		}
		Vendors vendor = VendorSession.findByContext().getVendor();

		List<SqlRow> productList = Products.searchMyProductsByTextQuery(vendor, searchText);

		List<HashMap<String, Object>> productsList = CreateResponseJson.getProductsListJsonRaw(productList);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.PRODUCTS, Json.toJson(productsList));
		return resultNode;
	}

	/* Search products for Users */
	public ObjectNode searchProducts(String searchText, int page, int limit) throws MyException, IOException {
		if (searchText == null) {
			throw new MyException(FailureMessages.INVALID_SEARCH_OPERATION);
		}
		if (searchText.length() < MyConstants.MIN_SEARCH_LENGTH) {
			throw new MyException(FailureMessages.SEARCH_TEXT_MIN_LENGTH_TWO);
		}

		List<HashMap<String, Object>> productsList = Products.searchProductsByText(searchText);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.PRODUCTS, Json.toJson(productsList));

		return resultNode;
	}

	public ObjectNode searchCategory(String searchText) throws MyException, IOException {
		if (searchText == null) {
			throw new MyException(FailureMessages.INVALID_SEARCH_OPERATION);
		}

		List<SqlRow> categoryList = Category.searchByText(searchText);

		ObjectNode resultNode = Json.newObject();
		resultNode.set(APIResponseKeys.CATEGORY_LIST, Json.toJson(categoryList));
		return resultNode;
	}

}
