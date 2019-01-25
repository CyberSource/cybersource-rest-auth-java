package com.cybersource.apisdk.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
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
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * Here all the data ( header + claimSet + Signature ) are set inside the HTTP
 * connection and the call is made to APIserver. On successful transaction,
 * response message and response code : 200 is obtained.
 */
public class JWTAuthenticationStrategy implements AuthenticationStrategy, Headers {

	private boolean isSignatureAlert = false;
	private HttpURLConnection connection = null;
	private MerchantConfig merchantConfig = null;
	private String requestData = null;
	private Logger logger = null;
	private Integer responseCode;
	private String responseMessage;
	private String jwtMethodType = null;
	private String getId = null;
	private String requestBody = null;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestData
	 *            - request Payload .
	 * @param logger
	 *            - logger to log all information in log file.
	 */
	public JWTAuthenticationStrategy(MerchantConfig merchantConfig, String requestData, Logger logger) {
		this.merchantConfig = merchantConfig;
		this.requestData = requestData;
		this.logger = logger;
		this.jwtMethodType = MerchantUtil.getRequestType();
		this.getId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());

	}

	Response responseObj;

	/**
	 * @return response.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response getJwtConnection() throws ConfigException, ClientException {
		
	  try {
		  
		  if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
				return executePatch(requestData);
		   } else {
				if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
						|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
					requestBody = getId;
				} else /* jwtMethod = POST */ {
					requestBody = requestData;
				}
				if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
						|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
		
					responseObj = jwtConnection();
					responseCode = connection.getResponseCode();
				} else if (jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| jwtMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
					responseObj = postDocument(requestBody);
					responseCode = connection.getResponseCode();
				}
				responseMessage = Masking.prepareMasking(responseObj.getResponseMessage());
				responseObj.setResponseMessage(responseMessage);
				maskingPayload(responseMessage, GlobalLabelParameters.RESPONSE_MSG_LABEL);
				endConnection();
				return responseObj;
		}
	  } catch (IOException e) {
		  Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, "", Level.ERROR);
    	  throw new ClientException(GlobalLabelParameters.IO_EXCEPTION);
      } catch (ConfigException e) {
    	  Utility.log(logger, e.getMessage(), "", Level.ERROR);
    	  throw e;
      } catch (ClientException e) {
    	  Utility.log(logger, e.getMessage(), "", Level.ERROR);
    	  throw e;
      }	catch (Exception e) {
    	  Utility.log(logger,e);
    	  throw new ClientException("Exception in getJwtConnection method");
	  }
	}

	/**
	 * Set Header-Data.
	 * 
	 * @throws ClientException
	 */
	@Override
	public void setHeaderData() throws ConfigException, ClientException {
		/* Addition Request Headers : Content Type */

		Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
				Level.INFO);
		this.connection.setRequestProperty(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);

		/* Add Request Header :: "Signature" */
		String encryptedJWTSignature = Authorization.getToken(merchantConfig, requestData, jwtMethodType);
		if (encryptedJWTSignature == null || encryptedJWTSignature.isEmpty()) {
			isSignatureAlert = true;
			return;
		}
		this.connection.setRequestProperty("Authorization", "Bearer " + encryptedJWTSignature);
		Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, encryptedJWTSignature, Level.INFO);
	}

	/**
	 * @return response.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response jwtConnection() throws ConfigException, ClientException {
      try {
    	/* Open Connection with API server. */
  		openConnection();
  		setHeaderData();
  		setRequestMethod(jwtMethodType);
  		/*
  		 * Set encrypted signature in the connection object. setSignature();
  		 */
  		if (isSignatureAlert) {
  			responseObj.setResponseMessage("No Response");
  			return responseObj;
  		}
  		responseCode = connection.getResponseCode();
  		Utility.logResponseCodeMessage(responseCode, logger);
  		return this.connection.getErrorStream() != null ? this.readInputStream(this.connection.getErrorStream())
  				: this.readInputStream(this.connection.getInputStream());

      } catch (IOException e) {
    	  Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, "", Level.ERROR);
    	  throw new ClientException(GlobalLabelParameters.IO_EXCEPTION);
      } catch (ConfigException e) {
    	  Utility.log(logger, "Exception in jwtConnection method", "", Level.ERROR);
    	  throw e;
      } catch (ClientException e) {
    	  Utility.log(logger, "Exception in jwtConnection method", "", Level.ERROR);
    	  throw e;
      }
   }

	/**
	 * 
	 * @param request
	 *            - request type.
	 * @return response.
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
	 */
	public Response postDocument(String request) throws ConfigException, ClientException, IOException {
		openConnection();
		setHeaderData();
		setRequestMethod(jwtMethodType);
		Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, Masking.prepareMasking(requestData), Level.INFO);
		if (isSignatureAlert) {
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
	 *            - input stream to generate response.
	 * @return response.
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
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
	 * 
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	@Override
	public void openConnection() throws ClientException {
		URL url = null;
		try {
			Proxy proxy = createProxy(merchantConfig);
			String getUrl = merchantConfig.getUrl();
			Utility.log(logger, "URL : ", " " + getUrl, Level.INFO);
			url = new URL(getUrl);
			this.connection = (HttpURLConnection) url.openConnection(proxy);
			if (proxy != Proxy.NO_PROXY) {
	            addProxyCredentials(connection, merchantConfig);
	        }
			int timeoutInMS = merchantConfig.getTimeout() * 1000;
			connection.setConnectTimeout(timeoutInMS);
			connection.setReadTimeout(timeoutInMS);
		} catch (MalformedURLException e) {
			Utility.log(logger, GlobalLabelParameters.URL_NOT_SET, url, Level.ERROR);
			throw new ClientException(GlobalLabelParameters.URL_NOT_SET);

		} catch (IOException e) {
			Utility.log(logger, GlobalLabelParameters.CONNECTION_ERROR, url, Level.ERROR);
			throw new ClientException(GlobalLabelParameters.CONNECTION_ERROR);
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
	 * Set method to call.
	 * 
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	@Override
	public void setRequestMethod(String method) throws ClientException {
		try {
			//Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), method, Level.INFO);
			this.connection.setRequestMethod(method);
			if (method.equalsIgnoreCase(GlobalLabelParameters.GET))
				logger.info("GETID" + " : " + this.getId);
		} catch (ProtocolException e) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_SET_ERROR.concat(" : "), method, Level.FATAL);
			throw new ClientException(GlobalLabelParameters.REQUEST_SET_ERROR.concat(method));
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
     * Creates a HTTP Proxy request
     * @param mc
     * @return
     */
    private static Proxy createProxy(MerchantConfig mc) {
        if (mc.getProxyAddress() == null) {
            return Proxy.NO_PROXY;
        }

        InetSocketAddress addr
                = new InetSocketAddress(
                mc.getProxyAddress(), mc.getProxyPort());

        return new Proxy(Proxy.Type.HTTP, addr);
    }
    
    /**
     * Add proxy credentails for creating a connection
     * @param con
     * @param mc
     */
    private static void addProxyCredentials(
            HttpURLConnection con, MerchantConfig mc){
        if (mc.getProxyUser() != null) {
            con.setRequestProperty(
                    "Proxy-Authorization",
                    "Basic " +
                            Base64.encode((mc.getProxyUser() + ":" + mc.getProxyPassword()).getBytes(Charset.forName("UTF-8"))));
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
			Utility.log(logger, "URL : ", merchantConfig.getUrl() , Level.INFO);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPatch httpPatch = new HttpPatch(merchantConfig.getUrl());
			httpPatch = setPatchHeader(httpPatch); 
			
			if (isSignatureAlert) {
				responseObj.setResponseMessage("No Response");
				return responseObj;
			}
			
			
			if (null != requestData) {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, " " + Masking.prepareMasking(requestData), Level.INFO);
				HttpEntity httpEntity;
				try {
					httpEntity = new ByteArrayEntity(requestData.getBytes("UTF-8"));
					httpPatch.setEntity(httpEntity);
				} catch (UnsupportedEncodingException e) {
					throw new ConfigException(GlobalLabelParameters.UNSUPPORTED_ENCODING);
				}
			} else {
				Utility.log(logger, GlobalLabelParameters.REQUEST_BODY_LABEL, "  NULL", Level.ERROR);
				throw new ConfigException(GlobalLabelParameters.REQUEST_EMPTY);
			}
			
			String responseString = "";
			response = httpClient.execute(httpPatch);
	        if(response != null) {
	        	responseString = EntityUtils.toString(response.getEntity());
	            Integer responseCode = response.getStatusLine().getStatusCode();
	            Response responseObj = new Response();
	            responseObj.setResponseCode(String.valueOf(responseCode));
	            Utility.logResponseCodeMessage(responseCode, logger);
	            Utility.log(logger, GlobalLabelParameters.RESPONSE_CODE + " : ", responseObj.getResponseCode(), Level.INFO);
	            
	            Header header = response.getFirstHeader(GlobalLabelParameters.V_C_CORRELATION_ID);
	           	responseObj.setVcCorelationId(header.getValue());
	           	Utility.log(logger, GlobalLabelParameters.V_C_CORRELATION_ID + " : ", responseObj.getVcCorelationId(), Level.INFO);
	           	
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
	    	Utility.log(logger, GlobalLabelParameters.IO_EXCEPTION, "", Level.FATAL);
	    	throw new ClientException(GlobalLabelParameters.IO_EXCEPTION);
		} catch (ConfigException e) {
		    Utility.log(logger, e.getLocalizedMessage(), "", Level.FATAL);
	    	throw e;
	    } catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.PATCH_CLIENT_EXCEPTION, "", Level.ERROR);
			Utility.log(logger,e);
	    	throw new ClientException(GlobalLabelParameters.PATCH_CLIENT_EXCEPTION);
		} finally {
				if(response != null) {
					try {
						response.close();
					} catch (IOException e) {
						Utility.log(logger,e);
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
			 * Add Request Header :: "Content Type"
			 */
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
					Level.INFO);
			httpPatch.setHeader(GlobalLabelParameters.CONTENT_TYPE, GlobalLabelParameters.APPLICATION_JSON);

			/* Add Request Header :: "Signature" */
			String encryptedJWTSignature = Authorization.getToken(merchantConfig, requestData, jwtMethodType);
			if (encryptedJWTSignature == null || encryptedJWTSignature.isEmpty()) {
				isSignatureAlert = true;
			}
			httpPatch.setHeader("Authorization", "Bearer " + encryptedJWTSignature);
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, encryptedJWTSignature, Level.INFO);
			
			/*
			 * Add Request Header :: "profile-id"
			 */
			httpPatch.setHeader(GlobalLabelParameters.PROFILE_ID, GlobalLabelParameters.PROFILE_ID_VAL);
			Utility.log(logger, GlobalLabelParameters.PROFILE_ID,  " : " + GlobalLabelParameters.PROFILE_ID_VAL, Level.INFO);
			return httpPatch;
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.MERCHANTID_NOT_SET, "", Level.FATAL);
			Utility.log(logger, e);
			throw new ConfigException(GlobalLabelParameters.MERCHANTID_NOT_SET);
		}
	}
}
