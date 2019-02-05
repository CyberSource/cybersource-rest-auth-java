package com.cybersource.SampleCode;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.AuthenticationRequestContext;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, for PUT operations.
 *
 */
public class PutGenerateHeaders {
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private AuthenticationRequestContext requestContext;
	private String authenticationType = null;
	private String requestData;
	private String requestType = "PUT";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private PayloadDigest digest;
	private Logger logger;

	private String tempSig;

	private static String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
	
	private String requestJsonPath = "src/main/resources/TRRReport.json";

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

	
	public PutGenerateHeaders()  {
		try {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			requestContext = new AuthenticationRequestContext();
			requestContext.setRequestTarget(requestTarget);
			requestContext.setRequestMethodType("PUT");
			requestContext.setRequestJsonPath("src/main/resources/TRRReport.json");
			logger = merchantConfig.getLogger();
			
			System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
			Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION, " Generate PUT Headers *******************",
					Level.INFO);
			if (requestContext.validateRequestDetails(requestJsonPath) ) {
				this.requestData = requestContext.getRequestBody();
				digest = new PayloadDigest(requestData);
				PutMethodHeaders(merchantConfig);
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
	 * @param merchantConfig
	 * @throws ConfigException
	 */
	private void PutMethodHeaders(MerchantConfig merchantConfig) throws ConfigException {
		authenticationType = merchantConfig.getAuthenticationType();
		/* Signature Header */
		tempSig = Authorization.getToken(merchantConfig, requestContext);

		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestContext.getRequestTarget(),
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
	 * This method initiate the PUT Header program.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ConfigException
	 */
	public static void main(String[] args)  {
		new PutGenerateHeaders();
	}
}
