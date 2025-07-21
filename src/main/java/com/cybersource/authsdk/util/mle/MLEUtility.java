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

    public static Object encryptRequestPayload(MerchantConfig merchantConfig, Object localVarPostBody) throws MLEException {
    	if (localVarPostBody == null) {
            return null;
        }
    	
    	X509Certificate mleCertificate = getCertificate(merchantConfig);        
    	// Handling special case : MLE Certificate is not currently available for HTTP Signature
        if(mleCertificate==null && GlobalLabelParameters.HTTP.equalsIgnoreCase(merchantConfig.getAuthenticationType())) {
		    logger.debug("The certificate to use for MLE for requests is not provided in the merchant configuration. Please ensure that the certificate path is provided.");
		    logger.debug("Currently, MLE for requests using HTTP Signature as authentication is not supported by Cybersource. By default, the SDK will fall back to non-encrypted requests.");        	
		    return localVarPostBody;
        }
        
    	String payload = new Gson().toJson(localVarPostBody);
        logger.debug("LOG_REQUEST_BEFORE_MLE: " + payload); 
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
    	}
    	else if(GlobalLabelParameters.HTTP.equalsIgnoreCase(merchantConfig.getAuthenticationType())){
    		//handle the special case when authType= http_Signature and mleCert=null
    		//Handling special case : MLE Certificate is not currently available for HTTP Signature
    		return null;
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
    
}
