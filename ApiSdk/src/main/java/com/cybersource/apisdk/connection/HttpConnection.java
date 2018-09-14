package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
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
	private String httpID;
	private String url;
	private Logger logger;
	private String merchantKeyId;
	private String merchantSecertKey;
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
		this.merchantKeyId = merchantConfig.getMerchantKeyId();
		this.merchantSecertKey = merchantConfig.getMerchantsecretKey();
		this.requestHost = merchantConfig.getRequestHost();
		this.merchantId = merchantConfig.getMerchantID();
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			this.httpID = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
			Utility.log(logger, "GET_ID : " + httpID, Level.INFO);
		}
		/*
		 * Establish the connection with server and receives the response
		 * message and code.
		 */
		try {
			httpConnection();
		} catch (Exception e) {
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void setHeaderData(String merchantId) {
		try {
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID + " : " + merchantId, Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.V_C_MERCHANTID, merchantId);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void openConnection() {
		try {
			url = merchantConfig.getURL();
			URL obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			/* Creating Time out. */
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			con.setConnectTimeout(timeoutInMS);
			con.setReadTimeout(timeoutInMS);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET + url, Level.ERROR);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void setDigest() {
		con.setRequestProperty(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
		this.digestMessageBody = digestPayLoad.getMessageBody();
	}

	/**
	 * 
	 */
	@Override
	public void setContentType() {
		try {
			Utility.log(logger,
					GlobalLabelParameters.CONTENT_TYPE + " : " + GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE_NOT_SET, Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void maskingPayload(String serverResponse) {
		Utility.log(logger,
				"Response Message : " + new Masking(serverResponse, this.logger).getMaskingResult(), Level.INFO);
	}

	/**
	 * 
	 */
	@Override
	public void endConnection() {
		con.disconnect();
		con = null;

	}

	/**
	 * 
	 */
	@Override
	public void setUserAgent() {
		try {
			Utility.log(logger,
					GlobalLabelParameters.USERAGENT + " : " + GlobalLabelParameters.USER_AGENT_VALUE, Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.USER_AGENT_ERROR, Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void setSignature() {
		Authorization authorization = new Authorization();
		try {
			String signatureHeaderValue = authorization.getToken(merchantConfig, merchantKeyId, merchantSecertKey, merchantId,
					requestType, httpID);

			con.setRequestProperty(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR, Level.FATAL);
			Utility.log(logger, e);
		}

	}

	@Override
	public void setRequestMethod(String method) {
		try {
			Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE + ": " + method, Level.INFO);
			con.setRequestMethod(method);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR, Level.FATAL);
			Utility.log(logger, e);
		}
	}

	/**
	 * 
	 * @throws IOException 
	 */
	private void httpConnection() throws IOException  {
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
		con.setRequestProperty(GlobalLabelParameters.DATE, getDate());
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

		int responseCode = con.getResponseCode();
		String resVcCorelationId = con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID);
		merchantConfig.setResponseCode(String.valueOf(responseCode));
		merchantConfig.setVcCorelationID(resVcCorelationId);

		Utility.log(logger, GlobalLabelParameters.DATE.concat(" : ").concat(getDate()), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.HOST.concat(" : ").concat(requestHost), Level.INFO);
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			Utility.log(logger, GlobalLabelParameters.DIGEST + " : "
					+ new Masking(this.digestMessageBody, logger).getMaskingResult(), Level.INFO);
		}
		Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : " + responseCode,
				Level.INFO);
		Utility.logResponseCodeMessage(responseCode, logger);
		Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : " + resVcCorelationId,
				Level.INFO);

		/* Reading Response Message */
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		/* Close HTTP Connection. */
		endConnection();
		/* Masking the response payload from the server. */
		maskingPayload(response.toString());
		/* Set Response Message */
		merchantConfig.setResponseMessage(new Masking(response.toString(), logger).getMaskingResult());
	}

	/**
	 * This Method returns Date in GMT format as defined by RFC7231. 
	 * @return date.
	 */
	private String getDate() {
		return (DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT"))));
	}

	

}
