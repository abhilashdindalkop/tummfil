package com.ecommerce.models.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import services.CreateResponseJson;
import utils.MyConstants;
import utils.MyConstants.APIRequestKeys;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.ProductStatus;
import utils.MyException;

@Entity
public class Products extends Model {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String productId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Vendors vendor;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	private String imageUrl = null;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Category category;

	private int status;

	private int productType = 0;

	private double units = 1;

	private double price;

	private Boolean isDeleted = false;

	private Boolean isFeatured = false;

	@JsonIgnore
	@Column(columnDefinition = "TIMESTAMP")
	private Date updatedTime;

	@JsonIgnore
	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Vendors getVendor() {
		return vendor;
	}

	public void setVendor(Vendors vendor) {
		this.vendor = vendor;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public double getUnits() {
		return units;
	}

	public void setUnits(double units) {
		this.units = units;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static Query<Products> basicQueryVendor() throws MyException {
		Query<Products> query = Ebean.createQuery(Products.class);
		query.where().eq("isDeleted", false);
		return query;
	}

	public static Query<Products> basicQuery() throws MyException {
		Query<Products> query = Ebean.createQuery(Products.class);
		query.where().eq("isDeleted", false).eq("status", ProductStatus.AVAILABLE);
		return query;
	}

	public static List<Category> getVendorCategories(Vendors vendor) throws MyException {
		Set<Products> listProducts = basicQueryVendor().where().eq("vendor", vendor).select("category")
				.setDistinct(true).findSet();
		List<Category> categoryList = new ArrayList<Category>();
		for (Products product : listProducts) {
			categoryList.add(product.getCategory());
		}
		return categoryList;
	}

	public void add(Vendors vendor, Category category) throws MyException, IOException {
		this.setProductId(UUID.randomUUID().toString());
		this.setStatus(ProductStatus.AVAILABLE);
		this.setVendor(vendor);
		this.setCategory(category);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.save();
	}

	public static Products findById(String productId) throws MyException {
		Products product = Ebean.find(Products.class).where().eq("product_id", productId).findUnique();
		if (product == null) {
			throw new MyException(FailureMessages.PRODUCT_DOESNT_EXIST);
		}
		return product;
	}

	public static void updateProduct(JsonNode inputJson, Products product) throws MyException, IOException {
		if (inputJson.has(APIRequestKeys.PRODUCT_NAME)) {
			product.setName(inputJson.findValue(APIRequestKeys.PRODUCT_NAME).asText());
		}
		if (inputJson.has(APIRequestKeys.DESCRIPTION)) {
			product.setDescription(inputJson.findValue(APIRequestKeys.DESCRIPTION).asText());
		}
		if (inputJson.has(APIRequestKeys.PRICE)) {
			product.setPrice(inputJson.findValue(APIRequestKeys.PRICE).asDouble());
		}
		if (inputJson.has(APIRequestKeys.CATEGORY_ID)) {
			Category category = Category.findById(inputJson.findValue(APIRequestKeys.CATEGORY_ID).asInt());
			product.setCategory(category);
		}
		if (inputJson.has(APIRequestKeys.STATUS)) {
			int status = inputJson.findValue(APIRequestKeys.STATUS).asInt();
			if (!MyConstants.productStatusList.contains(status)) {
				throw new MyException(FailureMessages.INVALID_PRODUCT_STATUS);
			}
			product.setStatus(status);
		}
		if (inputJson.has(APIRequestKeys.UNITS)) {
			product.setUnits(inputJson.findValue(APIRequestKeys.UNITS).asDouble());
		}

		if (inputJson.has(APIRequestKeys.PRODUCT_TYPE)) {
			int productType = inputJson.findValue(APIRequestKeys.PRODUCT_TYPE).asInt();
			if (!MyConstants.productTypeList.contains(productType)) {
				throw new MyException(FailureMessages.INVALID_PRODUCT_TYPE);
			}
			product.setProductType(productType);
		}
		product.setUpdatedTime(new Date());
		product.update();
	}

	public static void deleteProduct(Products product) throws MyException, IOException {
		// TODO check if any active orders with this product
		// Do not allow to delete

		if (product.getIsDeleted()) {
			throw new MyException(FailureMessages.PRODUCT_ALREADY_DELETED);
		}

		product.setIsDeleted(true);
		product.update();
	}

	public static void setAvailability(List<String> productIds, boolean isAvailable) throws MyException, IOException {
		int status;
		if (isAvailable) {
			status = ProductStatus.AVAILABLE;
		} else {
			status = ProductStatus.UNAVAILABLE;
		}
		String ids = StringUtils.join(productIds, "\",\"");
		String sql = "update products set status=:status where product_id IN (\"" + ids + "\")";
		SqlUpdate update = Ebean.createSqlUpdate(sql).setParameter("status", status);
		if (update.execute() == 0) {
			throw new MyException(FailureMessages.UPDATE_AVAILABILITY_FAILED);
		}
	}

	public static Query<Products> findVendorProductsQuery(Vendors vendor, Category category) throws MyException {
		Query<Products> query = basicQueryVendor();
		query.where().eq("vendor", vendor);
		if (category != null) {
			query.where().eq("category", category);
		}
		query.order().desc("created_time");
		return query;
	}

	public static ObjectNode findProductsList(JsonNode filterNode, int page, int limit)
			throws MyException, IOException {

		List<HashMap<String, Object>> emptyProductList = new ArrayList<HashMap<String, Object>>();
		ObjectNode resultNode = Json.newObject();
		// Filters searchText, product type, price, category
		Query<Products> productQuery = basicQuery();

		// TODO remove the below comment after image upload
		// productQuery.where().ne("image_url", null);

		boolean isVendor = false;
		if (filterNode.has(APIRequestKeys.VENDOR_ID)) {
			isVendor = true;
			String vendorId = filterNode.findValue(APIRequestKeys.VENDOR_ID).asText();
			Vendors vendor = Vendors.findById(vendorId);
			productQuery.where().eq("vendor", vendor);

			resultNode.set(APIResponseKeys.VENDOR_DETAILS, CreateResponseJson.constructVendorResponseJson(vendor));
		}

		if (filterNode.has(APIRequestKeys.IS_FEATURED)) {
			boolean isFeatured = filterNode.findValue(APIRequestKeys.IS_FEATURED).asBoolean();
			if (isFeatured) {
				productQuery.where().eq("is_featured", isFeatured);
			}
		}

		if (filterNode.has(APIRequestKeys.CATEGORY_ID)) {
			int categoryId = filterNode.findValue(APIRequestKeys.CATEGORY_ID).asInt();
			Category category = Category.findById(categoryId);
			productQuery.where().eq("category", category);
		}

		if (filterNode.has(APIRequestKeys.PRODUCT_TYPE)) {
			int productType = filterNode.findValue(APIRequestKeys.PRODUCT_TYPE).asInt();
			productQuery.where().eq("product_type", productType);
		}

		if (filterNode.has(APIRequestKeys.MIN_PRICE)) {
			float minPrice = filterNode.findValue(APIRequestKeys.MIN_PRICE).floatValue();
			productQuery.where().ge("price", minPrice);
		}
		if (filterNode.has(APIRequestKeys.MAX_PRICE)) {
			float maxPrice = filterNode.findValue(APIRequestKeys.MAX_PRICE).floatValue();
			productQuery.where().le("price", maxPrice);
		}
		String searchText = null;
		if (filterNode.has(APIRequestKeys.SEARCH_TEXT)) {
			searchText = filterNode.findValue(APIRequestKeys.SEARCH_TEXT).asText();
			List<SqlRow> searchedProductList = searchProductsByTextQuery(searchText);
			List<String> searchedProductIds = getProductIdsList(searchedProductList);
			if (!searchedProductIds.isEmpty()) {
				productQuery.where().in("product_id", searchedProductIds);
			} else {
				resultNode.set(APIResponseKeys.PRODUCTS, Json.toJson(emptyProductList));
				resultNode.put(APIResponseKeys.TOTAL_COUNT, 0);
				return resultNode;
			}
		}
		long totalCount = productQuery.findRowCount();
		productQuery.order().asc("category_id");
		List<Products> products = new ArrayList<Products>();

		if (isVendor) {
			products = productQuery.findList();
		} else {
			products = productQuery.findPagedList(page, limit).getList();
		}

		resultNode.set(APIResponseKeys.PRODUCTS, CreateResponseJson.getProductsFeedListJson(products));
		resultNode.put(APIResponseKeys.TOTAL_COUNT, totalCount);
		return resultNode;
	}

	public static List<SqlRow> searchMyProductsByTextQuery(Vendors vendor, String searchText) {
		searchText = searchText.replace(" ", "").toLowerCase();
		String searchProductQuery = "SELECT product_id, name, description, category_id, "
				+ "status, product_type, units, price, unit_type FROM "
				+ "products WHERE vendor_id = :vendorId AND is_deleted = false AND REPLACE(LOWER(name), ' ', '') LIKE '%"
				+ searchText + "%'";

		SqlQuery rawSqlQuery = Ebean.createSqlQuery(searchProductQuery).setParameter("vendorId", vendor.getId());

		return rawSqlQuery.findList();
	}

	/*****************************************************
	 * CUSTOMER METHODS
	 * 
	 * @throws IOException
	 *****************************************************
	 */
	public static List<HashMap<String, Object>> searchProductsByText(String searchText) throws IOException {
		List<SqlRow> productList = searchProductsByTextQuery(searchText);
		return CreateResponseJson.getProductsListJsonRaw(productList);
	}

	public static List<String> getProductIdsList(List<SqlRow> productList) {
		List<String> productIds = new ArrayList<String>();
		for (SqlRow product : productList) {
			productIds.add(product.getString("product_id"));
		}
		return productIds;
	}

	public static List<SqlRow> searchProductsByTextQuery(String searchText) {
		searchText = searchText.replace(" ", "").toLowerCase();
		String searchProductQuery = "SELECT product_id, name, description, category_id, "
				+ "status, product_type, units, price, unit_type FROM "
				+ "products WHERE is_deleted = false AND REPLACE(LOWER(name), ' ', '') LIKE '%" + searchText + "%'";

		SqlQuery rawSqlQuery = Ebean.createSqlQuery(searchProductQuery);

		return rawSqlQuery.findList();
	}

	public void updateImageUrlStatus(String imageUrl) throws MyException {
		this.setImageUrl(imageUrl);
		this.update();
	}

	public static Query<Products> getFeaturedProductsQuery() throws MyException, IOException {
		// TODO calculate featured products
		// Calculate most ordered products
		Query<Products> productQuery = basicQuery();

		productQuery.where().ne("image_url", null);
		productQuery.where().eq("isFeatured", true);

		return productQuery;
	}
}
