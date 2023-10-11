package com.cybersource.authsdk.cache;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.JWEUtility;

public class Cache {
	private static Logger logger = LogManager.getLogger(Cache.class);	
	public static boolean isCache = false;
	public static long lastModifiedTime;
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, CachedJWEPrivateKey> cachePEM = new ConcurrentHashMap<>();

	private boolean isTimeStamp;
	private MerchantConfig merchantConfig;
	private Identity identity = new Identity();
	private CachedJWEPrivateKey cachedJWEPrivateKey = new CachedJWEPrivateKey();
	private X509Certificate x509Certificate;
	private RSAPrivateKey rsaPrivateKey;
	private String merchantID;
	private String accessToken;
	private String refreshToken;

	public void setX509Certificate(X509Certificate x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRsaPrivateKey(RSAPrivateKey rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public RSAPrivateKey getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public X509Certificate getX509Certificate() {
		return x509Certificate;
	}

	/**
	 * @param merchantConfig - contains all information for merchant.
	 */
	public Cache(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
		logger = LogManager.getLogger(this.getClass());
		this.merchantID = merchantConfig.getMerchantID();
		if (cacheP12.isEmpty()) {
			Cache.isCache = true;
		} else {
			Cache.isCache = false;
		}
	}

	/**
	 * @throws ConfigException - if some value is missing or wrong for merchant.
	 */
	public void setP12FileDetailsInCache() throws ConfigException {
		if (cacheP12.isEmpty()) {
			setUpP12Cache();
		} else {
			isTimeStamp = isLastModifiedTimeP12();
			if (isTimeStamp) {
				retrieveP12DataFromCache();
			} else {
				setUpP12Cache();
			}
		}
	}

	/**
	 * @throws ConfigException - if some value is missing or wrong for merchant.
	 */
	public void setUpP12Cache() throws ConfigException {
		identity.setLastModifiedDate(getLastModifiedFileTime(merchantConfig.getKeyFile().getAbsolutePath()));
		identity.setX509(x509Certificate);
		identity.setRsaPrivateKey(rsaPrivateKey);
		String tempMerchantID = merchantConfig.getMerchantID();
		cacheP12.put(tempMerchantID, identity);
		lastModifiedTime = merchantConfig.getKeyFile().lastModified();
	}

	public void retrieveP12DataFromCache() {
		Identity tempIdentity = cacheP12.get(merchantID);
		if (tempIdentity != null) {
			x509Certificate = tempIdentity.getX509();
			rsaPrivateKey = tempIdentity.getRsaPrivateKey();
		}
	}

	/**
	 * @return true if file is modified else return false.
	 */
	public boolean isLastModifiedTimeP12() {
		Identity tempIdentity = cacheP12.get(merchantID);
		if (tempIdentity != null) {
			long tempLastModifiedTime = tempIdentity.getLastModifiedDate();
			if (lastModifiedTime == tempLastModifiedTime) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public PrivateKey getJWECachedPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (cachePEM.isEmpty()) {
			setUpPEMCache();
		} else {
			CachedJWEPrivateKey cachedJWEPrivateKey = cachePEM.get("privateKeyFromPEMFile");
			if (cachedJWEPrivateKey.getLastModifiedTimeStamp() != getLastModifiedFileTime(merchantConfig.getPemFileDirectory())) {
				setUpPEMCache();
			}
		}
		CachedJWEPrivateKey cachedJWEPrivateKey = cachePEM.get("privateKeyFromPEMFile");
		return cachedJWEPrivateKey.getPrivateKey();
	}
	
	private void setUpPEMCache() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey privateKey = JWEUtility.readPemFileToGetPrivateKey(merchantConfig.getPemFileDirectory());
		long lastModifiedTime = getLastModifiedFileTime(merchantConfig.getPemFileDirectory());
		cachedJWEPrivateKey.setLastModifiedTimeStamp(lastModifiedTime);
		cachedJWEPrivateKey.setPrivateKey(privateKey);
		cachePEM.put("privateKeyFromPEMFile", cachedJWEPrivateKey);
	}

	
	/**
	 * @return value for last modified.
	 */
	@SuppressWarnings("null")
	public long getLastModifiedFileTime(String path) {
		File f;
		try {
			f = new File(path);
			return f.lastModified();
		} catch (Exception e) {
			logger.error(e);
			return (Long) null;
		}
	}
}
