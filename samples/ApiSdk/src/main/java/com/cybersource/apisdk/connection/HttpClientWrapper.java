package com.cybersource.apisdk.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

public class HttpClientWrapper {

	private MerchantConfig merchantConfig;
	private static volatile HttpClientWrapper instance;
	private Logger logger;
	HttpClient httpClient = null;

	private HttpClientWrapper(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
		this.logger = merchantConfig.getLogger();
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains all information for merchant.
	 * @return instance of HttpClientWrapper
	 */
	public static HttpClientWrapper getInstance(MerchantConfig merchantConfig) {
		if (instance == null) {
			synchronized (HttpClientWrapper.class) {
				if (instance == null) {
					instance = new HttpClientWrapper(merchantConfig);
				}
			}
		}
		return instance;
	}

	/**
	 * sendRequest
	 * 
	 * @param requestContext
	 *            - contains all request details.
	 * @return Response
	 */
	public Response sendRequest(RequestContext requestContext)
			throws ConfigException, ClientException, SignatureGenerationException {
		disableHttpClientLog(); /* Disable the HttpClient log */
		CloseableHttpResponse response = null;
		try {
			String requestBody = requestContext.getRequestBody();
			Utility.log(logger, "URL : ", requestContext.getUrl(), Level.INFO);

			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeoutInMS)
					.setConnectTimeout(timeoutInMS).setConnectionRequestTimeout(timeoutInMS).build();

			httpClient = openConnection();
			HttpClientContext context = HttpClientContext.create();

			String requestMethodType = requestContext.getRequestMethodType();

			if (StringUtils.isNotBlank(merchantConfig.getProxyUser())) {
				context.setCredentialsProvider(credentialsProvider());
			}

			if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				HttpPatch httpPatch = new HttpPatch(requestContext.getUrl());
				httpPatch.setConfig(setProxy(defaultRequestConfig));
				setHeader(httpPatch, getHeader(requestContext));
				httpPatch.setEntity(setRequestBody(httpPatch, requestBody));
				response = (CloseableHttpResponse) httpClient.execute(httpPatch, context);
			} else if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)) {
				HttpPost httpPost = new HttpPost(requestContext.getUrl());
				httpPost.setConfig(setProxy(defaultRequestConfig));
				setHeader(httpPost, getHeader(requestContext));
				httpPost.setEntity(setRequestBody(httpPost, requestBody));
				response = (CloseableHttpResponse) httpClient.execute(httpPost, context);
			} else if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				HttpPut httpPut = new HttpPut(requestContext.getUrl());
				httpPut.setConfig(setProxy(defaultRequestConfig));
				setHeader(httpPut, getHeader(requestContext));
				httpPut.setEntity(setRequestBody(httpPut, requestBody));
				response = (CloseableHttpResponse) httpClient.execute(httpPut, context);
			} else if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)) {
				HttpGet httpGet = new HttpGet(requestContext.getUrl());
				httpGet.setConfig(setProxy(defaultRequestConfig));
				setHeader(httpGet, getHeader(requestContext));
				response = (CloseableHttpResponse) httpClient.execute(httpGet, context);
			} else if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
				HttpDelete httpDelete = new HttpDelete(requestContext.getUrl());
				httpDelete.setConfig(setProxy(defaultRequestConfig));
				setHeader(httpDelete, getHeader(requestContext));
				response = (CloseableHttpResponse) httpClient.execute(httpDelete, context);
			} else {
				throw new ConfigException(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);
			}

			String responseString = "";
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
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
	}

	/**
	 * disableHttpClientLog - For disable the apache client logs
	 */
	private void disableHttpClientLog() {
		/* For Disable the org.apache.commons.logging */
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
	}

	/**
	 * getHeader
	 * 
	 * @param requestContext
	 *            - contains all request details.
	 * @return headerMap
	 */
	public Map<String, String> getHeader(RequestContext requestContext)
			throws ConfigException, ClientException, SignatureGenerationException {
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return getHttpHeader(requestContext);
		} else if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return getJWTHeader(requestContext);
		} else {
			return null;
		}
	}

	/**
	 * getHttpHeader
	 * 
	 * @param requestContext
	 *            - contains all request details.
	 * @return HTTP headerMap
	 */
	public Map<String, String> getHttpHeader(RequestContext requestContext)
			throws ConfigException, ClientException, SignatureGenerationException {

		Map<String, String> headerMap = new HashMap<String, String>();
		PayloadDigest payloadDigest;
		String digestMessageBody;
		String requestMethodType = requestContext.getRequestMethodType();
		/* Add Request Headers :: Content Type */
		/* Add Request Headers :: Digest */
		if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
					" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
			headerMap.put(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			payloadDigest = new PayloadDigest(requestContext.getRequestBody());
			digestMessageBody = payloadDigest.getDigest();
			headerMap.put(GlobalLabelParameters.DIGEST, payloadDigest.getDigest());
			Utility.log(logger, GlobalLabelParameters.DIGEST, " : ".concat(digestMessageBody), Level.INFO);

			/*
			 * Add Request Header :: "profile-id"
			 */
			if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				headerMap.put(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
				Utility.log(logger, GlobalLabelParameters.PROFILE_ID,
						" : ".concat(GlobalLabelParameters.PROFILE_ID_VAL), Level.INFO);
			}
		}

		try {
			/*
			 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
			 * Merchant ID or v-c-merchant-id This ID can be found on portal
			 */
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANT_ID, " : ".concat(merchantConfig.getMerchantID()),
					Level.INFO);
			headerMap.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, "", Level.FATAL);
			Utility.log(logger, e);
			throw new ConfigException(GlobalLabelParameters.MERCHANTID_NOT_SET, e);
		}

		/*
		 * Add Request Header :: "Date" The date and time that the message was
		 * originated from. "HTTP-date" format as defined by RFC7231.
		 */
		headerMap.put(GlobalLabelParameters.DATE, requestContext.getDate());
		Utility.log(logger, GlobalLabelParameters.DATE, " : ".concat(requestContext.getDate()), Level.INFO);
		/*
		 * Add Request Header :: "Host" Sandbox Host :: apitest.cybersource.com
		 * Production Host :: api.cybersource.com
		 */
		headerMap.put(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());
		Utility.log(logger, GlobalLabelParameters.HOST, " : ".concat(merchantConfig.getRequestHost()), Level.INFO);
		/* Add Request Header :: "Signature" */
		String signatureHeaderValue = Authorization.getToken(merchantConfig, requestContext);
		headerMap.put(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);
		Utility.log(logger, GlobalLabelParameters.SIGNATURE, " : ".concat(signatureHeaderValue), Level.INFO);

		/* Addition Request Headers : User Agent */
		headerMap.put(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);
		Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
				Level.INFO);
		return headerMap;
	}

	/**
	 * getJWTHeader
	 * 
	 * @param requestContext
	 *            - contains all request details.
	 * @return JWT headerMap
	 */
	public Map<String, String> getJWTHeader(RequestContext requestContext)
			throws ConfigException, SignatureGenerationException, ClientException {
		Map<String, String> headerMap = new HashMap<String, String>();
		/* Addition Request Headers : Content Type */
		Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
				Level.INFO);
		headerMap.put(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);

		/* Add Request Header :: "Signature" */
		String encryptedJWTSignature = Authorization.getToken(merchantConfig, requestContext);
		headerMap.put("Authorization", "Bearer " + encryptedJWTSignature);
		Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, encryptedJWTSignature, Level.INFO);
		/*
		 * Add Request Header :: "profile-id"
		 */
		if (requestContext.getRequestMethodType().equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			headerMap.put(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
			Utility.log(logger, GlobalLabelParameters.PROFILE_ID, " : ".concat(GlobalLabelParameters.PROFILE_ID_VAL),
					Level.INFO);
		}
		return headerMap;
	}

	/**
	 * Credentials provider for authenticating with proxy address
	 */
	private CredentialsProvider credentialsProvider() {
		/* sets up credentials object */
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(new HttpHost(merchantConfig.getProxyAddress())),
				new UsernamePasswordCredentials(merchantConfig.getProxyUser(), merchantConfig.getProxyPassword()));
		return credsProvider;
	}

	/**
	 * Open HTTP Client Connection
	 * 
	 * @return HttpClient
	 * @throws ClientException
	 */
	public HttpClient openConnection() throws ClientException {
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, (chain, authType) -> true);
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
					NoopHostnameVerifier.INSTANCE);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (NoSuchAlgorithmException e) {
			throw new ClientException(GlobalLabelParameters.NO_SUCH_ALGORITHM_CLIENT_EXCEPTION, e);
		} catch (KeyStoreException e) {
			throw new ClientException(GlobalLabelParameters.KEY_STORE_CLIENT_EXCEPTION, e);
		} catch (KeyManagementException e) {
			throw new ClientException(GlobalLabelParameters.KEY_MANAGEMENT_CLIENT_EXCEPTION, e);
		}
	}

	/**
	 * setProxy
	 * 
	 * @param defaultRequestConfig
	 * @return RequestConfig
	 * @throws ClientException
	 */
	private RequestConfig setProxy(RequestConfig defaultRequestConfig) {
		if (StringUtils.isNotBlank(merchantConfig.getProxyAddress())) {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setProxy(new HttpHost(merchantConfig.getProxyAddress(), merchantConfig.getProxyPort())).build();
			return requestConfig;
		} else {
			return defaultRequestConfig;
		}
	}

	/**
	 * set headers for HTTP method
	 * 
	 * @param httpMethod
	 * @param headerMap
	 */
	private <T> T setHeader(T httpMethod, Map<String, String> headerMap) {
		headerMap.forEach((k, v) -> {
			((HttpMessage) httpMethod).setHeader(k, v);
		});
		return httpMethod;
	}

	/**
	 * setRequestBody
	 * 
	 * @param httpMethod
	 * @param requestBody
	 * @return HttpEntity
	 * @throws ConfigException
	 * @throws ClientException
	 */
	private <T> HttpEntity setRequestBody(T httpMethod, String requestBody) throws ConfigException, ClientException {
		try {
			if (null != requestBody) {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, " " + Masking.prepareMasking(requestBody),
						Level.INFO);
				return new ByteArrayEntity(requestBody.getBytes("UTF-8"));
			} else {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, "  NULL", Level.ERROR);
				throw new ConfigException(GlobalLabelParameters.REQUEST_EMPTY);
			}
		} catch (UnsupportedEncodingException e) {
			throw new ClientException(GlobalLabelParameters.UNSUPPORTED_ENCODING, e);
		}
	}

	/**
	 * Mask the Response.
	 * 
	 * @param serverResponse
	 * @param title
	 * @
	 */
	public void maskingPayload(String serverResponse, String title) {
		Utility.log(logger, title, Masking.prepareMasking(serverResponse), Level.INFO);
	}

	/**
	 * Sets the Response code message
	 * 
	 * @param responseCode
	 * @param logger
	 */
	public void logResponseCodeMessage(Integer responseCode, Logger logger) {
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
