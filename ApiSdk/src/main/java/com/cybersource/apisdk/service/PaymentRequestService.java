package com.cybersource.apisdk.service;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.connection.HttpConnection;
import com.cybersource.apisdk.connection.JwtConnection;
import com.cybersource.apisdk.model.Response;
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
	 * @throws IOException 
	 */
	public Response processPaymentRequest() throws IOException {

		if (authenticatonType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			HttpConnection connection = new HttpConnection(merchantConfig, logger);
			return connection.httpConnection();
		} else if (authenticatonType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			JwtConnection jwtConnection=new JwtConnection(merchantConfig, logger);
			return jwtConnection.getJwtConnection();
		}
		return null;
		
	}
}
