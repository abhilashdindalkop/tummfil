package com.ecommerce.models.sql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Model;

@Entity
public class UserPushNotifications extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String receiverId;

	@Column(columnDefinition = "TEXT")
	public String message;

	public int platform;

	public String deviceToken;

	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Date timestamp;

	public int accountType;

	public boolean isSent = false;

	public static Finder<Long, UserPushNotifications> find = new Finder<Long, UserPushNotifications>(
			UserPushNotifications.class);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}

	public boolean getIsSent() {
		return isSent;
	}

	public void setIsSent(boolean isSent) {
		this.isSent = isSent;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public Long create(String receiverId, String message, String deviceToken, int accountType, int platform)
			throws PersistenceException {
		this.receiverId = receiverId;
		this.message = message;
		this.deviceToken = deviceToken;
		this.timestamp = new Date();
		this.accountType = accountType;
		this.platform = platform;
		this.save();
		return this.id;
	}

	public void updateNotificationStatus() {
		this.isSent = true;
		this.update();
	}
}