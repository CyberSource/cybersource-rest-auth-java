package com.cybersource.authsdk.payloaddigest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * This class will generate Digest value which is SHA-256 hash of payLoad that
 * is BASE64 encoded
 */
public class PayloadDigest {

	private String requestData;

	/**
	 * @param requestData
	 *            - request data.
	 */
	public PayloadDigest(String requestData) {
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
			byte[] digestBytes = MessageDigest.getInstance(GlobalLabelParameters.SHA_256)
					.digest(getPayLoad().getBytes("UTF-8"));
			return GlobalLabelParameters.SHA_256 + "=" + Base64.getEncoder().encodeToString(digestBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new ConfigException(GlobalLabelParameters.NO_SUCH_ALGORITHM);
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
	 */
	public String getDigest() throws ConfigException {
		return digestGeneration();
	}
}
