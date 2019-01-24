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
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, Delete operations. *
 */
public class DeleteGenerateHeaders {

	private Properties merchantProp;
	private static MerchantConfig merchantConfig;
	private MerchantUtil merchantUtil;
	private String authenticationType = null;
	private static String requestType = "DELETE";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	Logger logger;
	private String tempSig;
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

	/**
	 * 
	 * @param getID
	 *            : Unique getID
	 * @throws ConfigException
	 * @throws IOException
	 */
	public DeleteGenerateHeaders() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		setGenerateHeaders(merchantConfig);
		Log4j.logEnable = false;
		System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
		Log4j.logEnable = merchantConfig.getEnableLog();
		Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSCATION,
				" Generate DELETE Headers *******************", Level.INFO);
		if (MerchantUtil.validateMerchantDetails()) {
			generateDeleteHeaders();
		}
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION, "", Level.INFO);
		Utility.log(this.logger, " ", "", Level.OFF);
	}

	public DeleteGenerateHeaders(MerchantConfig merchantConf) throws ConfigException, IOException {
		merchantConfig = merchantConf;
		setGenerateHeaders(merchantConfig);
		Log4j.logEnable = false;
		System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
		Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSCATION,
				" Generate DELETE Headers *******************", Level.INFO);
		MerchantUtil.validateMerchantDetails();
		generateDeleteHeaders();
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSCATION, "", Level.INFO);
		Utility.log(this.logger, " ", "", Level.OFF);
	}

	private void setGenerateHeaders(MerchantConfig merchantConfig) throws IOException, ConfigException {
		merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		authenticationType = merchantConfig.getAuthenticationType();
		merchantUtil.setRequestType(requestType);
		this.logger = Log4j.getInstance(merchantConfig);
	}

	/**
	 * This method generates the detailed header detail of the transaction and
	 * displays and log them.
	 * 
	 * @throws ConfigException
	 */
	private void generateDeleteHeaders() throws ConfigException {
		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestTarget, Level.INFO);
		authenticationType = merchantConfig.getAuthenticationType();
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID.concat(" : "), merchantConfig.getMerchantID(),
				Level.INFO);
		Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), date, Level.INFO);
		Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
		

		System.out.println(" Request Type        : " + requestType);
		System.out.println(" Date                : " + date);
		System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
		System.out.println(" HOST                : " + merchantConfig.getRequestHost());

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			Utility.log(logger, GlobalLabelParameters.USERAGENT.concat(" : "), GlobalLabelParameters.USER_AGENT_VALUE,
					Level.INFO);
			/* Signature Header */
			tempSig = Authorization.getToken(merchantConfig, merchantUtil.getRequestData(), requestType);
			Utility.log(logger, GlobalLabelParameters.SIGNATURE.concat(" : "), tempSig, Level.INFO);
			System.out.println(" Signature           : " + tempSig.toString());

		} else {
			/* JWT */
			tempSig = Authorization.getToken(merchantConfig, merchantUtil.getRequestData(), requestType);
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, tempSig, Level.INFO);
			System.out.println(" Authorization, Bearer " + tempSig.toString());
		}
	}

	/**
	 * 
	 * @param args
	 *            : Contains getId entry.
	 * @throws ConfigException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ConfigException {
		new DeleteGenerateHeaders();
	}
}
