package com.ecommerce.models.sql;

import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.MyConstants.FailureMessages;
import utils.MyException;

@Entity
public class UserAddress extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@JsonIgnore
	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Users user;

	private String address;

	private double latitude;

	private double longitude;

	private String landmark;

	private long pincode;

	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Cities city;

	private int addressType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getPincode() {
		return pincode;
	}

	public void setPincode(long pincode) {
		this.pincode = pincode;
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

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public int getAddressType() {
		return addressType;
	}

	public void setAddressType(int addressType) {
		this.addressType = addressType;
	}

	public void deleteAddress() throws MyException {
		this.delete();
	}

	public void add(Users user, Cities city) throws MyException {
		this.setCity(city);
		this.setUser(user);
		this.save();
	}

	public static List<UserAddress> findByUserId(Users user) throws MyException {
		List<UserAddress> addressList = Ebean.find(UserAddress.class).where().eq("user", user).findList();
		return addressList;
	}

	public static UserAddress findByUserIdAndAddressType(Users user, int addressType) throws MyException {
		UserAddress address = Ebean.find(UserAddress.class).where().eq("user", user).eq("address_type", addressType)
				.findUnique();
		return address;
	}

	public static UserAddress findById(long id) throws MyException {
		UserAddress address = Ebean.find(UserAddress.class).where().eq("id", id).findUnique();
		if (address == null) {
			throw new MyException(FailureMessages.INVALID_ADDRESS_ID);
		}
		return address;
	}

	public static void deleteById(long id) throws MyException {
		UserAddress address = findById(id);
		Ebean.delete(address);
	}
}
