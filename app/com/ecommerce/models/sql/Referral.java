package com.ecommerce.models.sql;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import utils.MyConstants.ReferralStatus;
import utils.MyException;

@Entity
public class Referral extends Model {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	private Users referredBy;

	@Column(nullable = false)
	private Users referredTo;

	@Column(nullable = false)
	private float credit;

	@Column(nullable = false)
	private int status = ReferralStatus.NOT_CREDITED;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
	private Date createdTime;

	public boolean isExpired() {
		return this.getStatus() == ReferralStatus.CREDITED;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(Users referredBy) {
		this.referredBy = referredBy;
	}

	public Users getReferredTo() {
		return referredTo;
	}

	public void setReferredTo(Users referredTo) {
		this.referredTo = referredTo;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public static Referral findByReferredTo(Users referredTo) {
		return Ebean.find(Referral.class).where().eq("referred_to", referredTo)
				.eq("status", ReferralStatus.NOT_CREDITED).findUnique();
	}
	
	public static List<Referral> getHistory(Users referredBy) {
		return Ebean.find(Referral.class).where().eq("referredBy", referredBy).findList();
	}

	public static void createReferral(Users referredBy, Users referredTo) throws MyException {
		// TODO
	}

	public static void addCreditToProfileWallet(String profileId) throws MyException {
		// TODO
	}

	public static void addReferralCredit() throws MyException {
		// TODO
	}


}
