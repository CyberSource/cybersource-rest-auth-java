package com.cybersource.apisdk.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.jwt.JwtSignatureTokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + claimSet + Signature ) are set inside the HTTP
 * connection and the call is made to APIserver. On successful transaction,
 * response message and response code : 200 is obtained.
 */
public class JWTAuthenticationStrategy implements Headers {

	private boolean isSignatureAlert = false;
	private Logger logger = null;
	private RequestContext requestContext;
	private JwtSignatureTokenGenerator tokenGenerator;
	private HttpClientWrapper httpClientWrapper;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ClientException
	 * @throws ConfigException
	 */
	public JWTAuthenticationStrategy(MerchantConfig merchantConfig, RequestContext authRequestContext)
			throws ClientException, ConfigException {
		this.requestContext = authRequestContext;
		this.logger = merchantConfig.getLogger();
		tokenGenerator = new JwtSignatureTokenGenerator(merchantConfig, requestContext);
		httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig, requestContext);
	}

	/**
	 * @return response.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 * @throws SignatureGenerationException
	 *             - throws custom client exception.
	 */
	public Response getJwtConnection() throws ConfigException, ClientException, SignatureGenerationException {
		Response responseObj = null;
		Map<String, String> headerMap = setHeaderData();
		if (isSignatureAlert) {
			responseObj = new Response();
			responseObj.setResponseMessage("No Response");
		}
		responseObj = httpClientWrapper.sendRequest(headerMap);
		return responseObj;
	}

	/**
	 * Set Header-Data.
	 * 
	 * @throws ConfigException
	 * @throws SignatureGenerationException
	 * @throws ClientException
	 */
	@Override
	public Map<String, String> setHeaderData() throws ConfigException, SignatureGenerationException, ClientException {
		Map<String, String> headerMap = new HashMap<String, String>();
		/* Addition Request Headers : Content Type */
		Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
				Level.INFO);
		headerMap.put(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);

		/* Add Request Header :: "Signature" */
		// String encryptedJWTSignature = Authorization.getToken(merchantConfig,
		// requestContext);
		String encryptedJWTSignature = tokenGenerator.getToken();
		if (StringUtils.isBlank(encryptedJWTSignature)) {
			isSignatureAlert = true;
			return null;
		} else {
			headerMap.put("Authorization", "Bearer " + encryptedJWTSignature);
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, encryptedJWTSignature, Level.INFO);
		}

		/*
		 * Add Request Header :: "profile-id"
		 */
		if (requestContext.getRequestMethodType().equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			headerMap.put(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
			Utility.log(logger, GlobalLabelParameters.PROFILE_ID, " : ".concat(GlobalLabelParameters.PROFILE_ID_VAL),
					Level.INFO);
		}
		return headerMap;
	}

}
