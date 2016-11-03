package com.ecommerce.models.sql;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class DeliveryBoySession extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private DeliveryBoys deliveryBoy;

	@Column(nullable = false)
	private String encryptedId;

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

	public DeliveryBoys getDeliveryBoy() {
		return deliveryBoy;
	}

	public void setDeliveryBoy(DeliveryBoys deliveryBoy) {
		this.deliveryBoy = deliveryBoy;
	}

	public String getEncryptedId() {
		return encryptedId;
	}

	public void setEncryptedId(String encryptedId) {
		this.encryptedId = encryptedId;
	}

	public int getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(int badgeCount) {
		this.badgeCount = badgeCount;
	}

}
