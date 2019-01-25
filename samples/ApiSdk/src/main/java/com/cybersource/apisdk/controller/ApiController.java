package com.cybersource.apisdk.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.apisdk.service.PaymentRequestService;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will make call as per request type.
 */
public class ApiController {

	private Logger logger;
	private boolean isAuthicationTypeValid;
	private boolean isMerchantConfigValid;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - if some value is missing for merchant..
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentGet(MerchantConfig merchantConfig, String requestData) throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestData);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPost(MerchantConfig merchantConfig, String requestData) throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestData);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPut(MerchantConfig merchantConfig, String requestData) throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestData);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentDelete(MerchantConfig merchantConfig, String requestData) throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestData);
	}
	
	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPatch(MerchantConfig merchantConfig, String requestData)	throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestData);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	private Response processPayment(MerchantConfig merchantConfig, String requestData) throws ConfigException, ClientException {
		Response responseObj = null;
		try {
			logger = Log4j.getInstance(merchantConfig);
			Utility.log(logger, GlobalLabelParameters.BEGIN_TRANSACTION, "", Level.INFO);
			commonApiControllerCheck(merchantConfig);
			if (isAuthicationTypeValid) {
				if (isMerchantConfigValid) {
					Utility.log(logger, GlobalLabelParameters.USING_REQUEST_TARGET, merchantConfig.getRequestTarget(), Level.INFO);
					Utility.log(logger, GlobalLabelParameters.AUTENTICATION_TYPE, merchantConfig.getAuthenticationType(), Level.INFO);
					Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE, " : " + MerchantUtil.getRequestType(), Level.INFO);
					Utility.log(logger, GlobalLabelParameters.MERCH_CONF_LABEL, " " + merchantConfig.toString(), Level.INFO);
					PaymentRequestService paymentRequest = new PaymentRequestService(merchantConfig, requestData, logger);
					responseObj = paymentRequest.processPaymentRequest();
					return responseObj;
				} else {
					Utility.log(logger, GlobalLabelParameters.MERCHANT_VALIDATION_ERROR, "", Level.INFO);
					throw new ConfigException(GlobalLabelParameters.MERCHANT_VALIDATION_ERROR);
				}
			} else {
				authenticationException(merchantConfig.getAuthenticationType());
			}
			return responseObj;

		} finally {
			Utility.log(logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
			Utility.log(logger, " ", "", Level.OFF);
		}
    }

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 */
	public void commonApiControllerCheck(MerchantConfig merchantConfig) throws ConfigException {
		/* Retrieve authentication type for validating. */
		String authenticationType = merchantConfig.getAuthenticationType();
		isAuthicationTypeValid = Utility.checkAuthenticationValidation(authenticationType);
		if (MerchantUtil.isSetMerchantDetails()) {
			/* Validate Merchant Details. */
			isMerchantConfigValid = MerchantUtil.validateMerchantDetails();
		} else {
			logger = Log4j.getInstance(merchantConfig);
		}
	}

	/**
	 * 
	 * @throws ConfigException
	 *             - contains information for merchant.
	 */
	private void authenticationException(String authenticationType) throws ConfigException {
		Utility.log(logger, GlobalLabelParameters.AUTH_ERROR, authenticationType, Level.FATAL);
		throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
	}
}
