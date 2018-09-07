package com.cybersource.SampleCode;

import java.util.Properties;
import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.PropertiesUtil;

/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the test url 'apitest.cybersource.com'.
 */
public class DeleteMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	/* Request Type is always PUT */
	private String requestType = "DELETE";
	/* Give the url path to where the data needs to be authenticated. */
	private String url;
	/* Request Target. */
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
	/* Request json path */
	private String requestJsonPath = "src/main/resources/TRRReport.json";

	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		/**
		 * 
		 */
		new DeleteMethod(args);
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public DeleteMethod(String[] args) throws Exception {
		apiController = new ApiController();

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		/*
		 * Set Json Path, request target, request data into merchant config
		 * object.
		 */
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL with respect to PUTID. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setURL(url);
		/* Begin PUT process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws Exception
	 */
	private void process() throws Exception {
		System.out.println(authenticationType.toUpperCase() + " Request");
		/* Call payment method of Api Controller class */
		apiController.paymentGet(merchantConfig);
		/* Display Response Message from the server. */
		try {
			if (!merchantConfig.getResponseCode().isEmpty() && !merchantConfig.getResponseMessage().isEmpty()
					&& merchantConfig.getResponseMessage() != null && merchantConfig.getResponseCode() != null){
				new DeleteGenerateHeaders(merchantConfig);
			System.out.println(" URL                : " + url);
			System.out.println(" Response Code      : " + merchantConfig.getResponseCode());
			System.out.println(" V-C-Corealation ID : " + merchantConfig.getV_c_CorelationID());
			System.out.println(" Response Message   : " + merchantConfig.getResponseMessage());
			}
		} catch (Exception e) {
			return;
		}
		}
}
