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
	private String requestType = "PUT";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private PayloadDigest digest;
	private Logger logger;

	private String tempSig;

	private static String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

	/**
	 * @param merchantConfig
	 * @param requestData
	 * @throws ConfigException
	 */
	public PutGenerateHeaders(MerchantConfig merchantConfig, String requestData) throws ConfigException {
		this.requestData = requestData;
		logger = merchantConfig.getLogger();
		digest = new PayloadDigest(requestData);
		PutMethodHeaders(merchantConfig);
	}

	/**
	 * @throws ConfigException
	 * @throws IOException
	 */
	public PutGenerateHeaders() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(requestTarget);
		merchantUtil.setRequestType("PUT");
		merchantUtil.setRequestJsonPath("src/main/resources/TRRReport.json");
		logger = merchantConfig.getLogger();
		this.requestData = merchantUtil.getRequestData();
		digest = new PayloadDigest(requestData);

		System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
		Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION, " Generate PUT Headers *******************",
				Level.INFO);
		boolean isMerchant = MerchantUtil.validateMerchantDetails();
		if (isMerchant) {
			PutMethodHeaders(merchantConfig);
		}
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
		Utility.log(this.logger, " ", "", Level.OFF);
	}

	/**
	 * @param merchantConfig
	 * @throws ConfigException
	 */
	private void PutMethodHeaders(MerchantConfig merchantConfig) throws ConfigException {
		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestTarget, Level.INFO);
		authenticationType = merchantConfig.getAuthenticationType();
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.MERCH_CONF_LABEL, merchantConfig.toString(), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
				Level.INFO);
		System.out.println(" Request Type        : " + requestType);
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);

		/* Signature Header */
		tempSig = Authorization.getToken(merchantConfig, requestData);

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID.concat(" : "), merchantConfig.getMerchantID(),
					Level.INFO);
			Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), date, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
			System.out.println(" Date                : " + date);
			digest = new PayloadDigest(requestData);
			System.out.println(" Digest              : " + digest.getDigest());
			Utility.log(logger, GlobalLabelParameters.DIGEST.concat(" : "), digest.getDigest(), Level.INFO);
			System.out.println(" Signature Header    : " + tempSig.toString());
			Utility.log(logger, GlobalLabelParameters.SIGNATURE.concat(" : "), tempSig, Level.INFO);
			System.out.println(" HOST                : " + merchantConfig.getRequestHost());

		} else {
			/* JWT */
			System.out.println(" " + GlobalLabelParameters.AUTHORIZARION_BEARER + tempSig.toString());
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, tempSig.toString(), Level.INFO);
		}
	}

	/**
	 * This method initiate the PUT Header program.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args) throws ConfigException, IOException {
		new PutGenerateHeaders();
	}
}
