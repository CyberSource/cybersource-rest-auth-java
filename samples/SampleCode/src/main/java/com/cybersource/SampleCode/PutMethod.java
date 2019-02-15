package com.cybersource.SampleCode;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.PropertiesUtil;

/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the given working domain.
 */
public class PutMethod {
	private ApiController apiController;
	private String authenticationType;
	private Response response;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private RequestContext requestContext;
	/**
	 * REQUEST-TARGET [Editable]
	 */
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
	/**
	 * JSON-PATH [Editable]
	 */
	private String requestJsonPath = "src/main/resources/TRRReport.json";
	/**
	 * REQUEST-TYPE. [Non-Editable]
	 */
	private String requestType = "PUT";
	private String url;

	/**
	 * This method initiates or begins the process.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new PutMethod();
	}

	public PutMethod() {
		try {
			apiController = new ApiController();
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new RequestContext(merchantConfig.getLogger());

			requestContext.setRequestMethodType(requestType);
			/* Extract Authentication Type from cybs.properties */
			authenticationType = merchantConfig.getAuthenticationType();
			/*
			 * Set Json Path, request target, request data into merchant config
			 * object.
			 */
			requestContext.setRequestJsonPath(requestJsonPath);
			/* Set request target */
			requestContext.setRequestTarget(requestTarget);
			/* Set request Data */
			requestContext.setRequestBody(requestContext.getRequestBody());

			/* Construct the URL with respect to PUTID. */
			url = "https://" + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			/* Set the URL. */
			requestContext.setUrl(url);
			/* Begin PUT process. */
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
		/* Call payment method of Api Controller class */
		response = apiController.paymentPut(merchantConfig, requestContext);

		if (!StringUtils.isBlank(response.getResponseCode()) && !StringUtils.isBlank(response.getResponseMessage())) {
			System.out.println(" URL                 : " + url);
			System.out.println(" Response Code       : " + response.getResponseCode());
			System.out.println(" v-c-Co-relation ID  : " + response.getVcCorelationId());
			System.out.println(" Response Message    : " + response.getResponseMessage());
		}

	}
}
