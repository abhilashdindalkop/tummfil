package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import play.Logger;

public class MessageReaderFactory {

	private static Properties environmentProperties = null;
	private static Properties properties = null;
	private static Properties mongoProperties = null;

	public static Properties getEnvironmentProperties() {
		if (environmentProperties == null) {
			try {
				Logger.info("====Loading environmentProperties message file======");
				File configFile = new File(
						System.getProperty("user.dir") + "/conf/resources/" + "environmentMode.properties");
				FileReader fileReader = new FileReader(configFile);
				environmentProperties = new Properties();
				environmentProperties.load(fileReader);
			} catch (Exception e) {
				Logger.info("====Error in loading properties file======");
				Logger.info(e.fillInStackTrace().toString());
			}
		}
		return environmentProperties;
	}

	public static Properties getProperties() {
		if (properties == null) {
			try {
				FileReader fileReader = null;
				Properties property = MessageReaderFactory.getEnvironmentProperties();
				String envMode = property.getProperty("is_production").trim();

				if (envMode != null && !envMode.isEmpty()) {
					if (Boolean.valueOf(envMode)) {
						Logger.info("====Loading properties message file for production======");
						File configFile = new File(
								System.getProperty("user.dir") + "/conf/resources/" + "messageKeyValues.properties");
						fileReader = new FileReader(configFile);
					} else {
						Logger.info("====Loading properties message file for dev======");
						File configFile = new File(
								System.getProperty("user.dir") + "/conf/resources/" + "devMessageKeyValues.properties");
						fileReader = new FileReader(configFile);
					}
				} else {
					Logger.info("====Loading properties message file for dev======");
					File configFile = new File(
							System.getProperty("user.dir") + "/conf/resources/" + "devMessageKeyValues.properties");
					fileReader = new FileReader(configFile);
				}

				properties = new Properties();
				properties.load(fileReader);
			} catch (Exception e) {
				Logger.info("====Error in loading properties file======");
				Logger.info(e.fillInStackTrace().toString());
			}
		}
		return properties;
	}

	public static String getPropertyValue(String key) throws IOException {
		String value = null;
		try {
			properties = MessageReaderFactory.getProperties();
			value = properties.getProperty(key);
		} catch (Exception e) {
			System.out.println("Exception ::" + e.getMessage());
		}
		return value;
	}

	public static Properties getMongoProperties() {
		if (mongoProperties == null) {
			try {
				FileReader fileReader = null;
				Properties property = MessageReaderFactory.getEnvironmentProperties();
				String envMode = property.getProperty("is_production").trim();

				if (envMode != null && !envMode.isEmpty()) {
					if (Boolean.valueOf(envMode)) {
						Logger.info("====Loading mongo properties message file for production======");
						File configFile = new File(
								System.getProperty("user.dir") + "/conf/resources/" + "mongoCredentials.properties");
						fileReader = new FileReader(configFile);
					} else {
						Logger.info("====Loading mongo properties message file for dev======");
						File configFile = new File(
								System.getProperty("user.dir") + "/conf/resources/" + "devMongoCredentials.properties");
						fileReader = new FileReader(configFile);
					}
				} else {
					Logger.info("====Loading mongo properties message file for dev======");
					File configFile = new File(
							System.getProperty("user.dir") + "/conf/resources/" + "devMongoCredentials.properties");
					fileReader = new FileReader(configFile);
				}
				mongoProperties = new Properties();
				mongoProperties.load(fileReader);
			} catch (Exception e) {
				Logger.info("====Error in loading mongo properties file======");
				Logger.info(e.fillInStackTrace().toString());
			}
		}
		return mongoProperties;
	}
}
