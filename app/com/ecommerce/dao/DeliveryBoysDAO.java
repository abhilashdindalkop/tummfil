package com.ecommerce.dao;

import java.util.List;
import java.util.UUID;

import com.avaje.ebean.Ebean;
import com.ecommerce.models.sql.Cities;
import com.ecommerce.models.sql.DeliveryBoys;
import com.ecommerce.models.sql.Vendors;

import authentication.PasswordEncryptDecrypt;
import utils.MyConstants.FailureMessages;
import utils.MyException;

public class DeliveryBoysDAO {

	public DeliveryBoys findById(String boyId) throws MyException {
		DeliveryBoys boy = Ebean.find(DeliveryBoys.class).where().eq("encrypted_id", boyId).findUnique();
		if (boy == null) {
			throw new MyException(FailureMessages.DELIVERY_BOY_DOESNT_EXIST);
		}
		return boy;
	}

	public DeliveryBoys add(DeliveryBoys newBoy, Vendors vendor, String phoneNo, String password, Cities city)
			throws MyException {
		newBoy.setEncryptedId(UUID.randomUUID().toString());
		newBoy.setVendor(vendor);
		newBoy.setCity(city);
		newBoy.setPhoneNo(phoneNo);
		newBoy.setPassword(PasswordEncryptDecrypt.generatePasswordHash(password));
		newBoy.save();
		return newBoy;
	}

	public void delete(String boyId) throws MyException {
		DeliveryBoys boy = Ebean.find(DeliveryBoys.class).where().eq("encrypted_id", boyId).findUnique();
		if (boy == null) {
			throw new MyException(FailureMessages.DELIVERY_BOY_DOESNT_EXIST);
		}
		Ebean.delete(boy);
	}

	public List<DeliveryBoys> getVendorDeliveryBoys(Vendors vendor) throws MyException {
		List<DeliveryBoys> boys = Ebean.find(DeliveryBoys.class).where().eq("vendor", vendor).eq("isDeleted", false)
				.findList();
		return boys;
	}

	public DeliveryBoys findByPhoneNo(String phoneNo) {
		DeliveryBoys boy = Ebean.find(DeliveryBoys.class).where().eq("phoneNo", phoneNo).eq("isDeleted", false)
				.findUnique();
		return boy;
	}

	public Boolean isPhoneNoExists(String phoneNo) {
		return findByPhoneNo(phoneNo) != null;
	}

}
