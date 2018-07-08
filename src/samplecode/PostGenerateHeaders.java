package samplecode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authentication.core.Authorization;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.log.Log4j;
import com.cybersource.authentication.payloaddigest.PayloadDigest;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;
import com.cybersource.authentication.util.Utility;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, for POST operation.
 *
 */
public class PostGenerateHeaders {
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String authenticationType = null;
	private Authorization auth;
	private PayloadDigest digest;
	private Logger logger = null;
	private String tempSig;
	/**
	 * DATE GMT
	 * [Non - Editable]
	 * 
	 */
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	/**
	 * REQUEST - TYPE
	 * [Non-Editable]
	 * 
	 */
	private String requestType = "POST";
	/**
	 * 
	 * @throws Exception
	 *             : Error while generating headers.
	 */
	public PostGenerateHeaders() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType(requestType);
		Utility.logEnable = false;
		logger = Log4j.getInstance(merchantConfig);
		auth = new Authorization();
		digest = new PayloadDigest(merchantConfig);
		generatePostHeaders();
	}

	/**
	 * 
	 * @param merchantConfig
	 *            : this object contains all the cybs.properites details.
	 * @throws Exception
	 */
	public PostGenerateHeaders(MerchantConfig merchantConfig) throws Exception {
		this.merchantConfig = merchantConfig;
		Utility.logEnable = false;
		auth = new Authorization();
		logger = Log4j.getInstance(merchantConfig);
		digest = new PayloadDigest(this.merchantConfig);
		generatePostHeaders();
	}

	/**
	 * This method generates, displays and log the detailed header truncation
	 * for POST operation.
	 * 
	 * @throws Exception
	 */
	private void generatePostHeaders() throws Exception {
		authenticationType = merchantConfig.getAuthenticationType().trim();
		Utility.logException(this.logger, GlobalLabelParameters.AUTENTICATION_TYPE + authenticationType, Level.INFO);
		Utility.logException(this.logger, GlobalLabelParameters.REQUEST_TYPE + " : " + requestType, Level.INFO);
		System.out.println(" Request Type        : " + requestType);
		System.out.println(" Date                : " + date);
		Utility.logException(logger, GlobalLabelParameters.DATE + " : " + date, Level.INFO);
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "           : " + GlobalLabelParameters.APPLICATION_JSON);
		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID().trim());
			System.out.println(" Date                : " + date);
			System.out.println(" Digest              : " + digest.getDigest());
			/* Signature Header */
			tempSig = auth.getToken(merchantConfig, merchantConfig.getMerchantKeyId().trim(),
					merchantConfig.getMerchantsecretKey().trim(), merchantConfig.getMerchantID().trim(), requestType,
					null);
			System.out.println(" Signature           : " + tempSig.toString());
			System.out.println(" HOST                : " + merchantConfig.getRequestHost());
		} else {
			/* JWT */
			String jwtRequestBody = PropertiesUtil.getJSONInput(merchantConfig.getRequestJsonPath());
			auth.setJWTRequestBody(jwtRequestBody);
			auth.setLogger(this.logger);
			tempSig = auth.getToken(merchantConfig, null, null, null, requestType, null);
			System.out.println(" Authorization, Bearer " + tempSig.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		new PostGenerateHeaders();
	}
}
