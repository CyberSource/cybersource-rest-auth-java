package com.cybersource.apisdk.model;

public class Response {

	public String responseMessage;
	public String responseCode;
	public String VcCorelationId;

	public String getVcCorelationId() {
		return VcCorelationId;
	}

	public void setVcCorelationId(String resVcCorelationId) {
		this.VcCorelationId = resVcCorelationId;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

}
