package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.CorsComposition;

public class Application extends Controller {

	public Result index() {
		return ok("Your new application is ready.");
	}

	@CorsComposition.Cors
	public Result preflight(String path) {
		return ok("");
	}

}
