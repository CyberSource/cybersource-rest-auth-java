package com.cybersource.SampleCode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, for POST operation.
 *
 */
public class PostGenerateHeaders {
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String requestData;
	private MerchantUtil merchantUtil;
	private String authenticationType = null;
	private PayloadDigest digest;
	private Logger logger = null;
	private String tempSig;
	/**
	 * REQUEST - TYPE [Non-Editable]
	 * 
	 */
	private String requestType = "POST";
	/**
	 * JSON PATH
	 * [Editable]
	 */

	private String requestJsonPath = "src/main/resources/request.json";

	/**
	 * 
	 * @throws IOException 
	 * @throws ConfigException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PostGenerateHeaders() throws IOException, ConfigException, NoSuchAlgorithmException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		this.logger = Log4j.getInstance(merchantConfig);
		merchantUtil=new MerchantUtil(merchantConfig);
		Log4j.logEnable = false;
		System.out.println(" Authentication Type : " +  merchantConfig.getAuthenticationType());
		Utility.log(this.logger,
				GlobalLabelParameters.BEGIN_TRANSCATION , " Generate POST Headers *******************", Level.INFO);
		merchantConfig.setRequestType(requestType);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		requestData=merchantUtil.getRequestData();
		digest = new PayloadDigest(this.merchantConfig,requestData);
		boolean isMerchant = MerchantUtil.validateMerchantDetails();
		Log4j.logEnable = merchantConfig.getEnableLog();
		digest = new PayloadDigest(merchantConfig,requestData);
		if (isMerchant) {
			generatePostHeaders();
		}
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(this.logger, " ","", Level.OFF);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : this object contains all the cybs.properites details.
	 * @throws ConfigException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PostGenerateHeaders(MerchantConfig merchantConfig,String requestData) throws ConfigException, NoSuchAlgorithmException, UnsupportedEncodingException{
		this.merchantConfig = merchantConfig;
		this.requestData=requestData;
		Log4j.logEnable = false;
		Authorization.getToken(merchantConfig, requestData);
		logger = Log4j.getInstance(merchantConfig);
		digest = new PayloadDigest(this.merchantConfig,requestData);
		generatePostHeaders();

	}

	/**
	 * This method generates, displays and log the detailed header truncation
	 * for POST operation.
	 * @throws ConfigException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	private void generatePostHeaders() throws ConfigException, NoSuchAlgorithmException, UnsupportedEncodingException  {
		authenticationType = merchantConfig.getAuthenticationType();

		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE , authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE .concat(" : ") , requestType, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.DATE .concat(" : ") , PropertiesUtil.date, Level.INFO);
		Utility.log(logger,
				GlobalLabelParameters.V_C_MERCHANTID .concat(" : ") , merchantConfig.getMerchantID(), Level.INFO);
		Utility.log(logger,
				GlobalLabelParameters.CONTENT_TYPE .concat(" : "), GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);
		Utility.log(logger, GlobalLabelParameters.HOST .concat(" : ") , merchantConfig.getRequestHost(),
				Level.INFO);
		
		System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
		System.out.println(" Date                : " + PropertiesUtil.date);
		System.out.println(" Request Type        : " + merchantConfig.getRequestType());
		System.out.println(" HOST                : " + merchantConfig.getRequestHost());
		
		tempSig = Authorization.getToken(merchantConfig,requestData);
		
		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			Utility.log(logger,
					GlobalLabelParameters.USERAGENT .concat(" : ") , GlobalLabelParameters.USER_AGENT_VALUE, Level.INFO);
			digest = new PayloadDigest(this.merchantConfig,requestData);
			System.out.println(" Digest              : " + digest.getDigest());
			/* Signature Header */
			
			System.out.println(" Signature           : " + tempSig.toString());
			
		} else {
			/* JWT */
			System.out.println(" Authorization, Bearer " + tempSig.toString());
		}
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ConfigException{
		new PostGenerateHeaders();
	}
}
