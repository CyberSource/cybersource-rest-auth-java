package com.cybersource.authsdk.core;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 */
public class MerchantConfig {
	/**/
	private Properties props;
	/**/
	private String keysDirectory;
	private String keyAlias;
	/* KeyPassword*/
	private String keyPass;
	private String keyType;
	private String keyFilename;
	private String password;
	private boolean enableLog;
	private int timeout;
	private String proxyUser;
	private String proxyPassword;
	private String recipientID;
	private String effectivePassword;
	private String requestData;
	private String runEnvironment;
	private String proxyAddress;
	private int proxyPort;

	private File keyFile;
	private boolean isRequestType;
	public static boolean isSetMerchantDetails = true;
	/**
	 * HTTP Merchant Config Parameters
	 * 
	 */
	private String merchantKeyId;
	private String merchantsecretKey;
	/* Common parameters for HTTP and JWT. */
	private String merchantID;
	private String URL;
	private String requestTarget;
	private String authenticationType;
	private boolean sendToProduction;
	private String getID;
	private String requestHost;
	private String responseMessage;
	private String responseCode;
	private String v_c_CorelationID;
	private String requestType;

	private boolean isRequestJson = true;
	/* Others. */
	private Logger log;

	public void setRunEnvironment(String runEnvironment) {
		this.runEnvironment = runEnvironment;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getProxyAddress() {
		return proxyAddress;
	}

	public void setProxyAddress(String proxyAddress) {
		this.proxyAddress = proxyAddress;
	}

	public boolean isRequestJson() {
		return isRequestJson;
	}

	public String getRequestData() {

		try {
			if (this.getRequestJsonPath() != null && !this.getRequestJsonPath().isEmpty()) {
				requestData = PropertiesUtil.getJSONInput(this.getRequestJsonPath());
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_FOUND + this.getRequestJsonPath(), Level.FATAL);
			Utility.log(log, e);
			return null;
		}
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getV_c_CorelationID() {
		return v_c_CorelationID;
	}

	public void setV_c_CorelationID(String v_c_CorelationID) {
		this.v_c_CorelationID = v_c_CorelationID;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) throws NullPointerException {
		this.responseMessage = responseMessage;
	}

	/* Logging Parameters. */
	private boolean logSignedData;
	private String logDirectory;
	private String logFilename;

	private String logMaximumSize;
	/* Others */
	private String date;
	private String requestJsonPath;
	private String urlRequestHeader;

	public String getUrlRequestHeader() {
		return urlRequestHeader;
	}

	public void setUrlRequestHeader(String urlRequestHeader) {
		this.urlRequestHeader = urlRequestHeader;
	}

	public String getRequestHost() {
		return requestHost;
	}

	public String getRequestJsonPath() {

		return requestJsonPath;
	}

	public void setRequestJsonPath(String requestJsonPath) {
		this.requestJsonPath = requestJsonPath;
	}

	public String getGetID() {
		return getID;
	}

	public String getMerchantsecretKey() {
		return merchantsecretKey;
	}

	public String getMerchantKeyId() {
		return merchantKeyId;
	}

	public String getDate() {
		return date;
	}

	public String getMerchantID() {
		return this.merchantID;
	}

	public void setRecipientID(String recipientID) {
		this.recipientID = recipientID;
	}

	public String getRecipientID() {
		return this.recipientID;
	}

	public String getKeysDirectory() {
		return this.keysDirectory;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public boolean getrequestJson() {
		return false;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getKey() {
		return this.keyPass;
	}

	public String getKeyType() {
		return this.keyType != null ? this.keyType : "p12";
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getAuthenticationType() {
		return this.authenticationType;
	}

	public boolean getSendToProduction() {
		return this.sendToProduction;
	}

	public String getKeyFilename() {
		return this.keyFilename;
	}

	public String getURL() {
		return this.URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public void setRequestTarget(String requestTarget) {
		this.requestTarget = requestTarget;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean getEnableLog() {
		return this.enableLog;
	}

	public Properties getProps() {
		return props;
	}

	public void setKeysDirectory(String keysDirectory) {
		this.keysDirectory = keysDirectory;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public void setKeyFilename(String keyFilename) {
		this.keyFilename = keyFilename;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public void setEffectivePassword(String effectivePassword) {
		this.effectivePassword = effectivePassword;
	}

	public void setSendToProduction(boolean sendToProduction) {
		this.sendToProduction = sendToProduction;
	}

	public void setGetID(String getID) {
		this.getID = getID;
	}

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public void setRequestJson(boolean isRequestJson) {
		this.isRequestJson = isRequestJson;
	}

	public void setLogSignedData(boolean logSignedData) {
		this.logSignedData = logSignedData;
	}

	public void setLogMaximumSize(String logMaximumSize) {
		this.logMaximumSize = logMaximumSize;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setEnableLog(boolean enableLog) {
		this.enableLog = enableLog;
	}

	public boolean getLogSignedData() {
		return this.logSignedData;
	}

	public String getLogDirectory() {
		return this.logDirectory;
	}

	public String getLogFilename() {
		return this.logFilename;
	}

	public String getLogMaximumSize() {
		return this.logMaximumSize;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return this.proxyUser;
	}

	public String getProxyPassword() {
		return this.proxyPassword != null ? this.proxyPassword : "";
	}

	public String getEffectivePassword() {
		return this.effectivePassword;
	}

	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	public MerchantConfig() throws ConfigException {

	}

	public void setIsRequestType(boolean b) {
		this.isRequestType = b;

	}

	public MerchantConfig(Properties _props) throws NullPointerException, Exception {
		if (_props != null) {
			this.props = _props;
			this.merchantID = this.props.getProperty("merchantID");
			this.date = date();
			setMerchantDetails();
		}
	}

	public boolean validateMerchantDetails(Logger logger) {
		this.log = logger;
		isRequestType = checkRequestType();
		boolean isCheckMerchantSecertKey = false;
		boolean isCheckMerchantKeyId = false;
		boolean isGetKeyAlias = false;
		boolean isCheckKeyPassword = false;
		boolean isCheckKeyFile = false;
		boolean isCheckJsonPath;
		boolean isCheckLogMaxSize;
		boolean isCheckRunEnvironment;
		boolean isMerchantDetailStatus = false;

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isCheckMerchantSecertKey = checkMerchantSecertKey();
			isCheckMerchantKeyId = checkMerchantKeyId();
		} else if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)
				|| authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWE)) {
			isGetKeyAlias = checkKeyAlias();
			isCheckKeyPassword = checkKeyPassword();
			isCheckKeyFile = checkKeyFile();
		}

		isCheckJsonPath = checkJsonPath(getRequestJsonPath());
		isCheckLogMaxSize = checkLogMaxSizeAndFolder();
		isCheckRunEnvironment = checkRunEvironment();

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isMerchantDetailStatus = isCheckMerchantSecertKey && isCheckMerchantKeyId && isCheckJsonPath
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType && isSetMerchantDetails;
		} else if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			isMerchantDetailStatus = isGetKeyAlias && isCheckKeyPassword && isCheckKeyFile && isCheckJsonPath
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType && isSetMerchantDetails;
		}
		return isMerchantDetailStatus;
	}

	public boolean checkRequestType() {
		if (this.requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			return true;
		}
		Utility.log(log, GlobalLabelParameters.INVALID_REQUEST_TYPE + this.requestType, Level.ERROR);
		return false;
	}

	/**
	 * 
	 */
	public boolean checkKeyPassword() {
		if (this.keyPass == null || this.keyPass.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_PASSWORD_EMPTY_NULL, Level.ERROR);
			this.keyPass = this.merchantID;
			
		}
		return true;
	}

	/**
	 * 
	 */
	public boolean checkRunEvironment() {
		if (this.runEnvironment == null || this.runEnvironment.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.RUN_ENVIRONMENT_ERR, Level.ERROR);
			return false;
		} else if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)
				|| this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
			return true;
		} else
			return true;
	}

	public boolean checkMerchantKeyId() {
		if (this.merchantKeyId == null || this.merchantKeyId.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANT_KEYID_MISSING, Level.ERROR);
			return false;
		}
		return true;
	}

	public void setMerchantKeyId(String merchantKeyId) {
		this.merchantKeyId = merchantKeyId;
	}

	public boolean checkMerchantSecertKey() {
		if (this.merchantsecretKey == null || this.merchantsecretKey.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING, Level.ERROR);
			return false;
		}
		return true;
	}

	public void setMerchantsecretKey(String merchantsecretKey) {
		this.merchantsecretKey = merchantsecretKey;
	}

	/**
	 * 
	 * @param checkParameter
	 */
	public boolean checkLogMaxSizeAndFolder() {
		
			if (this.logDirectory == null || this.logDirectory.isEmpty()) {
				Utility.log(log, GlobalLabelParameters.NO_LOG_DIRECTORY, Level.ERROR);
				this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
				return true;
			}
		return getLogFile();
	}

	/**
	 * 
	 * @param checkParameter
	 */
	public boolean checkJsonPath(String jsonPath) {
		File f = null;
		if (isRequestType) {
			/* Continue, No logic check */
		} else {
			return false;
		}
		try {
			if (this.requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
					|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
				return true;
			}
			if (jsonPath == null || jsonPath.isEmpty()) {
				Utility.log(log, GlobalLabelParameters.REQUEST_JSON_MIS_NULL, Level.ERROR);
				return false;
			}
			f = new File(jsonPath);
			if (!f.exists()) {
				Utility.log(log, GlobalLabelParameters.REQUEST_JSON_ERROR + f.getAbsolutePath(), Level.ERROR);
				return false;
			}
			if (!f.canRead()) {
				Utility.log(log, GlobalLabelParameters.FILE_NOT_READ + f.getAbsolutePath(), Level.ERROR);
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		isRequestJson = true;
		return true;
	}

	public boolean checkKeyAlias() {
		if (this.keyAlias == null || this.keyAlias.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY + this.merchantID, Level.ERROR);
			this.keyAlias = merchantID;
		} else if (!this.keyAlias.equalsIgnoreCase(this.merchantID)) {
			this.keyAlias = this.merchantID;
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_MISMATCH, Level.ERROR);
		}
		return true;
	}

	/**
	 * 
	 * @throws ConfigException
	 */
	public void setMerchantDetails()  {
		/* Common Parameters */
		
		//this.requestJsonPath = this.getProperty(this.requestJsonPath, "requestJsonPath").trim();

		/* Logging Parameters */
		this.logDirectory = this.getProperty(this.merchantID, "logDirectory").trim();
		this.logFilename = this.getProperty(this.merchantID, "logFilename").trim();
		this.logMaximumSize = this.getProperty(this.merchantID, "logMaximumSize").trim();
		/* JWT Parameters */
		//this.recipientID = this.getProperty(this.merchantID, "recipientID").trim();
		this.keysDirectory = this.getProperty(this.merchantID, "keysDirectory").trim();
		this.keyAlias = this.getProperty(this.merchantID, "keyAlias").trim();
		this.keyPass = this.getProperty(this.merchantID, "keyPass");
		//this.keyType = this.getProperty(this.merchantID, "keyType").trim();
		this.keyFilename = this.getProperty(this.merchantID, "keyFileName").trim();
		//this.password = this.getProperty(this.merchantID, "password").trim();
		this.enableLog = this.checkBooleanLogValue(this.merchantID, "enableLog");
		/* Optional use [ TIME OUT ]*/
		this.timeout = this.getIntegerProperty(this.merchantID, "timeout", 130);
		this.proxyPort = this.getIntegerProperty(this.merchantID, "proxyPort", 8080);
		//this.proxyUser = this.getProperty(this.merchantID, "proxyUser").trim();
		this.proxyPassword = this.getProperty(this.merchantID, "proxyPassword");
		this.proxyAddress = this.getProperty(this.merchantID, "proxyAddress");
		/* HTTP Parameters */
		this.merchantKeyId = this.getProperty(this.merchantID, "merchantKeyId").trim();
		this.merchantsecretKey = this.getProperty(this.merchantID, "merchantsecretKey").trim();
		this.runEnvironment = this.getProperty(this.merchantID, "runEnvironment").trim();
		/* Run Environment.*/
		try {
			if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)) {
				this.requestHost = GlobalLabelParameters.SANDBOX_URL;
			} else if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
				this.requestHost = GlobalLabelParameters.PRODUCTION_URL;
			}  else if (this.runEnvironment.isEmpty()){    
				this.log = Log4j.getInstance(this);
				Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, Level.INFO);
				Utility.log(this.log, GlobalLabelParameters.RUN_ENV_NULL_EMPTY, Level.ERROR);
				isSetMerchantDetails = false;
			} else {
				this.requestHost = this.runEnvironment;
			}
		} catch (Exception e) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.RUN_ENV_NULL_EMPTY, Level.ERROR);
			isSetMerchantDetails = false;
		}
		if (this.merchantID == null || this.merchantID.isEmpty()) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.MERCHANT_NULL_EMPTY, Level.ERROR);
			isSetMerchantDetails = false;
		} 
		checkMandatoryFields();	
	}

	
	private void checkMandatoryFields() {
			/* Authentication Type.*/
		try {
			this.authenticationType = this.getProperty(this.merchantID, "authenticationType").trim();
			if(this.authenticationType.isEmpty()){
				authenticationException();
				return;
			 }
			} catch (Exception e) {
				authenticationException();
			    return;
		}	
	}
	
	private void authenticationException(){
		isSetMerchantDetails = false;	
		this.authenticationType = "Not Present in file";
		this.log = Log4j.getInstance(this);
		Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, Level.INFO);
		return;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}


	/**
	 * 
	 * @return .p12 File
	 * @throws ConfigException
	 */

	public File getKeyFile() {
		return keyFile;
	}

	public boolean checkKeyFile() {
		keyFile = null;
		try {
			if (this.keyFilename == null || this.keyFilename.isEmpty()) {
				Utility.log(log, GlobalLabelParameters.KEY_FILE_EMPTY + this.merchantID, Level.ERROR);
				if (this.merchantID != null) {
					this.keyFilename = this.merchantID;
				}
			}
		} catch (Exception e) {
			return false;
		}

		try {
			if (this.keysDirectory.isEmpty() || this.keysDirectory == null) {
				Utility.log(log, GlobalLabelParameters.KEY_DIRECTORY_EMPTY, Level.ERROR);
				this.keysDirectory = GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH;
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.KEY_DIRECTORY_EMPTY, Level.ERROR);
			this.keysDirectory = GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH;
		}

		keyFile = new File(this.keysDirectory);

		if (!keyFile.isDirectory()) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND + this.keysDirectory,
					Level.ERROR);
			System.err.println(GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND + this.keysDirectory);
			keyFile = null;
			return false;
		}
		try {
			keyFile = new File(this.keysDirectory, this.keyFilename + GlobalLabelParameters.P12_EXTENSION);
			if (!keyFile.exists()) {
				Utility.log(log, GlobalLabelParameters.KEY_FILE_NOT_FOUND + this.keysDirectory + "/"
						+ this.keyFilename + GlobalLabelParameters.P12_EXTENSION, Level.ERROR);
				System.err.println(GlobalLabelParameters.KEY_FILE_NOT_FOUND + this.keysDirectory + "/"
						+ this.keyFilename + GlobalLabelParameters.P12_EXTENSION);
				keyFile = null;
				return false;
			}
			Utility.log(log, GlobalLabelParameters.KEY_FILE + this.keysDirectory + "/" + this.keyFilename
					+ GlobalLabelParameters.P12_EXTENSION, Level.INFO);
		} catch (Exception e) {
			keyFile = null;
			return false;
		}
		boolean isRead = keyFile.canRead();

		if (isRead) {
			/* Do Nothing. */
			return true;
		} else {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ + this.keysDirectory + this.keyFilename,
					Level.ERROR);
			keyFile = null;
			return false;
		}
	}

	public boolean getLogFile() {
		File dir = new File(this.getLogDirectory());
		if (!dir.isDirectory()) {
			Utility.log(log, GlobalLabelParameters.INVALID_LOG_DIRECTORY, Level.ERROR);
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			return getLogFileNameStatus();
		}
		return getLogFileNameStatus();
	}

	/**
	 * 
	 * 
	 */
	private boolean getLogFileNameStatus() {
		try {
			if (this.logFilename == null || this.logFilename.isEmpty()) {
				Utility.log(log, GlobalLabelParameters.NO_LOG_FILENAME, Level.ERROR);
				this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME + GlobalLabelParameters.LOG_EXTENSION;
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.NO_LOG_FILENAME, Level.ERROR);
			this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME + GlobalLabelParameters.LOG_EXTENSION;
			return true;
		}
		return true;
	}

	/*
	 * @param merchantID
	 * 
	 * @param prop
	 * 
	 * @return
	 */
	public String getProperty(String merchantID, String prop) {
		return this.getProperty(merchantID, prop, (String) null);
	}

	/**
	 * 
	 * @param merchantID
	 * @param prop
	 * @param defaultVal
	 * @return
	 */
	public String getProperty(String merchantID, String prop, String defaultVal) {
		String val = null;
		String merchantSpecificProp = merchantID != null ? merchantID + "." + prop : null;
		if (this.props != null && merchantSpecificProp != null) {
			val = this.props.getProperty(merchantSpecificProp);
		}

		if (this.props != null && val == null) {
			val = this.props.getProperty(prop);
		}

		if (val == null && merchantSpecificProp != null) {
			val = System.getProperty("cybs." + merchantSpecificProp);
		}

		if (val == null) {
			val = System.getProperty("cybs." + prop);
		}

		if (val == null) {
			val = defaultVal;
		}

		return val;
	}

	/**
	 * 
	 * @param merchantID
	 * @param prop
	 * @param defaultVal
	 * @return
	 * @throws ConfigException
	 */
	public boolean getBooleanProperty(String merchantID, String prop, boolean defaultVal) throws ConfigException {
		String strValue = this.getProperty(merchantID, prop);
		if (strValue == null) {
			return defaultVal;
		} else if (!"1".equals(strValue) && !"true".equalsIgnoreCase(strValue)) {
			if (!"0".equals(strValue) && !"false".equalsIgnoreCase(strValue)) {
				throw new ConfigException(prop + " has an invalid value.");
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param merchantID
	 * @param prop
	 * @param defaultVal
	 * @return
	 * @throws ConfigException
	 */
	public int getIntegerProperty(String merchantID, String prop, int defaultVal) {
		String strValue = this.getProperty(merchantID, prop);
		if (strValue == null) {
			return defaultVal;
		} else {
			try {
				return Integer.parseInt(strValue);
			} catch (NumberFormatException var6) {
				System.err.println((prop + " has an invalid value."));
			}
		}
		return defaultVal;
	}

	/**
	 * 
	 * @return
	 */
	private String date() {
		return date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	}
	
	private boolean checkBooleanLogValue(String merchantID, String prop) {
		
		String logBoolean = this.getProperty(merchantID, prop);
		try {
			if(logBoolean == null || logBoolean.isEmpty()){
				return true;
			} else if (logBoolean.equalsIgnoreCase("1")) {    
				return true;
			} else if (logBoolean.equalsIgnoreCase("0")) {
				return false;
			} else if( logBoolean.equalsIgnoreCase("true") ){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

}
