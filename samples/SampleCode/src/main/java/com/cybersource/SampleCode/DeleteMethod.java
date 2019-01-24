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

/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the test url 'apitest.cybersource.com'.
 */
public class DeleteMethod {
	private ApiController apiController;
	private String authenticationType;
	private Response response;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String requestType = "DELETE";
	/* Give the url path to where the data needs to be authenticated. */
	private String url;
	/* Request Target. */
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

	/**
	 * @param args
	 * This method initiates or begins the process.
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args) throws IOException, ConfigException {
		new DeleteMethod(args);
	}

	/**
	 * @param
	 * @return
	 * @throws IOException
	 * @throws ConfigException
	 * @throws Exception
	 */
	public DeleteMethod(String[] args) throws IOException, ConfigException {
		apiController = new ApiController();

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		MerchantUtil merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		merchantUtil.setRequestType(requestType);
		authenticationType = merchantConfig.getAuthenticationType();
		/*
		 * Set Json Path, request target, request data into merchant config
		 * object.
		 */
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL with respect to PUTID. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setUrl(url);
		/* Begin PUT process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");

		try {
			/* Call payment method of Api Controller class */
			response = apiController.paymentDelete(merchantConfig, null);
			/* Display Response Message from the server. */
			if (StringUtils.isNotBlank(response.getResponseCode())
					&& StringUtils.isNotBlank(response.getResponseMessage())) {
				//new DeleteGenerateHeaders(merchantConfig);
				System.out.println(" URL                : " + url);
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
