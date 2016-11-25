package com.ecommerce.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecommerce.models.sql.Cities;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class createOrderResponseDTO {

	public String orderId;

	public Integer orderStatus;

	public List<ObjectNode> productList = new ArrayList<ObjectNode>();

	public Double extraFee;
	
	public Double totalPrice;

	public String name;

	public Cities city;

	public Integer orderType;

	public String paymentType;

	public Integer paymentStatus;

	public String address;

	public Integer pincode;

	public String phoneNo;

	public String description;

	public Double latitude;

	public Double longitude;

	public Date deliveryTime;

	public Date createdTime;

}
