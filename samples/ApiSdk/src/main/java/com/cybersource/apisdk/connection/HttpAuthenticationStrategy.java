package com.cybersource.apisdk.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.http.HttpSignatureTokenGenerator;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + digest + Signature ) are set inside the HTTP
 * connection and the call is made to API server and response message and code
 * is obtained.
 */
public class HttpAuthenticationStrategy implements Headers {

	private MerchantConfig merchantConfig;
	private Logger logger;
	private RequestContext requestContext;
	private String requestMethodType;
	private HttpSignatureTokenGenerator tokenGenerator;
	private HttpClientWrapper httpClientWrapper;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant. @param requestContext -
	 *            contains request details @param logger - logger to log all
	 *            information in log file. @throws ClientException - if some
	 *            error will occur. @throws ConfigException @throws
	 */
	public HttpAuthenticationStrategy(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ClientException, ConfigException {

		this.merchantConfig = merchantConfig;
		this.requestContext = requestContext;
		this.logger = merchantConfig.getLogger();
		this.requestMethodType = requestContext.getRequestMethodType();
		tokenGenerator = new HttpSignatureTokenGenerator(merchantConfig, requestContext);
		httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig, requestContext);
	}

	public Map<String, String> setHeaderData() throws ConfigException, ClientException, SignatureGenerationException {

		Map<String, String> headerMap = new HashMap<String, String>();
		PayloadDigest payloadDigest;
		String digestMessageBody;
		/* Add Request Headers :: Content Type */
		/* Add Request Headers :: Digest */
		if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
					" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
			headerMap.put(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			payloadDigest = new PayloadDigest(requestContext.getRequestBody());
			digestMessageBody = payloadDigest.getDigest();
			headerMap.put(GlobalLabelParameters.DIGEST, payloadDigest.getDigest());
			Utility.log(logger, GlobalLabelParameters.DIGEST, " : ".concat(digestMessageBody), Level.INFO);

			/*
			 * Add Request Header :: "profile-id"
			 */
			if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				headerMap.put(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
				Utility.log(logger, GlobalLabelParameters.PROFILE_ID,
						" : ".concat(GlobalLabelParameters.PROFILE_ID_VAL), Level.INFO);
			}
		}

		try {
			/*
			 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
			 * Merchant ID or v-c-merchant-id This ID can be found on portal
			 */
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANT_ID, " : ".concat(merchantConfig.getMerchantID()),
					Level.INFO);
			headerMap.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, "", Level.FATAL);
			Utility.log(logger, e);
			throw new ConfigException(GlobalLabelParameters.MERCHANTID_NOT_SET, e);
		}

		/*
		 * Add Request Header :: "Date" The date and time that the message was
		 * originated from. "HTTP-date" format as defined by RFC7231.
		 */
		headerMap.put(GlobalLabelParameters.DATE, requestContext.getDate());
		Utility.log(logger, GlobalLabelParameters.DATE, " : ".concat(requestContext.getDate()), Level.INFO);
		/*
		 * Add Request Header :: "Host" Sandbox Host :: apitest.cybersource.com
		 * Production Host :: api.cybersource.com
		 */
		headerMap.put(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());
		Utility.log(logger, GlobalLabelParameters.HOST, " : ".concat(merchantConfig.getRequestHost()), Level.INFO);
		/* Add Request Header :: "Signature" */
		// String signatureHeaderValue =
		// Authorization.getToken(merchantConfig, requestContext);
		String signatureHeaderValue = tokenGenerator.getToken();
		headerMap.put(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
		Utility.log(logger, GlobalLabelParameters.SIGNATURE, " : ".concat(signatureHeaderValue), Level.INFO);

		/* Addition Request Headers : User Agent */
		headerMap.put(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
		Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
				Level.INFO);
		return headerMap;
	}

	public Response getHttpConnection() throws ConfigException, ClientException, SignatureGenerationException {
		return httpClientWrapper.sendRequest(setHeaderData());
	}
}
