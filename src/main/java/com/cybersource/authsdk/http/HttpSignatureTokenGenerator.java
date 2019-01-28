package com.cybersource.authsdk.http;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.Utility;

/* HttpSigToken return SignatureHeader Value that contains following parameters
 * keyId     -- Merchant ID obtained from EBC portal
 * algorithm -- Should have value as "HmacSHA256"
 * headers   -- List of all header name passed in the Signature parameter 
                Note: Digest is not passed for GET calls
 * signature -- Signature header has parameter called signature
                Parameter 'Signature' must contain all the parameters mentioned in header above in given order  */

public class HttpSignatureTokenGenerator implements TokenGenerator {

	private MerchantConfig merchantConfig = null;
	private String httpMethod = null;
	private String requestData;
	private Logger log;

	/**
	 * @param merchantConfig
	 *            -contains information for merchant.
	 * @param requestData
	 *            - request data.
	 */
	public HttpSignatureTokenGenerator(MerchantConfig merchantConfig, String requestData) {

		this.merchantConfig = merchantConfig;
		this.requestData = requestData;
		this.httpMethod = MerchantUtil.getRequestType();
		this.log = Log4j.getInstance(merchantConfig);
	}

	/**
	 * Method to get Token.
	 * 
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	@Override
	public String getToken() throws ConfigException {
		String signature = null;
		try {
			try {
				signature = getSignatureHeader();
			} catch (UnsupportedEncodingException e) {
				Utility.log(log, GlobalLabelParameters.UNSUPPORTED_ENCODING, "", Level.INFO);
				throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
			}
		} catch (InvalidKeyException e) {
			Utility.log(log, GlobalLabelParameters.INVALID_KEY, "", Level.INFO);
			throw new ConfigException(GlobalLabelParameters.INVALID_KEY);

		} catch (NoSuchAlgorithmException e) {
			Utility.log(log, GlobalLabelParameters.No_SUCH_ALGORITHM, "", Level.INFO);
			throw new ConfigException(GlobalLabelParameters.No_SUCH_ALGORITHM);

		} catch (IllegalArgumentException e) {
			Utility.log(log, GlobalLabelParameters.UNSUPPORTED_ENCODING, "", Level.INFO);
			throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.DIGEST_GEN_FAILED, "", Level.INFO);
			throw new ConfigException(GlobalLabelParameters.DIGEST_GEN_FAILED);
		}
		return signature;
	}

	/**
	 * @return headers to generate signature.
	 * 
	 * @throws InvalidKeyException
	 *             - if key is not valid.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm is not available.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws UnsupportedEncodingException
	 *             - if encoding is not supported.
	 */
	private String getSignatureHeader()
			throws InvalidKeyException, NoSuchAlgorithmException, ConfigException, UnsupportedEncodingException {

		StringBuilder signatureHeader = new StringBuilder();
		/* KeyId is the key obtained from EBC */
		signatureHeader.append("keyid=\"" + merchantConfig.getMerchantKeyId() + "\"");
		/* Algorithm should be always HmacSHA256 for http signature */
		signatureHeader.append(", algorithm=\"HmacSHA256\"");

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.GET) + "\"");
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.DELETE) + "\"");
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.POST) + "\"");

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PATCH) + "\"");

		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PUT) + "\"");
		/*
		 * Get Value for paramter 'Signature' to be passed to Signature Header
		 */
		String signatureValue = new SignatureGenerator(merchantConfig, requestData, httpMethod).generateSignature();
		signatureHeader.append(", signature=\"" + signatureValue + "\"");
		return signatureHeader.toString();
	}

	/**
	 * 
	 * @param requestType
	 *            : GET/POST/PUT
	 * @return request header as per request type.
	 */
	private String getRequestHeaders(String requestType) {
		String requestHeader = null;
		switch (requestType) {
		case GlobalLabelParameters.GET:
			requestHeader = "host date (request-target)" + " " + "v-c-merchant-id";
			break;
		case GlobalLabelParameters.DELETE:
			requestHeader = "host date (request-target)" + " " + "v-c-merchant-id";
			break;
		case GlobalLabelParameters.POST:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		case GlobalLabelParameters.PUT:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		case GlobalLabelParameters.PATCH:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		default:
			return requestHeader;
		}
		return requestHeader;
	}
}
