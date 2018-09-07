package com.cybersource.apisdk.model;

public class ProcessingInformation {

	private String commerceIndicator;

	public ProcessingInformation commerceIndicator(String commerceIndicator) {
		this.commerceIndicator = commerceIndicator;
		return this;
	}

	public String getcommerceIndicator() {
		return commerceIndicator;
	}

	public void setcommerceIndicator(String commerceIndicator) {
		this.commerceIndicator = commerceIndicator;
	}

}
