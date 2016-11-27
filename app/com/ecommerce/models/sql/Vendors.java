package com.ecommerce.models.sql;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.ecommerce.dao.VendorLocationDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.PasswordEncryptDecrypt;
import play.libs.Json;
import services.CreateResponseJson;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.VendorType;
import utils.MyException;

@Entity
public class Vendors extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String encryptedVendorId;

	private String name;

	private String vendorName;

	private int vendorType = VendorType.NORMAL;

	@Column(columnDefinition = "TEXT")
	private String vendorAddress;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Cities city;

	private double latitude;

	private double longitude;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(unique = true)
	private String phoneNo;

	@Column(unique = true)
	private String email;

	private String password;

	private String imageUrl;

	private Boolean isVendorAvailable = false;

	private Boolean isVendorVerified = false;

	private Boolean isDeleted = false;

	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date updatedTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date lastLogin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEncryptedVendorId() {
		return encryptedVendorId;
	}

	public void setEncryptedVendorId(String encryptedVendorId) {
		this.encryptedVendorId = encryptedVendorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsVendorVerified() {
		return isVendorVerified;
	}

	public void setIsVendorVerified(Boolean isVendorVerified) {
		this.isVendorVerified = isVendorVerified;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getVendorType() {
		return vendorType;
	}

	public void setVendorType(int vendorType) {
		this.vendorType = vendorType;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public Boolean getIsVendorAvailable() {
		return isVendorAvailable;
	}

	public void setIsVendorAvailable(Boolean isVendorAvailable) {
		this.isVendorAvailable = isVendorAvailable;
	}

	public static Query<Vendors> basicQuery() {
		Query<Vendors> query = Ebean.createQuery(Vendors.class);
		query.where().eq("is_deleted", false).eq("is_vendor_verified", true);
		return query;
	}

	public static Vendors findById(String encryptedVendorId) throws MyException {
		Vendors vendor = Ebean.find(Vendors.class).where().eq("encrypted_vendor_id", encryptedVendorId).findUnique();
		if (vendor == null) {
			throw new MyException(FailureMessages.VENDOR_DOESNT_EXIST);
		}
		return vendor;
	}

	public static Vendors findByEmail(String email) {
		Vendors vendor = Ebean.find(Vendors.class).where().eq("email", email).findUnique();
		return vendor;
	}

	public static Vendors findByPhoneNo(String phoneNo) {
		Vendors vendor = Ebean.find(Vendors.class).where().eq("phone_no", phoneNo).findUnique();
		return vendor;
	}

	public static Boolean isEmailExists(String email) {
		return Ebean.find(Vendors.class).where().eq("email", email).findUnique() != null;
	}

	public static Boolean isPhoneNoExists(String phoneNo) {
		return Ebean.find(Vendors.class).where().eq("phone_no", phoneNo).findUnique() != null;
	}

	public static Boolean checkPassword(Vendors vendor, String password) {
		if (PasswordEncryptDecrypt.isPasswordSame(password, vendor.getPassword())) {
			return true;
		}
		return false;
	}

	public static ObjectNode createVendorUsingPhoneNo(Vendors newVendor, String deviceToken, String deviceId,
			int deviceTypeId, String password, long cityId) throws NoSuchAlgorithmException, IOException, MyException {
		try {
			Ebean.beginTransaction();
			/* Create Vendor */
			Cities city = Cities.findById(cityId);
			newVendor.add(password, city);

			VendorLocationDAO vendorLocationDAO = new VendorLocationDAO();
			vendorLocationDAO.add(newVendor, 0d, 0d, 0d);
			/* Create Vendor Session */
			VendorSession session = VendorSession.create(newVendor, deviceToken, deviceId, deviceTypeId);
			Ebean.commitTransaction();

			ObjectNode resultNode = Json.newObject();
			resultNode.put(APIResponseKeys.VENDOR_ID, newVendor.getEncryptedVendorId());
			resultNode.put(APIResponseKeys.TOKEN, session.getToken());

			return resultNode;
		} finally {
			if (Ebean.currentTransaction() != null) {
				Ebean.currentTransaction().rollback();
			}
		}
	}

	public void add(String password, Cities city) {
		this.setCity(city);
		this.setPassword(PasswordEncryptDecrypt.generatePasswordHash(password));
		this.setEncryptedVendorId(UUID.randomUUID().toString());
		this.setCreatedTime(new Date());
		this.setLastLogin(new Date());
		this.save();
	}

	public void updateVendor() {
		this.setUpdatedTime(new Date());
		this.save();
	}

	public static ObjectNode getVendorFeeds(JsonNode filterNode, int page, int limit) throws MyException, IOException {
		/*
		 * Get All Vendors with image. Filters : searchText, city
		 */
		boolean isEmpty = false;

		ObjectNode resultNode = Json.newObject();
		Query<Vendors> vendorQuery = basicQuery();

		if (filterNode.has(APIRequestKeys.VENDOR_ID)) {
			String vendorId = filterNode.findValue(APIRequestKeys.VENDOR_ID).asText();
			vendorQuery.where().eq("encryptedVendorId", vendorId);
		}

		if (filterNode.has(APIRequestKeys.CITY_ID)) {
			int cityId = filterNode.findValue(APIRequestKeys.CITY_ID).asInt();
			vendorQuery.where().eq("city_id", cityId);
		}
		String searchText = null;
		if (filterNode.has(APIRequestKeys.SEARCH_TEXT)) {
			searchText = filterNode.findValue(APIRequestKeys.SEARCH_TEXT).asText();
			List<String> searchedVendorIds = searchVendorByText(searchText);
			if (!searchedVendorIds.isEmpty()) {
				vendorQuery.where().in("encrypted_vendor_id", searchedVendorIds);
			} else {
				isEmpty = true;
			}
		}

		if (filterNode.has(APIRequestKeys.LATITUDE) && filterNode.has(APIRequestKeys.LONGITUDE)) {
			double latitude = filterNode.findValue(APIRequestKeys.LATITUDE).asDouble();
			double longitude = filterNode.findValue(APIRequestKeys.LONGITUDE).asDouble();

			double distance = MyConstants.SHIPPING_DISTANCE;

			VendorLocationDAO vendorLocationDAO = new VendorLocationDAO();
			List<Long> vendorIdList = vendorLocationDAO.findNearbyVendorIds(latitude, longitude, distance, page, limit);
			if (!vendorIdList.isEmpty()) {
				vendorQuery.where().in("id", vendorIdList);
			} else {
				isEmpty = true;
			}
		}

		List<Vendors> vendorsList = new ArrayList<Vendors>();
		int totalCount = 0;
		if (!isEmpty) {
			totalCount = vendorQuery.findRowCount();
			vendorsList = vendorQuery.findPagedList(page, limit).getList();
		}

		resultNode.put(APIResponseKeys.TOTAL_COUNT, totalCount);
		resultNode.set(APIResponseKeys.VENDOR_LIST, Json.toJson(CreateResponseJson.getVendorsJsonList(vendorsList)));
		return resultNode;
	}

	public static List<String> searchVendorByText(String searchText) {

		searchText = searchText.replace(" ", "").toLowerCase();
		String searchVendorQuery = "SELECT encrypted_vendor_id FROM vendors WHERE is_deleted=false AND is_vendor_verified=true AND REPLACE(LOWER(vendor_name), ' ', '') LIKE '%"
				+ searchText + "%'";
		List<String> vendorIds = new ArrayList<String>();
		SqlQuery rawSqlQuery = Ebean.createSqlQuery(searchVendorQuery);
		List<SqlRow> rowList = rawSqlQuery.findList();
		for (SqlRow eventType : rowList) {
			vendorIds.add(eventType.getString("encrypted_vendor_id"));
		}
		return vendorIds;
	}

	public void updateImageUrlStatus(String imageUrl) throws MyException {
		this.setImageUrl(imageUrl);
		this.update();
		this.refresh();
	}

}
