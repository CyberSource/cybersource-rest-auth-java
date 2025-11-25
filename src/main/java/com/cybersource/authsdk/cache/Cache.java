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
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.JWEUtility;
import com.cybersource.authsdk.util.Utility;
import com.cybersource.authsdk.util.mle.MLEException;


public class Cache {
	private static Logger logger = LogManager.getLogger(Cache.class);
	
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, CachedJWEPrivateKey> cachePEM = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, Identity> cacheMLE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, CachedMLEKId> cacheMLEKId = new ConcurrentHashMap<>();

	public Cache() {
	}

	/**
	 * @throws Exception
	 */
	private void setUpP12Cache(MerchantConfig merchantConfig) throws Exception {
		KeyCertificateGenerator newKeyCertGen = new KeyCertificateGenerator();
		X509Certificate jwtX509Certificate = newKeyCertGen.initializeCertificateOfMerchantForJWT(merchantConfig);
		RSAPrivateKey jwtRsaPrivateKey = newKeyCertGen.initializePrivateKey(merchantConfig);
		//X509Certificate mleX509Certificate = newKeyCertGen.initializeMLECertificate(merchantConfig);
		
		Identity identity = new Identity();
		identity.setLastModifiedDate(getLastModifiedFileTime(merchantConfig.getKeyFile().getAbsolutePath()));
		identity.setX509(jwtX509Certificate);
		identity.setRsaPrivateKey(jwtRsaPrivateKey);
		//identity.setMleCert(mleX509Certificate);
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
    public CachedMLEKId getMLEKIdDataFromCache(MerchantConfig merchantConfig) {
        String cacheKey = merchantConfig.getResponseMlePrivateKeyFilePath() + GlobalLabelParameters.RESPONSE_MLE_P12_PFX_CACHE_IDENTIFIER;
        String filePath = merchantConfig.getResponseMlePrivateKeyFilePath();
        if (!cacheMLEKId.containsKey(cacheKey)) {
            setUpMLEKIdCache(merchantConfig);
        } else {
            CachedMLEKId cachedMLEKId = cacheMLEKId.get(cacheKey);
            if (cachedMLEKId == null || cachedMLEKId.getLastModifiedTimeStamp() != getLastModifiedFileTime(filePath)) {
                logger.info("MLE KID cache outdated or file modified. Refreshing cache for: {}", filePath);
                setUpMLEKIdCache(merchantConfig);
            }
        }

        return cacheMLEKId.get(cacheKey);
    }

    private void setUpMLEKIdCache(MerchantConfig merchantConfig) {
        String filePath = null;
        String cacheKey = null;
        try {
            filePath = merchantConfig.getResponseMlePrivateKeyFilePath();
            cacheKey = merchantConfig.getResponseMlePrivateKeyFilePath() + GlobalLabelParameters.RESPONSE_MLE_P12_PFX_CACHE_IDENTIFIER;
            X509Certificate merchantCert = Utility.getCertificateByAliasFromPKCS(filePath, merchantConfig.getResponseMlePrivateKeyFilePassword(), merchantConfig.getMerchantID());
            if (merchantCert == null) {
                throw new MLEException("No certificate found for Response MLE Private Key file with merchant ID alias " + merchantConfig.getMerchantID());
            }
            
            // Check if this is a CyberSource-generated P12 file
            boolean isCyberSourceP12 = Utility.isP12GeneratedByCyberSource(filePath, merchantConfig.getResponseMlePrivateKeyFilePassword());

            CachedMLEKId cachedMLEKId = new CachedMLEKId();
            cachedMLEKId.setLastModifiedTimeStamp(getLastModifiedFileTime(filePath));
            
            if (isCyberSourceP12) {
                String mleKID = null;
                try {
                    mleKID = Utility.extractSerialNumber(merchantCert);
                } catch (IllegalArgumentException e) {
                    throw new MLEException("Failed to extract serial number from certificate for Response MLE: " + e.getMessage());
                }
                cachedMLEKId.setKid(mleKID);
            }            
            cacheMLEKId.put(cacheKey, cachedMLEKId);
        } catch (Exception e) {
            logger.error("Failed to load MLE KID from Response MLE Private Key file: {}. Error: {}", filePath, e.getMessage());

            if (cacheKey != null) {
                CachedMLEKId failureMarker = new CachedMLEKId();
                failureMarker.setKid(null);
                failureMarker.setLastModifiedTimeStamp(0);
                cacheMLEKId.put(cacheKey, failureMarker);
            }
        }
    }
	
	
	public X509Certificate getRequestMLECertFromCache(MerchantConfig merchantConfig) throws MLEException{
		
		String merchantId = merchantConfig.getMerchantID();
		String identifier = "";
		String cacheKey = "";
		String filePath = "";
    	
		//1. First priority: get cert from merchantConfig.mleForRequestPublicCertPath if certPath is provided
		if (!StringUtils.isEmpty(merchantConfig.getMleForRequestPublicCertPath())) {
			identifier= GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_CONFIG_CERT;
			cacheKey = merchantId + "_" + identifier;
			filePath = merchantConfig.getMleForRequestPublicCertPath();	
		}

		//2. Priority 2: if merchantConfig.mleForRequestPublicCertPath not provided by merchant then get mlecert from merchant p12 if provided and jwt auth type
		else if(GlobalLabelParameters.JWT.equalsIgnoreCase(merchantConfig.getAuthenticationType()) && merchantConfig.getKeyFile() != null) {
			identifier= GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_P12_CERT;
			cacheKey = merchantId + "_" + identifier;
			filePath = merchantConfig.getKeyFile().getAbsolutePath();
		}

		//3. Priority 3: get mlecert from default mle cert in SDK as per CAS or PROD env.
		else {
			logger.debug("The certificate to use for MLE for requests is not provided in the merchant configuration. Please ensure that the certificate path is provided.");
			return null;
		}
		
		X509Certificate mleCert = getMLECertBasedOnCacheKey(merchantConfig, cacheKey, filePath);
		Utility.validateCertificateExpiry(mleCert, merchantConfig.getRequestMleKeyAlias(), identifier);
		return mleCert;
	}

	private X509Certificate getMLECertBasedOnCacheKey(MerchantConfig merchantConfig, String cacheKey, String filePath) throws MLEException {
		if(!cacheMLE.containsKey(cacheKey)) {
			setUpMLECache(merchantConfig, cacheKey, filePath);
		}else {
			Identity mleIdentity = cacheMLE.get(cacheKey);
			if (mleIdentity==null || mleIdentity.getLastModifiedDate() != getLastModifiedFileTime(filePath)) {
				setUpMLECache(merchantConfig, cacheKey, filePath);
			}
		}
		return cacheMLE.get(cacheKey).getMleCert();
	}

	private void setUpMLECache(MerchantConfig merchantConfig, String cacheKey, String filePath) throws MLEException {
		if(cacheKey.endsWith(GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_RESPONSE_PRIVATE_KEY)) {
			char[] password = merchantConfig.getResponseMlePrivateKeyFilePassword();
			PrivateKey mlePrivateKey = null;
		    try {
		        String fileExtension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
		        // Case 1 - PKCS#12 formats (.p12, .pfx)
		        if ("p12".equals(fileExtension) || "pfx".equals(fileExtension)) {
		            mlePrivateKey = Utility.readPrivateKeyFromP12(filePath, password);
		        }
		        // Case 2 - PEM-based formats (.pem, .key, .p8) - handles PKCS#1 and PKCS#8, encrypted and unencrypted
		        else if ("pem".equals(fileExtension) || "key".equals(fileExtension) || "p8".equals(fileExtension)) {
		            mlePrivateKey = Utility.loadPrivateKeyFromPemFile(filePath, password);
		        }
		        else {
		            throw new MLEException("Unsupported Response MLE Private Key file format: " + fileExtension + 
		                ". Supported formats are: .p12, .pfx, .pem, .key, .p8");
		        }
		        
		        // Cache the private key
		        Identity mleIdentity = new Identity();
		        mleIdentity.setLastModifiedDate(getLastModifiedFileTime(filePath));
		        mleIdentity.setRsaPrivateKey((RSAPrivateKey) mlePrivateKey);
		        cacheMLE.put(cacheKey, mleIdentity);
		        
		    } catch (Exception e) {
		    	logger.error("Error loading MLE response private key from: " + filePath + ". Error: " + e.getMessage());
		        throw new MLEException("Error loading MLE response private key from: " + filePath + ". Error: " + e.getMessage(),e);
		    }
		    return;
		}
		
		X509Certificate mleCert =null;
		
		if(cacheKey.endsWith(GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_CONFIG_CERT)) {
			try {
				Collection<X509Certificate> certs = Utility.loadCertificatesFromPemFile(filePath);
				//search the cert for given key alias
				mleCert = Utility.searchCertificateByAlias(certs, merchantConfig.getRequestMleKeyAlias());
				if (mleCert == null) {
					// If no certificate found for the specified alias, fall back to first certificate
					String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
					logger.warn("No certificate found for the specified requestMleKeyAlias '{}'. Using the first certificate from file {} as the MLE request certificate.", merchantConfig.getRequestMleKeyAlias(), fileName);
					mleCert = certs.iterator().next(); //first cert
				}
			}catch(Exception e) {
				throw new MLEException("Error occurred while loading MLE certificate for Request from requestPublicCertPath in Merchant Config: " + e.getMessage(), e);
			}
		}
		
		if(cacheKey.endsWith(GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_P12_CERT)) {
			try {
				KeyCertificateGenerator newKeyCertGen = new KeyCertificateGenerator();
				mleCert = newKeyCertGen.initializeMLECertificate(merchantConfig);
			}catch(Exception e) {
				throw new MLEException("Error occurred while loading MLE certificate for Request from MerchantP12 in Merchant Config: " + e.getMessage(), e);
			}
		}
		
		//set the cache
		Identity mleIdentity = new Identity();
		mleIdentity.setLastModifiedDate(getLastModifiedFileTime(filePath));
		mleIdentity.setMleCert(mleCert);
		cacheMLE.put(cacheKey, mleIdentity);
		
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
	
	public PrivateKey getMleResponsePrivateKeyFromFilePath(MerchantConfig merchantConfig) throws MLEException {
		String merchantId = merchantConfig.getMerchantID();
		String identifier = GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_RESPONSE_PRIVATE_KEY;
		String cacheKey = merchantId + "_" + identifier;
		String mleResponsePrivateKeyFilePath = merchantConfig.getResponseMlePrivateKeyFilePath();
		
		if(!cacheMLE.containsKey(cacheKey)) {
			setUpMLECache(merchantConfig, cacheKey, mleResponsePrivateKeyFilePath);
		}else {
			Identity mleIdentity = cacheMLE.get(cacheKey);
			if (mleIdentity==null || mleIdentity.getLastModifiedDate() != getLastModifiedFileTime(mleResponsePrivateKeyFilePath)) {
				setUpMLECache(merchantConfig, cacheKey, mleResponsePrivateKeyFilePath);
			}
		}
		return cacheMLE.get(cacheKey).getRsaPrivateKey();
	}

	
	/**
	 * Gets the last modified time of a file.
	 *
	 * @param path the file path to check
	 * @return the last modified time in milliseconds
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
