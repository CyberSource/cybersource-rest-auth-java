package com.cybersource.SampleCode;

import java.util.Properties;

import com.cybersource.Data.Configuration;
import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.authsdk.core.MerchantConfig;

public class GetObjectMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String responseCode, responseMessage;
		/**
	 * REQUEST TARGET
	 * [Editable]
	 */
	private String requestTarget = "/pts/v2/payments/5265502011846829204101";
	/* Request Type [Non-Editable.]*/
	private String requestType = "GET";
	/* [Non Editable]*/
	private String url;
	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		new GetObjectMethod();
	}

	/**
	 * @throws Exception
	 */
	public GetObjectMethod() throws Exception {
		apiController = new ApiController();
		/* Read Merchant details. */
		merchantProp = Configuration.getMerchantDetails();
		merchantConfig = new MerchantConfig(merchantProp);
		/* Set Request Type*/
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL with respect to GETID. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setURL(url);
		/* Begin the Get process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws Exception
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");
		apiController = new ApiController();
		/* Calling APISDK, com.cybersouce.api.controller. */
		apiController.paymentGet(merchantConfig);
		/* Display response message and Headers in console. */
		responseCode = merchantConfig.getResponseCode();
		responseMessage = merchantConfig.getResponseMessage();
		try {
			if (!responseCode.isEmpty() && !responseMessage.isEmpty() && responseMessage != null
					&& responseCode != null) {
				new GetGenerateHeaders(merchantConfig);
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + responseCode);
				System.out.println(" V-C-Corealation ID  : " + merchantConfig.getVcCorelationID());
				System.out.println(" Response Message    : " + responseMessage);
			}
		} catch (Exception e) {
			return;
		}
	}
}
