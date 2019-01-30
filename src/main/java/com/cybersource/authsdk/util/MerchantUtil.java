package com.cybersource.authsdk.util;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;


public class MerchantUtil {

	private String requestData;
	private static String requestJsonPath;
	private static MerchantConfig merchantConfig;
	private static boolean isRequestJson = true;
	private static String requestType;
	private static String requestTarget;
	private static String url;
	

	/* Others. */
	private static Logger log;
	
	public static String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	
	public MerchantUtil(MerchantConfig merchantConfig) {
		MerchantUtil.merchantConfig = merchantConfig;
	}

	
	public static String getRequestType() {
		return requestType;
	}


	public void setRequestType(String requestType) {
		MerchantUtil.requestType = requestType;
	}
	
	
	public static String getRequestTarget() {
		return requestTarget;
	}


	public void setRequestTarget(String requestTarget) {
		MerchantUtil.requestTarget = requestTarget;
	}


	public static String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		MerchantUtil.url = url;
	}


	public static String getRequestJsonPath() {
		return requestJsonPath;
	}

	public void setRequestJsonPath(String requestJsonPath) {
		MerchantUtil.requestJsonPath = requestJsonPath;
	}

	public void setRequestJson(boolean isRequestJson) {
		MerchantUtil.isRequestJson = isRequestJson;
	}

	public boolean isRequestJson() {
		return isRequestJson;
	}
	

	/**
	 * @return request data according to availability of request Json.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public String getRequestData() throws ConfigException {

		try {
			if(StringUtils.isNotBlank(requestJsonPath) && !requestJsonPath.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED) ){
				requestData = PropertiesUtil.getJsonInput(requestJsonPath);
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_FOUND, requestJsonPath, Level.FATAL);
			Utility.log(log, e);
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_FOUND);
		}
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	/**
	 * @return true or false as per validation
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public static boolean validateMerchantDetails() throws ConfigException {
		boolean isRequestType = isRequestTypeMethodValid();

		boolean isMerchantId = true;
		boolean isCheckMerchantSecertKey = false;
		boolean isCheckMerchantKeyId = false;
		boolean isGetKeyAlias = false;
		boolean isCheckKeyPassword = false;
		boolean isCheckKeyFile = false;
		// change true to false
		boolean isCheckJsonPath = false;
		boolean isCheckLogMaxSize = true;
		boolean isCheckRunEnvironment;
		boolean isMerchantDetailStatus = false;
		checkAuthenticationType();
		isMerchantId = checkMerchantId();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isCheckMerchantSecertKey = checkMerchantSecertKey();
			isCheckMerchantKeyId = checkMerchantKeyId();
		} else if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)
				|| merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWE)) {
			isGetKeyAlias = checkKeyAlias();
			isCheckKeyPassword = checkKeyPassword();
			isCheckKeyFile = checkKeyFile();
		}

		isCheckJsonPath = checkJsonPath(requestJsonPath);
		isCheckLogMaxSize = checkLogMaxSizeAndFolder();
		isCheckRunEnvironment = checkRunEvironment();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isMerchantDetailStatus = isMerchantId && isCheckMerchantSecertKey && isCheckMerchantKeyId && isCheckJsonPath
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType;
		} else if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			isMerchantDetailStatus = isMerchantId && isGetKeyAlias && isCheckKeyPassword && isCheckKeyFile
					&& isCheckJsonPath && isCheckLogMaxSize && isCheckRunEnvironment && isRequestType;
		}
		return isMerchantDetailStatus;
	}

	/**
	 * @return true if authentication type is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private static boolean checkAuthenticationType() throws ConfigException {
		/* Authentication Type. */
		boolean check = true;
		if (merchantConfig.getAuthenticationType() == null || merchantConfig.getAuthenticationType().isEmpty()) {
			log = merchantConfig.getLogger();
			check = false;
			throw new ConfigException(GlobalLabelParameters.AUTH_NULL_EMPTY);
		}
		if (!(merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)
				|| merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)
				|| merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWE))) {
			log = merchantConfig.getLogger();
			Utility.log(log, GlobalLabelParameters.AUTH_ERROR, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
		return check;
	}

	/**
	 * @return true if merchant id or not null or return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkMerchantId() throws ConfigException {
		boolean check = true;
		if (StringUtils.isBlank(merchantConfig.getMerchantID())) {
			log = merchantConfig.getLogger();
			Utility.log(log, GlobalLabelParameters.MERCHANT_NULL_EMPTY, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.MERCHANT_NULL_EMPTY);
		}
		return check;

	}

	/**
	 * @return true if valid request type else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean isRequestTypeMethodValid() throws ConfigException {
		if (requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			return true;
		} else {
			Utility.log(log, GlobalLabelParameters.INVALID_REQUEST_TYPE, requestType, Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.INVALID_REQUEST_TYPE);

		}
	}

	/**
	 * @return true if valid password else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkKeyPassword() throws ConfigException {
		if (merchantConfig.getKeyPass() == null || merchantConfig.getKeyPass().isEmpty()) {
			merchantConfig.setKeyPass(merchantConfig.getMerchantID());
			Utility.log(log, GlobalLabelParameters.KEY_PASSWORD_EMPTY_NULL, "", Level.ERROR);
		}
		return true;
	}

	/**
	 * @return true if valid run environment else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkRunEvironment() throws ConfigException {
		boolean check = false;

		if (StringUtils.isNotBlank(merchantConfig.getRunEnvironment()) && 
				((merchantConfig.getRunEnvironment().equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX))
						|| (merchantConfig.getRunEnvironment().equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)))) {
				check = true;
		} else {
			Utility.log(log, GlobalLabelParameters.RUN_ENVIRONMENT_ERR, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);

		}
		return check;

	}

	/**
	 * @return true if merchant id is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkMerchantKeyId() throws ConfigException {
		if (merchantConfig.getMerchantKeyId() == null || merchantConfig.getMerchantKeyId().isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANT_KEYID_MISSING, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
		}
		return true;
	}

	/**
	 * @return true if secretKey is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkMerchantSecertKey() throws ConfigException {
		if (merchantConfig.getMerchantSecretKey() == null || merchantConfig.getMerchantSecretKey().isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
		}
		return true;
	}

	/**
	 * @return true if log size is valid else return false,
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkLogMaxSizeAndFolder() throws ConfigException {

		if (merchantConfig.getLogDirectory() == null || merchantConfig.getLogDirectory().isEmpty()) {
			merchantConfig.setLogDirectory(GlobalLabelParameters.DEFAULT_LOG_PATH);
			Utility.log(log, GlobalLabelParameters.NO_LOG_DIRECTORY, "", Level.ERROR);

		}
		return merchantConfig.getLogFile();
	}

	/**
	 * @param jsonPath
	 *            -path for request json.
	 * @return true if path is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkJsonPath(String jsonPath) throws ConfigException {
		File f = null;
		boolean check = true;
		
		if (!isRequestTypeMethodValid()) {
			return false;
		}

		if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			return check;
		}
		
		if (StringUtils.isBlank(jsonPath) ) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_MIS_NULL, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_MIS_NULL);
		}
		
		if (StringUtils.isNotBlank(jsonPath) && jsonPath.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED)) {
			return check;
		}

		f = new File(jsonPath);
		if (!f.exists()) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_ERROR, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_ERROR);

		}
		if (!f.canRead()) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_READ);
		}

		isRequestJson = true;
		return check;
	}

	/**
	 * @return true if keyAlias is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkKeyAlias() throws ConfigException {
		if (merchantConfig.getKeyAlias() == null || merchantConfig.getKeyAlias().isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY, merchantConfig.getMerchantID(), Level.ERROR);
			merchantConfig.setKeyAlias(merchantConfig.getMerchantID());

		} else if (!merchantConfig.getKeyAlias().equalsIgnoreCase(merchantConfig.getMerchantID())) {
			merchantConfig.setKeyAlias(merchantConfig.getMerchantID());
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_MISMATCH, "", Level.ERROR);
		}
		return true;
	}

	/**
	 * @return true if key file is available else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean checkKeyFile() throws ConfigException {
		boolean check = true;
		merchantConfig.setKeyFile(null);

		if (merchantConfig.getKeyFilename() == null || merchantConfig.getKeyFilename().isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_EMPTY, merchantConfig.getMerchantID(), Level.ERROR);
			if (merchantConfig.getMerchantID() != null) {
				merchantConfig.setKeyFilename(merchantConfig.getMerchantID());
			}

		}
		if (merchantConfig.getKeysDirectory().isEmpty() || merchantConfig.getKeysDirectory() == null) {
			merchantConfig.setKeysDirectory(GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH);
			Utility.log(log, GlobalLabelParameters.KEY_DIRECTORY_EMPTY, "", Level.INFO);

		}

		merchantConfig.setKeyFile(new File(merchantConfig.getKeysDirectory()));

		if (!merchantConfig.getKeyFile().isDirectory()) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND, merchantConfig.getKeysDirectory(),
					Level.ERROR);
			merchantConfig.setKeyFile(null);
			check = false;
			return check;
		}
		try {
			merchantConfig.setKeyFile(new File(merchantConfig.getKeysDirectory(),
					merchantConfig.getKeyFilename().concat(GlobalLabelParameters.P12_EXTENSION)));
			if (!merchantConfig.getKeyFile().exists()) {
				Utility.log(log, GlobalLabelParameters.KEY_FILE_NOT_FOUND,
						merchantConfig.getKeysDirectory().concat("/").concat(merchantConfig.getKeyFilename())
								+ GlobalLabelParameters.P12_EXTENSION,
						Level.ERROR);
				merchantConfig.setKeyFile(null);
				check = false;
				return check;
			}
		} catch (Exception e) {
			merchantConfig.setKeyFile(null);
			check = false;
			return check;
		}
	  
		if (!merchantConfig.getKeyFile().canRead()) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ,
					merchantConfig.getKeysDirectory().concat(merchantConfig.getKeyFilename()), Level.ERROR);
			merchantConfig.setKeyFile(null);
			check = false;
			return check;
		}
		return check;
	}

}
