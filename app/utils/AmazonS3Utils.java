package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import play.Logger;
import play.Play;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.ImageResizeType;

public class AmazonS3Utils {
	private static File amazonS3CredentialFile = null;
	private static AmazonS3 s3Client = null;

	private static final String SUFFIX = "/";
	private static final float WEB_MAX_WIDTH = 320;
	private static final float WEB_MAX_HEIGHT = 150;
	private static final float ANDROID_MAX_WIDTH = 800;
	private static final float ANDROID_MAX_HEIGHT = 800;
	private static final float THUMBNAIL_SIZE_MAX_WIDTH = 200;
	private static final float THUMBNAIL_SIZE_MAX_HEIGHT = 200;
	private final static String JPG_TYPE = "jpg";
	private final static String JPG_MIME = "image/jpeg";
	private final static String CSV_TYPE = "text/csv";

	static {
		try {
			amazonS3CredentialFile = new File(
					Play.application().path().getAbsolutePath() + "/conf/resources/devS3Credentials.properties");

			s3Client = new AmazonS3Client(new PropertiesCredentials(amazonS3CredentialFile));
		}

		catch (Exception e) {
			Logger.info(e.getMessage());
		}
	}

	public static void uploadStandardSizeFile(File file, String filePath) throws MyException, IOException {

		if (file != null) {
			InputStream inputStream = new FileInputStream(file);
			try {
				String bucketName = MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME").trim();
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(JPG_TYPE);
				Logger.info(filePath);

				PutObjectRequest putObjectUploadRequest = new PutObjectRequest(bucketName, filePath, inputStream,
						metadata);
				PutObjectResult result = s3Client.putObject(putObjectUploadRequest);

				Logger.info(result.getETag());

			} catch (Exception e) {
				Logger.info("s3 error log : " + e);
				throw new MyException(FailureMessages.ERROR_WHILE_UPLOADING_IMAGE);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}

	public static void uploadCSVFile(File file, String filePath) throws IOException, MyException {

		if (file != null) {
			InputStream inputStream = new FileInputStream(file);
			try {
				String bucketName = MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME").trim();
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(CSV_TYPE);
				metadata.setContentDisposition("attachment;filename=" + file.getName());

				PutObjectRequest putObjectUploadRequest = new PutObjectRequest(bucketName, filePath, inputStream,
						metadata);
				PutObjectResult result = s3Client.putObject(putObjectUploadRequest);

				Logger.info(result.getETag());

			} catch (Exception e) {
				throw new MyException(FailureMessages.ERROR_WHILE_UPLOADING_IMAGE);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}

	public static void deleteFile(String filePath) throws IOException, AmazonClientException, AmazonServiceException {
		String bucketName = MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME").trim();

		try {
			s3Client.deleteObject(bucketName, filePath);
		} catch (AmazonClientException e) {
			Logger.info(e.getMessage());
		}
	}

	public static boolean isImageFile(File file) throws IOException {
		Boolean isValidFormat = false;
		String mimetype = Files.probeContentType(file.toPath());
		String type = mimetype.split(SUFFIX)[0];
		if (type.equals("image")) {
			isValidFormat = true;
		}
		return isValidFormat;
	}

	public static void uploadResizedFile(File file, String filePath, int resizeType)
			throws AmazonClientException, AmazonServiceException, IOException, MyException {
		InputStream objectData = new FileInputStream(file);
		InputStream inputStream = null;
		ByteArrayOutputStream os = null;
		try {
			float maxWidth = 0;
			float maxHeight = 0;
			switch (resizeType) {
			case ImageResizeType.THUMBNAIL_SIZE:
				maxWidth = THUMBNAIL_SIZE_MAX_WIDTH;
				maxHeight = THUMBNAIL_SIZE_MAX_HEIGHT;
				filePath = filePath.concat(MessageReaderFactory.getPropertyValue("THUMB_NAIL_IMAGE"))
						.concat(MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION"));
				break;
			case ImageResizeType.ANDROID_SIZE:
				maxWidth = ANDROID_MAX_WIDTH;
				maxHeight = ANDROID_MAX_HEIGHT;
				filePath = filePath.concat(MessageReaderFactory.getPropertyValue("ANDROID_RESIZED_IMAGE")
						.concat(MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION")));
				break;
			case ImageResizeType.WEB_SIZE:
				maxWidth = WEB_MAX_WIDTH;
				maxHeight = WEB_MAX_HEIGHT;
				filePath = filePath.concat(MessageReaderFactory.getPropertyValue("WEB_RESIZED_IMAGE"))
						.concat(MessageReaderFactory.getPropertyValue("IMAGE_EXTENSION"));
				break;
			}

			String bucketName = MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME").trim();

			// Read the source image
			BufferedImage srcImage = ImageIO.read(objectData);
			int srcHeight = srcImage.getHeight();
			int srcWidth = srcImage.getWidth();

			// Infer the scaling factor to avoid stretching the image
			// unnaturally
			float scalingFactor = Math.min(maxWidth / srcWidth, maxHeight / srcHeight);
			int width = (int) (scalingFactor * srcWidth);
			int height = (int) (scalingFactor * srcHeight);

			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();

			// Fill with white before applying semi-transparent (alpha) images
			g.setPaint(Color.white);
			g.fillRect(0, 0, width, height);

			// Simple bilinear resize
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImage, 0, 0, width, height, null);
			g.dispose();

			// Re-encode image to target format
			os = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, JPG_TYPE, os);
			inputStream = new ByteArrayInputStream(os.toByteArray());

			// Set Content-Length and Content-Type
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(os.size());
			metadata.setContentType(JPG_MIME);

			// Uploading to S3 destination bucket
			PutObjectResult result = s3Client.putObject(bucketName, filePath, inputStream, metadata);
			Logger.info(result.getETag());

		} catch (Exception e) {
			throw new MyException(FailureMessages.ERROR_WHILE_UPLOADING_IMAGE);
		} finally {
			if (objectData != null) {
				objectData.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

	// public static String constructCsvFilePathUrl(String csvFileName, String
	// exportTypePath) throws IOException {
	//
	// return NetworkConstants.HOST_URL_HTTPS +
	// MessageReaderFactory.getPropertyValue("DOMAIN_NAME")
	// .concat(MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME")).concat(SUFFIX).concat(exportTypePath)
	// .concat(csvFileName);
	// }

	static void deleteObjectsInFolder(String folderPath) throws IOException {
		String bucketName = MessageReaderFactory.getPropertyValue("S3_BUCKET_NAME").trim();

		for (S3ObjectSummary file : s3Client.listObjects(bucketName, folderPath).getObjectSummaries()) {
			s3Client.deleteObject(bucketName, file.getKey());
		}
	}

}
