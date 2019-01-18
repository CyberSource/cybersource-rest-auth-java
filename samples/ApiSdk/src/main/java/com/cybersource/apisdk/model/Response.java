package com.cybersource.apisdk.model;

public class Response {

	public String responseMessage;
	public String responseCode;
	public String vcCorelationId;

	public String getVcCorelationId() {
		return vcCorelationId;
	}

	public void setVcCorelationId(String resVcCorelationId) {
		this.vcCorelationId = resVcCorelationId;
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
