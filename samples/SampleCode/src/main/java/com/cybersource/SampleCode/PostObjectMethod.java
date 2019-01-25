package com.cybersource.SampleCode;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.Data.PayloadData;
import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * POST operation on the test URL 'apitest.cybersource.com'.
 * 
 */
public class PostObjectMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private MerchantUtil merchantUtil;
	private Response response;
	/**
	 * REQUEST-TYPE. [Non-Editable]
	 */
	private String requestType = "POST";
	/* Give the URL path to where the data needs to be authenticated. */
	private String url;
	private String requestData;
	/**
	 * REQUEST TARGET [Editable]
	 */
	private String requestTarget = "/pts/v2/payments";

	/**
	 * This method initiates or begins the process. *
	 * 
	 * @param args
	 * @throws ConfigException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ConfigException, IOException {
		new PostObjectMethod();
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws ConfigException
	 * @throws IOException
	 * @throws Exception
	 */
	public PostObjectMethod() throws ConfigException, IOException {
		apiController = new ApiController();
		/* Read Merchant details. */
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil = new MerchantUtil(merchantConfig);

		merchantUtil.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		/* Set request data */
		merchantUtil.setRequestJsonPath("not required");
		requestData = PayloadData.readData();
		/* Set request target */
		merchantConfig.setRequestTarget(requestTarget);
		/* Set URL path w.r.t Post operation. */

		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setUrl(url);

		/* Begin Post process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws Exception
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");
		try {
			/* Call payment method of Api Controller class */
			response = apiController.paymentPost(merchantConfig, requestData);
			/* Display Response Message from the server and Headers. */
			if (!StringUtils.isBlank(response.getResponseCode())
					&& !StringUtils.isBlank(response.getResponseMessage())) {
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + response.getResponseCode());
				System.out.println(" v-c-Co-relation ID  : " + response.getVcCorelationId());
				System.out.println(" Response Message    : " + response.responseMessage);
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
