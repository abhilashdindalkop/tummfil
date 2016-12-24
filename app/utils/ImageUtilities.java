package utils;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.ecommerce.models.sql.Category;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.Vendors;

import utils.MyConstants.ImageResizeType;
import utils.MyConstants.NetworkConstants;

public class ImageUtilities {

	/* This method is used to retrieve the images associated to a user */

	/* To construct the image url */
	public static String getProductImageUrl(String productId, String prodImageUrl, int imageSizeType)
			throws IOException {
		if (prodImageUrl == null) {
			return null;
		}

		String imageUrl = NetworkConstants.HOST_URL_HTTP + MessageReaderFactory.getPropertyValue("S3_DOMAIN_NAME") + "/"
				+ MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME") + "/";
		switch (imageSizeType) {
		case ImageResizeType.STANDARD:

			imageUrl = imageUrl + getProductImageFilePath(productId, prodImageUrl, ImageResizeType.STANDARD);
			break;
		case ImageResizeType.THUMBNAIL_SIZE:

			imageUrl = imageUrl + getProductImageFilePath(productId, prodImageUrl, ImageResizeType.THUMBNAIL_SIZE);
			break;
		case ImageResizeType.ANDROID_SIZE:
			imageUrl = imageUrl + getProductImageFilePath(productId, prodImageUrl, ImageResizeType.ANDROID_SIZE);
			break;
		case ImageResizeType.WEB_SIZE:
			imageUrl = imageUrl + getProductImageFilePath(productId, prodImageUrl, ImageResizeType.WEB_SIZE);
			break;
		}
		return imageUrl;
	}

	/* To delete an object from S3 bucket */
	public static void deleteProductImageFromS3(Products product)
			throws IOException, AmazonClientException, AmazonServiceException {

		// Delete standard size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(product.getProductId(), product.getImageUrl(), ImageResizeType.STANDARD));

		// Delete thumbnail size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(product.getProductId(), product.getImageUrl(), ImageResizeType.THUMBNAIL_SIZE));

		// Delete android size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(product.getProductId(), product.getImageUrl(), ImageResizeType.ANDROID_SIZE));

		// Delete web size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(product.getProductId(), product.getImageUrl(), ImageResizeType.WEB_SIZE));

	}

	public static String getProductImageFilePath(String productId, String prodImageUrl, int imageSizeType)
			throws IOException {

		String imageUrl = MessageReaderFactory.getPropertyValue("PRODUCT_IMAGE_CONSTANT_PATH") + productId + "/"
				+ prodImageUrl;
		switch (imageSizeType) {
		case ImageResizeType.STANDARD:

			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.THUMBNAIL_SIZE:

			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("THUMB_NAIL_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.ANDROID_SIZE:
			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("ANDROID_RESIZED_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.WEB_SIZE:
			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("WEB_RESIZED_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		}
		return imageUrl;
	}

	/* To delete an object from S3 bucket */
	public static void deleteVendorImageFromS3(Vendors vendor)
			throws IOException, AmazonClientException, AmazonServiceException {

		// Delete standard size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(vendor.getEncryptedVendorId(), vendor.getImageUrl(), ImageResizeType.STANDARD));

		// Delete thumbnail size image
		AmazonS3Utils.deleteFile(getProductImageFilePath(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
				ImageResizeType.THUMBNAIL_SIZE));

		// Delete android size image
		AmazonS3Utils.deleteFile(getProductImageFilePath(vendor.getEncryptedVendorId(), vendor.getImageUrl(),
				ImageResizeType.ANDROID_SIZE));

		// Delete web size image
		AmazonS3Utils.deleteFile(
				getProductImageFilePath(vendor.getEncryptedVendorId(), vendor.getImageUrl(), ImageResizeType.WEB_SIZE));

	}

	public static String getVendorImageFilePath(String vendorId, String vendorImageUrl, int imageSizeType)
			throws IOException {

		String imageUrl = MessageReaderFactory.getPropertyValue("VENDOR_IMAGE_CONSTANT_PATH") + vendorId + "/"
				+ vendorImageUrl;
		switch (imageSizeType) {
		case ImageResizeType.STANDARD:

			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.THUMBNAIL_SIZE:

			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("THUMB_NAIL_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.ANDROID_SIZE:
			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("ANDROID_RESIZED_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		case ImageResizeType.WEB_SIZE:
			imageUrl = imageUrl + MessageReaderFactory.getPropertyValue("WEB_RESIZED_IMAGE")
					+ MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION");
			break;
		}

		return imageUrl;
	}

	/* To construct the image url */
	public static String getVendorImageUrl(String vendorId, String vendorImageUrl, int imageSizeType)
			throws IOException {
		if (vendorImageUrl == null) {
			return null;
		}

		String imageUrl = NetworkConstants.HOST_URL_HTTP + MessageReaderFactory.getPropertyValue("S3_DOMAIN_NAME") + "/"
				+ MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME") + "/";

		switch (imageSizeType) {
		case ImageResizeType.STANDARD:

			imageUrl = imageUrl + getVendorImageFilePath(vendorId, vendorImageUrl, ImageResizeType.STANDARD);
			break;
		case ImageResizeType.THUMBNAIL_SIZE:

			imageUrl = imageUrl + getVendorImageFilePath(vendorId, vendorImageUrl, ImageResizeType.THUMBNAIL_SIZE);
			break;
		case ImageResizeType.ANDROID_SIZE:
			imageUrl = imageUrl + getVendorImageFilePath(vendorId, vendorImageUrl, ImageResizeType.ANDROID_SIZE);
			break;
		case ImageResizeType.WEB_SIZE:
			imageUrl = imageUrl + getVendorImageFilePath(vendorId, vendorImageUrl, ImageResizeType.WEB_SIZE);
			break;
		}
		return imageUrl;
	}

	/* Construct Category Image url */
	public static String constructCategoryImageUrl(Category category) {
		String imageUrl = MyConstants.CATEGORY_IMAGE_BASE_PATH + category.getType() + ".svg";
		return imageUrl;

	}

}
