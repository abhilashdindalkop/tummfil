package com.ecommerce.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.ecommerce.models.mongo.VendorLocations;
import com.ecommerce.models.sql.Vendors;

import integrations.MongoConnection;
import utils.MyConstants.FailureMessages;
import utils.MyException;

public class VendorLocationDAO {

	private static Datastore ds = MongoConnection.getDS();

	public Query<VendorLocations> findNearbyVendors(double latitude, double longitude, double distance) {
		double distanceInKM = distance / 1000;
		return ds.find(VendorLocations.class).field("location").near(latitude, longitude, distanceInKM / 111.12)
				.order("createdTime");
	}

	public List<Long> findNearbyVendorIds(double latitude, double longitude, double distance, int page, int limit) {
		List<VendorLocations> vendorList = findNearbyVendors(latitude, longitude, distance).limit(limit)
				.offset(page * limit).asList();
		List<Long> vendorIds = new ArrayList<Long>();
		for (VendorLocations vendor : vendorList) {
			vendorIds.add(vendor.getVendorId());
		}
		return vendorIds;
	}

	public VendorLocations findById(long vendorId) throws MyException {
		VendorLocations vendorLocation = ds.find(VendorLocations.class).filter("vendorId", vendorId).get();
		if (vendorLocation == null) {
			throw new MyException(FailureMessages.VENDOR_LOCATION_DOESNT_EXIST);
		}
		return vendorLocation;
	}

	public VendorLocations add(Vendors newVendor, double latitude, double longitude, double shippingDistance) {
		VendorLocations newVendorLoc = new VendorLocations();
		newVendorLoc.setVendorId(newVendor.getId());
		newVendorLoc.setLocation(Arrays.asList(longitude, latitude));
		newVendorLoc.setCreatedTime(new Date());
		newVendorLoc.setShippingDistance(shippingDistance);
		ds.save(newVendorLoc);
		return newVendorLoc;
	}

	public void updateLocation(long vendorId, double latitude, double longitude) throws MyException {

		Query<VendorLocations> vendorUpdate = ds.find(VendorLocations.class).filter("vendorId", vendorId);
		UpdateOperations<VendorLocations> ops = ds.createUpdateOperations(VendorLocations.class);
		ops.set("location", Arrays.asList(longitude, latitude));
		ds.update(vendorUpdate, ops);
	}

	public void updateShippingDistance(long vendorId, double shippingDistance) throws MyException {

		Query<VendorLocations> vendorUpdate = ds.find(VendorLocations.class).filter("vendorId", vendorId);
		UpdateOperations<VendorLocations> ops = ds.createUpdateOperations(VendorLocations.class);
		ops.set("shippingDistance", shippingDistance);
		ds.update(vendorUpdate, ops);
	}

}
