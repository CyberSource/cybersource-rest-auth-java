package com.cybersource.SampleCode;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.Data.Configuration;
import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;

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
	
	private RequestContext requestContext;

	/* This method initiates or begins the process. */
	public static void main(String[] args) {
		new GetObjectMethod();
	}

	/**
	 * @throws ConfigException
	 * @throws IOException
	 */
	public GetObjectMethod() {
		try {
			apiController = new ApiController();
			/* Read Merchant details. */
			merchantProp = Configuration.getMerchantDetails();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestJsonPath("not required");
			/* Set Request Type */
			requestContext.setRequestMethodType(requestType);
			/* Extract Authentication Type from cybs.properties */
			authenticationType = merchantConfig.getAuthenticationType();
			requestContext.setRequestTarget(requestTarget);
			/* Construct the URL */
			url = "https://" + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			/* Set the URL. */
			requestContext.setUrl(url);
			/* Begin the Get process. */
			process();

		} catch (ConfigException e) {
			System.out.println(e.getMessage());
		}  catch (SignatureGenerationException e) {
			System.out.println(e.getMessage());
		} catch (ClientException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws ClientException
	 * @throws ConfigException
	 * @throws SignatureGenerationException 
	 */
	private void process() throws ConfigException, ClientException, SignatureGenerationException {
		System.out.println(authenticationType.toUpperCase() + " Request");

		/* Calling APISDK, com.cybersouce.api.controller. */
		response = apiController.paymentGet(merchantConfig, requestContext);
		/* Display response message and Headers in console. */
		if (StringUtils.isNotBlank(response.getResponseCode())
				&& StringUtils.isNotBlank(response.getResponseMessage())) {
			System.out.println(" URL                 : " + url);
			System.out.println(" Response Code       : " + response.getResponseCode());
			System.out.println(" V-C-Corealation ID  : " + response.getVcCorelationId());
			System.out.println(" Response Message    : " + response.getResponseMessage());
		}
	}
}
