package com.ecommerce.models.sql;

import java.util.Date;
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
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.MyConstants.FailureMessages;
import utils.MyException;

@Entity
public class Locality extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	private String address;

	private double latitude;

	private double longitude;

	private int pincode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Cities city;

	@JsonIgnore
	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public static Locality findById(Long locId) throws MyException {
		Locality loc = Ebean.find(Locality.class).where().eq("id", locId).findUnique();
		if (loc == null) {
			throw new MyException(FailureMessages.LOCALITY_DOESNT_EXIST);
		}
		return loc;
	}

	public static List<SqlRow> searchByText(String searchText, long cityId) {

		searchText = searchText.replace(" ", "").toLowerCase();
		String searchTagQuery;
		SqlQuery rawSqlQuery;

		if (cityId == 0) {
			/* Search without city */
			searchTagQuery = "SELECT id FROM locality WHERE REPLACE(LOWER(address), ' ', '') LIKE '%" + searchText
					+ "%'";
			rawSqlQuery = Ebean.createSqlQuery(searchTagQuery);
		} else {
			/* Search with city */
			searchTagQuery = "SELECT id FROM locality WHERE city_id =:cityId AND REPLACE(LOWER(address), ' ', '') LIKE '%"
					+ searchText + "%'";
			rawSqlQuery = Ebean.createSqlQuery(searchTagQuery).setParameter("cityId", cityId);
		}

		List<SqlRow> rowList = rawSqlQuery.findList();
		return rowList;
	}

}
