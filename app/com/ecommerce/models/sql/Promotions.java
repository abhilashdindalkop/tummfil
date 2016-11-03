package com.ecommerce.models.sql;

import java.util.Date;
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
import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.MyConstants.FailureMessages;
import utils.MyException;

@Entity
public class Promotions extends Model {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn()
	private Vendors vendor;

	private String imageUrl = null;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonIgnore
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vendors getVendor() {
		return vendor;
	}

	public void setVendor(Vendors vendor) {
		this.vendor = vendor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static Promotions findById(long promotionId) throws MyException {
		Promotions promotion = Ebean.find(Promotions.class).where().eq("id", promotionId).findUnique();
		if (promotion == null) {
			throw new MyException(FailureMessages.INVALID_PROMOTION_ID);
		}
		return promotion;
	}

	public static List<Promotions> findAllPromotions() throws MyException {
		List<Promotions> promotions = Ebean.find(Promotions.class).findList();
		return promotions;
	}

}
