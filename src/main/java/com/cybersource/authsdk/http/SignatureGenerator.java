package com.cybersource.authsdk.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

/***
 * Module to generate digital signature which is then passed to signature header
 * parameter
 * <p>
 * The signature is created from the following values and then signed with
 * <code>Secret Key</code>
 * <p>
 * <strong>host</strong> - Sandbox
 * (<strong><code>apitest.cybersource.com</code></strong>) or Production
 * (<strong><code>api.cybersource.com</code></strong>) hostname
 * <p>
 * <strong>date</strong> - HTTP-date with format as defined by
 * <strong>RFC7231</strong>
 * <p>
 * <strong>request-target</strong> - Should be in format of httpMethod: path
 * <p>
 * 
 * <pre>
 *                 Example : <strong><code>post /pts/v2/payments</code></strong>
 * </pre>
 * <p>
 * <strong>Digest</strong> - Only needed for POST calls
 * <p>
 * <strong>v-c-merchant-id</strong> - Cybersource Merchant ID found on EBC
 * portal
 */
public class SignatureGenerator {

	private MerchantConfig merchantConfig;
	private String signatureParameterBase64Encoded;
	private String httpMethod;
	private String merchantSecretKey;
	private String merchantId;
	private String portfolioId;
	private boolean useMetaKey;

	/**
	 * @param merchantConfig-contains all information of merchant.
	 * @throws InvalidKeyException      - if key is not valid.
	 * @throws NoSuchAlgorithmException - if algorithm is not available.
	 */
//	public SignatureGenerator(MerchantConfig merchantConfig)
//			throws InvalidKeyException, NoSuchAlgorithmException, ConfigException {
//		this.merchantConfig = merchantConfig;
//		this.httpMethod = merchantConfig.getRequestType();
//		this.merchantSecretKey = merchantConfig.getMerchantSecretKey();
//		this.merchantId = merchantConfig.getMerchantID();
//		this.useMetaKey = merchantConfig.isUseMetaKey();
//		if (this.useMetaKey) {
//			this.portfolioId = merchantConfig.getPortfolioID();
//		}
//	}

	public SignatureGenerator(MerchantConfig merchantConfig, String method)
			throws InvalidKeyException, NoSuchAlgorithmException, ConfigException {
		this.merchantConfig = merchantConfig;
		this.httpMethod = method;
		this.merchantSecretKey = merchantConfig.getMerchantSecretKey();
		this.merchantId = merchantConfig.getMerchantID();
		this.useMetaKey = merchantConfig.isUseMetaKey();
		if (this.useMetaKey) {
			this.portfolioId = merchantConfig.getPortfolioID();
		}
	}

	/**
	 * @return generated signature.
	 * @throws NoSuchAlgorithmException - if algorithm is not available.
	 * @throws InvalidKeyException      - if key is not valid.
	 */
//	public String signatureGeneration() throws NoSuchAlgorithmException, InvalidKeyException {
//
//		StringBuilder signatureString = new StringBuilder();
//		signatureString.append('\n');
//		signatureString.append(GlobalLabelParameters.HOST.toLowerCase());
//		signatureString.append(": ");
//		signatureString.append(merchantConfig.getRequestHost());
//		signatureString.append('\n');
//		signatureString.append(GlobalLabelParameters.DATE.toLowerCase());
//		signatureString.append(": ");
//		signatureString.append(PropertiesUtil.getNewDate());
//		signatureString.append('\n');
//		signatureString.append("request-target");
//		signatureString.append(": ");
//
//		String requestTarget = null;
//		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)) {
//			requestTarget = getRequestTarget(GlobalLabelParameters.GET);
//		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)) {
//			requestTarget = getRequestTarget(GlobalLabelParameters.POST);
//		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
//			requestTarget = getRequestTarget(GlobalLabelParameters.PUT);
//		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
//			requestTarget = getRequestTarget(GlobalLabelParameters.DELETE);
//		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
//			requestTarget = getRequestTarget(GlobalLabelParameters.PATCH);
//		}
//		signatureString.append(requestTarget);
//		signatureString.append('\n');
//
//		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
//				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
//				|| (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))) {
//			signatureString.append(GlobalLabelParameters.DIGEST.toLowerCase());
//			signatureString.append(": ");
//			signatureString.append(new PayloadDigest(merchantConfig).getDigest());
//			signatureString.append('\n');
//		}
//
//		signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
//		signatureString.append(": ");
//		if (useMetaKey) {
//			signatureString.append(portfolioId);
//		} else {
//			signatureString.append(merchantId);
//		}
//		signatureString.delete(0, 1);
//		String signatureStr = signatureString.toString();
//
//		SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantSecretKey), "HmacSHA256");
//		Mac aKeyId = Mac.getInstance("HmacSHA256");
//		aKeyId.init(secretKey);
//		aKeyId.update(signatureStr.getBytes());
//		byte[] aHeaders = aKeyId.doFinal();
//		signatureParameterBase64Encoded = Base64.getEncoder().encodeToString(aHeaders);
//		secretKey = null;
//		return signatureParameterBase64Encoded;
//	}

	public String signatureGeneration(String requestData) throws NoSuchAlgorithmException, InvalidKeyException {

		StringBuilder signatureString = new StringBuilder();
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.HOST.toLowerCase());
		signatureString.append(": ");
		signatureString.append(merchantConfig.getRequestHost());
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.DATE.toLowerCase());
		signatureString.append(": ");
		signatureString.append(PropertiesUtil.getNewDate());
		signatureString.append('\n');
		signatureString.append("request-target");
		signatureString.append(": ");

		String requestTarget = null;
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.GET);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.POST);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.PUT);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.DELETE);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.PATCH);
		}
		signatureString.append(requestTarget);
		signatureString.append('\n');

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))) {
			signatureString.append(GlobalLabelParameters.DIGEST.toLowerCase());
			signatureString.append(": ");
			signatureString.append(new PayloadDigest(requestData).getDigest());
			signatureString.append('\n');
		}

		signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
		signatureString.append(": ");
		if (useMetaKey) {
			signatureString.append(portfolioId);
		} else {
			signatureString.append(merchantId);
		}
		signatureString.delete(0, 1);
		String signatureStr = signatureString.toString();

		SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantSecretKey), "HmacSHA256");
		Mac aKeyId = Mac.getInstance("HmacSHA256");
		aKeyId.init(secretKey);
		aKeyId.update(signatureStr.getBytes());
		byte[] aHeaders = aKeyId.doFinal();
		signatureParameterBase64Encoded = Base64.getEncoder().encodeToString(aHeaders);
		secretKey = null;
		return signatureParameterBase64Encoded;
	}

	/**
	 * 
	 * @param requestType - GET/PUT/POST/PATCH/DELETE
	 * @return request target as per request type.
	 */
	public String getRequestTarget(String requestType) {
		String requestTarget;
		switch (requestType) {
		case GlobalLabelParameters.POST:
			requestTarget = GlobalLabelParameters.POST.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
			break;
		case GlobalLabelParameters.GET:
			requestTarget = GlobalLabelParameters.GET.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
			break;
		case GlobalLabelParameters.PUT:
			requestTarget = GlobalLabelParameters.PUT.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
			break;
		case GlobalLabelParameters.DELETE:
			requestTarget = GlobalLabelParameters.DELETE.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
			break;
		case GlobalLabelParameters.PATCH:
			requestTarget = GlobalLabelParameters.PATCH.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
			break;
		default:
			requestTarget = null;
			break;
		}
		return requestTarget;
	}
}
