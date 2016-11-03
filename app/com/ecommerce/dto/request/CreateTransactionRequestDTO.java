package com.ecommerce.dto.request;

import play.data.validation.Constraints.Required;

public class CreateTransactionRequestDTO {

	@Required
	public String orderId;

	@Required
	public int paymentType;

}
