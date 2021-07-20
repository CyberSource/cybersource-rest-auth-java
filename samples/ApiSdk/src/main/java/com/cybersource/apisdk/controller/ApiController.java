package com.cybersource.apisdk.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.PaymentRequestService;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

public class ApiController {
	private static Logger logger = LogManager.getLogger(ApiController.class);
	
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
				logger.info("{} : {}", GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType);
				try {
					PaymentRequestService paymentRequest = new PaymentRequestService(merchantConfig);
					responseObj =  paymentRequest.processPaymentRequest();
					logger.info(GlobalLabelParameters.END_TRANSACTION);
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
		if (merchantConfig.isMerchantDetailsSet()) {
			logger.info(GlobalLabelParameters.BEGIN_TRANSACTION);
			/* Validate Merchant Details */
			isMerchantDetail = merchantConfig.validateMerchantDetails();
		}
	}

	/**
	 * @throws ConfigException 
	 * 
	 */
	private void authenticationException() throws ConfigException{
		logger.fatal(GlobalLabelParameters.AUTH_ERROR);
		try {
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		} finally{
			logger.info(GlobalLabelParameters.END_TRANSACTION);
		}
	}

	/**
	 * 
	 */
	private void paymentEndException() {
		logger.info(GlobalLabelParameters.END_TRANSACTION);
	}
}
