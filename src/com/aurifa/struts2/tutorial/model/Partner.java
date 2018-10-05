package com.aurifa.struts2.tutorial.model;

import java.util.Date;

public class Partner {

	private Integer id;

	private String addedBy;

	private Date date;

	private String firstName;

	private String lastName;
	
	private String name;
	
	private String userId;

	private String position;
	
	private boolean disabled;
	
	private String ipAddress;

	private String nationalId;
	
	private String nationality;

	private String homeAddress;
	
	private String state;

	private String postalCode;
	
	private String country;
	
	private String phoneNumber;
	
	private String homeNumber;

	private String email;

	private String password;

	private Business business;
	
	public Partner() {
	}

	public Partner(Integer partnerId, String addedBy, Date date, String name,
			String userId, String position, boolean disabled,
			String ipAddress, String nationalId, String nationality,
			String homeAddress, String state, String postalCode,
			String country, String phoneNumber, String homeNumber,
			String email, String password) {
		super();
		this.id = partnerId;
		this.addedBy = addedBy;
		this.date = date;
		this.name = name;
		this.userId = userId;
		this.position = position;
		this.disabled = disabled;
		this.ipAddress = ipAddress;
		this.nationalId = nationalId;
		this.nationality = nationality;
		this.homeAddress = homeAddress;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.homeNumber = homeNumber;
		this.email = email;
		this.password = password;
	}
	
	public Partner(Integer partnerId, String name, String email, Business business) {
		this.id = partnerId;
		this.name = name;
		this.email = email;
		this.business = business;
	}
	
	public Integer getPartnerId() {
		return id;
	}

	public void setPartnerId(Integer partnerId) {
		this.id = partnerId;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	@Override
	public String toString() {
		return "Partner [partnerId=" + id + ", addedBy=" + addedBy
				+ ", date=" + date + ", firstName=" + firstName + ", lastName="
				+ lastName + ", name=" + name + ", userId=" + userId
				+ ", position=" + position + ", disabled=" + disabled
				+ ", ipAddress=" + ipAddress + ", nationalId=" + nationalId
				+ ", nationality=" + nationality + ", homeAddress="
				+ homeAddress + ", state=" + state + ", postalCode="
				+ postalCode + ", country=" + country + ", phoneNumber="
				+ phoneNumber + ", homeNumber=" + homeNumber + ", email="
				+ email + ", password=" + password + ", business=" + business
				+ "]";
	}
	

}
