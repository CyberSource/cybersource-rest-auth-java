package com.cybersource.authsdk.payloaddigest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * This class will generate Digest value which is SHA-256 hash of payLoad that
 * is BASE64 encoded
 */
public class PayloadDigest {

	private MerchantConfig merchantConfig;
	private String requestData;

	/**
	 * @param merchantConfig-
	 *            contains all information for merchant.
	 * @param requestData
	 *            - request data.
	 */
	public PayloadDigest(MerchantConfig merchantConfig, String requestData) {
		this.merchantConfig = merchantConfig;
		this.requestData = requestData;
	}

	/**
	 * @return generated digest from requestBody.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	  */
	private String digestGeneration() throws ConfigException {
		/*
		 * This method return Digest value which is SHA-256 hash of payLoad that
		 * is BASE64 encoded
		 */
		try {
			String messageBody = getPayLoad();
			MessageDigest digestString;
			byte[] digestBytes = null;
			digestString = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
			digestBytes = digestString.digest(messageBody.getBytes("UTF-8"));
			String bluePrint = Base64.getEncoder().encodeToString(digestBytes);
			bluePrint = GlobalLabelParameters.SHA_256 + "=" + bluePrint;
			return bluePrint;
		} catch (NoSuchAlgorithmException e) {
			throw new ConfigException(GlobalLabelParameters.No_SUCH_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
		}
	}

	/**
	 * @return payLoad generated.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public String getPayLoad() throws ConfigException {
		return requestData;
	}

	/**
	 * 
	 * @return Digest generated from payload.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws UnsupportedEncodingException
	 *             - if encoding is not supported.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm is not available.
	 */
	public String getDigest() throws ConfigException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return digestGeneration();
	}
}
