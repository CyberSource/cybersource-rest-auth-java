package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + claimSet + Signature ) are set inside the HTTP
 * connection and the call is made to APIserver. On successful transaction,
 * response message and response code : 200 is obtained.
 */
public class JwtConnection implements Connection, Headers {
	private boolean signatureAlert = false;
	private HttpURLConnection connection = null;
	private MerchantConfig merchantConfig = null;
	private Logger logger = null;
	private URL url;
	private int responseCode;
	private String responseMessage;
	private String jwtMethodType = null;
	private String getId = null;

	private String requestBody = null;

	/**
	 * @param merchantConfig
	 * @param logger
	 */
	public JwtConnection(MerchantConfig merchantConfig, Logger logger) {

		this.merchantConfig = merchantConfig;
		this.logger = logger;
		this.jwtMethodType = merchantConfig.getRequestType();
		this.getId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());

	}

	Response responseObj;

	/**
	 * @return
	 */
	public Response getJwtConnection() {

		if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			requestBody = getId;
		} else /* jwtMethod = POST */ {
			requestBody = merchantConfig.getRequestData();
		}

		if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			try {
				responseObj = jwtConnection();
				responseCode = connection.getResponseCode();
			} catch (Exception e) {
				Utility.log(logger, e);
			}
		} else if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			try {
				responseObj = postDocument(requestBody);
				responseCode = connection.getResponseCode();
			} catch (Exception e) {

				Utility.log(logger, e);
			}
		}

		responseMessage = Masking.prepareMasking(responseObj.getResponseMessage(), logger);
		responseObj.setResponseMessage(responseMessage);
		responseObj.setResponseCode(String.valueOf(responseCode));
		maskingPayload(responseMessage);
		endConnection();
		return responseObj;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public Response jwtConnection() throws IOException {
		/* Set Proxy Connection. */
		setProxyConnection(merchantConfig);
		/* Open Connection with API server. */
		openConnection();
		/* Set Json input Type in the connection object. */
		setContentType();
		/* Set Request Type ( GET or POST ) */
		setRequestMethod(jwtMethodType);
		/* Set encrypted signature in the connection object. */
		setSignature();
		if (signatureAlert) {
			responseObj.setResponseMessage("No Response");
			return responseObj;
		}

		responseCode = connection.getResponseCode();
		Utility.logResponseCodeMessage(responseCode, logger);
		return this.connection.getErrorStream() != null ? this.readInputStream(this.connection.getErrorStream())
				: this.readInputStream(this.connection.getInputStream());
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public Response postDocument(String request) throws IOException {
		setProxyConnection(merchantConfig);
		openConnection();
		setContentType();
		setRequestMethod(jwtMethodType);
		Utility.log(logger, "ClaimSet", " : " + Masking.prepareMasking(requestBody, logger), Level.INFO);
		setSignature();
		if (signatureAlert) {
			responseObj.setResponseMessage("No Response");
			return responseObj;
		}

		this.connection.setDoOutput(true);

		OutputStream out1 = this.connection.getOutputStream();
		byte[] requestBytes1 = request.getBytes(Charset.forName("UTF-8"));
		out1.write(requestBytes1);
		out1.close();

		responseCode = connection.getResponseCode();
		Utility.logResponseCodeMessage(responseCode, logger);
		return this.connection.getErrorStream() != null ? this.readInputStream(this.connection.getErrorStream())
				: this.readInputStream(this.connection.getInputStream());
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private Response readInputStream(InputStream inputStream) throws IOException {
		responseObj = new Response();
		responseObj.setResponseCode(String.valueOf(responseCode));
		Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE.concat(" : "), responseCode, Level.INFO);

		String responseHeader = connection.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID);
		responseObj.setVcCorelationId(responseHeader);
		Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID.concat(" : "), String.valueOf(responseHeader),
				Level.INFO);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder response = new StringBuilder();

		String inputLine;
		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		String resp = response.toString();
		responseObj.setResponseMessage(resp);
		return responseObj;
	}

	/**
	 * open connection
	 */
	@Override
	public void openConnection() {
		try {
			String getUrl = merchantConfig.getUrl();
			url = new URL(getUrl);
			this.connection = (HttpURLConnection) url.openConnection();
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			connection.setConnectTimeout(timeoutInMS);
			connection.setReadTimeout(timeoutInMS);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET, url, Level.ERROR);

		}

	}

	/**
	 * Set content-type.
	 */
	@Override
	public void setContentType() {
		try {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "),
					GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
			this.connection.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE,
					GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE_NOT_SET, "", Level.FATAL);

		}
	}

	/**
	 * Mask the Response.
	 */
	@Override
	public void maskingPayload(String serverResponse) {
		Utility.log(logger, "Response Message : ", Masking.prepareMasking(serverResponse, this.logger), Level.INFO);
	}

	/**
	 * Set signature.
	 */
	@Override
	public void setSignature() {
		Authorization jwtSigAuth = new Authorization();
		jwtSigAuth.setLogger(logger);
		jwtSigAuth.setJWTRequestBody(requestBody);
		try {
			String encryptedJWTSignature = jwtSigAuth.getToken(merchantConfig);
			if (encryptedJWTSignature == null || encryptedJWTSignature.isEmpty()) {
				signatureAlert = true;
				return;
			}
			this.connection.setRequestProperty("Authorization", "Bearer " + encryptedJWTSignature);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR, "", Level.FATAL);
		}
	}

	/**
	 * Set method to call.
	 */
	@Override
	public void setRequestMethod(String method) {
		try {
			Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), method, Level.INFO);
			this.connection.setRequestMethod(method);
			if (method.equalsIgnoreCase(GlobalLabelParameters.GET))
				logger.info("GETID" + " : " + this.getId);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_SET_ERROR, "", Level.FATAL);

		}
	}

	/**
	 * end connection.
	 */
	@Override
	public void endConnection() {
		connection.disconnect();
		connection = null;
	}

	/**
	 * Set user-agent.
	 */
	@Override
	public void setUserAgent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set the digest.
	 */
	@Override
	public void setDigest() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set Header-Data.
	 */
	@Override
	public void setHeaderData(String merchantId) {
		throw new UnsupportedOperationException();
	}

}
