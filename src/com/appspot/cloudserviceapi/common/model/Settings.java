package com.appspot.cloudserviceapi.common.model;

import java.util.Date;

//import javax.jdo.annotations.IdGeneratorStrategy;
//import javax.jdo.annotations.IdentityType;
//import javax.jdo.annotations.PersistenceCapable;
//import javax.jdo.annotations.Persistent;
//import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

//@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Entity
public class Settings {
//	@PrimaryKey
//	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Basic
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Key key;

	private String pinCode;
//	@Persistent
	@Basic
	private String setting;
//	@Persistent
	@Basic
	private String status;
//	@Persistent
	@Basic
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