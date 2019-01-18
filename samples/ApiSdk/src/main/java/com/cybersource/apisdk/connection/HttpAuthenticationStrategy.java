package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

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
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
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
		this.digestPayLoad = new PayloadDigest(merchantConfig, requestData);
		this.requestType = merchantConfig.getRequestType();
		this.logger = logger;
		try {
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)) {
				String requestId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
				Utility.log(logger, GlobalLabelParameters.GET_ID, requestId, Level.INFO);
			}
		} catch (NullPointerException e) {
			throw new ClientException(GlobalLabelParameters.GET_ID_IS_MISSING);
		}

	}

	@Override
	public void setHeaderData()	throws ConfigException, ClientException {
	
		try {
		   
			try {
			    /*
				 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
				 * Merchant ID or v-c-merchant-id This ID can be found on EBC portal
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
			 * Add Request Header :: "Date" The date and time that the message was
			 * originated from. "HTTP-date" format as defined by RFC7231.
			 */
			con.setRequestProperty(GlobalLabelParameters.DATE, PropertiesUtil.date);
			/*
			 * Add Request Header :: "Host" Sandbox Host :: apitest.cybersource.com
			 * Production Host :: api.cybersource.com
			 */
			con.setRequestProperty(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());
			/*
			 * set digest if request type is POST or PUT
			 */
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				con.setRequestProperty(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
				this.digestMessageBody = digestPayLoad.getDigest();
			}
			/* Add Request Header :: "Signature" */
			String signatureHeaderValue = Authorization.getToken(merchantConfig, requestData);
			con.setRequestProperty(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);

			/* Addition Request Headers : User Agent */
			Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
					Level.INFO);
			con.setRequestProperty(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);

			/* Addition Request Headers : Content Type */
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
						" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
				con.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			}

	   } catch (NoSuchAlgorithmException e) {
			throw new ConfigException(GlobalLabelParameters.No_SUCH_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
		} catch (Exception e) {
			throw new ClientException(GlobalLabelParameters.HEADER_SET_EXCEPTION);
	    }
  }

	/**
	 * open connection
	 * 
	 * @throws ClientException
	 *             - throws custom client exception.
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
	 */
	@Override
	public void openConnection() throws ClientException, IOException {
		URL url = null;
		try {
			/**/
			Proxy proxy = createProxy(merchantConfig);
			String getUrl = merchantConfig.getUrl();
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
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET, url, Level.ERROR);
			throw new ClientException(GlobalLabelParameters.URL_NOT_SET);
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
			Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE, ": ".concat(method), Level.INFO);
			con.setRequestMethod(method);
		} catch (ProtocolException e) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_SET_ERROR, method, Level.FATAL);
			throw new ClientException(GlobalLabelParameters.REQUEST_SET_ERROR.concat(method));
		}
	}

	/**
	 * 
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm will be not there.
	 */
	public Response getHttpConnection() throws IOException, ConfigException, ClientException {
		if (merchantConfig.getRequestType().equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				return executePatch(requestData);
		} else {
			/* Make a new connection using the URL. */
			openConnection();
			/* Add Request Request Type :: "Get or Post" */
			setRequestMethod(requestType);
			/*
			 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
			 * Merchant ID or v-c-merchant-id This ID can be found on EBC portal
			 */
			setHeaderData();

			/* Send post request */

			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				con.setDoOutput(true);
				con.setDoInput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(requestData.getBytes("UTF-8"));
				wr.flush();
				wr.close();
			}
			/* Establishing HTTP connection */
			int responseCode = con.getResponseCode();
			Response responseObj = new Response();
			responseObj.setResponseCode(String.valueOf(responseCode));
			responseObj.setVcCorelationId(con.getHeaderField(GlobalLabelParameters.V_C_CORRELATION_ID));
			Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), PropertiesUtil.date, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
			if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
					|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
				Utility.log(logger, GlobalLabelParameters.DIGEST,
						" : " + Masking.prepareMasking(this.digestMessageBody, logger), Level.INFO);
			}
			Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : ", responseObj.getResponseCode(), Level.INFO);
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
			maskingPayload(response.toString());
			/* Set Response Message */
			String resp = Masking.prepareMasking(response.toString(), logger);
			responseObj.setResponseMessage(resp);

			return responseObj;

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
	 * @return response.
	 * @throws ConfigException
	 *             - throws exception if some configuration error comes.
	 * @throws ClientException
	 *             - throws custom client exception.
	 *  @throws IOException
	 *             - throws exception if some I/O error comes.
	 *  @throws NoSuchAlgorithmException
	 *             - throws exception if some NoSuchAlgorithmException comes.
	 */
	public Response executePatch(String requestData) throws ConfigException, ClientException, IOException {
		
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
		Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE, ": ".concat(GlobalLabelParameters.PATCH), Level.INFO);
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPatch httpPatch = new HttpPatch(merchantConfig.getUrl());
		httpPatch = setPatchHeader(httpPatch); 

		if (null != requestData) {
			Utility.log(logger, "ClaimSet", " : " + Masking.prepareMasking(requestData, logger), Level.INFO);
				HttpEntity	httpEntity = new ByteArrayEntity(requestData.getBytes("UTF-8"));
				httpPatch.setEntity(httpEntity);
		} else {
			Utility.log(logger, "ClaimSet", " : NULL", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.REQUEST_EMPTY);
		}
		
		String responseString = "";
		
            	response = httpClient.execute(httpPatch);
            	if(response != null) {
            		responseString = EntityUtils.toString(response.getEntity());
                	int responseCode = response.getStatusLine().getStatusCode();
            		Response responseObj = new Response();
            		responseObj.setResponseCode(String.valueOf(responseCode));
            		Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), PropertiesUtil.date, Level.INFO);
            		Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
            		Utility.log(logger, GlobalLabelParameters.DIGEST,
            					" : " + Masking.prepareMasking(this.digestMessageBody, logger), Level.INFO);
            		Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : ", responseObj.getResponseCode(), Level.INFO);
            		Utility.logResponseCodeMessage(responseCode, logger);
            		
            		Header header = response.getFirstHeader(GlobalLabelParameters.V_C_CORRELATION_ID);
            		responseObj.setVcCorelationId(header.getValue());
            		Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : ", responseObj.getVcCorelationId(), Level.INFO);
            		
            		StringBuilder responseBuilder = new StringBuilder();
            		responseBuilder.append(responseString);
            		maskingPayload(responseBuilder.toString());
            		/* Set Response Message */
            		String resp = Masking.prepareMasking(responseString, logger);
            		responseObj.setResponseMessage(resp);
            		return responseObj;
            	} else {
            		throw new ClientException(GlobalLabelParameters.EMPTY_RESPONSE);
            	}
		  } catch (UnsupportedEncodingException e) {
				throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
		  } catch (IOException e) {
		    	throw new ClientException(GlobalLabelParameters.IO_EXCEPTION);
		  } catch (Exception e) {
		    	throw new ClientException(GlobalLabelParameters.PATCH_CLIENT_EXCEPTION);
		  } finally {
			  if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
	     }
	}
	
	
	/**
	 * setPatchHeader
	 * @param httpPatch  
	 * @throws ConfigException
	 *             - if some configuration error comes.
	 *  @return HttpPatch          
	 */
	public HttpPatch setPatchHeader(HttpPatch httpPatch) throws ConfigException {
		try {
			/*
			 * Add Request Header :: "v-c-merchant-id" set value to Cybersource
			 * Merchant ID or v-c-merchant-id This ID can be found on EBC portal
			 */
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID, " : ".concat(merchantConfig.getMerchantID()),
					Level.INFO);
			httpPatch.setHeader(GlobalLabelParameters.V_C_MERCHANTID, merchantConfig.getMerchantID());
			/*
			 * Add Request Header :: "Date" The date and time that the message was
			 * originated from. "HTTP-date" format as defined by RFC7231.
			 */
			httpPatch.setHeader(GlobalLabelParameters.DATE, PropertiesUtil.date);
			/*
			 * Add Request Header :: "Host" Sandbox Host :: apitest.cybersource.com
			 * Production Host :: api.cybersource.com
			 */
			httpPatch.setHeader(GlobalLabelParameters.HOST, merchantConfig.getRequestHost());

			/*
			 * Add Request Header :: "profile-id"
			 */
			httpPatch.setHeader("profile-id", "93B32398-AD51-4CC2-A682-EA3E93614EB1");
			
			/*
			 * set digest if request type is POST or PUT
			 */
			
			httpPatch.setHeader(GlobalLabelParameters.DIGEST, digestPayLoad.getDigest());
				this.digestMessageBody = digestPayLoad.getDigest();
			
			/* Add Request Header :: "Signature" */
			String signatureHeaderValue = Authorization.getToken(merchantConfig, requestData);
			httpPatch.setHeader(GlobalLabelParameters.SIGNATURE, signatureHeaderValue);

			/* Addition Request Headers : User Agent */
			Utility.log(logger, GlobalLabelParameters.USERAGENT, " : ".concat(GlobalLabelParameters.USER_AGENT_VALUE),
					Level.INFO);
			httpPatch.setHeader(GlobalLabelParameters.USERAGENT, GlobalLabelParameters.USER_AGENT_VALUE);

			/* Addition Request Headers : Content Type */
				Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE,
						" : ".concat(GlobalLabelParameters.APPLICATION_JSON), Level.INFO);
			httpPatch.setHeader(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);
			return httpPatch;
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, "", Level.FATAL);
			Utility.log(logger, e);
			throw new ConfigException(GlobalLabelParameters.MERCHANTID_NOT_SET);
		}
	}

}
