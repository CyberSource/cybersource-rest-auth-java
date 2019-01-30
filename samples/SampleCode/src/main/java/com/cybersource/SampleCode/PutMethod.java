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
 * PUT operation on the given working domain.
 */
public class PutMethod {
	private ApiController apiController;
	private String authenticationType;
	private Response response;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private MerchantUtil merchantUtil;
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
	/* Request Data. */
	private String requestData = PayloadData.readData();

	/**
	 * This method initiates or begins the process.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args) throws IOException, ConfigException {
		new PutMethod(args);
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 * @throws ConfigException
	 */
	public PutMethod(String[] args) throws IOException, ConfigException {
		apiController = new ApiController();
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil = new MerchantUtil(merchantConfig);

		merchantUtil.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		/*
		 * Set Json Path, request target, request data into merchant config
		 * object.
		 */
		merchantUtil.setRequestJsonPath(requestJsonPath);
		/* Set request target */
		merchantUtil.setRequestTarget(requestTarget);
		/* Set request Data */
		requestData = merchantUtil.getRequestData();
		merchantUtil.setRequestData(requestData);
		
		/* Construct the URL with respect to PUTID. */
		url = "https://" + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		/* Set the URL. */
		merchantUtil.setUrl(url);
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
			response = apiController.paymentPut(merchantConfig, requestData);

			if (!StringUtils.isBlank(response.getResponseCode())
					&& !StringUtils.isBlank(response.getResponseMessage())) {
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + response.getResponseCode());
				System.out.println(" v-c-Co-relation ID  : " + response.getVcCorelationId());
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
