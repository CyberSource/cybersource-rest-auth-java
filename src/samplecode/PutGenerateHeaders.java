package samplecode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.cybersource.authentication.core.Authorization;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.log.Log4j;
import com.cybersource.authentication.payloaddigest.PayloadDigest;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;

/**
 * This class generates the Headers that are present in the token was sent the
 * server for transaction, for put operations.
 *
 */
public class PutGenerateHeaders {
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String cybsPath = "Resource/cybs.properties";
	private String authenticationType = null;
	private Authorization auth;
	private String requestType = "PUT";
	private String host = "apitest.cybersource.com";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private PayloadDigest digest;
	private Logger logger;

	private String TempSig;

	public PutGenerateHeaders() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setEnableLog(false);
		auth = new Authorization();
		logger = Log4j.getInstance(merchantConfig);
		digest = new PayloadDigest(merchantConfig);
		PUTMethodHeaders();
	}

	public PutGenerateHeaders(MerchantConfig merchantConfig) throws Exception {
		this.merchantConfig = merchantConfig;
		this.merchantConfig.setEnableLog(false);
		auth = new Authorization();
		logger = Log4j.getInstance(merchantConfig);
		digest = new PayloadDigest(this.merchantConfig);
		PUTMethodHeaders();
	}

	private void PUTMethodHeaders() throws Exception {
		authenticationType = merchantConfig.getAuthenticationType().trim();
		System.out.println(" Request Type        : " + requestType);
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "           : " + GlobalLabelParameters.APPLICATION_JSON);
		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID().trim());
			System.out.println(" Date                : " + date);
			System.out.println(" Digest              : " + digest.getDigest());
			/* Signature Header */
			TempSig = auth.getToken(merchantConfig, merchantConfig.getMerchantKeyId().trim(),
					merchantConfig.getMerchantsecretKey().trim(), merchantConfig.getMerchantID().trim(), requestType,
					null);
			System.out.println(" Signature Header    : " + TempSig.toString());
			System.out.println(" HOST                : " + host);
		} else {
			/* JWT */
			String jwtRequestBody = PropertiesUtil.getJSONInput(merchantConfig.getRequestJsonPath());
			auth.setJWTRequestBody(jwtRequestBody);
			auth.setLogger(this.logger);
			TempSig = auth.getToken(merchantConfig, null, null, null, requestType, null);
			System.out.println(" Authorization, Bearer " + TempSig.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		new PutGenerateHeaders();
	}
}
