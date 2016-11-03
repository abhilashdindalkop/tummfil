package com.ecommerce.dto.request;

import play.data.validation.Constraints.Required;

public class OrderedProductsRequestDTO {

	@Required
	public String productId;

	@Required
	public int quantity;

}
