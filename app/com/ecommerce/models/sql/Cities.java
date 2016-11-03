package com.ecommerce.models.sql;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.MyConstants.FailureMessages;
import utils.MyException;

@Entity
public class Cities extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	private String city;

	private String state;

	private String country;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonIgnore
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public static Cities findById(long id) throws MyException {
		Cities city = Ebean.find(Cities.class).where().eq("id", id).findUnique();
		if (city == null) {
			throw new MyException(FailureMessages.CITY_DOESNT_EXIST);
		}
		return city;
	}

	public static List<Cities> findAllCities() {
		List<Cities> citiesList = Ebean.find(Cities.class).findList();
		return citiesList;
	}

	public static boolean isCitiesUpdated(Date updatedTime) {
		List<Cities> citiesList = Ebean.find(Cities.class).where().ge("createdTime", updatedTime).findList();
		return !citiesList.isEmpty();
	}
}
