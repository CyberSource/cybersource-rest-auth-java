package com.cybersource.SampleCode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
 * server for transaction, for PUT operations.
 *
 */
public class PutGenerateHeaders {
	private static Properties merchantProp;
	private static MerchantConfig merchantConfig;
	private static MerchantUtil merchantUtil;
	private String authenticationType = null;
	private String requestData;
	private Authorization auth;
	private String requestType = "PUT";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private PayloadDigest digest;
	private Logger logger;

	private String TempSig;

	/**
	 * @param merchantConfig
	 * @param requestData
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws ConfigException
	 */
	public PutGenerateHeaders(MerchantConfig merchantConfig,String requestData) throws NoSuchAlgorithmException, UnsupportedEncodingException, ConfigException  {
		this.requestData=requestData;
		auth = new Authorization();
		logger = Log4j.getInstance(merchantConfig);
		digest = new PayloadDigest(merchantConfig,requestData);
		Log4j.logEnable = false;
		PutMethodHeaders(merchantConfig);
	}
	
	/**
	 * @throws ConfigException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public PutGenerateHeaders() throws ConfigException, NoSuchAlgorithmException, UnsupportedEncodingException{
		auth = new Authorization();
		logger = Log4j.getInstance(merchantConfig);
		this.requestData = merchantUtil.getRequestData();
		digest = new PayloadDigest(merchantConfig,requestData);
		logger = Log4j.getInstance(merchantConfig);
		Log4j.logEnable = merchantConfig.getEnableLog();
		Utility.log(this.logger,
				GlobalLabelParameters.BEGIN_TRANSCATION , " Generate PUT Headers *******************", Level.INFO);
		boolean isMerchant = MerchantUtil.validateMerchantDetails();
		if(isMerchant){
		PutMethodHeaders(merchantConfig);
		}
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(this.logger, " ","", Level.OFF);
	}

	/**
	 * @param merchantConfig
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws ConfigException
	 */
	private void PutMethodHeaders(MerchantConfig merchantConfig) throws NoSuchAlgorithmException, UnsupportedEncodingException, ConfigException  {
		authenticationType = merchantConfig.getAuthenticationType();
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE , authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.DATE .concat(" : "), date, Level.INFO);
		Utility.log(logger,
				GlobalLabelParameters.V_C_MERCHANTID .concat(" : "), merchantConfig.getMerchantID(), Level.INFO);
		Utility.log(logger,	GlobalLabelParameters.CONTENT_TYPE.concat(":") , GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.HOST .concat(" : "), merchantConfig.getRequestHost(),
				Level.INFO);
		
		
		System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
		System.out.println(" Date                : " + date);
		System.out.println(" Request Type        : " + requestType);
		System.out.println(" HOST                : " + merchantConfig.getRequestHost());
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);
		
		
		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
           digest=new PayloadDigest(merchantConfig, requestData);
			System.out.println(" Digest              : " + digest.getDigest());
			/* Signature Header */
			TempSig = auth.getToken(merchantConfig,requestData);
			System.out.println(" Signature           : " + TempSig.toString());
			
		} else {
			/* JWT */
			TempSig = auth.getToken(merchantConfig,requestData);
			System.out.println(" Authorization, Bearer " + TempSig.toString());
		}
	}

	/**
	 * This method initiate the PUT Header program.
	 * @param args
	 * @throws IOException
	 * @throws ConfigException
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args) throws IOException, ConfigException, NoSuchAlgorithmException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setEnableLog(true);
		merchantConfig.setRequestType("PUT");
		merchantUtil.setRequestJsonPath("src/main/resources/TRRReport.json");
		new PutGenerateHeaders();
	}
}
