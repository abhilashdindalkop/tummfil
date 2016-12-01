package controllers;

import javax.inject.Inject;

import com.ecommerce.dto.request.CreateTransactionRequestDTO;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.Transactions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.UserAuthenticator;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import services.CreateResponseJson;
import services.PaymentService;
import utils.CorsComposition;
import utils.MyConstants.APIResponseKeys;
import utils.MySuccessResponse;
import utils.ObjectMapperUtil;

public class PaymentController extends ParentController {

	PaymentService paymentService;

	@Inject
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	@Security.Authenticated(UserAuthenticator.class)
	public Result createTransaction() {
		try {
			JsonNode inputJson = request().body().asJson();

			ObjectMapper mapper = ObjectMapperUtil.getInstance();

			CreateTransactionRequestDTO requestDTO = mapper.convertValue(inputJson, CreateTransactionRequestDTO.class);

			Transactions transaction = paymentService.createTransaction(requestDTO);

			Orders myOrder = transaction.getOrder();
			ObjectNode resultNode = CreateResponseJson.constructTransactionResponse(transaction, myOrder);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

}
