package com.ecommerce.models.sql;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import play.mvc.Http;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.OsType;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class VendorSession extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Vendors vendor;

	@Column(nullable = false)
	private String encryptedVendorId;

	@Column(unique = true, nullable = false, length = 255)
	private String token;

	@Column(unique = true, nullable = false, length = 255)
	private String deviceId;

	@Column(unique = true, length = 255)
	private String deviceToken;

	@Column(length = 1023)
	private String endPointArn;

	@Column(length = 45)
	private String appVersion;

	private int badgeCount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private DeviceType deviceType;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date loginDatetime;

	@Column(length = 255)
	public static Finder<Long, VendorSession> find = new Finder<Long, VendorSession>(VendorSession.class);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLoginDatetime() {
		return loginDatetime;
	}

	public void setLoginDatetime(Date loginDatetime) {
		this.loginDatetime = loginDatetime;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEndPointArn() {
		return endPointArn;
	}

	public void setEndPointArn(String endPointArn) {
		this.endPointArn = endPointArn;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public Vendors getVendor() {
		return vendor;
	}

	public void setVendor(Vendors vendor) {
		this.vendor = vendor;
	}

	public String getEncryptedVendorId() {
		return encryptedVendorId;
	}

	public void setEncryptedVendorId(String encryptedVendorId) {
		this.encryptedVendorId = encryptedVendorId;
	}

	public int getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(int badgeCount) {
		this.badgeCount = badgeCount;
	}

	public void updateBadgeCount(int badgeCount) {
		this.setBadgeCount(badgeCount);
		this.update();
	}

	public static VendorSession findByDeviceToken(String deviceToken) {
		if (deviceToken == null) {
			return null;
		}
		return Ebean.find(VendorSession.class).where().eq("device_token", deviceToken).findUnique();
	}

	public static VendorSession create(Vendors vendor, String deviceToken, String deviceId, int deviceTypeId)
			throws IOException, NoSuchAlgorithmException {
		VendorSession session = null;
		String token = UUID.randomUUID().toString();

		try {
			if (deviceId != null) {
				// remove the session if already exists for the given device
				session = Ebean.find(VendorSession.class).where().eq("device_id", deviceId).findUnique();
				if (session != null) {
					Ebean.delete(session);
				}
			}

			if (deviceTypeId != OsType.ANDROID && deviceTypeId != OsType.IOS && deviceTypeId != OsType.BROWSER) {
				deviceTypeId = OsType.UNKNOWN;
			}

			session = new VendorSession();
			session.setToken(token);
			session.setLoginDatetime(new Date());

			/* Find by Device token */
			if (deviceToken != null) {
				VendorSession deviceSession = findByDeviceToken(deviceToken);
				if (deviceSession != null) {
					Ebean.delete(deviceSession);
				}
			}

			session.setDeviceToken(deviceToken);
			session.setVendor(vendor);
			session.setEncryptedVendorId(vendor.getEncryptedVendorId());
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

	public static VendorSession findByToken(String token) {
		return Ebean.find(VendorSession.class).where().eq("token", token).findUnique();
	}

	public static List<VendorSession> findByEncryptedVendorId(String encryptedVendorId) {
		return Ebean.find(VendorSession.class).where().eq("encryptedVendorId", encryptedVendorId).findList();
	}

	public static VendorSession findByContext() {
		String token = getSessionTokenByContext();
		return findByToken(token);
	}

	public static String getVendorEncryptedIdByContext() {
		String encryptedVendorId = (String) Http.Context.current().args.get(APIRequestKeys.ENCRYPTED_VENDOR_ID);
		return encryptedVendorId;
	}

	public static String getSessionTokenByContext() {
		String token = (String) Http.Context.current().args.get(APIRequestKeys.VENDOR_TOKEN);
		return token;
	}

	/* To delete a session used for logout */
	public static void deleteSession(VendorSession session) throws IOException {
		Ebean.delete(session);
	}

}
