package com.cybersource.apisdk.model;

public class Card {

	private String expirationYear;
	private String number;
	private String securityCode;
	private String expirationMonth;
	private String type;

	public Card expirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
		return this;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public Card number(String number) {
		this.number = number;
		return this;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Card securityCode(String securityCode) {
		this.securityCode = securityCode;
		return this;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public Card expirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
		return this;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Card type(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
