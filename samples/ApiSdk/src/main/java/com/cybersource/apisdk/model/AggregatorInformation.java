package com.cybersource.apisdk.model;

public class AggregatorInformation {
	private SubMerchant subMerchant;
	private String name;
	private String aggregatorID;

	public SubMerchant getSubMerchant() {
		return subMerchant;
	}

	public void setSubMerchant(SubMerchant subMerchant) {
		this.subMerchant = subMerchant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAggregatorID() {
		return aggregatorID;
	}

	public void setAggregatorID(String aggregatorID) {
		this.aggregatorID = aggregatorID;
	}

}
