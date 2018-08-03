package com.cybersource.apisdk.model;

public class AggregatorInformation {

	private SubMerchant subMerchant;
	private String name;
	private String aggregatorID;

	public AggregatorInformation subMerchant(SubMerchant subMerchant) {
		this.subMerchant = subMerchant;
		return this;
	}

	public AggregatorInformation name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AggregatorInformation aggregatorID(String aggregatorID) {
		this.aggregatorID = aggregatorID;
		return this;

	}

	public String getAggregatorID() {
		return aggregatorID;
	}

	public void setAggregatorID(String aggregatorID) {
		this.aggregatorID = aggregatorID;
	}

}
