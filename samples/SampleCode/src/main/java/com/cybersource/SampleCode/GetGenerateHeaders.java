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
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, GET operations.
 */
public class GetGenerateHeaders {

	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private AuthenticationRequestContext requestContext;
	private String authenticationType = null;
	private Logger logger;
	private String tempSig;
	/**
	 * DATE [Non-Editable]
	 */
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	/**
	 * REQUEST - TYPE [Non-Editable]
	 * 
	 */
	private String requestType = "GET";
	/**
	 * 
	 * UNQUIE id [Editable]
	 */
	private String id = "5383909529456080803003";

	private String requestTarget = "/pts/v2/payments/5383909529456080803003";

	/**
	 * 
	 * @param requestTarget
	 *            : requestTarget
	 * @throws ConfigException
	 * @throws IOException
	 */
	public GetGenerateHeaders(String requestTarget) throws IOException, ConfigException {
		setGenerateHeaders();
		this.id = Utility.retrieveGetIDFromRequestTarget(requestTarget);
		generateGetHeaders();
	}

	public GetGenerateHeaders() {
		try {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			this.logger = merchantConfig.getLogger();
			requestContext = new AuthenticationRequestContext();
			System.out.println(" Authentication Type : " + merchantConfig.getAuthenticationType());
			requestContext.setRequestMethodType(requestType);
			requestContext.setRequestJsonPath("not required");
			Utility.log(this.logger, GlobalLabelParameters.BEGIN_TRANSACTION,
					" Generate GET Headers *******************", Level.INFO);
			generateGetHeaders();
			Utility.log(this.logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
			Utility.log(this.logger, " ", "", Level.OFF);
		} catch (ConfigException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @throws IOException
	 * @throws ConfigException
	 */
	private void setGenerateHeaders() throws IOException, ConfigException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new AuthenticationRequestContext();
		requestContext.setRequestJsonPath("not required");
		/* Set Request Type into the merchant config object. */
		requestContext.setRequestMethodType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType();
		this.logger = merchantConfig.getLogger();
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
		/* Signature Header */
		tempSig = Authorization.getToken(merchantConfig, requestContext);

		Utility.log(this.logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestTarget, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE, authenticationType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.REQUEST_TYPE.concat(" : "), requestType, Level.INFO);
		Utility.log(this.logger, GlobalLabelParameters.MERCH_CONF_LABEL, merchantConfig.toString(), Level.INFO);
		Utility.log(logger, GlobalLabelParameters.GET + "_ID".concat(" : "), this.id, Level.INFO);

		System.out.println(" Request Type        : " + requestType);
		System.out.println(" GET_ID              : " + this.id);

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
		new GetGenerateHeaders();
	}
}
