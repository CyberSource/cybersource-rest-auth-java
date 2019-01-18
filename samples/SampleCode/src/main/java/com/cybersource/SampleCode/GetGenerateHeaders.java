package com.cybersource.SampleCode;

import java.io.IOException;
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
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;
/**
 * This class generates the Headers that are present in the token was sent the server for transaction,
 * GET operations.
 */
public class GetGenerateHeaders {

	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private MerchantUtil merchantUtil;
	private String authenticationType = null;
	private Logger logger;
	private String tempSig;
	/**
	 * DATE
	 * [Non-Editable]
	 */
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	/**
	 * REQUEST - TYPE
	 * [Non-Editable]
	 * 
	 */
	private String requestType = "GET";
	/**
	 * 
	 * UNQUIE id
	 * [Editable]
	 */
	private String id = "5265502011846829204101";
	/**
	 * 
	 * @param id
	 *            : Unique id
	 * @throws ConfigException 
	 * @throws IOException 
	 */
	public GetGenerateHeaders(String requestTarget) throws IOException, ConfigException{
		setGenerateHeaders();
		Log4j.logEnable = false;
		this.id = Utility.retrieveGetIDFromRequestTarget(requestTarget);
		generateGetHeaders();
	}
	
	/**
	 * 
	 * @param merchantConfig
	 *            : this object contains all the cybs.properites details.
	 * @throws ConfigException 
	 */
	public GetGenerateHeaders(MerchantConfig merchantConfig,String requestData) throws ConfigException{
		this.merchantConfig = merchantConfig;
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		Log4j.logEnable = false;
		merchantConfig.setId(id);
		merchantConfig.setRequestType(requestType);
		this.logger = Log4j.getInstance(merchantConfig);
		generateGetHeaders();
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(this.logger, " ","", Level.OFF);

	}

	/**
	 * @throws IOException
	 * @throws ConfigException
	 */
	public GetGenerateHeaders() throws IOException, ConfigException{
		setGenerateHeaders();
		
		System.out.println(" Authentication Type : " +  merchantConfig.getAuthenticationType());
		Log4j.logEnable = false;
		Utility.log(this.logger,
				GlobalLabelParameters.BEGIN_TRANSCATION , " Generate GET Headers *******************", Level.INFO);
		MerchantUtil.validateMerchantDetails();
		generateGetHeaders();
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION,"", Level.INFO);
		Utility.log(this.logger, " ","", Level.OFF);
	}

	/**
	 * @throws IOException
	 * @throws ConfigException
	 */
	private void setGenerateHeaders() throws IOException, ConfigException{
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		authenticationType = merchantConfig.getAuthenticationType();
		merchantConfig.setRequestType(requestType);
		this.logger = Log4j.getInstance(merchantConfig);
		Log4j.logEnable = false;
		Authorization.getToken(merchantConfig, null);
	}

	/**
	 * This method generates the detailed header detail of the transaction and
	 * displays and log them.
	 * 
	 * @throws ConfigException
	 *             : Throws runtime exception.
	 */
	private void generateGetHeaders() throws ConfigException {

		authenticationType = merchantConfig.getAuthenticationType();
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE , authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : ") , requestType, Level.INFO);
		Utility.log(logger,
				GlobalLabelParameters.V_C_MERCHANTID .concat(" : ") , merchantConfig.getMerchantID(), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.DATE .concat(" : ") , date, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.GET + "_ID" .concat(" : ") ,  this.id, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.HOST .concat(" : ") , merchantConfig.getRequestHost(),
				Level.INFO);
		
		System.out.println(" Request Type        : " + requestType);
		System.out.println(" ID                  : " + this.id);
		System.out.println(" Date                : " + date);
		System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
		System.out.println(" HOST                : " + merchantConfig.getRequestHost());
		
		

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			Utility.log(logger,
					GlobalLabelParameters.USERAGENT .concat(" : ") , GlobalLabelParameters.USER_AGENT_VALUE, Level.INFO);
			/* Signature Header */
			tempSig = Authorization.getToken(merchantConfig,merchantUtil.getRequestData());
			System.out.println(" Signature           : " + tempSig.toString());
			
		} else {
			/* JWT */
			tempSig = Authorization.getToken(merchantConfig,merchantUtil.getRequestData());
			System.out.println(" Authorization, Bearer " + tempSig.toString());
		}
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);
		Utility.log(logger,
				GlobalLabelParameters.CONTENT_TYPE .concat(" : "), GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
	}

	/**
	 * 
	 * @param args
	 *            : Contains id entry.
	 * @throws ConfigException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ConfigException {
		new GetGenerateHeaders();
	}
}
