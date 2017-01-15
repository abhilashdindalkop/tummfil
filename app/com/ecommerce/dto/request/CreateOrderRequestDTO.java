package com.ecommerce.dto.request;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

public class CreateOrderRequestDTO {

	@Required
	public String name;

	public Integer cityId;

	public Integer orderType;

	@Required
	public String address;

	public Long pincode;

	@Required
	public String phoneNo;

	public String description;

	public Double latitude;

	public Double longitude;

	public List<OrderedProductsRequestDTO> orderedProducts = new ArrayList<OrderedProductsRequestDTO>();

}
