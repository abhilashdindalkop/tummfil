package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

public class CorsComposition {

	/**
	 * Wraps the annotated action in an <code>CorsAction</code>.
	 */
	@With(CorsAction.class)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Cors {
		String value() default "*";
	}

	public static class CorsAction extends Action<Cors> {

		@Override
		public F.Promise<Result> call(Http.Context context) throws Throwable {
			Http.Response response = context.response();

			response.setHeader("Access-Control-Allow-Origin", "*");

			// Handle preflight requests
			if (context.request().method().equals("OPTIONS")) {

				response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers",
						"Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token, X-Custom-Header,token");
				response.setHeader("Access-Control-Allow-Credentials", "true");

				return delegate.call(context);
			}

			response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Auth-Token,token");
			return delegate.call(context);
		}
	}
}
