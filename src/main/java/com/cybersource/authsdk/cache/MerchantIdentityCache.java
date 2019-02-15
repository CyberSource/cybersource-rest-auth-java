package com.cybersource.authsdk.cache;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * class for caching X509Certificate, lastModified KeyFile time and private key.
 *
 */
public class MerchantIdentityCache {
	private ConcurrentHashMap<String, Identity> p12IdentityMap = new ConcurrentHashMap<>();
	private static MerchantIdentityCache instance;
	public Logger log;

	public static MerchantIdentityCache getInstance(Logger logger) {
		if (instance == null) {
			synchronized (MerchantIdentityCache.class) {
				if (instance == null) {
					instance = new MerchantIdentityCache(logger);
				}
			}
		}
		return instance;
	}

	/**
	 * @param log
	 *            - contains logger.
	 */
	public MerchantIdentityCache(Logger log) {
		this.log = log;
	}

	/**
	 * @param merchantConfig
	 *            - contains merchant Configuration
	 * @throws SignatureGenerationException
	 * @throws ConfigException
	 */
	public Identity setUpP12Cache(MerchantConfig merchantConfig) throws SignatureGenerationException, ConfigException {
		try {
			Utility.log(log, GlobalLabelParameters.CACHE_BEGIN, "", Level.INFO);
			Identity identity = new Identity();
			identity.setLastModifiedTime(merchantConfig.getKeyFile().lastModified());
			identity.setX509(KeyCertificateGenerator.initializeCertificate(merchantConfig));
			identity.setRsaPrivateKey(KeyCertificateGenerator.initializePrivateKey(merchantConfig));
			p12IdentityMap.put(merchantConfig.getMerchantID(), identity);
			return identity;
		} catch (ConfigException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw new SignatureGenerationException(GlobalLabelParameters.UNSUPPORTED_ENCODING, e);
		} catch (NoSuchAlgorithmException e) {
			throw new SignatureGenerationException(GlobalLabelParameters.NO_SUCH_ALGORITHM, e);
		} catch (IllegalArgumentException e) {
			throw new SignatureGenerationException(GlobalLabelParameters.UNSUPPORTED_ENCODING, e);
		} catch (Exception e) {
			throw new SignatureGenerationException(GlobalLabelParameters.SIG_FAILED, e);
		}

	}

	/**
	 * @param merchantConfig
	 *            - contains merchant Configuration
	 * @throws ConfigException
	 */
	public Identity retrieveP12DataFromCache(MerchantConfig merchantConfig) throws SignatureGenerationException {
		try {
			Identity identity = p12IdentityMap.get(merchantConfig.getMerchantID());
			if (identity != null && !isLastModifiedTimeUpdated(merchantConfig, identity)) {
				Utility.log(log, GlobalLabelParameters.CACHE_EXTRACT, "", Level.INFO);
				return identity;
			} else {
				Utility.log(log, GlobalLabelParameters.CACHE_EXTEND, "", Level.INFO);
				return setUpP12Cache(merchantConfig);
			}
		} catch (SignatureGenerationException e) {
			throw e;
		} catch (Exception e) {
			throw new SignatureGenerationException("Eception occured while setting the cache", e);
		}

	}

	/**
	 * @param merchantConfig
	 *            - contains MerchantConfig
	 * @param identity
	 *            - contains Identity
	 * @return true if file is modified else return false.
	 */
	public boolean isLastModifiedTimeUpdated(MerchantConfig merchantConfig, Identity identity) {
		if (identity.getLastModifiedTime() == merchantConfig.getKeyFile().lastModified()) {
			return false;
		} else {
			return true;
		}
	}
}
