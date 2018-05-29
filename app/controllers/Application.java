package controllers;

import javax.inject.Inject;

import com.ecommerce.dao.VendorLocationDAO;

import play.mvc.Controller;
import play.mvc.Result;
import utils.CorsComposition;

public class Application extends Controller {

	@Inject
	VendorLocationDAO vendorLocationDAO;
	
	public Result index() {
		return ok("Your new application is ready.");
	}
	
	public Result test() {
		
		return ok("Test Methods");
	}
	

	@CorsComposition.Cors
	public Result preflight(String path) {
		return ok("");
	}

}
