package com.cybersource.SampleCode;

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
	 * JSON PATH [Editable]
	 */

	private String requestJsonPath = "src/main/resources/request.json";

	private String requestTarget = "/pts/v2/payments/";

	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));

	public PostGenerateHeaders() {
		try {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			this.logger = merchantConfig.getLogger();
			merchantUtil = new MerchantUtil(merchantConfig);

			System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
			merchantUtil.setRequestType(requestType);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestTarget(requestTarget);
			requestData = merchantUtil.getRequestData();
			digest = new PayloadDigest(requestData);
			Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION,
					" Generate POST Headers *******************", Level.INFO);
			if (MerchantUtil.validateMerchantDetails()) {
				generatePostHeaders();
			}
			Utility.log(this.logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
			Utility.log(this.logger, " ", "", Level.OFF);
		} catch (ConfigException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method generates, displays and log the detailed header truncation
	 * for POST operation.
	 * 
	 * @throws ConfigException
	 */
	private void generatePostHeaders() throws ConfigException {
		authenticationType = merchantConfig.getAuthenticationType();
		/* Signature Header */
		tempSig = Authorization.getToken(merchantConfig, requestData);

		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, MerchantUtil.getRequestTarget(),
				Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.MERCH_CONF_LABEL, merchantConfig.toString(), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "), GlobalLabelParameters.APPLICATION_JSON,
				Level.INFO);

		System.out.println(" Request Type        : " + requestType);
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			digest = new PayloadDigest(requestData);

			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID.concat(" : "), merchantConfig.getMerchantID(),
					Level.INFO);
			Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), date, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
			Utility.log(logger, GlobalLabelParameters.USERAGENT.concat(" : "), GlobalLabelParameters.USER_AGENT_VALUE,
					Level.INFO);
			Utility.log(logger, GlobalLabelParameters.DIGEST.concat(" : "), digest.getDigest(), Level.INFO);
			Utility.log(logger, GlobalLabelParameters.SIGNATURE.concat(" : "), tempSig, Level.INFO);

			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
			System.out.println(" Date                : " + date);
			System.out.println(" HOST                : " + merchantConfig.getRequestHost());
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" Digest              : " + digest.getDigest());
			System.out.println(" Signature Header    : " + tempSig.toString());
		} else {
			/* JWT */
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, tempSig.toString(), Level.INFO);
			System.out.println(" " + GlobalLabelParameters.AUTHORIZARION_BEARER + tempSig.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PostGenerateHeaders();
	}
}
