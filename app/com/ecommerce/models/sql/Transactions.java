package com.ecommerce.models.sql;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Transactions extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String encryptedId;

	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Orders order;

	private String transactionId;

	private double amount;

	private int status;

	private int currency;

	private int paymentType;

	private int gatewayStatusCode;

	private String gatewayError;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEncryptedId() {
		return encryptedId;
	}

	public void setEncryptedId(String encryptedId) {
		this.encryptedId = encryptedId;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public int getGatewayStatusCode() {
		return gatewayStatusCode;
	}

	public void setGatewayStatusCode(int gatewayStatusCode) {
		this.gatewayStatusCode = gatewayStatusCode;
	}

	public String getGatewayError() {
		return gatewayError;
	}

	public void setGatewayError(String gatewayError) {
		this.gatewayError = gatewayError;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
