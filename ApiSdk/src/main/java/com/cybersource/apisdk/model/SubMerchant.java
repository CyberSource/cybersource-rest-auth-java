package com.cybersource.apisdk.model;

public class SubMerchant {

	private String cardAcceptorID;
	private String country;
	private String phoneNumber;
	private String address1;
	private String postalCode;
	private String locality;
	private String name;
	private String administrativeArea;
	private String region;
	private String email;

	public SubMerchant cardAcceptorID(String cardAcceptorID) {
		this.cardAcceptorID = cardAcceptorID;
		return this;
	}

	public String getCardAcceptorID() {
		return cardAcceptorID;
	}

	public void setCardAcceptorID(String cardAcceptorID) {
		this.cardAcceptorID = cardAcceptorID;
	}

	public SubMerchant country(String country) {
		this.country = country;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public SubMerchant phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public SubMerchant address1(String address1) {
		this.address1 = address1;
		return this;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public SubMerchant postalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public SubMerchant locality(String locality) {
		this.locality = locality;
		return this;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public SubMerchant name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubMerchant administrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
		return this;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public SubMerchant region(String region) {
		this.region = region;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public SubMerchant email(String email) {
		this.email = email;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
