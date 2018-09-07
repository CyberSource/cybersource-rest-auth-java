package com.cybersource.apisdk.model;

public class ClientReferenceInformation {

	private String code;

	public ClientReferenceInformation code(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
