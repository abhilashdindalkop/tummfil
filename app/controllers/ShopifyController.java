//package controllers;
//
//import com.ecommerce.models.sql.Category;
//import com.ecommerce.models.sql.Products;
//import com.ecommerce.models.sql.Vendors;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//
//import play.libs.Json;
//import play.mvc.BodyParser;
//import play.mvc.Result;
//import utils.CorsComposition;
//import utils.MySuccessResponse;
//
//public class ShopifyController extends ParentController {
//
//	/* To Sign Up a new user */
//	@CorsComposition.Cors
//	@BodyParser.Of(BodyParser.Json.class)
//	public Result shopifyProductTest() {
//		try {
//			JsonNode inputJson = request().body().asJson();
//
//			System.out.println("----------------------------------Arrived----------------------------------");
//
//			System.out.println("----------------------------------JSON BODY----------------------------------");
//			System.out.println(Json.toJson(inputJson));
//
//			System.out.println("----------------------------------Add in DB----------------------------------");
//			String name = inputJson.get("title").asText();
//			String description = inputJson.get("body_html").asText();
//
//			Products newProd = new Products();
//			newProd.setName(name);
//			newProd.setDescription(description);
//			newProd.setPrice(5000);
//
//			Vendors vendor = Vendors.findById("08eb802b-95a6-4b4e-8f65-f7efc213c5e5");
//			Category category = Category.findById(5);
//			newProd.add(vendor, category);
//
//			System.out.println("----------------------------------END----------------------------------");
//			response = new MySuccessResponse("");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response = createFailureResponse(e);
//		}
//		return response.getResult();
//	}
//
//	public static void buyShopifyBrand1(com.mashape.unirest.http.JsonNode product) throws UnirestException {
//		String username = "91cbfcab6da2fdf0d4387c41df045d03";
//		String password = "05079dab45846f337a400d88cce53c29";
//
//		String url = "https://shopsocial-1.myshopify.com/admin/products.json";
//
//		HttpResponse<com.mashape.unirest.http.JsonNode> resp = Unirest.post(url).basicAuth(username, password)
//				.body(product).asJson();
//		if (resp != null) {
//			System.out.println("Status : " + resp.getStatus());
//			System.out.println("Body : " + resp.getBody().getObject());
//		}
//	}
//
//	public static void buyShopifyBrand2(com.mashape.unirest.http.JsonNode product) throws UnirestException {
//
//		String username = "91cbfcab6da2fdf0d4387c41df045d03";
//		String password = "05079dab45846f337a400d88cce53c29";
//
//		String url = "https://shopsocial-1.myshopify.com/admin/products.json";
//
//		HttpResponse<com.mashape.unirest.http.JsonNode> resp = Unirest.post(url).basicAuth(username, password)
//				.body(product).asJson();
//		if (resp != null) {
//			System.out.println("Status : " + resp.getStatus());
//			System.out.println("Body : " + resp.getBody().getObject());
//		}
//	}
//
//}
