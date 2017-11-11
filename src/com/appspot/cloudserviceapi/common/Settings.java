package com.appspot.cloudserviceapi.common;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Settings {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	private String pinCode;
	@Persistent
	private String setting;
	@Persistent
	private String status;
	@Persistent
	private Date recorded;

	public Settings(String pinCode, String setting, String status,
			Date reportDateTime) {
		super();
		this.pinCode = pinCode;
		this.setting = setting;
		this.status = status;
		this.recorded = reportDateTime;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getReportDateTime() {
		return recorded;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.recorded = reportDateTime;
	}

}