package com.cybersource.apisdk.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.connection.HttpAuthenticationStrategy;
import com.cybersource.apisdk.connection.JWTAuthenticationStrategy;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * Here the call is made to respective connection class (HTTPConnection or
 * JWTConnection) depending on the Authentication type.
 */
public class PaymentRequestService {

	private MerchantConfig merchantConfig;
	private String requestData;
	private Logger logger;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request payload.
	 * @param logger
	 *            - logger to log all information in log file.
	 */
	public PaymentRequestService(MerchantConfig merchantConfig, String requestData, Logger logger) {

		this.merchantConfig = merchantConfig;
		this.requestData = requestData;
		this.logger = logger;
	}

	/**
	 * 
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
	 * @throws ClientException
	 *             - throws custom client exception.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm will be not there.
	 */
	public Response processPaymentRequest()	throws ConfigException, ClientException {
       String authenticatonType = merchantConfig.getAuthenticationType();
       if (authenticatonType != null && authenticatonType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
   			 HttpAuthenticationStrategy connection = new HttpAuthenticationStrategy(merchantConfig, requestData, logger);
   			 return connection.getHttpConnection();
   		} else if (authenticatonType != null && authenticatonType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
   			 JWTAuthenticationStrategy jwtConnection = new JWTAuthenticationStrategy(merchantConfig, requestData,logger);
   			 return jwtConnection.getJwtConnection();
   		} else {
   			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
   		}
	}
}
