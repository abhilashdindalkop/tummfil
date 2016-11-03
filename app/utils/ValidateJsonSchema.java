package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;

public interface ValidateJsonSchema {

	@With(JsonSchemaValidator.class)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ValidateJson {
		String value();
	}
}
