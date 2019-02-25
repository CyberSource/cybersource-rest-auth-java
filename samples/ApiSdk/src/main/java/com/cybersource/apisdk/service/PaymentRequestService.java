package com.cybersource.apisdk.service;

import org.apache.commons.lang.StringUtils;

import com.cybersource.apisdk.connection.HttpClientWrapper;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
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
	 * @throws SignatureGenerationException
	 *             - throws custom signature exception.
	 */
	public Response processPaymentRequest() throws ConfigException, ClientException, SignatureGenerationException {
		if (StringUtils.isNotBlank(merchantConfig.getAuthenticationType())) {
			HttpClientWrapper httpclientwrappper = HttpClientWrapper.getInstance(merchantConfig);
			return httpclientwrappper.sendRequest(requestContext);
		} else {
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
	}
}
