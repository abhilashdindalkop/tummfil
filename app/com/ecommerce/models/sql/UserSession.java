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
public class UserSession extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Users user;

	@Column(nullable = false)
	private String encryptedUserId;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DeviceType deviceType;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date loginDatetime;

	@Column(length = 255)
	public static Finder<Long, UserSession> find = new Finder<Long, UserSession>(UserSession.class);

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

	public String getEncryptedUserId() {
		return encryptedUserId;
	}

	public void setEncryptedUserId(String encryptedUserId) {
		this.encryptedUserId = encryptedUserId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public int getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(int badgeCount) {
		this.badgeCount = badgeCount;
	}

	public static UserSession findByDeviceToken(String deviceToken) {
		if (deviceToken == null) {
			return null;
		}
		return Ebean.find(UserSession.class).where().eq("deviceToken", deviceToken).findUnique();
	}

	public static UserSession create(Users user, String deviceToken, String deviceId, int deviceTypeId)
			throws IOException, NoSuchAlgorithmException {
		UserSession userSession = null;
		String token = UUID.randomUUID().toString();
		try {
			if (deviceId != null) {
				// remove the session if already exists for the given device
				userSession = Ebean.find(UserSession.class).where().eq("deviceId", deviceId).findUnique();
				if (userSession != null) {
					Ebean.delete(userSession);
				}
			}

			if (deviceTypeId != OsType.ANDROID && deviceTypeId != OsType.IOS && deviceTypeId != OsType.BROWSER) {
				deviceTypeId = OsType.UNKNOWN;
			}

			userSession = new UserSession();
			userSession.setToken(token);
			userSession.setLoginDatetime(new Date());

			/* Find by Device token */
			if (deviceToken != null) {
				UserSession deviceSession = findByDeviceToken(deviceToken);
				if (deviceSession != null) {
					Ebean.delete(deviceSession);
				}
			}

			userSession.setDeviceToken(deviceToken);
			userSession.setUser(user);
			userSession.setEncryptedUserId(user.getEncryptedUserId());
			userSession.setDeviceType(DeviceType.findById(deviceTypeId));

			if (deviceTypeId == OsType.BROWSER || deviceTypeId == OsType.UNKNOWN) {
				userSession.setDeviceId(token);
			} else {
				userSession.setDeviceId(deviceId);
			}

			// registering device with push notification server (apns, gcm)

			Ebean.save(userSession);
			user.updateLastLogin();

		} catch (PersistenceException e) {
			throw e;
		}
		return userSession;
	}

	public static UserSession findByToken(String token) {
		return Ebean.find(UserSession.class).where().eq("token", token).findUnique();
	}

	public static List<UserSession> findByEncryptedUserId(String encryptedUserId) {
		return Ebean.find(UserSession.class).where().eq("encryptedUserId", encryptedUserId).findList();
	}

	public static UserSession findByContext() {
		String token = getSessionTokenByContext();
		return findByToken(token);
	}

	public static String getUserEncryptedIdByContext() {
		String encryptedUserId = (String) Http.Context.current().args.get(APIRequestKeys.ENCRYPTED_USER_ID);
		return encryptedUserId;
	}

	public static String getSessionTokenByContext() {
		String token = (String) Http.Context.current().args.get(APIRequestKeys.USER_TOKEN_HEADER);
		return token;
	}

	/* To delete a session used for logout */
	public static void deleteSession(UserSession session) throws IOException {
		Ebean.delete(session);
	}

	public void updateBadgeCount(int badgeCount) {
		this.setBadgeCount(badgeCount);
		this.update();
	}

}
