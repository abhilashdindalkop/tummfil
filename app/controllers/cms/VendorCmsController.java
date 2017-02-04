package controllers.cms;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.ParentController;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.cms.VendorCmsService;
import utils.CorsComposition;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.SuccessMessages;
import utils.MyException;
import utils.MyFailureResponse;
import utils.MySuccessResponse;

public class VendorCmsController extends ParentController {

	VendorCmsService vendorService;

	@Inject
	public VendorCmsController(VendorCmsService vendorService) {
		this.vendorService = vendorService;
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateVendor() {
		/*
		 * isAvailable, isVerified, vendorType, vendorName, description,
		 * password, shippingDistance, name, cityId
		 */
		try {
			if (!isAdmin()) {
				throw new MyException(FailureMessages.INVALID_ACCESS);
			}

			JsonNode inputJson = request().body().asJson();
			vendorService.updateVendor(inputJson);
			response = new MySuccessResponse(SuccessMessages.VENDOR_UPDATE_SUCCESS);
		} catch (PersistenceException e) {
			response = new MyFailureResponse(FailureMessages.UNIQUE_DEVICE_ID_TOKEN);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	@BodyParser.Of(BodyParser.Json.class)
	public Result getVendorList() {
		/*
		 * page, limit, filters: {searchText, isAvailable, isVerified,
		 * isDeleted, vendorType}
		 */
		try {
			if (!isAdmin()) {
				throw new MyException(FailureMessages.INVALID_ACCESS);
			}

			JsonNode inputJson = request().body().asJson();
			ObjectNode resultNode = vendorService.getVendorsCmsList(inputJson);
			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	@CorsComposition.Cors
	public Result getVendorDetails(String vendorId) {
		try {
			if (!isAdmin()) {
				throw new MyException(FailureMessages.INVALID_ACCESS);
			}
			ObjectNode vendorNode = vendorService.getVendorDetails(vendorId);

			response = new MySuccessResponse(vendorNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}

	boolean isAdmin() {
		if (request().getHeader(APIRequestKeys.TOKEN).equals(MyConstants.CMS_KEY)) {
			return true;
		}
		return false;
	}

}
