package com.cybersource.SampleCode;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.AuthenticationRequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
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
	private AuthenticationRequestContext requestContext;

	/**
	 * @param args
	 *            This method initiates or begins the process.
	 */
	public static void main(String[] args) {
		new DeleteMethod(args);
	}

	/**
	 * @param args
	 */
	public DeleteMethod(String[] args) {
		try {
			apiController = new ApiController();

			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new AuthenticationRequestContext();
			requestContext.setRequestJsonPath("not required");
			requestContext.setRequestMethodType(requestType);
			authenticationType = merchantConfig.getAuthenticationType();
			/*
			 * Set Json Path, request target, request data into merchant config
			 * object.
			 */
			requestContext.setRequestTarget(requestTarget);
			/* Construct the URL with respect to PUTID. */
			url = "https://" + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			/* Set the URL. */
			requestContext.setUrl(url);
			/* Begin PUT process. */
			process();

		} catch (ConfigException e) {
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
	 */
	private void process() throws ConfigException, ClientException {

		System.out.println(authenticationType.toUpperCase() + " Request");
		/* Call payment method of Api Controller class */
		response = apiController.paymentDelete(merchantConfig, requestContext);
		/* Display Response Message from the server. */
		if (StringUtils.isNotBlank(response.getResponseCode())
				&& StringUtils.isNotBlank(response.getResponseMessage())) {
			System.out.println(" URL                : " + url);
			System.out.println(" Response Code       : " + response.getResponseCode());
			System.out.println(" V-C-Corealation ID  : " + response.getVcCorelationId());
			System.out.println(" Response Message    : " + response.getResponseMessage());
		}

	}
}
