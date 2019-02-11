package com.cybersource.authsdk.cache;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
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
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public Identity setUpP12Cache(MerchantConfig merchantConfig)
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, ConfigException {
		Utility.log(log, GlobalLabelParameters.CACHE_BEGIN, "", Level.INFO);
		Identity identity = new Identity();
		identity.setLastModifiedTime(merchantConfig.getKeyFile().lastModified());
		identity.setX509(KeyCertificateGenerator.initializeCertificate(merchantConfig));
		identity.setRsaPrivateKey(KeyCertificateGenerator.initializePrivateKey(merchantConfig));
		p12IdentityMap.put(merchantConfig.getMerchantID(), identity);
		return identity;

	}

	/**
	 * @param merchantConfig
	 *            - contains merchant Configuration
	 * @throws ConfigException
	 */
	public Identity retrieveP12DataFromCache(MerchantConfig merchantConfig) throws ConfigException {
		try {
			Identity identity = p12IdentityMap.get(merchantConfig.getMerchantID());
			if (identity != null && !isLastModifiedTimeUpdated(merchantConfig, identity)) {
				Utility.log(log, GlobalLabelParameters.CACHE_EXTRACT, "", Level.INFO);
				return identity;
			} else {
				Utility.log(log, GlobalLabelParameters.CACHE_EXTEND, "", Level.INFO);
				return setUpP12Cache(merchantConfig);
			}
		} catch (CertificateException e) {
			Utility.log(log, e);
			throw new ConfigException(GlobalLabelParameters.CERTIFICATE_EXCEPTION, e);
		} catch (NoSuchAlgorithmException e) {
			Utility.log(log, e);
			throw new ConfigException(GlobalLabelParameters.NO_SUCH_ALGORITHM, e);
		} catch (KeyStoreException e) {
			Utility.log(log, e);
			throw new ConfigException(GlobalLabelParameters.KEY_STORE_EXCEPTION, e);
		} catch (IOException e) {
			Utility.log(log, e);
			throw new ConfigException(e.toString(), e);
		} catch (Exception e) {
			throw new ConfigException("Eception occured while setting the cache", e);
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
