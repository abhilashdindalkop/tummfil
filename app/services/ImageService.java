package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ecommerce.models.sql.Vendors;
import com.ecommerce.models.sql.Products;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.AmazonS3Utils;
import utils.MyConstants;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.ImageResizeType;
import utils.MyException;
import utils.ImageUtilities;
import utils.MessageReaderFactory;

public class ImageService {

	public void uploadProductImage(MultipartFormData image, Products product)
			throws FileNotFoundException, IOException, MyException {

		FilePart imageFilePart = image.getFile(MyConstants.IMAGE_UPLOAD_KEY);
		if (imageFilePart != null) {
			File imageFile = imageFilePart.getFile();
			if (!imageFile.isFile()) {
				throw new MyException(FailureMessages.UNSUPPORTED_FILE_TYPE);
			}

			/* TO validate the uploaded file is an image */
			if (!AmazonS3Utils.isImageFile(imageFile)) {
				throw new MyException(FailureMessages.UNSUPPORTED_FILE_TYPE);
			}

			// if image exists delete it
			if (product.getImageUrl() != null) {
				ImageUtilities.deleteProductImageFromS3(product);
			}

			String filename = product.getId() + "_" + System.currentTimeMillis();

			String productImageConstantPath = MessageReaderFactory.getPropertyValue("PRODUCT_IMAGE_CONSTANT_PATH")
					+ product.getProductId() + "/" + filename;

			/* To upload the image to the S3 bucket */
			AmazonS3Utils.uploadStandardSizeFile(imageFile,
					productImageConstantPath + MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION"));

			// To upload thumbnail size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, productImageConstantPath, ImageResizeType.THUMBNAIL_SIZE);

			// To upload 800x800 size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, productImageConstantPath, ImageResizeType.ANDROID_SIZE);

			// To upload 400x400 size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, productImageConstantPath, ImageResizeType.WEB_SIZE);

			// update the DB that cover image is uploaded
			product.updateImageUrlStatus(filename);
		}
	}

	public void uploadVendorImage(MultipartFormData image, Vendors vendor)
			throws FileNotFoundException, IOException, MyException {

		FilePart imageFilePart = image.getFile(MyConstants.IMAGE_UPLOAD_KEY);
		if (imageFilePart != null) {
			File imageFile = imageFilePart.getFile();

			if (!imageFile.isFile()) {
				throw new MyException(FailureMessages.UNSUPPORTED_FILE_TYPE);
			}

			/* TO validate the uploaded file is an image */
			if (!AmazonS3Utils.isImageFile(imageFile)) {
				throw new MyException(FailureMessages.UNSUPPORTED_FILE_TYPE);
			}

			// if image exists delete it
			if (vendor.getImageUrl() != null) {
				ImageUtilities.deleteVendorImageFromS3(vendor);
			}

			String filename = vendor.getId() + "_" + System.currentTimeMillis();

			String filePath = MessageReaderFactory.getPropertyValue("VENDOR_IMAGE_CONSTANT_PATH")
					+ vendor.getEncryptedVendorId() + "/" + filename;

			/* To upload the image to the S3 bucket */
			AmazonS3Utils.uploadStandardSizeFile(imageFile,
					filePath + MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION"));

			// To upload thumbnail size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, filePath, ImageResizeType.THUMBNAIL_SIZE);

			// To upload 800x800 size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, filePath, ImageResizeType.ANDROID_SIZE);

			// To upload 400x400 size images to the S3
			AmazonS3Utils.uploadResizedFile(imageFile, filePath, ImageResizeType.WEB_SIZE);

			// update the DB that cover image is uploaded
			vendor.updateImageUrlStatus(filename);

		}
	}
}
