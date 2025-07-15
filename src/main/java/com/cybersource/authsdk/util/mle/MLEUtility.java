package com.cybersource.authsdk.util.mle;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;

import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    	X509Certificate mleCert = null;
    	mleCert = cache.getRequestMLECertFromCache(merchantConfig);
    	if(null != mleCert) {
    		return mleCert;
    	}else {
    		//throw MLE cert not found 
        	throw new MLEException("No certificate found for MLE Request");
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
    
    /**
     * Gets the default MLE certificate path based on the environment.
     * 
     * @param environment The environment (e.g., "apitest.cybersource.com" or "api.cybersource.com")
     * @return The absolute path to the default MLE certificate for the given environment
     * @throws MLEException 
     */
    public static String getDefaultMLECertPath(String environment) throws MLEException {
        if (environment == null || environment.isEmpty()) {
            logger.warn("Run Environment is null or empty.");
            return null;
        }
        
        String normalizedEnv = environment.toLowerCase();
        
        // Determine certificate file name based on environment
        if (normalizedEnv.contains("apitest.cybersource.com")) {
            logger.debug("Using Default CAS MLE Request certificate path from SDK.");
            return getCertificateAbsolutePath("apitest.cybersource.com.crt", "CAS");
        } else if (normalizedEnv.contains("api.cybersource.com")) {
            logger.debug("Using Default Prod MLE Request certificate path from SDK.");
            return getCertificateAbsolutePath("api.cybersource.com.crt", "production");
        } else {
            logger.error("Default Cert required for MLE Request does not exist in SDK for runEnvironment: " + environment);
            throw new MLEException("Default Cert required for MLE Request does not exist in SDK for runEnvironment: " + environment);
        }
    }
    
    /**
     * Helper method to get the absolute path of a certificate file from resources.
     * 
     * @param certificateFileName The name of the certificate file
     * @param environmentType The type of environment (for error messages)
     * @return The absolute path to the certificate file
     * @throws MLEException if the certificate file cannot be found
     */
    private static String getCertificateAbsolutePath(String certificateFileName, String environmentType) throws MLEException {
        String resourcePath = "/" + certificateFileName;
        
        try {
            // First try to get the resource URL
            java.net.URL resourceUrl = MLEUtility.class.getResource(resourcePath);
            if (resourceUrl != null) {
                return resourceUrl.getPath();
            }
            
            // Fallback: search through classpath
            String classPath = System.getProperty("java.class.path");
            String[] paths = classPath.split(System.getProperty("path.separator"));
            for (String path : paths) {
                java.io.File certFile = new java.io.File(path, certificateFileName);
                if (certFile.exists()) {
                    return certFile.getAbsolutePath();
                }
            }
            
            // Certificate not found
            throw new MLEException("Certificate file " + certificateFileName + " not found in resources");
            
        } catch (Exception e) {
            throw new MLEException("Failed to locate " + environmentType + " certificate file: " + e.getMessage(), e);
        }
    }
}
