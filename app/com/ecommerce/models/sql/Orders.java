package com.ecommerce.models.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.Query;
import com.ecommerce.dto.request.CreateOrderRequestDTO;
import com.ecommerce.dto.request.OrderedProductsRequestDTO;
import com.ecommerce.dto.response.createOrderResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import utils.GenericUtils;
import utils.MyConstants;
import utils.MyConstants.APIResponseKeys;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;
import utils.MyConstants.PaymentStatus;
import utils.MyException;
import utils.ObjectMapperUtil;

@Entity
public class Orders extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String orderId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Vendors vendor;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Users user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn()
	private Cities city;

	private String name;

	private Double extraFee = 0.0;

	private Double totalPrice;

	private Integer status;

	private Integer orderType;

	private Integer paymentType;

	private Integer paymentStatus;

	@Column(columnDefinition = "TEXT")
	private String address;

	private Long pincode;

	private String phoneNo;

	private Double latitude;

	private Double longitude;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(columnDefinition = "TIMESTAMP")
	private Date deliveryTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date updatedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public Double getExtraFee() {
		return extraFee;
	}

	public void setExtraFee(Double extraFee) {
		this.extraFee = extraFee;
	}

	public Vendors getVendor() {
		return vendor;
	}

	public void setVendor(Vendors vendor) {
		this.vendor = vendor;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
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

	public static JsonNode createOrder(CreateOrderRequestDTO requestDTO, Users user) throws MyException {
		try {
			Ebean.beginTransaction();

			if (requestDTO.orderedProducts.isEmpty()) {
				throw new MyException(FailureMessages.CART_EMPTY);
			}
			double totalPrice = 0, totalProductsPrice = 0;
			OrderedProductsRequestDTO firstOrderProdReq = requestDTO.orderedProducts.get(0);

			Vendors vendor = Products.findById(firstOrderProdReq.productId).getVendor();

			if (!(vendor.getIsVendorVerified() && vendor.getIsVendorAvailable())) {
				throw new MyException(FailureMessages.VENDOR_NOT_AVAILABLE);
			}

			double price = 0;
			List<OrderedProducts> listOrderedProducts = new ArrayList<OrderedProducts>();

			List<ObjectNode> orderedProdJsonList = new ArrayList<ObjectNode>();
			for (OrderedProductsRequestDTO orderedProdReq : requestDTO.orderedProducts) {

				Products product = Products.findById(orderedProdReq.productId);

				if (product.getVendor().getId() != vendor.getId()) {
					throw new MyException(FailureMessages.PRODUCTS_BELONGS_TO_MULTIPLE_VENDORS);
				}

				OrderedProducts orderedProduct = new OrderedProducts();
				orderedProduct.setProduct(product);
				orderedProduct.setQuantity(orderedProdReq.quantity);

				price = GenericUtils.computeProductTotalPrice(product, orderedProdReq.quantity);
				orderedProduct.setPrice(price);

				listOrderedProducts.add(orderedProduct);

				totalProductsPrice += price;

				// Construct response json
				ObjectNode prodJson = Json.newObject();
				prodJson.put(APIResponseKeys.PRODUCT_ID, product.getProductId());
				prodJson.put(APIResponseKeys.PRODUCT_NAME, product.getName());
				prodJson.put(APIResponseKeys.QUANTITY, orderedProduct.getQuantity());
				// TODO get tags
				prodJson.put(APIResponseKeys.PRICE, orderedProduct.getPrice());
				orderedProdJsonList.add(prodJson);
			}

			double extraFee = GenericUtils.computeExtraFee(vendor);

			totalPrice = totalProductsPrice + extraFee;

			/* Create order id */
			String orderId = GenericUtils.generateOrderId(user.getId(), vendor.getId());

			Orders newOrder = new Orders();
			newOrder.save(requestDTO, orderId, user, vendor, extraFee, totalPrice);
			newOrder.refresh();

			for (OrderedProducts orderedProdDb : listOrderedProducts) {
				orderedProdDb.add(newOrder);
			}

			Ebean.commitTransaction();

			createOrderResponseDTO orderResponse = new createOrderResponseDTO();
			/* Create response */
			ObjectMapper mapper = ObjectMapperUtil.getInstance();
			orderResponse = mapper.convertValue(newOrder, createOrderResponseDTO.class);
			orderResponse.orderStatus = newOrder.getStatus();
			orderResponse.extraFee = extraFee;
			orderResponse.totalPrice = totalPrice;
			orderResponse.products = orderedProdJsonList;

			return Json.toJson(orderResponse);
		} catch (MyException e) {
			if (Ebean.currentTransaction() != null) {
				Ebean.rollbackTransaction();
			}
			throw e;
		}

	}

	public void save(CreateOrderRequestDTO requestDTO, String orderId, Users user, Vendors vendor, double extraFee,
			double totalPrice) throws MyException {
		this.setOrderId(orderId);
		this.setVendor(vendor);
		this.setUser(user);
		if (requestDTO.cityId != null && requestDTO.cityId != 0) {
			Cities city = Cities.findById(requestDTO.cityId);
			this.setCity(city);
		}
		this.setName(requestDTO.name);
		this.setExtraFee(extraFee);
		this.setTotalPrice(totalPrice);
		this.setStatus(OrderStatus.PENDING);
		if (!MyConstants.orderTypeList.contains(requestDTO.orderType)) {
			throw new MyException(FailureMessages.INVALID_ORDER_TYPE);
		}
		this.setOrderType(requestDTO.orderType);
		this.setPaymentStatus(PaymentStatus.PENDING);
		this.setAddress(requestDTO.address);
		this.setPincode(requestDTO.pincode);
		this.setPhoneNo(requestDTO.phoneNo);
		this.setDescription(requestDTO.description);
		this.setLatitude(requestDTO.latitude);
		this.setLongitude(requestDTO.longitude);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.save();
	}

	public void updatePaymentTypeAndStatus(int paymentType, int paymentStatus) throws MyException {
		this.setPaymentType(paymentType);
		this.setPaymentStatus(paymentStatus);
		this.update();
	}

	public void updateDeliveryTime(Date deliveryTime) throws MyException {
		this.setDeliveryTime(deliveryTime);
		this.update();
	}

	public static Orders findById(String orderId) throws MyException {
		Orders order = Ebean.find(Orders.class).where().eq("order_id", orderId).findUnique();
		if (order == null) {
			throw new MyException(FailureMessages.ORDER_DOESNT_EXIST);
		}
		return order;
	}

	public static HashMap<String, Object> findVendorOrders(Vendors vendor, int status, int page, int limit)
			throws MyException {
		Query<Orders> query = Ebean.find(Orders.class).where().eq("vendor", vendor).eq("status", status).order()
				.desc("created_time");

		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("totalCount", query.findRowCount());

		List<Orders> orderList = query.findPagedList(page, limit).getList();
		orderMap.put("result", orderList);

		return orderMap;
	}

	public void updateStatus(int orderStatus) throws MyException {
		GenericUtils.orderStatusValidation(this.getStatus(), orderStatus);
		this.setStatus(orderStatus);
		this.setUpdatedTime(new Date());
		this.update();
	}

	public static HashMap<String, Object> findUserOrders(Users user, int page, int limit) throws MyException {
		Query<Orders> query = Ebean.find(Orders.class).where().eq("user", user).order().desc("created_time");

		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("totalCount", query.findRowCount());

		List<Orders> orderList = query.findPagedList(page, limit).getList();
		orderMap.put("result", orderList);

		return orderMap;
	}

}
