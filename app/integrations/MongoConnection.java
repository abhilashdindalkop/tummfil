package integrations;

import java.util.Arrays;
import java.util.Properties;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

import play.Logger;
import utils.MessageReaderFactory;

/**
 * 
 * @author Abhilash
 *
 */
public class MongoConnection {
	private static final Object someObj = new Object();

	private volatile static Datastore DS = null;

	private volatile static MongoDatabase db = null;

	private static Properties mongoProperties = MessageReaderFactory.getMongoProperties();

	/**
	 * private constructor singleton class
	 */
	private MongoConnection() {
	};

	/**
	 * 
	 * @return mongodb connection using morphia
	 */
	public static Datastore getDS() {

		if (DS == null) {
			synchronized (someObj) {
				try {
					// Primary Server
					ServerAddress primaryServer = new ServerAddress(mongoProperties.getProperty("PRIMARY_SERVER"),
							Integer.parseInt(mongoProperties.getProperty("PORT")));

					// Secondary Server
					ServerAddress secondaryServer = new ServerAddress(mongoProperties.getProperty("SECONDARY_SERVER"),
							Integer.parseInt(mongoProperties.getProperty("PORT")));

					// MongoDB read/write configuration
					MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
					mongoConf.writeConcern(WriteConcern.JOURNALED);
					mongoConf.readPreference(ReadPreference.nearest());

					MongoCredential mongoCred = MongoCredential.createScramSha1Credential(
							mongoProperties.getProperty("USERNAME"), mongoProperties.getProperty("DBNAME"),
							mongoProperties.getProperty("PASSWORD").toCharArray());

					// Creating MongoClient
					MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
							Arrays.asList(mongoCred, mongoCred), mongoConf.build());

					Morphia morphia = new Morphia();
					// return dataStore;
					DS = morphia.createDatastore(mongoClient, mongoProperties.getProperty("DBNAME"));
				} catch (Exception e) {
					Logger.info(
							"===========================Error in Connecting to Mongo db==============================");
					e.printStackTrace();
				}
			}
		}
		return DS;
	}

	/**
	 * 
	 * @return mongodb connection using java driver
	 */
	public static synchronized MongoDatabase getConnection() {

		if (db == null) {
			ServerAddress primaryServer = new ServerAddress(mongoProperties.getProperty("PRIMARY_SERVER"),
					Integer.parseInt(mongoProperties.getProperty("PORT")));

			// Secondary Server
			ServerAddress secondaryServer = new ServerAddress(mongoProperties.getProperty("SECONDARY_SERVER"),
					Integer.parseInt(mongoProperties.getProperty("PORT")));

			// MongoDB read/write configuration
			MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
			mongoConf.writeConcern(WriteConcern.JOURNALED);
			mongoConf.readPreference(ReadPreference.nearest());

			MongoCredential mongoCred = MongoCredential.createScramSha1Credential(
					mongoProperties.getProperty("USERNAME"), mongoProperties.getProperty("DBNAME"),
					mongoProperties.getProperty("PASSWORD").toCharArray());

			// Creating MongoClient
			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
					Arrays.asList(mongoCred, mongoCred), mongoConf.build());

			db = mongoClient.getDatabase(mongoProperties.getProperty("DBNAME"));
		}
		return db;
	}

}