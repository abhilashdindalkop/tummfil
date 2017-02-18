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

	public Result loadTestingLink() {

		return redirect("https://s3-us-west-2.amazonaws.com/cake-house/loaderio-1ee9557d5025abab3d22cc7d89c6888d.txt");
	}

}
