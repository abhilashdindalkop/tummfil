package utils;

public enum JsonSchemaResponseKeys {
	JSONSCHEMA_LEVEL_KEY("level"), JSONSCHEMA_MISSING_KEY("missing"), JSONSCHEMA_UNWANTED_KEY(
			"unwanted"), JSONSCHEMA_INSTANCE_KEY("instance"), JSONSCHEMA_POINTER_KEY("pointer");

	private String message;

	private JsonSchemaResponseKeys(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

}
