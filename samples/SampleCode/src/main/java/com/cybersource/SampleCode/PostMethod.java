package com.cybersource.SampleCode;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
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
	private RequestContext requestContext;
	private Response responseObj;
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

	public static void main(String[] args) {
		new PostMethod();
	}

	public PostMethod() {
		try {
			apiController = new ApiController();

			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new RequestContext(merchantConfig.getLogger());
			/* Extract Authentication Type from cybs.properties */
			authenticationType = merchantConfig.getAuthenticationType();
			/* Set request Type. */
			requestContext.setRequestMethodType(requestType);
			/* Set request data */

			requestContext.setRequestJsonPath(requestJsonPath);
			/* Set request target */
			requestContext.setRequestTarget(requestTarget);
			/* Set URL path w.r.t Post operation. */
			url = "https://" + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			requestContext.setUrl(url);
			/* Begin Post process. */
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
		responseObj = apiController.paymentPost(merchantConfig, requestContext);
		/* Display Response Message from the server and Headers. */
		if (StringUtils.isNotBlank(responseObj.getResponseCode())
				&& StringUtils.isNotBlank(responseObj.getResponseMessage())) {
			System.out.println(" URL                 : " + url);
			System.out.println(" Response Code       : " + responseObj.getResponseCode());
			System.out.println(" v-c-Co-relation ID  : " + responseObj.getVcCorelationId());
			System.out.println(" Response Message    : " + responseObj.getResponseMessage());
		}

	}
}
