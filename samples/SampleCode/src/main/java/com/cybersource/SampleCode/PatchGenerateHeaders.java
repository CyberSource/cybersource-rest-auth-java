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
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, for PATCH operation.
 *
 */
public class PatchGenerateHeaders {
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String requestData;
	private MerchantUtil merchantUtil;
	private String authenticationType = null;
	private PayloadDigest digest;
	private Logger logger = null;
	private String tempSig;
	/**
	 * DATE GMT [Non - Editable]
	 * 
	 */
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	/**
	 * REQUEST - TYPE [Non-Editable]
	 * 
	 */
	private String requestType = "PATCH";
	/**
	 * JSON PATH [Editable]
	 */

	private String requestJsonPath = "src/main/resources/request_patch.json";

	private String requestTarget = "/tms/v1/paymentinstruments/7DFB37D8E4FAFD1CE05341588E0A6B93";

	/**
	 * 
	 * @throws IOException
	 * @throws ConfigException
	 * @throws NoSuchAlgorithmException
	 */
	public PatchGenerateHeaders() throws IOException, ConfigException, NoSuchAlgorithmException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		this.logger = merchantConfig.getLogger();
		merchantUtil = new MerchantUtil(merchantConfig);
		System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
		Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION, " Generate PATCH Headers *******************",
				Level.INFO);
		merchantUtil.setRequestType(requestType);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		requestData = merchantUtil.getRequestData();
		digest = new PayloadDigest(requestData);
		if (MerchantUtil.validateMerchantDetails()) {
			generatePatchHeaders();
		}
		Utility.log(this.logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
		Utility.log(this.logger, " ", "", Level.OFF);
	}

	/**
	 * This method generates, displays and log the detailed header truncation
	 * for PATCH operation.
	 * 
	 * @throws ConfigException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	private void generatePatchHeaders() throws ConfigException, NoSuchAlgorithmException, UnsupportedEncodingException {
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
			System.out.println(" " + GlobalLabelParameters.AUTHORIZARION_BEARER + tempSig);
			System.out.println(" " + GlobalLabelParameters.PROFILE_ID.concat(" : ") + GlobalLabelParameters.PROFILE_ID_VAL);
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, tempSig, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.PROFILE_ID.concat(" : "), GlobalLabelParameters.PROFILE_ID_VAL, Level.INFO);
		}
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ConfigException {
		new PatchGenerateHeaders();
	}
}
