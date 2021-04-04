package com.cybersource.authsdk.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.cybersource.authsdk.core.ConfigException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/***
 * HttpSigToken returns SignatureHeader value that contains following parameters
 * <p>
 * keyid - Merchant ID obtained from EBC portal
 * <p>
 * algorithm - Should have value as "HmacSHA256"
 * <p>
 * headers - List of all header keys passed in the signature parameter
 * <p>
 * Note: Digest is not passed for GET calls
 * <p>
 * signature - Signature header has parameter called signature
 * <p>
 * Parameter 'signature' must contain all the parameters mentioned in header
 * above in given order
 */

public class HttpSignatureToken implements TokenGenerator {
	private MerchantConfig merchantConfigSigHead = null;
	private StringBuilder signatureHeader;
	private String merchantkeyId = null;
	private String httpMethod = null;
	private Logger logger;

	/**
	 * @param merchantConfig - merchant configuration information
	 */
	public HttpSignatureToken(MerchantConfig merchantConfig) {
		this.merchantConfigSigHead = merchantConfig;
		this.merchantkeyId = merchantConfig.getMerchantKeyId();
		this.httpMethod = merchantConfig.getRequestType();
		merchantConfig.getMerchantSecretKey();
		merchantConfig.getMerchantID();

		this.logger = LogManager.getLogger(this.getClass());
	}

	/**
	 * Method to get Token.
	 */
	@Override
	public String getToken() {
		String signature = null;
		try {
			signature = signatureHeader();
		} catch (NoSuchAlgorithmException e) {
			this.logger.error(e);
		} catch (InvalidKeyException e) {
			this.logger.error(e);
		} catch (ConfigException e) {
			this.logger.error(e);
		}
		return signature;
	}

	/**
	 * Get signature header to be sent as part of the request
	 * 
	 * @return string of headers used to generate signature.
	 * @throws InvalidKeyException      if key is not valid
	 * @throws NoSuchAlgorithmException if algorithm is not available
	 */
	private String signatureHeader() throws InvalidKeyException, NoSuchAlgorithmException, ConfigException {

		signatureHeader = new StringBuilder();
		signatureHeader.append("keyid=\"" + merchantkeyId + "\"");
		signatureHeader.append(", algorithm=\"HmacSHA256\"");

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.GET) + "\"");
		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.DELETE) + "\"");
		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.POST) + "\"");
		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PATCH) + "\"");
		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PUT) + "\"");

		String signatureValue = new SignatureGenerator(merchantConfigSigHead).signatureGeneration();
		signatureHeader.append(", signature=\"" + signatureValue + "\"");
		return signatureHeader.toString();
	}

	/**
	 * Get string of request headers to include as part of the request
	 * 
	 * @param requestType must be GET/POST/PUT/PATCH/DELETE
	 * @return request headers included according to the request type.
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
