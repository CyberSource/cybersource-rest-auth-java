package com.cybersource.authsdk.util.mle;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.CachedMLEKId;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.JWEUtility;
import com.cybersource.authsdk.util.Utility;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MLEUtility {
    private static final Logger logger = LogManager.getLogger(MLEUtility.class);
    private static Cache cache = new Cache();

    public static boolean checkIsMLEForAPI(MerchantConfig merchantConfig, String inboundMLEStatus, String operationIds) {

    	//isMLE for an api is false by default
        boolean isMLEForAPI = false;

        if ("optional".equalsIgnoreCase(inboundMLEStatus) && merchantConfig.isEnableRequestMLEForOptionalApisGlobally()) {
            isMLEForAPI = true;
        }

        if ("mandatory".equalsIgnoreCase(inboundMLEStatus)) {
            isMLEForAPI = !merchantConfig.isDisableRequestMLEForMandatoryApisGlobally();
        }
        
        //operationIds are array as there are multiple public function for apiCallFunction such as apiCall,apiCallAsync ..
        String[] operationArray = Arrays.stream(operationIds.split(",")).map(String::trim).toArray(String[]::new);
        
        //Control the MLE only from map
        //Special Note: If API expect MLE request mandatory and map is saying to send non MLE request then API will throw an error from CyberSource
        if (merchantConfig.getInternalMapToControlRequestMLEonAPI() != null && !merchantConfig.getInternalMapToControlRequestMLEonAPI().isEmpty() ) {
        	for (String operationId : operationArray) {
        		if(merchantConfig.getInternalMapToControlRequestMLEonAPI().containsKey(operationId)) {
        			isMLEForAPI = merchantConfig.getInternalMapToControlRequestMLEonAPI().get(operationId);
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
    	
    	X509Certificate mleCertificate = getRequestMleCertificate(merchantConfig);        
    	// Handling special case : MLE Certificate is not currently available for HTTP Signature
        if(mleCertificate==null && GlobalLabelParameters.HTTP.equalsIgnoreCase(merchantConfig.getAuthenticationType())) {
		    logger.debug("The certificate to use for MLE for requests is not provided in the merchant configuration. Please ensure that the certificate path is provided.");
		    logger.warn("Currently, MLE for requests using HTTP Signature as authentication is not supported by Cybersource. By default, the SDK will fall back to non-encrypted requests.");        	
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
    
    public static boolean checkIsResponseMLEForAPI(MerchantConfig merchantConfig, String operationIds) {

    	//isMLE for response for an api is false by default
        boolean isResponseMLEForAPI = false;

        if (merchantConfig.isEnableResponseMleGlobally()) {
            isResponseMLEForAPI = true;
        }

        
        //operationIds are array as there are multiple public function for apiCallFunction such as apiCall,apiCallAsync ..
        String[] operationArray = Arrays.stream(operationIds.split(",")).map(String::trim).toArray(String[]::new);
        
        //Control the Response MLE only from map
        //Special Note: If API expect MLE Response mandatory and map is saying to receive non MLE response then API might throw an error from CyberSource
        if (merchantConfig.getInternalMapToControlResponseMLEonAPI() != null && !merchantConfig.getInternalMapToControlResponseMLEonAPI().isEmpty() ) {
        	for (String operationId : operationArray) {
        		if(merchantConfig.getInternalMapToControlResponseMLEonAPI().containsKey(operationId)) {
        			isResponseMLEForAPI = merchantConfig.getInternalMapToControlResponseMLEonAPI().get(operationId);
        			break;
        		}
        	}
        }

        return isResponseMLEForAPI;
    }
    
    public static boolean checkIsMleEncryptedResponse(String responseBody) {
    	if (responseBody == null || responseBody.trim().isEmpty()) {
    		return false;
    	}
    	try {
			Gson gson = new Gson();
			JSONObject jsonObject = gson.fromJson(responseBody, JSONObject.class);
			// Check if the JSON object has exactly one key
			if (jsonObject.size() != 1) {
				return false;
			}
			// Check if the key is "encryptedResponse" and the value is a string
			if (jsonObject.containsKey("encryptedResponse")) {
				Object value = jsonObject.get("encryptedResponse");
				return value instanceof String;
			}
			return false;
		} catch (Exception e) {
			// If JSON parsing fails, it's not a valid JSON thus not a MLE response
			return false;
		}
    }
    
    public static String decryptMleResponsePayload(MerchantConfig merchantConfig, String mleResponseBody) throws MLEException {

    	if(!checkIsMleEncryptedResponse(mleResponseBody)) {
    		throw new MLEException("response body is not MLE encrypted.");
    	}
    	
    	PrivateKey mlePrivateKey = getMleResponsePrivateKey(merchantConfig);
    	String jweResponseToken = getResponseMleToken(mleResponseBody);
    	if(StringUtils.isEmpty(jweResponseToken)) {
    		// when mle token is empty or null then fall back to non mle encrypted response
    		return mleResponseBody;
    	}
    		
		try {
			logger.debug("LOG_NETWORK_RESPONSE_BEFORE_MLE_DECRYPTION: " + mleResponseBody);
			String decryptedResponse = JWEUtility.decryptJWEUsingPrivateKey(mlePrivateKey, jweResponseToken);
			logger.debug("LOG_NETWORK_RESPONSE_AFTER_MLE_DECRYPTION: " + decryptedResponse);
			return decryptedResponse;
		} catch (ParseException e) {
			throw new MLEException("MLE Response token ParseException:  "+ e.getMessage());
		}
		catch (JOSEException e) {
			if(e.getCause() != null && e.getCause() instanceof java.security.InvalidKeyException) {
				Throwable rootCause = e.getCause().getCause();
				if(rootCause instanceof javax.crypto.BadPaddingException && 
				   rootCause.getMessage() != null && 
				   rootCause.getMessage().contains("Padding error in decryption")) {
					throw new MLEException("MLE Response decryption JOSEException: Invalid private key provided for MLE Response decryption", e);
				}
			}
			throw new MLEException("MLE Response decryption JOSEException: " + e.getMessage(), e);
		}
    	
    }

    private static String getResponseMleToken(String mleResponseBody) {
    	try {
            Gson gson = new Gson();
            JSONObject jsonObject = gson.fromJson(mleResponseBody, JSONObject.class);
            return (String) jsonObject.get("encryptedResponse");
        } catch (Exception e) {
            logger.error("Failed to extract Response MLE token: " + e.getMessage());
            return null;
        }	
    }
    
    private static PrivateKey getMleResponsePrivateKey(MerchantConfig merchantConfig) throws MLEException {
    	//first priority - if privateKey is given in merchant config return that
    	if(null != merchantConfig.getResponseMlePrivateKey()) {
    		return merchantConfig.getResponseMlePrivateKey();
    	}
    	//second priority - get the privateKey from merchantConfig.responseMlePrivateKeyFilePath
    	PrivateKey responseMlePrivateKey = cache.getMleResponsePrivateKeyFromFilePath(merchantConfig);
    	return responseMlePrivateKey;	
    }
    
    private static X509Certificate getRequestMleCertificate(MerchantConfig merchantConfig) throws MLEException {
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
        String serialNumber = null;
        try {
            serialNumber = Utility.extractSerialNumber(x509Certificate);
        } catch (IllegalArgumentException e) {
            throw new MLEException("Failed to extract serial number from certificate for MLE: " + e.getMessage(), e);
        }
        
        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .keyID(serialNumber)
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

    public static String validateAndAutoExtractResponseMleKid(MerchantConfig merchantConfig) throws Exception {
        if (merchantConfig.getResponseMlePrivateKey() != null) {
            logger.debug("responseMlePrivateKey is provided directly, using configured responseMleKID");
            return merchantConfig.getResponseMleKID();
        }
        logger.debug("Validating responseMleKID for JWT token generation");
        String cybsKid = null;
        boolean p12File = false;
        // File path validity
        try {
            Utility.validatePathAndFile(merchantConfig.getResponseMlePrivateKeyFilePath(), "responseMlePrivateKeyFilePath");
            String extension = Utility.getFileExtension(merchantConfig.getResponseMlePrivateKeyFilePath());
            if (extension.equalsIgnoreCase("p12") || extension.equalsIgnoreCase("pfx")) {
                p12File = true;
            }
        } catch (IOException e) {
            logger.debug("No valid private key file path provided, skipping auto-extraction");
        }

        if (p12File) {
            logger.debug("P12/PFX file detected, checking if it is a CyberSource certificate");
            CachedMLEKId cachedData = cache.getMLEKIdDataFromCache(merchantConfig);
            if (cachedData != null) {
                if (cachedData.getKid() != null) {
                    // KID present means it's a CyberSource P12, use it
                    cybsKid = cachedData.getKid();
                } else {
                    // KID is null means either non-CyberSource P12 or extraction failed
                    logger.debug("Private key file is not a CyberSource generated P12/PFX file, skipping auto-extraction");
                }
            }
        } else {
            logger.debug("Private key file is not a P12/PFX file, skipping auto-extraction");
        }
        if (cybsKid != null) {
            logger.debug("Successfully auto-extracted responseMleKID from CyberSource P12 certificate");
        }

        String configuredKID = merchantConfig.getResponseMleKID();
        if (cybsKid == null && configuredKID == null) {
            throw new Exception("responseMleKID is required when response MLE is enabled. " +
                            "Could not auto-extract from certificate and no manual configuration provided. " +
                            "Please provide responseMleKID explicitly in your configuration."
            );
        } else if (cybsKid == null) {
            logger.debug("Using manually configured responseMleKID");
            return configuredKID;
        } else if (configuredKID == null) {
            logger.debug("Using auto-extracted responseMleKID from CyberSource certificate");
            return cybsKid;
        } else if (!cybsKid.equals(configuredKID)) {
            logger.warn("Auto-extracted responseMleKID does not match manually configured responseMleKID. Using configured value as preference");
        }
        return configuredKID;
    }
    
}
