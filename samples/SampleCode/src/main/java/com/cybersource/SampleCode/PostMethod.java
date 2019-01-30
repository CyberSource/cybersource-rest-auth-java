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
 * POST operation on the test url 'apitest.cybersource.com'.
 * 
 */
public class PostMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private MerchantUtil merchantUtil;
	private Response responseObj;
	private String requestData;
	/**
	 * REQUEST TARGET [Editable]
	 */
	private String requestTarget = "/pts/v2/payments/";
	/**
	 * JSON PATH [Editable]
	 */
	private String requestJsonPath = "src/main/resources/request.json";
	/**
	 * REQUEST-TYPE. [Non-Editable]
	 */
	private String requestType = "POST";
	private String url;

	/**
	 * This method initiates or begins the process.
	 * 
	 * @param args
	 * @throws ConfigException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ConfigException {
		new PostMethod();
	}

	/**
	 * @throws IOException
	 * @throws ConfigException
	 */
	public PostMethod() throws IOException, ConfigException {
		apiController = new ApiController();

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil = new MerchantUtil(merchantConfig);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		/* Set request Type. */
		merchantUtil.setRequestType(requestType);
		/* Set request data */

		merchantUtil.setRequestJsonPath(requestJsonPath);
		requestData = merchantUtil.getRequestData();
		/* Set request target */
		merchantUtil.setRequestTarget(requestTarget);
		/* Set URL path w.r.t Post operation. */
		url = "https://" + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		/* Begin Post process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");
		try {
			/* Call payment method of Api Controller class */
			responseObj = apiController.paymentPost(merchantConfig, requestData);
			/* Display Response Message from the server and Headers. */
			if (StringUtils.isNotBlank(responseObj.getResponseCode())
					&& StringUtils.isNotBlank(responseObj.getResponseMessage())) {
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + responseObj.getResponseCode());
				System.out.println(" v-c-Co-relation ID  : " + responseObj.getVcCorelationId());
				System.out.println(" Response Message    : " + responseObj.getResponseMessage());
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
