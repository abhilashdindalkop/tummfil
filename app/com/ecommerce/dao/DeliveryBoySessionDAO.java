package com.ecommerce.dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.ecommerce.models.sql.DeliveryBoySession;
import com.ecommerce.models.sql.DeliveryBoys;
import com.ecommerce.models.sql.DeviceType;

import play.mvc.Http;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.OsType;

public class DeliveryBoySessionDAO {

	public void updateBadgeCount(DeliveryBoySession boySession, int badgeCount) {
		boySession.setBadgeCount(badgeCount);
		boySession.update();
	}

	public DeliveryBoySession findByDeviceToken(String deviceToken) {
		if (deviceToken == null) {
			return null;
		}
		return Ebean.find(DeliveryBoySession.class).where().eq("device_token", deviceToken).findUnique();
	}

	public DeliveryBoySession create(DeliveryBoys boy, String deviceToken, String deviceId, int deviceTypeId)
			throws IOException, NoSuchAlgorithmException {
		DeliveryBoySession session = null;
		String token = UUID.randomUUID().toString();

		try {
			if (deviceId != null) {
				// remove the session if already exists for the given device
				session = Ebean.find(DeliveryBoySession.class).where().eq("device_id", deviceId).findUnique();
				if (session != null) {
					Ebean.delete(session);
				}
			}

			if (deviceTypeId != OsType.ANDROID && deviceTypeId != OsType.IOS && deviceTypeId != OsType.BROWSER) {
				deviceTypeId = OsType.UNKNOWN;
			}

			session = new DeliveryBoySession();
			session.setToken(token);
			session.setLoginDatetime(new Date());

			/* Find by Device token */
			if (deviceToken != null) {
				DeliveryBoySession deviceSession = findByDeviceToken(deviceToken);
				if (deviceSession != null) {
					Ebean.delete(deviceSession);
				}
			}

			session.setDeviceToken(deviceToken);
			session.setDeliveryBoy(boy);
			session.setEncryptedId(boy.getEncryptedId());
			session.setDeviceType(DeviceType.findById(deviceTypeId));

			if (deviceTypeId == OsType.BROWSER || deviceTypeId == OsType.UNKNOWN) {
				session.setDeviceId(token);
			} else {
				session.setDeviceId(deviceId);
			}

			// registering device with push notification server (apns, gcm)

			Ebean.save(session);
		} catch (PersistenceException e) {
			throw e;
		}
		return session;
	}

	public DeliveryBoySession findByToken(String token) {
		return Ebean.find(DeliveryBoySession.class).where().eq("token", token).findUnique();
	}

	public List<DeliveryBoySession> findByEncryptedId(String encryptedId) {
		return Ebean.find(DeliveryBoySession.class).where().eq("encryptedId", encryptedId).findList();
	}

	public DeliveryBoySession findByContext() {
		String token = getSessionTokenByContext();
		return findByToken(token);
	}

	public String getEncryptedIdByContext() {
		String encryptedId = (String) Http.Context.current().args.get(APIRequestKeys.ENCRYPTED_DELIVERY_BOY_ID);
		return encryptedId;
	}

	public String getSessionTokenByContext() {
		String token = (String) Http.Context.current().args.get(APIRequestKeys.DELIVERY_BOY_TOKEN);
		return token;
	}

	/* To delete a session used for logout */
	public void deleteSession(DeliveryBoySession session) throws IOException {
		Ebean.delete(session);
	}

}
