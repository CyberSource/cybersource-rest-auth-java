package com.cybersource.authsdk.cache;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.JWEUtility;

public class Cache {
	private static Logger logger = LogManager.getLogger(Cache.class);	
	
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, CachedJWEPrivateKey> cachePEM = new ConcurrentHashMap<>();

	public Cache() {
	}

	/**
	 * @throws Exception 
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws CertificateException 
	 */
	private void setUpP12Cache(MerchantConfig merchantConfig) throws Exception {
		KeyCertificateGenerator newKeyCertGen = new KeyCertificateGenerator();
		X509Certificate jwtX509Certificate = newKeyCertGen.initializeCertificateOfMerchantForJWT(merchantConfig);
		RSAPrivateKey jwtRsaPrivateKey = newKeyCertGen.initializePrivateKey(merchantConfig);
		X509Certificate mleX509Certificate = newKeyCertGen.initializeMLECertificate(merchantConfig);
		
		Identity identity = new Identity();
		identity.setLastModifiedDate(getLastModifiedFileTime(merchantConfig.getKeyFile().getAbsolutePath()));
		identity.setX509(jwtX509Certificate);
		identity.setRsaPrivateKey(jwtRsaPrivateKey);
		identity.setMleCert(mleX509Certificate);
		String tempMerchantID = merchantConfig.getMerchantID();
		cacheP12.put(tempMerchantID, identity);
	}
	
	public Identity getMerchantP12KeysFromCache(MerchantConfig merchantConfig) throws Exception{
		if (cacheP12.isEmpty() || !cacheP12.containsKey(merchantConfig.getMerchantID())) {
			logger.info(GlobalLabelParameters.CACHE_BEGIN);
			setUpP12Cache(merchantConfig);
		} else {
			Identity p12Keys = cacheP12.get(merchantConfig.getMerchantID());
			if (p12Keys==null || p12Keys.getLastModifiedDate() != getLastModifiedFileTime(merchantConfig.getKeyFile().getAbsolutePath())) {
				logger.info(GlobalLabelParameters.CACHE_EXTEND);
				setUpP12Cache(merchantConfig);
			}else {
				logger.info(GlobalLabelParameters.CACHE_EXTRACT);
			}
			
		}
		//return all three p12 file keys and certs
		Identity p12Keys = cacheP12.get(merchantConfig.getMerchantID());
		return p12Keys;
	}

	public PrivateKey getJWECachedPrivateKey(MerchantConfig merchantConfig) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (cachePEM.isEmpty()) {
			setUpPEMCache(merchantConfig);
		} else {
			CachedJWEPrivateKey cachedJWEPrivateKey = cachePEM.get("privateKeyFromPEMFile");
			if (cachedJWEPrivateKey.getLastModifiedTimeStamp() != getLastModifiedFileTime(merchantConfig.getPemFileDirectory())) {
				setUpPEMCache(merchantConfig);
			}
		}
		CachedJWEPrivateKey cachedJWEPrivateKey = cachePEM.get("privateKeyFromPEMFile");
		return cachedJWEPrivateKey.getPrivateKey();
	}
	
	private void setUpPEMCache(MerchantConfig merchantConfig) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey privateKey = JWEUtility.readPemFileToGetPrivateKey(merchantConfig.getPemFileDirectory());
		long lastModifiedTime = getLastModifiedFileTime(merchantConfig.getPemFileDirectory());
		CachedJWEPrivateKey cachedJWEPrivateKey = new CachedJWEPrivateKey();
		cachedJWEPrivateKey.setLastModifiedTimeStamp(lastModifiedTime);
		cachedJWEPrivateKey.setPrivateKey(privateKey);
		cachePEM.put("privateKeyFromPEMFile", cachedJWEPrivateKey);
	}

	
	/**
	 * 
	 * @param path
	 * @return
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
