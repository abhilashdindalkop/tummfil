package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.UserAuthenticator;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.CartService;
import utils.MyConstants.SuccessMessages;
import utils.MySuccessResponse;

public class CartController extends ParentController {

	CartService cartService;

	@Inject
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	/*
	 * Add products to cart
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result addProductToCart() {
		try {

			JsonNode inputJson = request().body().asJson();
			cartService.addProductToCart(inputJson);
			response = new MySuccessResponse(SuccessMessages.PRODUCT_ADDED_TO_CART_SUCCESS);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	/*
	 * delete product from Cart
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result deleteProductFromCart() {
		try {

			ObjectNode resultNode = null;
			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@Security.Authenticated(UserAuthenticator.class)
	public Result getCartProducts() {
		try {

			ObjectNode resultNode = cartService.getCartProducts();
			response = new MySuccessResponse(resultNode);

		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
