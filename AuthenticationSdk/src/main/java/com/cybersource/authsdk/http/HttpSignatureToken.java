package com.cybersource.authsdk.http;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
import org.apache.logging.log4j.Logger;

/* HttpSigToken return SignatureHeader Value that contains following paramters
 * keyid     -- Merchant ID obtained from EBC portal
 * algorithm -- Should have value as "HmacSHA256"
 * headers   -- List of all header name passed in the Signature paramter 
                Note: Digest is not passed for GET calls
 * signature -- Signature header has paramter called signature
                Paramter 'Signature' must contain all the paramters mentioned in header above in given order  */

public class HttpSignatureToken implements TokenGenerator {

	private MerchantConfig merchantConfigSigHead = null;
	private StringBuilder signatureHeader;
	private SignatureGenerator sigParam;
	private String merchantkeyIdSig = null;
	private String merchantSecertKeySig = null;
	private String httpMethodSig = null;
	private String httpMerchantID = null;
	private String httpGetID = null;
	private Logger logger;

	/**
	 * 
	 * @param merchantConfig
	 * @param merchantsecretKey
	 * @param merchantKeyId
	 * @param merchantId
	 * @param http_Get_ID
	 * @param method
	 * @throws Exception
	 */
	public HttpSignatureToken(MerchantConfig merchantConfig, String merchantKeyId, String merchantsecretKey,
			String merchantId, String httpMethod, String getID) throws Exception {

		this.merchantConfigSigHead = merchantConfig;
		this.merchantkeyIdSig = merchantKeyId;
		this.merchantSecertKeySig = merchantsecretKey;
		this.httpMethodSig = httpMethod;
		this.httpMerchantID = merchantId;
		this.httpGetID = getID;
		this.logger = Log4j.getInstance(merchantConfig);
	}

	@Override
	public String getToken() {
		String signature;
		try {
			signature = signatureHeader();
		} catch (Exception e) {
			Utility.log(logger, e);
			signature =  null;
		}
		return signature;
	}

	private String signatureHeader() throws Exception {

		signatureHeader = new StringBuilder();
		/* KeyId is the key obtained from EBC */
		signatureHeader.append("keyid=\"" + merchantkeyIdSig + "\"");
		/* Algorithm should be always HmacSHA256 for http signature */
		signatureHeader.append(", algorithm=\"HmacSHA256\"");

		if (httpMethodSig.equalsIgnoreCase(GlobalLabelParameters.GET))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.GET) + "\"");
		if (httpMethodSig.equalsIgnoreCase(GlobalLabelParameters.DELETE))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.DELETE) + "\"");
		if (httpMethodSig.equalsIgnoreCase(GlobalLabelParameters.POST))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.POST) + "\"");
		else if (httpMethodSig.equalsIgnoreCase(GlobalLabelParameters.PUT))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PUT) + "\"");
		/*
		 * Get Value for paramter 'Signature' to be passed to Signature Header
		 */
		String signatureValue = new SignatureGenerator(this.merchantConfigSigHead, merchantSecertKeySig, httpMerchantID,
				httpMethodSig, httpGetID).signatureGeneration();
		signatureHeader.append(", signature=\"" + signatureValue + "\"");
		return signatureHeader.toString();
	}

	/**
	 * 
	 * @param requestType
	 *            : GET/POST/PUT
	 * @return
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
		default:
			return requestHeader;
		}
		return requestHeader;
	}
}
