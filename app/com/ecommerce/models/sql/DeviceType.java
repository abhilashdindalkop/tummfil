package com.ecommerce.models.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import play.Logger;
import utils.MyConstants.AppConstants;

@Entity
public class DeviceType extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 50)
	private String deviceName;

	public DeviceType(int id, String deviceName) {
		this.id = id;
		this.deviceName = deviceName;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public static Model.Finder<Long, DeviceType> find = new Model.Finder<Long, DeviceType>(DeviceType.class);

	/*
	 * finds and returns a DeviceType by its id.
	 */
	public static DeviceType findById(Integer id) {
		DeviceType deviceType = null;
		try {
			deviceType = find.where().eq("id", id).findUnique();
			if (deviceType == null) {
				deviceType = new DeviceType(AppConstants.UNKNOWN_DEVICE_ID, AppConstants.UNKNOWN);
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
		return deviceType;
	}

}
