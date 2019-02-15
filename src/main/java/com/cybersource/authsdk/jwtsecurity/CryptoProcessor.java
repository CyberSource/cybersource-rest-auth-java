package com.cybersource.authsdk.jwtsecurity;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

public interface CryptoProcessor {

	String sign(String paramString, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate,
			Map<String, Object> paramMap);

}
