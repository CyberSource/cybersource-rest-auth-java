package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
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
	public HttpConnection(MerchantConfig merchantConfig) {
		this.logger = LogManager.getLogger(this.getClass());
		this.merchantConfig = merchantConfig;
		this.digestPayLoad = new PayloadDigest(merchantConfig);
		this.requestType = merchantConfig.getRequestType();
		this.requestHost = merchantConfig.getRequestHost();
		this.merchantId = merchantConfig.getMerchantID();
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			this.requestId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
			this.logger.info("GET_ID : {}", requestId);
		}
		
	}

	/**
	 * 
	 */
	@Override
	public void setHeaderData(String merchantId) {
		try {
			this.logger.info("{} : {}", GlobalLabelParameters.V_C_MERCHANTID, merchantId);
			con.setRequestProperty(GlobalLabelParameters.V_C_MERCHANTID, merchantId);
		} catch (Exception e) {
			this.logger.fatal(GlobalLabelParameters.MERCHANTID_NOT_SET);
			this.logger.error(e);
		}
	}

	/**
	 * open connection
	 */
	@Override
	public void openConnection() {
		try {
			url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			URL urlObj = new URL(url);
			con = (HttpURLConnection) urlObj.openConnection();			
		} catch (Exception e) {
			this.logger.error(GlobalLabelParameters.URL_NOT_SET);
			this.logger.error(e);
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
			this.logger.info("{} : {}", GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			this.logger.fatal(GlobalLabelParameters.CONTENT_TYPE_NOT_SET);
			this.logger.error(e);
		}
	}

	/**
	 * Mask the Response.
	 */
	@Override
	public void maskingPayload(String serverResponse) {
		this.logger.info("Response Message : {}", serverResponse);
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
			this.logger.info("{} : {}", GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
			con.setRequestProperty(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
		} catch (Exception e) {
			this.logger.fatal(GlobalLabelParameters.USER_AGENT_ERROR);
			this.logger.error(e);
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
			this.logger.fatal(GlobalLabelParameters.SIG_SET_ERROR);
			this.logger.error(e);
		}
	}
	
	/**
	 * set Request Method.
	 */
	@Override
	public void setRequestMethod(String method) {
		try {
			this.logger.info("{} : {}", GlobalLabelParameters.REQUEST_TYPE, method);
			con.setRequestMethod(method);
		} catch (Exception e) {
			this.logger.fatal(GlobalLabelParameters.INVALID_REQUEST_TYPE);
			this.logger.error(e);
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
        
        this.logger.info("{} : {}", GlobalLabelParameters.DATE, PropertiesUtil.getNewDate());
        this.logger.info("{} : {}", GlobalLabelParameters.HOST, requestHost);
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			this.logger.info("{} : {}", GlobalLabelParameters.DIGEST, this.digestMessageBody);
		}
		
		this.logger.info("{} : {}", GlobalLabelParameters.RESPONSE_CODE, responseObj.getResponseCode());
		this.logger.info("{} : {}", GlobalLabelParameters.V_C_CORRELATION_ID, responseObj.getVcCorelationId());

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
			String resp = Masking.prepareMasking(response.toString());
			responseObj.setResponseMessage(resp);
		}

		return responseObj;
	}
}
