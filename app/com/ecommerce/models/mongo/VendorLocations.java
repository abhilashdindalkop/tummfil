package com.ecommerce.models.mongo;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Id;

public class VendorLocations {

	@Id
	private String id;

	private Long vendorId;

	private List<Double> location;

	private double shippingDistance;

	private Date createdTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public List<Double> getLocation() {
		return location;
	}

	public void setLocation(List<Double> location) {
		this.location = location;
	}

	public double getShippingDistance() {
		return shippingDistance;
	}

	public void setShippingDistance(double shippingDistance) {
		this.shippingDistance = shippingDistance;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
