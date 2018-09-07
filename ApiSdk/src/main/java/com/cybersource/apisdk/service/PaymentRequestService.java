package com.cybersource.apisdk.service;

import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.connection.HttpConnection;
import com.cybersource.apisdk.connection.JwtUrlConnection;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * Here the call is made to respective connection class (HTTPConnection or
 * JWTConnection) depending on the Authentication type.
 * 
 * 
 *
 */
public class PaymentRequestService {

	private String authenticatonType = null;
	private MerchantConfig merchantConfig;
	private Logger logger;

	
	/**
	 * 
	 * @param merchantConfig
	 * @param logger
	 */
	public PaymentRequestService(MerchantConfig merchantConfig, Logger logger) {

		authenticatonType = merchantConfig.getAuthenticationType().trim();
		this.merchantConfig = merchantConfig;
		this.logger = logger;
	}

	/**
	 */
	public void processPaymentRequest() {

		if (authenticatonType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			new HttpConnection(merchantConfig, logger);
		} else if (authenticatonType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			new JwtUrlConnection(merchantConfig, logger);
		}
	}
}
