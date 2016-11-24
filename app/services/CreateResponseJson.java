package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avaje.ebean.SqlRow;
import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Category;
import com.ecommerce.models.sql.OrderedProducts;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.Products;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.ImageResizeType;
import utils.MyException;
import utils.ImageUtilities;

public class CreateResponseJson {

	/*
	 * Construct Products Json
	 */

	/* Json for Product feeds - Based on category */
	public static ObjectNode getProductsFeedListJson(List<Products> productList) throws IOException {
		long prevId = 0;
		String prevType = null;
		Category curCat;
		ObjectNode productCategoryNode = Json.newObject();

		List<HashMap<String, Object>> categoryProductList = new ArrayList<HashMap<String, Object>>();
		for (Products product : productList) {
			curCat = product.getCategory();
			if (curCat.getId() != prevId && prevId != 0) {
				productCategoryNode.set(prevType, Json.toJson(categoryProductList));
				categoryProductList = new ArrayList<HashMap<String, Object>>();
			}
			HashMap<String, Object> productHM = CreateResponseJson.getProductJson(product);
			categoryProductList.add(productHM);
			prevId = curCat.getId();
			prevType = curCat.getType();
		}
		if (prevId != 0) {
			productCategoryNode.set(prevType, Json.toJson(categoryProductList));
		}
		return productCategoryNode;
	}

	public static List<HashMap<String, Object>> getProductsListJsonRaw(List<SqlRow> productRowList) throws IOException {
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();
		for (SqlRow product : productRowList) {
			HashMap<String, Object> newHM = new HashMap<String, Object>();
			String productId = product.getString("product_id");
			newHM.put(APIResponseKeys.PRODUCT_ID, productId);
			newHM.put(APIResponseKeys.PRODUCT_NAME, product.getString("name"));
			newHM.put(APIResponseKeys.DESCRIPTION, product.getString("description"));
			String imageUrl = product.getString("image_url");
			if (imageUrl != null) {
				newHM.put(APIResponseKeys.PRODUCT_THUMBNAIL_IMAGE_URL,
						ImageUtilities.getProductImageUrl(productId, imageUrl, ImageResizeType.THUMBNAIL_SIZE));
				newHM.put(APIResponseKeys.PRODUCT_IMAGE_URL,
						ImageUtilities.getProductImageUrl(productId, imageUrl, ImageResizeType.STANDARD));
			}
			newHM.put(APIResponseKeys.CATEGORY_ID, product.getInteger("category_id"));
			newHM.put(APIResponseKeys.STATUS, product.getInteger("status"));
			newHM.put(APIResponseKeys.PRODUCT_TYPE, product.getInteger("product_type"));
			newHM.put(APIResponseKeys.UNITS, product.getDouble("units"));
			newHM.put(APIResponseKeys.UNIT_TYPE, product.getDouble("unit_type"));
			newHM.put(APIResponseKeys.PRICE, product.getDouble("price"));
			newHM.put(APIResponseKeys.IS_FEATURED, product.getBoolean("is_featured"));
			productList.add(newHM);
		}
		return productList;
	}

	public static List<HashMap<String, Object>> getProductsListJson(List<Products> productList) throws IOException {
		List<HashMap<String, Object>> productJsonList = new ArrayList<HashMap<String, Object>>();
		for (Products product : productList) {
			HashMap<String, Object> newHM = getProductJson(product);
			productJsonList.add(newHM);
		}
		return productJsonList;
	}

	public static HashMap<String, Object> getProductJson(Products product) throws IOException {
		HashMap<String, Object> newHM = new HashMap<String, Object>();
		String productId = product.getProductId();
		newHM.put(APIResponseKeys.PRODUCT_ID, productId);
		newHM.put(APIResponseKeys.PRODUCT_NAME, product.getName());
		newHM.put(APIResponseKeys.DESCRIPTION, product.getDescription());
		if (product.getImageUrl() != null) {
			newHM.put(APIResponseKeys.PRODUCT_THUMBNAIL_IMAGE_URL, ImageUtilities
					.getProductImageUrl(product.getProductId(), product.getImageUrl(), ImageResizeType.THUMBNAIL_SIZE));
			newHM.put(APIResponseKeys.PRODUCT_IMAGE_URL, ImageUtilities.getProductImageUrl(product.getProductId(),
					product.getImageUrl(), ImageResizeType.STANDARD));
		}
		newHM.put(APIResponseKeys.CATEGORY_ID, product.getCategory().getId());
		newHM.put(APIResponseKeys.STATUS, product.getStatus());
		newHM.put(APIResponseKeys.PRODUCT_TYPE, product.getProductType());
		newHM.put(APIResponseKeys.UNITS, product.getUnits());
		newHM.put(APIResponseKeys.UNIT_TYPE, product.getUnitType());
		newHM.put(APIResponseKeys.PRICE, product.getPrice());
		newHM.put(APIResponseKeys.IS_FEATURED, product.getIsFeatured());
		return newHM;
	}

	/*
	 * Construct Vendor Json
	 */

	public static List<ObjectNode> getVendorsJsonList(List<Vendors> vendorsList) throws IOException {
		List<ObjectNode> vendorListNode = new ArrayList<ObjectNode>();
		for (Vendors vendor : vendorsList) {
			ObjectNode vendorNode = Json.newObject();
			vendorNode = CreateResponseJson.constructVendorResponseJson(vendor);
			vendorListNode.add(vendorNode);
		}
		return vendorListNode;
	}

	public static ObjectNode constructVendorResponseJson(Vendors vendor) throws IOException {
		ObjectNode vendorNode = Json.newObject();
		vendorNode.put(APIResponseKeys.VENDOR_ID, vendor.getEncryptedVendorId());
		vendorNode.put(APIResponseKeys.NAME, vendor.getName());
		vendorNode.put(APIResponseKeys.VENDOR_NAME, vendor.getVendorName());
		vendorNode.put(APIResponseKeys.VENDOR_ADDRESS, vendor.getVendorAddress());
		vendorNode.put(APIResponseKeys.DESCRIPTION, vendor.getDescription());
		vendorNode.put(APIResponseKeys.VENDOR_ID, vendor.getEncryptedVendorId());
		vendorNode.put(APIResponseKeys.LATITUDE, vendor.getLatitude());
		vendorNode.put(APIResponseKeys.LONGITUDE, vendor.getLongitude());
		vendorNode.set(APIResponseKeys.CITY, Json.toJson(vendor.getCity()));
		if (vendor.getImageUrl() != null) {
			vendorNode.put(APIResponseKeys.VENDOR_IMAGE_URL, ImageUtilities
					.getVendorImageUrl(vendor.getEncryptedVendorId(), vendor.getImageUrl(), ImageResizeType.STANDARD));
			vendorNode.put(APIResponseKeys.VENDOR_THUMBNAIL_IMAGE_URL, ImageUtilities.getVendorImageUrl(
					vendor.getEncryptedVendorId(), vendor.getImageUrl(), ImageResizeType.THUMBNAIL_SIZE));
		}
		return vendorNode;
	}

	/*
	 * Construct Vendor Json
	 */
	public static List<ObjectNode> getVendorOrdersJsonResult(List<Orders> orderList) throws MyException, IOException {
		List<ObjectNode> orderJsonList = new ArrayList<ObjectNode>();
		for (Orders order : orderList) {
			ObjectNode orderNode = Json.newObject();
			orderNode.put(APIResponseKeys.ORDER_ID, order.getOrderId());
			orderNode.put(APIResponseKeys.ADDRESS, order.getAddress());
			orderNode.put(APIResponseKeys.DESCRIPTION, order.getDescription());
			orderNode.put(APIResponseKeys.LATITUDE, order.getLatitude());
			orderNode.put(APIResponseKeys.LONGITUDE, order.getLongitude());
			orderNode.put(APIResponseKeys.CUSTOMER_NAME, order.getName());
			orderNode.put(APIResponseKeys.ORDER_TYPE, order.getOrderType());
			orderNode.put(APIResponseKeys.PHONE_NO, order.getPhoneNo());
			orderNode.put(APIResponseKeys.ORDER_STATUS, order.getStatus());
			orderNode.put(APIResponseKeys.PAYMENT_TYPE, order.getPaymentType());
			orderNode.put(APIResponseKeys.PAYMENT_STATUS, order.getPaymentStatus());

			if (order.getDeliveryTime() != null) {
				orderNode.put(APIResponseKeys.DELIVERY_TIME, order.getDeliveryTime().getTime());
			}
			if (order.getCreatedTime() != null) {
				orderNode.put(APIResponseKeys.ORDERED_TIME, order.getCreatedTime().getTime());
			}

			List<ObjectNode> productJsonList = new ArrayList<ObjectNode>();
			List<OrderedProducts> productList = OrderedProducts.findProductsForOrder(order);
			for (OrderedProducts orderedProduct : productList) {
				ObjectNode productNode = Json.newObject();
				Products myProduct = orderedProduct.getProduct();
				productNode.put(APIResponseKeys.PRODUCT_ID, myProduct.getProductId());
				productNode.put(APIResponseKeys.PRODUCT_NAME, myProduct.getName());
				if (myProduct.getImageUrl() != null) {
					productNode.put(APIResponseKeys.PRODUCT_THUMBNAIL_IMAGE_URL, ImageUtilities.getProductImageUrl(
							myProduct.getProductId(), myProduct.getImageUrl(), ImageResizeType.THUMBNAIL_SIZE));
				}
				productNode.put(APIResponseKeys.CATEGORY_ID, myProduct.getCategory().getId());
				productNode.put(APIResponseKeys.PRODUCT_TYPE, myProduct.getProductType());
				productNode.put(APIResponseKeys.PRICE, orderedProduct.getPrice());
				productNode.put(APIResponseKeys.QUANTITY, orderedProduct.getQuantity());
				// TODO get tags
				productJsonList.add(productNode);
			}
			orderNode.set(APIResponseKeys.PRODUCTS, Json.toJson(productJsonList));
			orderJsonList.add(orderNode);
		}
		return orderJsonList;
	}
}
