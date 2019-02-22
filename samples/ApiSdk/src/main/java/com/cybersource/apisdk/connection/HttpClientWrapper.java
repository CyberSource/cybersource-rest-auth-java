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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
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
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

public class HttpClientWrapper implements HttpClientStrategy {

	private MerchantConfig merchantConfig;
	private HttpURLConnection con;
	private RequestContext requestContext;
	private static volatile HttpClientWrapper instance;
	private Logger logger;

	private HttpClientWrapper(MerchantConfig merchantConfig, RequestContext requestContext) {
		this.merchantConfig = merchantConfig;
		this.requestContext = requestContext;
		this.logger = merchantConfig.getLogger();
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains all information for merchant.
	 * @return instance of HttpClientWrapper
	 */
	public static HttpClientWrapper getInstance(MerchantConfig merchantConfig, RequestContext requestContext) {
		if (instance == null) {
			synchronized (HttpClientWrapper.class) {
				if (instance == null) {
					instance = new HttpClientWrapper(merchantConfig, requestContext);
				}
			}
		}
		return instance;
	}

	/**
	 * Open connection object
	 */
	public void openConnection() throws ClientException {
		String conUrl = "";
		try {
			Proxy proxy = createProxy(merchantConfig);
			conUrl = requestContext.getUrl();
			Utility.log(logger, "URL : ", " ".concat(conUrl), Level.INFO);
			URL url = new URL(conUrl);
			con = (HttpURLConnection) url.openConnection(proxy);
			if (proxy != Proxy.NO_PROXY) {
				addProxyCredentials(con, merchantConfig);
			}
			/* Creating Time out. */
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			con.setConnectTimeout(timeoutInMS);
			con.setReadTimeout(timeoutInMS);
		} catch (IOException e) {
			throw new ClientException(GlobalLabelParameters.URL_NOT_SET.concat(conUrl), e);
		}
	}

	/**
	 * Add proxy credentails for creating a connection
	 * 
	 * @param con
	 * @param mc
	 */
	private void addProxyCredentials(HttpURLConnection con, MerchantConfig mc) {
		if (StringUtils.isNotBlank(mc.getProxyUser())) {
			con.setRequestProperty("Proxy-Authorization", "Basic " + Base64
					.encode((mc.getProxyUser() + ":" + mc.getProxyPassword()).getBytes(Charset.forName("UTF-8"))));
		}
	}

	/**
	 * SSL Validation
	 * 
	 * @param con
	 * @param mc
	 */
	public void sslValidation() throws ClientException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
		} };
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			throw new ClientException("SSL Key Management Exception, check proxy config", e);
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier validHosts = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
	}

	/**
	 * Creates a HTTP Proxy request
	 * 
	 * @param mc
	 * @return
	 * @throws ClientException
	 */
	private Proxy createProxy(MerchantConfig mc) throws ClientException {
		if (StringUtils.isBlank(mc.getProxyAddress())) {
			return Proxy.NO_PROXY;
		} else {
			if (StringUtils.isBlank(mc.getProxyUser())) {
				sslValidation();
			}
			InetSocketAddress addr = new InetSocketAddress(mc.getProxyAddress(), mc.getProxyPort());
			return new Proxy(Proxy.Type.HTTP, addr);
		}
	}

	/**
	 * set the Request Method
	 * 
	 * @param method
	 */
	public void setRequestMethod(String method) throws ClientException {
		try {
			con.setRequestMethod(method);
		} catch (ProtocolException e) {
			throw new ClientException(GlobalLabelParameters.REQUEST_SET_ERROR.concat(method), e);
		}
	}

	/**
	 * set the Header data
	 * 
	 * @param headerMap
	 */
	public void setHeaderData(Map<String, String> headerMap)
			throws ConfigException, ClientException, SignatureGenerationException {
		for (Entry<String, String> entry : headerMap.entrySet()) {
			con.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * end connection.
	 */
	public void endConnection() {
		if (con != null) {
			con.disconnect();
			con = null;
		}
	}

	/**
	 * send request
	 * 
	 * @param headerMap
	 *            - contains header values of the request
	 * @return response
	 */
	public Response sendRequest(Map<String, String> headerMap)
			throws ConfigException, ClientException, SignatureGenerationException {
		BufferedReader inpStream = null;
		try {
			String requestMethodType = requestContext.getRequestMethodType();
			String requestBody = requestContext.getRequestBody();
			if (requestContext.getRequestMethodType().equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				return executePatch(headerMap);
			} else {
				/* Make a new connection using the URL. */
				openConnection();
				/* Add Request Request Type :: "Get or Post" */
				setRequestMethod(requestContext.getRequestMethodType());
				/*
				 * Add Request Header :: "v-c-merchant-id" set value to
				 * Cybersource Merchant ID or v-c-merchant-id This ID can be
				 * found on EBC portal
				 */
				setHeaderData(headerMap);

				/* Send post request */

				if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
					/* Masking Request Body */
					Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, Masking.prepareMasking(requestBody),
							Level.INFO);
					con.setDoOutput(true);
					con.setDoInput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.write(requestBody.getBytes("UTF-8"));
					wr.flush();
					wr.close();
				}
				/* Establishing HTTP connection */
				Integer responseCode = con.getResponseCode();
				Response responseObj = new Response();
				responseObj.setResponseCode(String.valueOf(responseCode));
				responseObj.setVcCorelationId(con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID));
				Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE.concat(" : "), responseObj.getResponseCode(),
						Level.INFO);
				logResponseCodeMessage(responseCode, logger);
				Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID.concat(" : "),
						responseObj.getVcCorelationId(), Level.INFO);

				/* Reading Response Message */
				if (this.con.getErrorStream() != null) {
					inpStream = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				} else {
					inpStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
				}
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = inpStream.readLine()) != null) {
					response.append(inputLine);
				}
				/* Masking the response payload from the server. */
				maskingPayload(response.toString(), GlobalLabelParameters.RESPONSE_MSG_LABEL);
				/* Set Response Message */
				String resp = Masking.prepareMasking(response.toString());
				responseObj.setResponseMessage(resp);
				return responseObj;
			}
		} catch (IOException e) {
			Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, e.toString(), Level.FATAL);
			throw new ClientException(GlobalLabelParameters.IO_EXCEPTION.concat(e.toString()), e);
		} catch (ConfigException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (SignatureGenerationException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (ClientException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (Exception e) {
			Utility.log(logger, e.getMessage(), "", Level.ERROR);
			Utility.log(logger, e);
			throw new ClientException("Exception in sendRequest method : ".concat(e.getMessage()), e);
		} finally {
			if (inpStream != null) {
				try {
					inpStream.close();
				} catch (IOException e) {
					Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, e.toString(), Level.FATAL);
				}
			}
			endConnection();
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
	protected Response executePatch(Map<String, String> headerMap) throws ConfigException, ClientException {

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
			String requestBody = requestContext.getRequestBody();
			Utility.log(logger, "URL : ", requestContext.getUrl(), Level.INFO);

			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPatch httpPatch = new HttpPatch(requestContext.getUrl());
			httpPatch = setPatchHeader(httpPatch, headerMap);

			if (null != requestBody) {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, " " + Masking.prepareMasking(requestBody),
						Level.INFO);
				HttpEntity httpEntity = new ByteArrayEntity(requestBody.getBytes("UTF-8"));
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
				Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE.concat(" : "), responseObj.getResponseCode(),
						Level.INFO);
				logResponseCodeMessage(responseCode, logger);

				Header header = response.getFirstHeader(GlobalLabelParameters.V_C_CORRELATION_ID);
				responseObj.setVcCorelationId(header.getValue());
				Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID.concat(" : "),
						responseObj.getVcCorelationId(), Level.INFO);

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
			throw new ClientException(GlobalLabelParameters.IO_EXCEPTION.concat(e.getMessage()), e);
		} catch (ConfigException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(GlobalLabelParameters.PATCH_CLIENT_EXCEPTION.concat(e.getMessage()), e);
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
	 * set headers for patch method
	 * 
	 * @param httpPatch
	 * @param headerMap
	 */
	private HttpPatch setPatchHeader(HttpPatch httpPatch, Map<String, String> headerMap) {
		for (Entry<String, String> entry : headerMap.entrySet()) {
			httpPatch.setHeader(entry.getKey(), entry.getValue());
		}
		return httpPatch;
	}

	/**
	 * Mask the Response.
	 * 
	 * @param serverResponse
	 * @param title
	 * @
	 */
	@Override
	public void maskingPayload(String serverResponse, String title) {
		Utility.log(logger, title, Masking.prepareMasking(serverResponse), Level.INFO);
	}

}
