package controllers.cms;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.ecommerce.models.sql.Category;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.VendorAuthenticator;
import controllers.ParentController;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import services.ImageService;
import services.cms.VendorCmsService;
import utils.CorsComposition;
import utils.ImageUtilities;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.ImageResizeType;
import utils.MyConstants.SuccessMessages;
import utils.MyException;
import utils.MyFailureResponse;
import utils.MySuccessResponse;

public class VendorCmsController extends ParentController {

	VendorCmsService vendorService;
	ImageService imageService;

	@Inject
	public VendorCmsController(VendorCmsService vendorService, ImageService imageService) {
		this.vendorService = vendorService;
		this.imageService = imageService;
	}

	boolean isAdmin() {
		return request().getHeader(APIRequestKeys.TOKEN).equals(MyConstants.CMS_KEY);
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

	@BodyParser.Of(BodyParser.MultipartFormData.class)
	public Result addCategory() {
		try {

			if (!isAdmin()) {
				throw new MyException(FailureMessages.INVALID_ACCESS);
			}

			MultipartFormData multipartFormData = request().body().asMultipartFormData();

			String imageUrl = null;
			String categoryName = null;
			Long categoryId = null;

			String[] catName = multipartFormData.asFormUrlEncoded().get("categoryName");
			String[] catId = multipartFormData.asFormUrlEncoded().get("categoryId");
			if (catName != null) {
				categoryName = catName[0];
			}
			if (catId != null) {
				categoryId = Long.parseLong(catId[0]);
			}
			if (categoryName == null && categoryId == null) {
				throw new MyException(FailureMessages.CATEGORY_NAME_NOT_FOUND);
			}

			Category category = null;
			if (categoryId != null) {
				category = Category.findById(categoryId);
				imageService.uploadCategoryImage(multipartFormData, category.getType());
				imageUrl = ImageUtilities.getCategoryImageUrl(category.getType());

			} else if (categoryName != null) {
				imageService.uploadCategoryImage(multipartFormData, categoryName);
				imageUrl = ImageUtilities.getCategoryImageUrl(categoryName);
				category = Category.add(categoryName, imageUrl);
			}

			ObjectNode resultNode = Json.newObject();
			resultNode.set("category", Json.toJson(category));

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}
}
