package com.cybersource.SampleCode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, Delete operations. *
 */
public class DeleteGenerateHeaders {

	private Properties merchantProp;
	private static MerchantConfig merchantConfig;
	private RequestContext requestContext;
	private String authenticationType = null;
	private static String requestType = "DELETE";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	Logger logger;
	private String tempSig;
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

	public DeleteGenerateHeaders() {
		try {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestJsonPath("not required");
			authenticationType = merchantConfig.getAuthenticationType();
			requestContext.setRequestMethodType(requestType);
			this.logger = merchantConfig.getLogger();
			System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
			Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION,
					" Generate DELETE Headers *******************", Level.INFO);
			generateDeleteHeaders();
			Utility.log(this.logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
			Utility.log(this.logger, " ", "", Level.OFF);

		} catch (ConfigException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method generates the detailed header detail of the transaction and
	 * displays and log them.
	 * 
	 * @throws ConfigException
	 */
	private void generateDeleteHeaders() throws ConfigException {
		authenticationType = merchantConfig.getAuthenticationType();
		/* Signature Header */
		tempSig = Authorization.getToken(merchantConfig, requestContext);

		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestTarget, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.MERCH_CONF_LABEL, merchantConfig.toString(), Level.INFO);

		System.out.println(" Request Type        : " + requestType);

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Utility.log(logger, GlobalLabelParameters.V_C_MERCHANTID.concat(" : "), merchantConfig.getMerchantID(),
					Level.INFO);
			Utility.log(logger, GlobalLabelParameters.DATE.concat(" : "), date, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.HOST.concat(" : "), merchantConfig.getRequestHost(), Level.INFO);
			Utility.log(logger, GlobalLabelParameters.USERAGENT.concat(" : "), GlobalLabelParameters.USER_AGENT_VALUE,
					Level.INFO);
			Utility.log(logger, GlobalLabelParameters.SIGNATURE.concat(" : "), tempSig, Level.INFO);

			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID());
			System.out.println(" Date                : " + date);
			System.out.println(" HOST                : " + merchantConfig.getRequestHost());
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" Signature           : " + tempSig);
		} else {
			/* JWT */
			Utility.log(logger, GlobalLabelParameters.CONTENT_TYPE.concat(" : "),
					GlobalLabelParameters.APPLICATION_JSON, Level.INFO);
			Utility.log(logger, GlobalLabelParameters.AUTHORIZARION_BEARER, tempSig, Level.INFO);
			System.out.println(
					" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);
			System.out.println(" " + GlobalLabelParameters.AUTHORIZARION_BEARER + tempSig.toString());
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new DeleteGenerateHeaders();
	}
}
