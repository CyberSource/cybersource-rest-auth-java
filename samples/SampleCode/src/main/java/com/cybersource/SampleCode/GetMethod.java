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

public class GetMethod {
	private ApiController apiController;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private Response response;
	private String authenticationType;
	/**
	 * REQUEST TARGET / UNIQUE GET ID [Editable]
	 */
	private String requestTarget = "/pts/v2/payments/5383909529456080803003";
	/**
	 * REQUEST-TYPE. [Non-Editable]
	 */
	private String requestType = "GET";
	private String url;

	/**
	 * This method initiates or begins the process.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String args[]) throws IOException, ConfigException {
		new GetMethod();
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ConfigException
	 */
	public GetMethod() throws IOException, ConfigException {
		apiController = new ApiController();

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		MerchantUtil merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		/* Set Request Type into the merchant config object. */
		merchantUtil.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setUrl(url);
		/* Begin the Get process. */
		process();
	}

	/**
	 * @throws Exception
	 * 
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");

		try {
			/* Calling APISDK, com.cybersouce.api.controller. */
			response = apiController.paymentGet(merchantConfig, null);
			/* Display response message and Headers in console. */
			if (StringUtils.isNotBlank(response.getResponseCode())
					&& StringUtils.isNotBlank(response.getResponseMessage())) {
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
