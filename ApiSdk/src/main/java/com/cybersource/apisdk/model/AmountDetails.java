package com.cybersource.apisdk.model;

public class AmountDetails {

	private String totalAmount;
	private String currency;

	public AmountDetails totalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public AmountDetails currency(String currency) {
		this.currency = currency;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
