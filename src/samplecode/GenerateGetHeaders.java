package samplecode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.cybersource.authentication.core.Authorization;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.log.Log4j;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;
import com.cybersource.authentication.util.Utility;
/**
 * This class generates the Headers that are present in the token was sent the server for transaction,
 * GET operations.
 *  *
 */
public class GenerateGetHeaders {

	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String cybsPath = "Resource/cybs.properties";
	private String authenticationType = null;
	private Authorization auth;
	private String requestType = "GET";
	private String getID = "5246387105766473203529";
	private String host = "apitest.cybersource.com";
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	Logger logger;

	private String tempSig;

	/**
	 * 
	 * @param getID
	 *            : Unique getID
	 * @throws Exception
	 *             :
	 */
	public GenerateGetHeaders(String getID) throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
		merchantConfig = new MerchantConfig(merchantProp);
		Utility.logEnable = false;
		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		auth = new Authorization();
		this.getID = getID;
		generateGetHeaders();
	}

	/**
	 * 
	 * @param getID
	 *            : Unique getID
	 * @param merchantConfig:
	 *            This object contains all the details present in the
	 *            cybs.properties.
	 * @throws Exception
	 */
	public GenerateGetHeaders(String getID, MerchantConfig merchantConfig) throws Exception {
		this.merchantConfig = merchantConfig;
		Utility.logEnable = false;
		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		auth = new Authorization();
		this.getID = getID;
		generateGetHeaders();
	}

	/**
	 * This method generates the detailed header detail of the transaction and displays and log them. 
	 * @throws Exception
	 */
	private void generateGetHeaders() throws Exception {
		authenticationType = merchantConfig.getAuthenticationType().trim();
		System.out.println(" Request Type        : " + requestType);
		System.out.println(" ID                  : " + this.getID);

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			System.out.println(
					" " + GlobalLabelParameters.USERAGENT + "          : " + GlobalLabelParameters.USER_AGENT_VALUE);
			System.out.println(" MerchantID          : " + merchantConfig.getMerchantID().trim());
			System.out.println(" Date                : " + date);
			/* Signature Header */
			tempSig = auth.getToken(merchantConfig, merchantConfig.getMerchantKeyId().trim(),
					merchantConfig.getMerchantsecretKey().trim(), merchantConfig.getMerchantID().trim(), requestType,
					getID);
			System.out.println(" Signature Header    : " + tempSig.toString());
			System.out.println(" HOST                : " + host);
		} else {
			/* JWT */
			String jwtRequestBody = getID;
			auth.setJWTRequestBody(jwtRequestBody);
			auth.setLogger(this.logger);
			tempSig = auth.getToken(merchantConfig, null, null, null, requestType, null);
			System.out.println(" Authorization, Bearer " + tempSig.toString());
		}
		System.out.println(
				" " + GlobalLabelParameters.CONTENT_TYPE + "        : " + GlobalLabelParameters.APPLICATION_JSON);
	}

	/**
	 * 
	 * @param args : Contains getId entry. 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String getID = "5246387105766473203529";
		new GenerateGetHeaders(getID);
	}
}
