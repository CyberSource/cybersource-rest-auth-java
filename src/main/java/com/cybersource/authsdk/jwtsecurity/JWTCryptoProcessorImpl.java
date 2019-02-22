package com.cybersource.authsdk.jwtsecurity;

import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.util.Utility;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64;

public class JWTCryptoProcessorImpl implements CryptoProcessor {
	private Logger logger;
	private static volatile CryptoProcessor cryptoProcessor;

	private JWTCryptoProcessorImpl(Logger logger) {
		this.logger = logger;
	}

	public static CryptoProcessor getInstance(Logger logger) {
		if (cryptoProcessor == null) {
			synchronized (CryptoProcessor.class) {
				if (cryptoProcessor == null) {
					cryptoProcessor = new JWTCryptoProcessorImpl(logger);
				}
			}
		}
		return cryptoProcessor;
	}

	public String sign(String content, PrivateKey privateKey, X509Certificate x509Certificate,
			Map<String, Object> customHeaders) {
		return serializeToken(signPayload(content, privateKey, x509Certificate, customHeaders));
	}

	private JOSEObject signPayload(String content, PrivateKey privateKey, X509Certificate x509Certificate,
			Map<String, Object> customHeaders) {
		if ((isNullOrEmpty(content)) || (x509Certificate == null) || (privateKey == null)) {
			Utility.log(logger, "content or public certificate or Private key is null or empty", "", Level.ERROR);
			return null;
		}
		String serialNumber = null;
		String serialNumberPrefix = "SERIALNUMBER=";
		String principal = x509Certificate.getSubjectDN().getName().toUpperCase();
		int beg = principal.indexOf(serialNumberPrefix);
		if (beg >= 0) {
			int end = principal.indexOf(",", beg);
			if (end == -1) {
				end = principal.length();
			}
			serialNumber = principal.substring(beg + serialNumberPrefix.length(), end);
		} else {
			serialNumber = x509Certificate.getSerialNumber().toString();
		}
		List<com.nimbusds.jose.util.Base64> x5cBase64List = new ArrayList<Base64>();
		try {
			x5cBase64List.add(com.nimbusds.jose.util.Base64.encode(x509Certificate.getEncoded()));
		} catch (CertificateEncodingException e) {
			Utility.log(logger, "Can't signAndEncrypt the payload", e, Level.ERROR);
			return null;
		}
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		Payload payload = new Payload(content);

		JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).customParams(customHeaders).keyID(serialNumber)
				.x509CertChain(x5cBase64List).build();
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		try {
			RSASSASigner signer = new RSASSASigner(rsaPrivateKey);
			jwsObject.sign(signer);
			if (!jwsObject.getState().equals(JWSObject.State.SIGNED)) {
				Utility.log(logger, "Payload signing failed.", "", Level.ERROR);
				return null;
			}
		} catch (JOSEException joseException) {
			Utility.log(logger, "Can't signAndEncrypt the payload", joseException, Level.ERROR);
			return null;
		}
		return jwsObject;
	}

	private String serializeToken(JOSEObject joseObject) {
		return joseObject.serialize();
	}

	private boolean isNullOrEmpty(String string) {
		if ((string == null) || (string.trim().length() == 0)) {
			return true;
		}
		return false;
	}

}
