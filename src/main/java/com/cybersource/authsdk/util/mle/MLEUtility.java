package com.cybersource.authsdk.util.mle;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;

import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MLEUtility {
    private static final Logger logger = LogManager.getLogger(MLEUtility.class);
    private static Cache cache = new Cache();

    public static boolean checkIsMLEForAPI(MerchantConfig merchantConfig, boolean isMLESupportedByCybsForApi, String operationIds) {

    	//isMLE for an api is false by default
        boolean isMLEForAPI = false;
        
        //check here useMLEGlobally True or False
        //if API is part of MLE then check for useMLEGlobally global parameter
        if (isMLESupportedByCybsForApi == true && merchantConfig.isUseMLEGlobally() == true) {
          isMLEForAPI = true;
        }
        
        //operationIds are array as there are multiple public function for apiCallFunction such as apiCall,apiCallAsync ..
        String[] operationArray = Arrays.stream(operationIds.split(",")).map(String::trim).toArray(String[]::new);
        
        //Control the MLE only from map
        if (merchantConfig.getMapToControlMLEonAPI() != null && !merchantConfig.getMapToControlMLEonAPI().isEmpty() ) {
        	for (String operationId : operationArray) {
        		if(merchantConfig.getMapToControlMLEonAPI().containsKey(operationId)) {
        			isMLEForAPI = merchantConfig.getMapToControlMLEonAPI().get(operationId);
        			break;
        		}
        	}
        }

        return isMLEForAPI;
    }

    public static JSONObject encryptRequestPayload(MerchantConfig merchantConfig, Object localVarPostBody) throws MLEException {
    	
    	if (localVarPostBody == null) {
            return null;
        }
    	String payload = new Gson().toJson(localVarPostBody);
    	logger.debug("LOG_REQUEST_BEFORE_MLE: " + payload);
        X509Certificate mleCertificate = getCertificate(merchantConfig);
        Map<String, Object> customHeaders = new HashMap<>();
        customHeaders.put("iat", ZonedDateTime.now().toInstant().getEpochSecond());
        String encryptedPayload = encryptAttributeWithAlgo(payload, mleCertificate, customHeaders);
        JSONObject mleRequest= createJsonObject(encryptedPayload);
        logger.debug("LOG_REQUEST_AFTER_MLE: " + new Gson().toJson(mleRequest));
        return mleRequest;
    }

    private static X509Certificate getCertificate(MerchantConfig merchantConfig) throws MLEException {
        //get the MLE cert
    	X509Certificate mleCert = null;
    	try {
    		Identity p12Keys = cache.getMerchantP12KeysFromCache(merchantConfig);
    		mleCert = p12Keys.getMleCert();
    	}catch (IOException e) {
            throw new MLEException("Certificate with alias " + merchantConfig.getMleKeyAlias() + " not found in " + merchantConfig.getKeyFilename() + ".p12", e);
        } catch (CertificateException e) {
            throw new MLEException("Failed to load certificates in keystore", e);
        } catch (KeyStoreException e) {
            throw new MLEException("Failed to instantiate keystore", e);
        } catch (Exception e) {
            throw new MLEException("Error occurred while loading certificate for MLE: " + e.getMessage(), e);
        }
    	
    	if(null != mleCert) {
    		validateCertificateExpiry(mleCert, merchantConfig.getMleKeyAlias());
    		return mleCert;
    	}else {
    		//throw MLE cert not found
        	throw new MLEException("No certificate found for MLE for given mleKeyAlias "+  merchantConfig.getMleKeyAlias() + " in p12 file " + merchantConfig.getKeyFilename() + ".p12");
    	}
    	
    	
    }

    private static String encryptAttributeWithAlgo(String content, X509Certificate x509Certificate, Map<String, Object> customHeaders) throws MLEException {
        String serialNumber = Utility.extractSerialNumber(x509Certificate);
        if (serialNumber == null) {
            throw new MLEException("No serial number found in certificate for MLE");
        }
        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .keyID(Utility.extractSerialNumber(x509Certificate))
                        .customParams(customHeaders)
                        .build(),
                new Payload(content));
        try {
            jweObject.encrypt(new RSAEncrypter((RSAPublicKey) x509Certificate.getPublicKey()));
            return jweObject.serialize();
        } catch (JOSEException e) {
            throw new MLEException("Failed to encrypt payload for MLE", e);
        }
    }

    private static JSONObject createJsonObject(String jweToken) {
        JSONObject json = new JSONObject();
        json.put("encryptedRequest", jweToken);
        return json;
    }

    private static void validateCertificateExpiry(X509Certificate certificate, String keyAlias) throws MLEException {
        if (certificate.getNotAfter() == null) {
            //certificate do not have expire date
        	logger.warn("Certificate for MLE don't have expiry date.");
        } else if (certificate.getNotAfter().before(new Date())) {
        	//certificate is already expired
        	logger.warn("Certificate with MLE alias " + keyAlias + " is expired as of " + certificate.getNotAfter() + ". Please update p12 file.");
            //throw new MLEException("Certificate required for MLE has been expired on : " + certificate.getNotAfter());
        } else {
            long timeToExpire = certificate.getNotAfter().getTime() - new Date().getTime();
            if (timeToExpire < GlobalLabelParameters.CERTIFICATE_EXPIRY_DATE_WARNING_DAYS * 24 * 60 * 60) {
            	logger.warn("Certificate for MLE with alias " + keyAlias + " is going to expired on " + certificate.getNotAfter() + ". Please update p12 file before that.");
            }
        }
    }
}
