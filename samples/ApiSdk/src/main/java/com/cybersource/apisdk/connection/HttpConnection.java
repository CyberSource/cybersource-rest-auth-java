package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + digest + Signature ) are set inside the HTTP
 * connection and the call is made to API server and response message and code
 * is obtained.
 * 
 *
 */
public class HttpConnection implements Connection, Headers {

	private MerchantConfig merchantConfig;
	private PayloadDigest digestPayLoad;
	private HttpURLConnection con;
	private String requestType;
	private String requestId;
	private String url;
	private Logger logger;
	private String merchantId;
	private String digestMessageBody;
	private String requestHost;
	
	/**
	 * 
	 * @param merchantConfig
	 * @param logger
	 */
	public HttpConnection(MerchantConfig merchantConfig, Logger logger) {

		this.merchantConfig = merchantConfig;
		this.digestPayLoad = new PayloadDigest(merchantConfig);
		this.requestType = merchantConfig.getRequestType();
		this.logger = logger;
		this.requestHost = merchantConfig.getRequestHost();
		this.merchantId = merchantConfig.getMerchantID();
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			this.requestId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
			Utility.log(logger, "GET_ID : " , requestId, Level.INFO);
		}
		
	}

	/**
	 * 
	 */
	@Override
	public void setHeaderData(String merchantId) {
		try {
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID , " : ".concat(merchantId) , Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.V_C_MERCHANTID, merchantId);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET,"", Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * open connection
	 */
	@Override
	public void openConnection() {
		try {
			url = merchantConfig.getUrl();
			URL urlObj = new URL(url);
			con = (HttpURLConnection) urlObj.openConnection();
			/* Creating Time out. */
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			con.setConnectTimeout(timeoutInMS);
			con.setReadTimeout(timeoutInMS);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET ,url, Level.ERROR);
			Utility.log(logger, e);
		}
	}

	/**
	 * Set the digest.
	 */
	@Override
	public void setDigest() {
		con.setRequestProperty(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
		this.digestMessageBody = digestPayLoad.getMessageBody();
	}

	/**
	 * Set content_type.
	 */
	@Override
	public void setContentType() {
		try {
			Utility.log(logger,
					GlobalLabelParameters.CONTENT_TYPE , " : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE_NOT_SET,"", Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * Mask the Response.
	 */
	@Override
	public void maskingPayload(String serverResponse) {
		Utility.log(logger,
				"Response Message : " ,Masking.prepareMasking(serverResponse, this.logger), Level.INFO);
	}

	/**
	 * end connection.
	 */
	@Override
	public void endConnection() {
		con.disconnect();
		con = null;

	}

	/**
	 * set user-agent.
	 */
	@Override
	public void setUserAgent() {
		try {
			Utility.log(logger,
					GlobalLabelParameters.USERAGENT , " : " .concat(GlobalLabelParameters.USER_AGENT_VALUE), Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.USER_AGENT_ERROR,"", Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * set signature.
	 */
	@Override
	public void setSignature() {
		Authorization authorization = new Authorization();
		try {
			String signatureHeaderValue = authorization.getToken(merchantConfig);

			con.setRequestProperty(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR,null, Level.FATAL);
			Utility.log(logger, e);
		}

	}
	/**
	 * set Request Method.
	 */

	@Override
	public void setRequestMethod(String method) {
		try {
			Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE , ": ".concat(method) , Level.INFO);
			con.setRequestMethod(method);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR,"", Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 * @throws IOException 
	 */
	public Response httpConnection() throws IOException  {
		/* Set proxy Connection */
		setProxyConnection(merchantConfig);
		/* Make a new connection using the URL. */
		openConnection();
		/*
		 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
		 * Merchant ID or v-c-merchant-id This ID can be found on EBC portal
		 */
		setHeaderData(merchantId);
		/*
		 * Add Request Header :: "Date" The date and time that the message was
		 * originated from. "HTTP-date" format as defined by RFC7231.
		 */
		con.setRequestProperty(GlobalLabelParameters.DATE, PropertiesUtil.getNewDate());
		/*
		 * Add Request Header :: "Host" Sandbox Host :: apitest.cybersource.com
		 * Production Host :: api.cybersource.com
		 */
		con.setRequestProperty(GlobalLabelParameters.HOST, requestHost);

		if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			setDigest();
		}
		/* Add Request Header :: "Signature" */
		setSignature();
		/* Add Request Request Type :: "Get or Post" */
		setRequestMethod(requestType);
		/* Addition Request Headers : User Agent */
		setUserAgent();
		/* Addition Request Headers : Input Type */
		setContentType();
		/* Send post request */
		
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			con.setDoOutput(true);
			con.setDoInput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(this.digestPayLoad.getPayLoad().getBytes("UTF-8"));
			wr.flush();
			wr.close();
		}
		/* Establishing HTTP connection */
		int responseCode=con.getResponseCode();
        Response responseObj=new Response();
        responseObj.setResponseCode(String.valueOf(responseCode));
        responseObj.setVcCorelationId(con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID));
		Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "),PropertiesUtil.getNewDate(), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "),requestHost, Level.INFO);
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			Utility.log(logger, GlobalLabelParameters.DIGEST , " : "
					+ Masking.prepareMasking(this.digestMessageBody, logger), Level.INFO);
		}
		Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE+" : " , responseObj.getResponseCode(),
				Level.INFO);
		Utility.logResponseCodeMessage(responseCode, logger);
		Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : " , responseObj.getVcCorelationId(),
				Level.INFO);

		StringBuilder response = new StringBuilder();

		if (responseCode != 404) {
			/* Reading Response Message */
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}

		/* Close HTTP Connection. */
		endConnection();
		/* Masking the response payload from the server. */
		if (response != null) {
			maskingPayload(response.toString());
			/* Set Response Message */
			String resp=Masking.prepareMasking(response.toString(), logger);
			responseObj.setResponseMessage(resp);
		}

		return responseObj;
	}

}
