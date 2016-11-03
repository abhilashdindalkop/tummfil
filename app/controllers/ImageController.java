package controllers;

import javax.inject.Inject;

import com.ecommerce.models.sql.VendorSession;
import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Products;
import com.fasterxml.jackson.databind.node.ObjectNode;

import authentication.VendorAuthenticator;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.mvc.Security;
import services.ImageService;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.ImageResizeType;
import utils.MyException;
import utils.MySuccessResponse;
import utils.ImageUtilities;

public class ImageController extends ParentController {

	ImageService imageService;

	@Inject
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@BodyParser.Of(BodyParser.MultipartFormData.class)
	@Security.Authenticated(VendorAuthenticator.class)
	public Result uploadImage() {
		try {

			MultipartFormData multipartFormData = request().body().asMultipartFormData();

			String standard = null;
			String android = null;
			String web = null;
			String thumbnail = null;
			String vendorId = null;
			String productId = null;

			String[] vendorid = multipartFormData.asFormUrlEncoded().get("vendorId");
			String[] prodid = multipartFormData.asFormUrlEncoded().get("productId");
			if (prodid != null) {
				productId = prodid[0];
			}
			if (vendorid != null) {
				vendorId = vendorid[0];
			}
			if (productId == null && vendorId == null) {
				throw new MyException(FailureMessages.PRODUCT_VENDOR_ID_NOT_FOUND);
			}

			String curVendorEncryptedId = VendorSession.getVendorEncryptedIdByContext();

			if (productId != null) {
				Products product = Products.findById(productId);
				if (!product.getVendor().getEncryptedVendorId().equals(curVendorEncryptedId)) {
					throw new MyException(FailureMessages.INVALID_ACCESS);
				}
				imageService.uploadProductImage(multipartFormData, product);

				standard = ImageUtilities.getProductImageUrl(productId, product.getImageUrl(),
						ImageResizeType.STANDARD);
				thumbnail = ImageUtilities.getProductImageUrl(productId, product.getImageUrl(),
						ImageResizeType.THUMBNAIL_SIZE);
				android = ImageUtilities.getProductImageUrl(productId, product.getImageUrl(),
						ImageResizeType.ANDROID_SIZE);
				web = ImageUtilities.getProductImageUrl(productId, product.getImageUrl(), ImageResizeType.WEB_SIZE);

			} else if (vendorId != null) {
				Vendors vendor = Vendors.findById(vendorId);
				if (!vendor.getEncryptedVendorId().equals(curVendorEncryptedId)) {
					throw new MyException(FailureMessages.INVALID_ACCESS);
				}
				// upload vendor image
				imageService.uploadVendorImage(multipartFormData, vendor);

				standard = ImageUtilities.getVendorImageUrl(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
						ImageResizeType.STANDARD);
				thumbnail = ImageUtilities.getVendorImageUrl(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
						ImageResizeType.THUMBNAIL_SIZE);
				android = ImageUtilities.getVendorImageUrl(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
						ImageResizeType.ANDROID_SIZE);
				web = ImageUtilities.getVendorImageUrl(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
						ImageResizeType.WEB_SIZE);

			}
			ObjectNode resultNode = Json.newObject();
			resultNode.put(APIResponseKeys.STANDARD_IMAGE_URL, standard);
			resultNode.put(APIResponseKeys.THUMBNAIL_IMAGE_URL, thumbnail);
			resultNode.put(APIResponseKeys.ANDROID_IMAGE_URL, android);
			resultNode.put(APIResponseKeys.WEB_IMAGE_URL, web);

			response = new MySuccessResponse(resultNode);
		} catch (Exception e) {
			response = createFailureResponse(e);
		}
		return response.getResult();
	}
}
