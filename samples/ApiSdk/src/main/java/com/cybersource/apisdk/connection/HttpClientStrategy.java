package com.cybersource.apisdk.connection;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.SignatureGenerationException;

/**
 * This interface will be used to get common behavior for connection for JWT and
 * HTTP.
 */
public interface HttpClientStrategy {

	/**
	 * Send Request
	 * 
	 * @param headerMap
	 * @return Response
	 */
	Response sendRequest(Map<String, String> headerMap)
			throws ConfigException, ClientException, SignatureGenerationException;

	/**
	 * The response message obtained form the server is sent for masking the
	 * important fields.
	 * 
	 * @param serverResponse
	 * @param title
	 */
	void maskingPayload(String serverResponse, String title);

	/**
	 * Sets the Response code message
	 * 
	 * @param responseCode
	 * @param logger
	 */
	default void logResponseCodeMessage(Integer responseCode, Logger logger) {
		if (logger != null) {
			String tempResponseCode = String.valueOf(responseCode);
			String tempResponseCodeMessage = "Status Information :: ";
			switch (tempResponseCode) {
			case "200":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Transaction Successful");
				logger.info(tempResponseCodeMessage);
				break;
			case "400":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Bad Request");
				logger.info(tempResponseCodeMessage);
				break;
			case "401":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Authentication Failed");
				logger.info(tempResponseCodeMessage);
				break;
			case "201":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Transaction Successful");
				logger.info(tempResponseCodeMessage);
				break;
			case "500":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Internal Server Error");
				logger.info(tempResponseCodeMessage);
				break;
			case "502":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Bad Gateway");
				logger.info(tempResponseCodeMessage);
				break;
			case "503":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("SERVICE UNAVAILABLE");
				logger.info(tempResponseCodeMessage);
				break;
			case "504":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Gateway Timeout");
				logger.info(tempResponseCodeMessage);
				break;
			case "404":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Not Found");
				logger.info(tempResponseCodeMessage);
				break;
			case "403":
				tempResponseCodeMessage = tempResponseCodeMessage.concat("Forbidden");
				logger.info(tempResponseCodeMessage);
				break;
			default:
				String unDefinedResponse = "Un-Identified";
				logger.info(tempResponseCodeMessage.concat(unDefinedResponse));
			}
		}
	}

}
