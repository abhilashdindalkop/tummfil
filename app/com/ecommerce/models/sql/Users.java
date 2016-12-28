package com.ecommerce.models.sql;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.PasswordEncryptDecrypt;
import play.libs.Json;
import utils.GenericUtils;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyException;

/**
 * @author abhilash
 *
 */
@Entity
public class Users extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true)
	private String encryptedUserId;

	private String name;

	private String facebookId;

	@Column(columnDefinition = "TEXT")
	private String address;

	private String pincode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true)
	private Cities city;

	private String phoneNo;

	@Column(unique = true)
	private String email;

	private String password;

	@Column(unique = true)
	private String referralCode;

	private double walletAmount = 0;

	private Boolean isUserVerified = false;

	private Boolean isDeleted = false;

	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date lastLogin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEncryptedUserId() {
		return encryptedUserId;
	}

	public void setEncryptedUserId(String encryptedUserId) {
		this.encryptedUserId = encryptedUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public Boolean getIsUserVerified() {
		return isUserVerified;
	}

	public void setIsUserVerified(Boolean isUserVerified) {
		this.isUserVerified = isUserVerified;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public double getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(double walletAmount) {
		this.walletAmount = walletAmount;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public static Users findById(String encryptedUserId) {
		Users user = Ebean.find(Users.class).where().eq("encryptedUserId", encryptedUserId).findUnique();
		return user;
	}

	public static Users findByReferralCode(String referralCode) {
		return Ebean.find(Users.class).where().eq("referralCode", referralCode).findUnique();
	}

	public static Users findByEmail(String email) {
		Users user = Ebean.find(Users.class).where().eq("email", email).eq("isDeleted", false).findUnique();
		return user;
	}

	public static Users findByPhoneNo(String phoneNo) {
		Users user = Ebean.find(Users.class).where().eq("phoneNo", phoneNo).eq("isDeleted", false).findUnique();
		return user;
	}

	public static Boolean isEmailExists(String email) {
		return findByEmail(email) != null;
	}

	public static Boolean isPhoneNoExists(String phoneNo) {
		return findByPhoneNo(phoneNo) != null;
	}

	public static Boolean isExists(String encryptedUserId) {
		return findById(encryptedUserId) != null;
	}

	public static Boolean checkPassword(Users user, String password) {
		if (PasswordEncryptDecrypt.isPasswordSame(password, user.getPassword())) {
			return true;
		}
		return false;
	}

	public static Users findByFacebookId(String facebookId) {
		if (facebookId == null) {
			return null;
		}
		Users user = Ebean.find(Users.class).where().eq("facebookId", facebookId).findUnique();
		return user;
	}

	public void addUser(String password, Cities city) {
		this.setCity(city);
		this.setPassword(PasswordEncryptDecrypt.generatePasswordHash(password));
		String uuid = UUID.randomUUID().toString();
		this.setEncryptedUserId(uuid);
		String referralCode = GenericUtils.generateReferralCode(uuid);
		this.setReferralCode(referralCode);
		this.setLastLogin(new Date());
		this.save();
	}

	public void updateUser(JsonNode inputJson) throws MyException {

		if (inputJson.has(APIRequestKeys.NAME)) {
			String name = inputJson.findValue(APIRequestKeys.NAME).asText();
			this.setName(name);
		}
		if (inputJson.has(APIRequestKeys.ADDRESS)) {
			String address = inputJson.findValue(APIRequestKeys.ADDRESS).asText();
			this.setAddress(address);
		}
		if (inputJson.has(APIRequestKeys.PINCODE)) {
			String pincode = inputJson.findValue(APIRequestKeys.PINCODE).asText();
			this.setPincode(pincode);
		}
		if (inputJson.has(APIRequestKeys.CITY_ID)) {
			long cityId = inputJson.findValue(APIRequestKeys.CITY_ID).asLong();
			Cities city = Cities.findById(cityId);
			this.setCity(city);
		}
		if (inputJson.has(APIRequestKeys.EMAIL)) {
			String email = inputJson.findValue(APIRequestKeys.EMAIL).asText();
			this.setEmail(email);
		}
		this.update();
	}

	public void updateCreatedTime() {
		this.setCreatedTime(new Date());
		this.update();
	}

	public void updateLastLogin() {
		this.setLastLogin(new Date());
		this.update();
	}

	public static ObjectNode createUserUsingFacebook(String facebookId, String phoneNo, String email, String name,
			String deviceToken, String deviceId, int deviceTypeId)
					throws NoSuchAlgorithmException, IOException, MyException {

		ObjectNode resultNode = Json.newObject();

		if (email != null && isEmailExists(email)) {
			throw new MyException(FailureMessages.EMAIL_ALREADY_EXISTS);
		}

		try {

			Ebean.beginTransaction();
			Users newUser = new Users();
			newUser.setName(name);
			newUser.setEmail(email);
			newUser.setPhoneNo(phoneNo);
			newUser.setCreatedTime(new Date());
			newUser.setLastLogin(new Date());
			newUser.setEncryptedUserId(UUID.randomUUID().toString());
			newUser.setFacebookId(facebookId);
			newUser.save();
			newUser.refresh();

			UserSession session = UserSession.create(newUser, deviceToken, deviceId, deviceTypeId);

			Ebean.commitTransaction();

			resultNode.put(APIResponseKeys.USER_ID, newUser.getEncryptedUserId());
			resultNode.put(APIResponseKeys.TOKEN, session.getToken());

		} finally {
			if (Ebean.currentTransaction() != null) {
				Ebean.currentTransaction().rollback();
			}
		}
		return resultNode;
	}
}
