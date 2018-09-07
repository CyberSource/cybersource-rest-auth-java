package com.cybersource.apisdk.connection;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
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

/**
 * Here all the data ( header + claimSet + Signature ) are set inside the HTTP
 * connection and the call is made to APIserver. On successful transaction,
 * response message and response code : 200 is obtained.
 */
public class JwtUrlConnection implements Connection, Headers {
	private boolean signatureAlert = false;
	private HttpURLConnection con = null;
	private MerchantConfig merchantConfig = null;
	private Logger logger = null;
	private URL url;
	private int responseCode;
	/**/
	private String method = null;
	private String id = null;

	private String requestBody = null;

	public JwtUrlConnection(MerchantConfig mc, Logger logger) {

		this.merchantConfig = mc;
		this.logger = logger;
		this.method = mc.getRequestType();
		this.id = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());

		jwtHttpConnection();
	}

	/**/
	private void jwtHttpConnection() {

		if (method.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| method.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			requestBody = id;
		} else /* jwtMethod = POST */ {
			requestBody = merchantConfig.getRequestData();
		}

		String responseMessage = "";
		if (method.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| method.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			try {
				responseMessage = get();
			} catch (Exception e) {
				Utility.log(logger, e);
			}
		} else if (method.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| method.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			try {
				responseMessage = postDocument(requestBody);
			} catch (Exception e) {
				Utility.log(logger, e);
			}
		}
		merchantConfig.setResponseMessage(new Masking(responseMessage, logger).getMaskingResult());
		maskingPayload(responseMessage);
		endConnection();
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public String get() throws IOException  {
		/* Set Proxy Connection.*/
		setProxyConnection(merchantConfig);
		/* Open Connection with API server. */
		openConnection();
		/* Set Json input Type in the connection object. */
		setContentType();
		/* Set Request Type ( GET or POST ) */
		setRequestMethod(method);
		/* Set encrypted signature in the connection object. */
		setSignature();
		if (signatureAlert) {
			return "No Response";
		}
		responseCode = con.getResponseCode();
		Utility.logResponseCodeMessage(responseCode, logger);
		return this.con.getErrorStream() != null ? this.readInputStream(this.con.getErrorStream())
				: this.readInputStream(this.con.getInputStream());
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public String postDocument(String request) throws IOException  {
		setProxyConnection(merchantConfig);
		openConnection();
		setContentType();
		setRequestMethod(method);
		Utility.log(logger, "ClaimSet" + " : " + new Masking(requestBody, logger).getMaskingResult(),
				Level.INFO);
		setSignature();
		if (signatureAlert) {
			return "No Response";
		}

		this.con.setDoOutput(true);

		OutputStream out1 = this.con.getOutputStream();
		byte[] requestBytes1 = request.getBytes(Charset.forName("UTF-8"));
		out1.write(requestBytes1);
		out1.close();
		
		responseCode = con.getResponseCode();
		Utility.logResponseCodeMessage(responseCode, logger);
		return this.con.getErrorStream() != null ? this.readInputStream(this.con.getErrorStream())
				: this.readInputStream(this.con.getInputStream());
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private String readInputStream(InputStream inputStream) throws IOException {
		
		merchantConfig.setResponseCode(String.valueOf(responseCode));
		Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : " + responseCode,
				Level.INFO);
		
		String responseHeader = con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID);
		merchantConfig.setV_c_CorelationID(responseHeader);
		Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : " + String.valueOf(responseHeader),
				Level.INFO);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder response = new StringBuilder();

		String inputLine;
		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		return response.toString();
	}

	@Override
	public void openConnection() {
		try {
			String getUrl = merchantConfig.getURL();
			url = new URL(getUrl);
			this.con = (HttpURLConnection) url.openConnection();
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			con.setConnectTimeout(timeoutInMS);
			con.setReadTimeout(timeoutInMS);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET + url, Level.ERROR);

		}

	}

	@Override
	public void setContentType() {
		try {
			Utility.log(logger,
					GlobalLabelParameters.CONTENT_TYPE + " : " + GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
			this.con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE_NOT_SET, Level.FATAL);

		}
	}

	@Override
	public void maskingPayload(String serverResponse) {
		Utility.log(logger,
				"Response Message : " + new Masking(serverResponse, this.logger).getMaskingResult(), Level.INFO);
	}

	@Override
	public void setSignature() {
		Authorization jwtSigAuth = new Authorization();
		jwtSigAuth.setLogger(logger);
		jwtSigAuth.setJWTRequestBody(requestBody);
		try {
			String encryptedJWTSignature = jwtSigAuth.getToken(merchantConfig, null, null, null, method, null);
			if (encryptedJWTSignature == null || encryptedJWTSignature.isEmpty()) {
				signatureAlert = true;
				return;
			}
			this.con.setRequestProperty("Authorization", "Bearer " + encryptedJWTSignature);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_SET_ERROR, Level.FATAL);
		}
	}

	@Override
	public void setRequestMethod(String method) {
		try {
			Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE + " : " + method, Level.INFO);
			this.con.setRequestMethod(method);
			if (method.equalsIgnoreCase(GlobalLabelParameters.GET))
				logger.info("GETID" + " : " + this.id);
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_SET_ERROR, Level.FATAL);

		}
	}

	@Override
	public void endConnection() {
		con.disconnect();
		con = null;
	}


	@Override
	public void setUserAgent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDigest() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeaderData(String merchantId) {
		throw new UnsupportedOperationException();
	}

}
