package com.cybersource.authsdk.jwtsecurity;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

public interface CryptoProcessor {
	String encrypt(String paramString, X509Certificate paramX509Certificate);

	String encrypt(String paramString, X509Certificate paramX509Certificate,
			Map<String, Object> paramMap);

	String signAndEncrypt(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2);

	String signAndEncrypt(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2, Map<String, Object> paramMap);

	String sign(String paramString, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate);

	String sign(String paramString, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate,
			Map<String, Object> paramMap);

	String validateSignature(String paramString, X509Certificate paramX509Certificate);

	String validateSignature(String paramString, byte[] paramArrayOfByte);

	boolean isValidSignature(String paramString, X509Certificate paramX509Certificate);

	boolean isValidSignature(String paramString, byte[] paramArrayOfByte);

	String decryptAndValidateSignature(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate);

	String decrypt(String paramString, PrivateKey paramPrivateKey);

	BigInteger getModulus(String paramString);

	String getKid(String paramString);

	boolean isEncrypted(String paramString);

	String getSerialNumber(String paramString1, String paramString2);

	String getPayload(String paramString);

	AuthAttributes getAuthAttributes(String paramString1, String paramString2);

	String getCustomParam(String paramString1, String paramString2);

	String createBodyDigest(String paramString);

	JWTPayload getJWTPayload(String paramString);
}
