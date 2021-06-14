package com.cybersource.apisdk.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.PaymentRequestService;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * 
 *
 */
public class ApiController {

	private Logger logger;
	private boolean authType;
	private String authenticationType;
	private boolean isMerchantDetail;
	private Response responseObj;

	/**
	 * 
	 * @param merchantConfig
	 * @throws Exception 
	 */
	public Response paymentGet(MerchantConfig merchantConfig) throws Exception {
		return processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 * @throws Exception 
	 */
	public Response paymentPost(MerchantConfig merchantConfig) throws Exception {
		return processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 * @throws Exception 
	 * 
	 */
	public Response paymentPut(MerchantConfig merchantConfig) throws Exception {
		return processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 * @throws Exception 
	 * 
	 */
	public Response paymentDelete(MerchantConfig merchantConfig) throws Exception {
		return processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 * @throws Exception 
	 */
	private Response processPayment(MerchantConfig merchantConfig) throws Exception {
		commonApiControllerCheck(merchantConfig);
		if (authType) {
			if (isMerchantDetail) {
				Utility.log(logger, GlobalLabelParameters.AUTENTICATION_TYPE , authenticationType,
						Level.INFO);
				try {
					PaymentRequestService paymentRequest = new PaymentRequestService(merchantConfig, logger);
					responseObj =  paymentRequest.processPaymentRequest();
					Utility.log(logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
					Utility.log(logger, " ","", Level.OFF);
					return responseObj;
				} catch (Exception e) {
					paymentEndException();
					throw(e);
				}
			} else {
				paymentEndException();
				throw(new Exception("Merchant Configuration is incorrect."));
			}
		} else {
			authenticationException();
		}
		return responseObj;
	}

	/**
	 * @param merchantConfig
	 * @throws ConfigException 
	 * 
	 */
	public void commonApiControllerCheck(MerchantConfig merchantConfig) throws ConfigException {
		/* Retrieve authentication type for validating. */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		authType = Utility.checkAuthenticationValidation(authenticationType);
		if (MerchantConfig.isSetMerchantDetails) {
			/* Initializing the logging object. */
			logger = Log4j.getInstance(merchantConfig);
			Utility.log(logger, GlobalLabelParameters.BEGIN_TRANSCATION,"", Level.INFO);
			/* Validate Merchant Details. */
			isMerchantDetail = merchantConfig.validateMerchantDetails(logger);
		} else {
			logger = Log4j.getInstance(merchantConfig);
		}
	}

	/**
	 * @throws ConfigException 
	 * 
	 */
	private void authenticationException() throws ConfigException{
		Utility.log(logger, GlobalLabelParameters.AUTH_ERROR , authenticationType, Level.FATAL);
		try {
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		} finally{
		Utility.log(logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		}
		
	}

	/**
	 * 
	 */
	private void paymentEndException() {
		Utility.log(logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(logger, " ","", Level.OFF);
	}
}
