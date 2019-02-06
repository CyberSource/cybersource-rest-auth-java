package com.cybersource.apisdk.service;

import com.cybersource.apisdk.connection.HttpAuthenticationStrategy;
import com.cybersource.apisdk.connection.JWTAuthenticationStrategy;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * Here the call is made to respective connection class (HTTPConnection or
 * JWTConnection) depending on the Authentication type.
 */
public class PaymentRequestService {

	private MerchantConfig merchantConfig;
	private RequestContext requestContext;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @param logger
	 *            - logger to log all information in log file.
	 */
	public PaymentRequestService(MerchantConfig merchantConfig, RequestContext requestContext) {
		this.merchantConfig = merchantConfig;
		this.requestContext = requestContext;
	}

	/**
	 * 
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response processPaymentRequest() throws ConfigException, ClientException {
		String authenticatonType = merchantConfig.getAuthenticationType();
		if (authenticatonType != null && authenticatonType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			HttpAuthenticationStrategy connection = new HttpAuthenticationStrategy(merchantConfig, requestContext);
			return connection.getHttpConnection();
		} else if (authenticatonType != null && authenticatonType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			JWTAuthenticationStrategy jwtConnection = new JWTAuthenticationStrategy(merchantConfig, requestContext);
			return jwtConnection.getJwtConnection();
		} else {
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
	}
}
