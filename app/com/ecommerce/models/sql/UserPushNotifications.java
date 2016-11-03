package com.ecommerce.models.sql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

@Entity
public class UserPushNotifications extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String senderProfileId;

	@Column(nullable = false)
	private String receiverProfileId;

	@Column(columnDefinition = "TEXT")
	public String message;

	public String deviceToken;

	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Date timestamp;

	public int notificationType;

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

	public String getSenderProfileId() {
		return senderProfileId;
	}

	public void setSenderProfileId(String senderProfileId) {
		this.senderProfileId = senderProfileId;
	}

	public String getReceiverProfileId() {
		return receiverProfileId;
	}

	public void setReceiverProfileId(String receiverProfileId) {
		this.receiverProfileId = receiverProfileId;
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

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
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

	public Long create(String senderProfileId, String receiverProfileId, String message, String deviceToken,
			int notificationType, int accountType) throws PersistenceException {
		this.senderProfileId = senderProfileId;
		this.receiverProfileId = receiverProfileId;
		this.message = message;
		this.deviceToken = deviceToken;
		this.timestamp = new Date();
		this.notificationType = notificationType;
		this.accountType = accountType;
		this.save();
		return this.id;
	}

	public void updateNotificationStatus(Long id) {
		UserPushNotifications userNotificationMessages = find.byId(id);
		userNotificationMessages.isSent = true;
		Ebean.update(userNotificationMessages);
	}
}