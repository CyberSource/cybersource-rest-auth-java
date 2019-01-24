package com.cybersource.SampleCode;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class GetObjectMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private Response response;
	/**
	 * REQUEST TARGET [Editable]
	 */
	private String requestTarget = "/pts/v2/payments/5383910703156063603002";
	/* Request Type [Non-Editable.] */
	private String requestType = "GET";
	/* [Non Editable] */
	private String url;

	/* This method initiates or begins the process. */
	public static void main(String[] args) throws ConfigException, IOException {
		new GetObjectMethod();
	}

	/**
	 * @throws ConfigException 
	 * @throws IOException 
	 */
	public GetObjectMethod() throws ConfigException, IOException {
		apiController = new ApiController();
		/* Read Merchant details. */
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		MerchantUtil merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		/* Set Request Type */
		merchantUtil.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL with respect to GETID. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setUrl(url);
		/* Begin the Get process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 */
	private void process(){
		System.out.println(authenticationType.toUpperCase() + " Request");

		try {
			/* Calling APISDK, com.cybersouce.api.controller. */
			response = apiController.paymentGet(merchantConfig,null);
			/* Display response message and Headers in console. */
			if (StringUtils.isNotBlank(response.getResponseCode()) && StringUtils.isNotBlank(response.getResponseMessage())) {
				//new GetGenerateHeaders(merchantConfig,null);
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + response.getResponseCode());
				System.out.println(" V-C-Corealation ID  : " + response.getVcCorelationId());
				System.out.println(" Response Message    : " + response.getResponseMessage());
			}
		} catch (ConfigException e) {
			System.out.println(e.getMessage());
		} catch (ClientException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
