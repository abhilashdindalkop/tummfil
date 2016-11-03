package com.ecommerce.dto.request;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

public class CreateOrderRequestDTO {

	@Required
	public String name;

	@Required
	public int cityId;

	public int orderType;

	@Required
	public String address;

	@Required
	public int pincode;

	@Required
	public String phoneNo;

	public String description;

	public double latitude;

	public double longitude;

	public List<OrderedProductsRequestDTO> orderedProducts = new ArrayList<OrderedProductsRequestDTO>();

}
