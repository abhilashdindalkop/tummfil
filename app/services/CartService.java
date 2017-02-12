package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.models.sql.Cart;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.UserSession;
import com.ecommerce.models.sql.Users;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyException;

public class CartService {

	CartDAO cartDAO;

	public CartService(CartDAO cartDAO) {
		this.cartDAO = cartDAO;
	}

	public void addProductToCart(JsonNode inputJson) throws MyException {
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		String productId = inputJson.findValue(APIRequestKeys.PRODUCT_ID).asText();
		Products product = Products.findById(productId);

		Users user = Users.findById(encryptedUserId);

		Cart cartProduct = cartDAO.findByUserAndProduct(user, product);

		if (cartDAO.isDifferentVendor(user, product)) {
			boolean isEmptyCart = inputJson.findValue(APIRequestKeys.IS_EMPTY_CART).asBoolean();
			if (isEmptyCart) {
				cartDAO.deleteUserCart(user);
			} else {
				throw new MyException(FailureMessages.DIFFERENT_VENDOR);
			}
		}

		if (cartProduct == null) {
			cartProduct = new Cart();
			cartDAO.add(cartProduct, user, product);
		} else {
			int quantity = cartProduct.getQuantity() + 1;
			cartDAO.updateProduct(cartProduct, quantity);
		}
	}

	public void deleteProductFromCart(JsonNode inputJson) throws MyException {
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);

		String productId = inputJson.findValue(APIRequestKeys.PRODUCT_ID).asText();
		Products product = Products.findById(productId);

		Cart cartProduct = cartDAO.findByUserAndProduct(user, product);
		if (cartProduct == null) {
			throw new MyException(FailureMessages.PRODUCT_DOESNT_EXIST_IN_CART);
		}

		if (cartProduct.getQuantity() == 1) {
			cartDAO.deleteByCartProductId(cartProduct.getId());
		} else {
			int quantity = cartProduct.getQuantity() - 1;
			cartDAO.updateProduct(cartProduct, quantity);
		}
	}

	public ObjectNode getCartProducts() throws IOException, MyException {
		ObjectNode resultNode = Json.newObject();
		String encryptedUserId = UserSession.getUserEncryptedIdByContext();

		Users user = Users.findById(encryptedUserId);

		List<Cart> cartProductList = cartDAO.findUserCartProducts(user, false, true);

		Vendors vendor = null;
		if (!cartProductList.isEmpty()) {
			vendor = cartProductList.get(0).getVendor();
		}

		List<ObjectNode> productJsonList = new ArrayList<ObjectNode>();
		for (Cart cartProduct : cartProductList) {
			ObjectNode productNode = Json.newObject();
			Products product = cartProduct.getProduct();
			productNode.put(APIResponseKeys.CART_PRODUCT_ID, cartProduct.getId());
			productNode.put(APIResponseKeys.PRODUCT_ID, product.getProductId());
			productNode.put(APIResponseKeys.PRODUCT_NAME, product.getName());
			productNode.put(APIResponseKeys.STATUS, product.getStatus());
			productNode.put(APIResponseKeys.PRODUCT_TYPE, product.getProductType());
			productNode.put(APIResponseKeys.QUANTITY, cartProduct.getQuantity());
			productNode.put(APIResponseKeys.PRICE, product.getPrice());
			productJsonList.add(productNode);
		}
		resultNode.put(APIResponseKeys.VENDOR_ID, vendor.getEncryptedVendorId());
		resultNode.set(APIResponseKeys.PRODUCTS, Json.toJson(productJsonList));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, cartProductList.size());
		return resultNode;
	}

}
