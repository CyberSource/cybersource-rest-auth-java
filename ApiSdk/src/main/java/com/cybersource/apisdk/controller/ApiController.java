package com.cybersource.apisdk.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import com.cybersource.apisdk.service.PaymentRequestService;
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

	/**
	 * 
	 * @param merchantConfig
	 */
	public void paymentGet(MerchantConfig merchantConfig) {
		processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 */
	public void paymentPost(MerchantConfig merchantConfig) {
		processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 * 
	 */
	public void paymentPut(MerchantConfig merchantConfig) {
		processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : This object contains all the details present in the
	 *            cybs.properties.
	 * 
	 */
	public void paymentDelete(MerchantConfig merchantConfig) {
		processPayment(merchantConfig);
	}

	/**
	 * 
	 * @param merchantConfig
	 */
	private void processPayment(MerchantConfig merchantConfig) {
		commonApiControllerCheck(merchantConfig);
		if (authType) {
			if (isMerchantDetail) {
				Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE , authenticationType,
						Level.INFO);
				try {
					PaymentRequestService paymentRequest = new PaymentRequestService(merchantConfig, this.logger);
					paymentRequest.processPaymentRequest();
					Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
					Utility.log(this.logger, " ","", Level.OFF);
				} catch (Exception e) {
					paymentEndException();
				}
			} else {
				paymentEndException();
			}
		} else {
			authenticationException();
		}
	}

	/**
	 * @param merchantConfig
	 * 
	 */
	public void commonApiControllerCheck(MerchantConfig merchantConfig) {
		/* Retrieve authentication type for validating. */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		authType = Utility.checkAuthenticationValidation(authenticationType);
		if (MerchantConfig.isSetMerchantDetails) {
			/* Initializing the logging object. */
			this.logger = Log4j.getInstance(merchantConfig);
			Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSCATION,"", Level.INFO);
			/* Validate Merchant Details. */
			isMerchantDetail = merchantConfig.validateMerchantDetails(this.logger);
		} else {
			this.logger = Log4j.getInstance(merchantConfig);
		}
	}

	/**
	 * 
	 */
	private void authenticationException() {
		Utility.log(this.logger, GlobalLabelParameters.AUTH_ERROR , authenticationType, Level.FATAL);
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		
	}

	/**
	 * 
	 */
	private void paymentEndException() {
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(this.logger, " ","", Level.OFF);
	}
}
