package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + digest + Signature ) are set inside the HTTP
 * connection and the call is made to API server and response message and code
 * is obtained.
 */
public class HttpAuthenticationStrategy implements AuthenticationStrategy, Headers {

	private MerchantConfig merchantConfig;
	private PayloadDigest digestPayLoad;
	private HttpURLConnection con;
	private String requestType;
	private Logger logger;
	private String digestMessageBody;
	private String requestData;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request Payload .
	 * @param logger
	 *            - logger to log all information in log file.
	 * @throws ClientException
	 *             - if some error will occur.
	 */
	public HttpAuthenticationStrategy(MerchantConfig merchantConfig, String requestData, Logger logger)
			throws ClientException {

		this.merchantConfig = merchantConfig;
		this.requestData = requestData;
		this.digestPayLoad = new PayloadDigest(requestData);
		this.requestType = MerchantUtil.getRequestType();
		this.logger = logger;
	}

	@Override
	public void setHeaderData() throws ConfigException, ClientException {

		try {

			/* Add Request Headers :: Content Type */
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
						" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
				con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			}

			try {
				/*
				 * Add Request Header :: "v-c-merchant-id" set value to
				 * Cybersource Merchant ID or v-c-merchant-id This ID can be
				 * found on portal
				 */
				Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID, " : ".concat(merchantConfig.getMerchantID()),
						Level.INFO);
				con.setRequestProperty(GlobalLabelParameters.V_C_MERCHANTID, merchantConfig.getMerchantID());
			} catch (Exception e) {
				Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, "", Level.FATAL);
				Utility.log(logger, e);
				throw new ConfigException(GlobalLabelParameters.MERCHANTID_NOT_SET);
			}

			/*
			 * Add Request Header :: "Date" The date and time that the message
			 * was originated from. "HTTP-date" format as defined by RFC7231.
			 */
			con.setRequestProperty(GlobalLabelParameters.DATE, MerchantUtil.date);
			Utility.log(logger, GlobalLabelParameters.DATE, " : ".concat(MerchantUtil.date), Level.INFO);
			/*
			 * Add Request Header :: "Host" Sandbox Host ::
			 * apitest.cybersource.com Production Host :: api.cybersource.com
			 */
			con.setRequestProperty(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());
			Utility.log(logger, GlobalLabelParameters.HOST, " : ".concat(merchantConfig.getRequestHost()), Level.INFO);
			/*
			 * set digest if request type is POST or PUT
			 */
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				con.setRequestProperty(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
				this.digestMessageBody = digestPayLoad.getDigest();
				Utility.log(logger, GlobalLabelParameters.DIGEST, " : ".concat(this.digestMessageBody), Level.INFO);
			}
			/* Add Request Header :: "Signature" */
			String signatureHeaderValue = Authorization.getToken(merchantConfig, requestData);
			con.setRequestProperty(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
			Utility.log(logger, GlobalLabelParameters.SIGNATURE, " : ".concat(signatureHeaderValue), Level.INFO);

			/* Addition Request Headers : User Agent */
			con.setRequestProperty(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
			Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
					Level.INFO);
		} catch (ConfigException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(GlobalLabelParameters.HEADER_SET_EXCEPTION + e.getMessage());
		}
	}

	/**
	 * open connection
	 * 
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	@Override
	public void openConnection() throws ClientException {
		URL url = null;
		try {
			/**/
			Proxy proxy = createProxy(merchantConfig);
			String getUrl = MerchantUtil.getUrl();
			Utility.log(logger, "URL : ", " " + getUrl, Level.INFO);
			url = new URL(getUrl);
			con = (HttpURLConnection) url.openConnection(proxy);
			/**/
			if (proxy != Proxy.NO_PROXY) {
				addProxyCredentials(con, merchantConfig);
			}
			/* Creating Time out. */
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			con.setConnectTimeout(timeoutInMS);
			con.setReadTimeout(timeoutInMS);
		} catch (IOException e) {
			throw new ClientException(GlobalLabelParameters.URL_NOT_SET + url);
		}
	}

	/**
	 * Mask the Response.
	 */
	@Override
	public void maskingPayload(String serverResponse, String title) {
		Utility.log(logger, title, Masking.prepareMasking(serverResponse), Level.INFO);
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
	 * set Request Method.
	 * 
	 * @throws ClientException
	 *             - throws custom client exception.
	 */

	@Override
	public void setRequestMethod(String method) throws ClientException {
		try {
			con.setRequestMethod(method);
		} catch (ProtocolException e) {
			throw new ClientException(GlobalLabelParameters.REQUEST_SET_ERROR.concat(method));
		}
	}

	/**
	 * 
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response getHttpConnection() throws ConfigException, ClientException {

		try {

			if (requestType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				return executePatch(requestData);
			} else {
				/* Make a new connection using the URL. */
				openConnection();
				/* Add Request Request Type :: "Get or Post" */
				setRequestMethod(requestType);
				/*
				 * Add Request Header :: "v-c-merchant-id" set value to
				 * Cybersource Merchant ID or v-c-merchant-id This ID can be
				 * found on EBC portal
				 */
				setHeaderData();

				/* Send post request */

				if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
					/* Masking Request Body */
					Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, Masking.prepareMasking(requestData),
							Level.INFO);
					con.setDoOutput(true);
					con.setDoInput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.write(requestData.getBytes("UTF-8"));
					wr.flush();
					wr.close();
				}
				/* Establishing HTTP connection */
				Integer responseCode = con.getResponseCode();
				Response responseObj = new Response();
				responseObj.setResponseCode(String.valueOf(responseCode));
				responseObj.setVcCorelationId(con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID));
				Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : ", responseObj.getResponseCode(),
						Level.INFO);
				Utility.logResponseCodeMessage(responseCode, logger);
				Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : ", responseObj.getVcCorelationId(),
						Level.INFO);

				BufferedReader in = null;
				/* Reading Response Message */
				if (this.con.getErrorStream() != null) {
					in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				} else {
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				}
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				/* Close HTTP Connection. */
				endConnection();
				/* Masking the response payload from the server. */
				maskingPayload(response.toString(), GlobalLabelParameters.RESPONSE_MSG_LABEL);
				/* Set Response Message */
				String resp = Masking.prepareMasking(response.toString());
				responseObj.setResponseMessage(resp);
				return responseObj;
			}
		} catch (IOException e) {
			Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, e.toString(), Level.FATAL);
			throw new ClientException(GlobalLabelParameters.IO_EXCEPTION + e.toString());
		} catch (ConfigException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (ClientException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (Exception e) {
			Utility.log(logger, e.getMessage(), "", Level.ERROR);
			Utility.log(logger, e);
			throw new ClientException("Exception in getHttpConnection method : " + e.getMessage());
		}
	}

	/**
	 * Creates a HTTP Proxy request
	 * 
	 * @param mc
	 * @return
	 */
	private static Proxy createProxy(MerchantConfig mc) {
		if (mc.getProxyAddress() == null) {
			return Proxy.NO_PROXY;
		}

		InetSocketAddress addr = new InetSocketAddress(mc.getProxyAddress(), mc.getProxyPort());

		return new Proxy(Proxy.Type.HTTP, addr);
	}

	/**
	 * Add proxy credentails for creating a connection
	 * 
	 * @param con
	 * @param mc
	 */
	private static void addProxyCredentials(HttpURLConnection con, MerchantConfig mc) {
		if (mc.getProxyUser() != null) {
			con.setRequestProperty("Proxy-Authorization", "Basic " + Base64
					.encode((mc.getProxyUser() + ":" + mc.getProxyPassword()).getBytes(Charset.forName("UTF-8"))));
		}
	}

	/**
	 * 
	 * Execute Patch Call
	 * 
	 * @return response.
	 * @throws ConfigException
	 *             - throws exception if some configuration error comes.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response executePatch(String requestData) throws ConfigException, ClientException {

		/* For Disable the org.apache.log */
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
		/* For Disable the org.apache.log */
		CloseableHttpResponse response = null;
		try {
			Utility.log(logger, "URL : ", MerchantUtil.getUrl(), Level.INFO);

			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPatch httpPatch = new HttpPatch(MerchantUtil.getUrl());
			httpPatch = setPatchHeader(httpPatch);

			if (null != requestData) {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, " " + Masking.prepareMasking(requestData),
						Level.INFO);
				HttpEntity httpEntity = new ByteArrayEntity(requestData.getBytes("UTF-8"));
				httpPatch.setEntity(httpEntity);
			} else {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, "  NULL", Level.ERROR);
				throw new ConfigException(GlobalLabelParameters.REQUEST_EMPTY);
			}

			String responseString = "";

			response = httpClient.execute(httpPatch);
			if (response != null) {
				responseString = EntityUtils.toString(response.getEntity());
				Integer responseCode = response.getStatusLine().getStatusCode();
				Response responseObj = new Response();
				responseObj.setResponseCode(String.valueOf(responseCode));
				Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : ", responseObj.getResponseCode(),
						Level.INFO);
				Utility.logResponseCodeMessage(responseCode, logger);

				Header header = response.getFirstHeader(GlobalLabelParameters.V_C_CORRELATION_ID);
				responseObj.setVcCorelationId(header.getValue());
				Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : ", responseObj.getVcCorelationId(),
						Level.INFO);

				StringBuilder responseBuilder = new StringBuilder();
				responseBuilder.append(responseString);
				maskingPayload(responseBuilder.toString(), GlobalLabelParameters.RESPONSE_MSG_LABEL);
				/* Set Response Message */
				String resp = Masking.prepareMasking(responseString);
				responseObj.setResponseMessage(resp);
				return responseObj;
			} else {
				Utility.log(logger, GlobalLabelParameters.EMPTY_RESPONSE, "", Level.FATAL);
				throw new ClientException(GlobalLabelParameters.EMPTY_RESPONSE);
			}
		} catch (IOException e) {
			throw new ClientException(GlobalLabelParameters.IO_EXCEPTION.concat(e.getMessage()));
		} catch (ConfigException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(GlobalLabelParameters.PATCH_CLIENT_EXCEPTION.concat(e.getMessage()));
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					Utility.log(logger, e);
				}
			}
		}
	}

	/**
	 * setPatchHeader
	 * 
	 * @param httpPatch
	 * @throws ConfigException
	 *             - if some configuration error comes.
	 * @return HttpPatch
	 * @throws ClientException
	 */
	public HttpPatch setPatchHeader(HttpPatch httpPatch) throws ConfigException, ClientException {
		try {
			/* Addition Request Header : "Content Type" */
			httpPatch.setHeader(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
					" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
			/*
			 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
			 * Merchant ID or v-c-merchant-id This ID can be found on EBC portal
			 */
			httpPatch.setHeader(GlobalLabelParameters.V_C_MERCHANTID, merchantConfig.getMerchantID());
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID, " : ".concat(merchantConfig.getMerchantID()),
					Level.INFO);
			/*
			 * Add Request Header :: "Date" The date and time that the message
			 * was originated from. "HTTP-date" format as defined by RFC7231.
			 */
			httpPatch.setHeader(GlobalLabelParameters.DATE, MerchantUtil.date);
			Utility.log(logger, GlobalLabelParameters.DATE, " : ".concat(MerchantUtil.date), Level.INFO);
			/*
			 * Add Request Header :: "Host" Sandbox Host ::
			 * apitest.cybersource.com Production Host :: api.cybersource.com
			 */
			httpPatch.setHeader(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());
			Utility.log(logger, GlobalLabelParameters.HOST, " : ".concat(merchantConfig.getRequestHost()), Level.INFO);

			/*
			 * Add Request Header :: "profile-id"
			 */
			httpPatch.setHeader(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
			Utility.log(logger, GlobalLabelParameters.PROFILE_ID, " : ".concat(GlobalLabelParameters.PROFILE_ID_VAL),
					Level.INFO);

			/*
			 * Add Request Header :: "Digest"
			 */

			httpPatch.setHeader(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
			this.digestMessageBody = digestPayLoad.getDigest();
			Utility.log(logger, GlobalLabelParameters.DIGEST, " : ".concat(this.digestMessageBody), Level.INFO);

			/* Add Request Header :: "Signature" */
			String signatureHeaderValue = Authorization.getToken(merchantConfig, requestData);
			httpPatch.setHeader(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
			Utility.log(logger, GlobalLabelParameters.SIGNATURE, " : ".concat(signatureHeaderValue), Level.INFO);

			/* Addition Request Header : "User Agent" */
			httpPatch.setHeader(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
			Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
					Level.INFO);
			return httpPatch;
		} catch (ConfigException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(GlobalLabelParameters.HEADER_SET_EXCEPTION.concat(e.getMessage()));
		}
	}

}
