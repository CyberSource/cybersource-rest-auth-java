package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
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
	private static Logger logger = LogManager.getLogger(JwtConnection.class);
	
	private boolean signatureAlert = false;
	private HttpURLConnection connection = null;
	private MerchantConfig merchantConfig = null;
	private URL url;
	private int responseCode;
	private String responseMessage;
	private String jwtMethodType = null;
	private String getId = null;
	private Response responseObj;
	private String requestBody = null;

	/**
	 * @param merchantConfig
	 * @param logger
	 */
	public JwtConnection(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
		this.jwtMethodType = merchantConfig.getRequestType();
		this.getId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
	}

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
				logger.error(e);
			}
		} else if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			try {
				responseObj = postDocument(requestBody);
				responseCode = connection.getResponseCode();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		responseMessage = Masking.prepareMasking(responseObj.getResponseMessage());
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
		logger.info("{} : {}", GlobalLabelParameters.RESPONSE_CODE, responseCode);
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
		logger.info("Claim Set : {}", requestBody);
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
		logger.info("{} : {}", GlobalLabelParameters.RESPONSE_CODE, responseCode);
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
		logger.info("{} : {}", GlobalLabelParameters.RESPONSE_CODE, responseCode);

		String responseHeader = connection.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID);
		responseObj.setVcCorelationId(responseHeader);
		logger.info("{} : {}", GlobalLabelParameters.V_C_CORRELATION_ID, String.valueOf(responseHeader));

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
			String getUrl = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			url = new URL(getUrl);
			this.connection = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			logger.error(GlobalLabelParameters.URL_NOT_SET);
		}

	}

	/**
	 * Set content-type.
	 */
	@Override
	public void setContentType() {
		try {
			logger.info("{} : {}", GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			this.connection.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE,
					GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.CONTENT_TYPE_NOT_SET);
		}
	}

	/**
	 * Mask the Response.
	 */
	@Override
	public void maskingPayload(String serverResponse) {
		logger.info("Response Message : {}", serverResponse);
	}

	/**
	 * Set signature.
	 */
	@Override
	public void setSignature() {
		Authorization jwtSigAuth = new Authorization();
		jwtSigAuth.setJWTRequestBody(requestBody);
		try {
			String encryptedJWTSignature = jwtSigAuth.getToken(merchantConfig);
			if (encryptedJWTSignature == null || encryptedJWTSignature.isEmpty()) {
				signatureAlert = true;
				return;
			}
			this.connection.setRequestProperty("Authorization", "Bearer " + encryptedJWTSignature);
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.SIG_SET_ERROR);
		}
	}

	/**
	 * Set method to call.
	 */
	@Override
	public void setRequestMethod(String method) {
		try {
			logger.info("{} : {}", GlobalLabelParameters.REQUEST_TYPE, method);
			this.connection.setRequestMethod(method);
			if (method.equalsIgnoreCase(GlobalLabelParameters.GET)) {
				logger.info("GET ID" + " : " + this.getId);
			}
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.REQUEST_SET_ERROR);
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
