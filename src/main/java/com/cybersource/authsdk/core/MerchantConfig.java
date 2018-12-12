package com.cybersource.authsdk.core;

import java.io.File;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will set merchant details as per properties file.
 */
public class MerchantConfig {
	private Properties props;
	/**
	 * JWT Merchant Config Parameters
	 *
	 */
	private String keysDirectory;
	private String keyAlias;
	private String keyPass;
	private String keyType;
	private String keyFilename;
	private File keyFile;

	/**
	 * HTTP Merchant Config Parameters
	 *
	 */
	private String merchantKeyId;
	private String merchantSecretKey;

	/* Common parameters for HTTP and JWT. */
	private String merchantID;
	private String url;
	private String requestTarget;
	private String authenticationType;
	private String getID;
	private String requestHost;
	private String requestType;
	public static boolean isSetMerchantDetails = true;
	/* Logging Parameters. */
	private Logger log;
	private boolean enableLog;
	private String logDirectory;
	private String logFilename;
	private String logMaximumSize;
	
	/* Others. */
	private int timeout;
	private String proxyUser;
	private String proxyPassword;
	private String recipientID;
	private String runEnvironment;
	private String proxyAddress;
	private int proxyPort;
	private String date;



	public String getRunEnvironment() {
		return runEnvironment;
	}

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

	public String getRequestHost() {
		return requestHost;
	}

	public String getGetID() {
		return getID;
	}

	public String getMerchantSecretKey() {
		return merchantSecretKey;
	}

	public void setMerchantSecretKey(String merchantSecretKey) {
		this.merchantSecretKey = merchantSecretKey;
	}

	public String getMerchantKeyId() {
		return merchantKeyId;
	}

	public void setMerchantKeyId(String merchantKeyId) {
		this.merchantKeyId = merchantKeyId;
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

	public String getKeyPass() {
		return keyPass;
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

	public String getKeyFilename() {
		return this.keyFilename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public void setRequestTarget(String requestTarget) {
		this.requestTarget = requestTarget;
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

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public void setGetID(String getID) {
		this.getID = getID;
	}

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
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

	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public void setKeyFile(File keyFile) {
		this.keyFile = keyFile;
	}

	public File getKeyFile() {
		return keyFile;
	}

	public MerchantConfig() throws ConfigException {

	}

	public MerchantConfig(Properties _props) throws ConfigException {
		if (_props != null) {
			this.props = _props;
			this.merchantID = this.props.getProperty("merchantID");
			this.date = PropertiesUtil.date;
			setMerchantDetails();
		}
	}

	public void setMerchantDetails() throws ConfigException {
		this.authenticationType = this.getProperty(this.merchantID, "authenticationType");
		this.merchantID = this.getProperty(this.merchantID, "merchantID");
		/* Logging Parameters */
		this.logDirectory = this.getProperty(this.merchantID, "logDirectory").trim();
		this.logFilename = this.getProperty(this.merchantID, "logFilename").trim();
		this.logMaximumSize = this.getProperty(this.merchantID, "logMaximumSize").trim();
		/* JWT Parameters */
		if (authenticationType != null) {
			if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
				this.keysDirectory = this.getProperty(this.merchantID, "keysDirectory").trim();
				this.keyAlias = this.getProperty(this.merchantID, "keyAlias").trim();
				this.keyPass = this.getProperty(this.merchantID, "keyPass");
				this.keyFilename = this.getProperty(this.merchantID, "keyFileName").trim();
			}
		}
		this.enableLog = this.checkBooleanLogValue(this.merchantID, "enableLog");
		/* Optional use [ TIME OUT ] */
		this.timeout = this.getIntegerProperty(this.merchantID, "timeout", 130);
		this.proxyPort = this.getIntegerProperty(this.merchantID, "proxyPort", 8080);
		// this.proxyUser =
		// this.getProperty(this.merchantID,"proxyUser").trim();
		this.proxyPassword = this.getProperty(this.merchantID, "proxyPassword");
		this.proxyAddress = this.getProperty(this.merchantID, "proxyAddress");
		/* HTTP Parameters */
		if (authenticationType != null) {
			if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
				this.merchantKeyId = this.getProperty(this.merchantID, "merchantKeyId").trim();
				this.merchantSecretKey = this.getProperty(this.merchantID, "merchantsecretKey").trim();
			}
		}
		/* Run Environment. */
		this.runEnvironment = this.getProperty(this.merchantID, "runEnvironment").trim();

		try {
			if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)) {
				this.requestHost = GlobalLabelParameters.SANDBOX_URL;
			} else if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
				this.requestHost = GlobalLabelParameters.PRODUCTION_URL;
			} else {
				this.requestHost = this.runEnvironment;
			}
		} catch (Exception e) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, "", Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.RUN_ENV_NULL_EMPTY, "", Level.ERROR);
			isSetMerchantDetails = false;
		}

	}

	/**
	 * @return true if log file is correct else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean getLogFile() throws ConfigException {
		File dir = new File(this.getLogDirectory());
		if (!dir.isDirectory()) {
			Utility.log(log, GlobalLabelParameters.INVALID_LOG_DIRECTORY, "", Level.ERROR);
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
		}
		return getLogFileNameStatus();
	}

	/**
	 * @return true if logFtle name is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private boolean getLogFileNameStatus() throws ConfigException {
		boolean status = true;

		if (this.logFilename == null || this.logFilename.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.NO_LOG_FILENAME, "", Level.ERROR);
			this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME.concat(GlobalLabelParameters.LOG_EXTENSION);
		}

		return status;
	}

	/**
	 * @param merchantID
	 *            -merchant id for merchant.
	 * @param prop
	 *            -prop value from properties file.
	 * @return property as merchant.
	 */
	public String getProperty(String merchantID, String prop) {
		return this.getProperty(merchantID, prop, (String) null);
	}

	/**
	 *
	 * @param merchantID
	 *            - merchant id for merchant.
	 * @param prop
	 *            -prop value from properties file.
	 * @param defaultVal
	 *            -default value if its not available.
	 * @return value of the property as per prop value.
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
	 *            - merchant id for merchant.
	 * @param prop
	 *            -prop from properties file.
	 * @param defaultVal
	 *            -default value for property.
	 * @return true if valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
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
	 *            - id for merchant.
	 * @param prop
	 *            - prop from properties file.
	 * @param defaultVal
	 *            - default value for property
	 * @return integer value as per property.
	 * @throws ConfigException
	 */
	public int getIntegerProperty(String merchantID, String prop, int defaultVal) throws ConfigException {
		String strValue = this.getProperty(merchantID, prop);
		if (strValue == null) {
			return defaultVal;
		} else {
			try {
				return Integer.parseInt(strValue);
			} catch (NumberFormatException var6) {
				Utility.log(log, GlobalLabelParameters.PROP_INVALID, "", Level.ERROR);
				throw new ConfigException(prop + " has an invalid value.");

			}
		}

	}

	/**
	 * @param merchantID
	 *            - merchant id for merchant.
	 * @param prop
	 *            - property from properties file.
	 * @return true if valid else return false.
	 */
	private boolean checkBooleanLogValue(String merchantID, String prop) {

		String logBoolean = this.getProperty(merchantID, prop);
		try {
			if (logBoolean == null || logBoolean.isEmpty()) {
				return true;
			} else if (logBoolean.equalsIgnoreCase("1")) {
				return true;
			} else if (logBoolean.equalsIgnoreCase("0")) {
				return false;
			} else if (logBoolean.equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

}
