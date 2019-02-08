package com.cybersource.authsdk.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.log.Log4jWrapper;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will set merchant details as per properties file.
 */
public class MerchantConfig {
	private Properties props;
	/**
	 * JWT Merchant Config Parameters
	 */
	private String keysDirectory;
	private String keyAlias;
	private String keyPass;
	private String keyType;
	private String keyFilename;
	private File keyFile;

	/**
	 * HTTP Merchant Config Parameters
	 */
	private String merchantKeyId;
	private String merchantSecretKey;

	/* Common parameters for HTTP and JWT. */
	private String merchantID;
	private String authenticationType;
	private String requestHost;

	/* Logging Parameters. */
	private Logger log;
	private boolean isLogEnable;
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

	private static MerchantConfig instance;

	private MerchantConfig(Properties props) throws ConfigException {
		if (props != null) {
			this.props = props;
			setMerchantDetails();
		} else {
			throw new ConfigException(GlobalLabelParameters.PROP_ERROR);
		}
	}

	public static MerchantConfig getInstance(Properties properties) throws IOException, ConfigException {
		if (instance == null) {
			synchronized (MerchantConfig.class) {
				if(instance == null) {
					instance = new MerchantConfig(properties);
				}
		   }
		}
		return instance;
	}	

	public String getRunEnvironment() {
		return runEnvironment;
	}

	public void setRunEnvironment(String runEnvironment) {
		this.runEnvironment = runEnvironment;
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

	public boolean isLogEnable() {
		return isLogEnable;
	}

	public void setLog(boolean isLogEnable) {
		this.isLogEnable = isLogEnable;
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

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public void setLogMaximumSize(String logMaximumSize) {
		this.logMaximumSize = logMaximumSize;
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

	public void setPropertiesByAuthType(String authType) throws ConfigException {
		if (StringUtils.isNotBlank(authType)) {
			switch (authType.toUpperCase()) {
			case (GlobalLabelParameters.JWT):
				/* JWT Parameters */
				this.keysDirectory = this.getProperty(this.merchantID, "keysDirectory");
				this.keyAlias = this.getProperty(this.merchantID, "keyAlias");
				checkKeyAlias();
				this.keyPass = this.getProperty(this.merchantID, "keyPass");
				checkKeyPassword();
				this.keyFilename = this.getProperty(this.merchantID, "keyFileName");
				checkKeyFile();
				break;
			case (GlobalLabelParameters.HTTP):
				/* HTTP Parameters */
				this.merchantKeyId = this.getProperty(this.merchantID, "merchantKeyId");
				checkMerchantKeyId();
				this.merchantSecretKey = this.getProperty(this.merchantID, "merchantsecretKey");
				checkMerchantSecertKey();
				break;
			default:
				throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
			}
		} else {
			throw new ConfigException(GlobalLabelParameters.AUTH_NULL_EMPTY);
		}
	}

	public void setMerchantDetails() throws ConfigException {
		try {
			this.merchantID = this.getProperty(this.merchantID, "merchantID");
			if (StringUtils.isBlank(this.merchantID)) {
				throw new ConfigException("merchantId not set");
			}
			/* Logging Parameters */
			this.isLogEnable = this.checkBooleanLogValue(this.merchantID, "enableLog");
			if (isLogEnable) {
				this.logDirectory = this.getProperty(this.merchantID, "logDirectory");
				this.logFilename = this.getProperty(this.merchantID, "logFilename");
				this.logMaximumSize = this.getProperty(this.merchantID, "logMaximumSize");

				/* Validate log props */
				String logErrorMsg = checkLogMaxSizeAndFolder();
				/* Initialize log */
				this.log = Log4jWrapper.getInstance(this);
				if (StringUtils.isNotBlank(logErrorMsg)) {
					Utility.log(log, logErrorMsg, "", Level.ERROR);
				}
			}
			/* validate merchant ID */
			if (StringUtils.isBlank(this.merchantID)) {
				throw new ConfigException(GlobalLabelParameters.MERCHANT_NULL_EMPTY);
			}
			/* Optional use [ TIME OUT ] */
			this.timeout = this.getIntegerProperty(this.merchantID, "timeout", 130);
			this.proxyPort = this.getIntegerProperty(this.merchantID, "proxyPort", 8080);
			// this.proxyUser =
			// this.getProperty(this.merchantID,"proxyUser").trim();
			this.proxyPassword = this.getProperty(this.merchantID, "proxyPassword");
			this.proxyAddress = this.getProperty(this.merchantID, "proxyAddress");
			/* Run Environment. */
			this.runEnvironment = this.getProperty(this.merchantID, "runEnvironment");
			if (StringUtils.isBlank(this.runEnvironment)) {
				throw new ConfigException(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
			} else {
				if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)) {
					this.requestHost = GlobalLabelParameters.SANDBOX_URL;
				} else if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
					this.requestHost = GlobalLabelParameters.PRODUCTION_URL;
				} else {
					this.requestHost = this.runEnvironment;
				}
			}
			/* Set the Auth type Props */
			this.authenticationType = this.getProperty(this.merchantID, "authenticationType");
			setPropertiesByAuthType(this.authenticationType);
		} catch (ConfigException e) {
			Utility.log(log, e.getMessage(), "", Level.ERROR);
			throw e;
		} catch (Exception e) {
			Utility.log(log, e.getMessage(), "", Level.ERROR);
			throw new ConfigException(e.getMessage());
		}
	}

	/**
	 * @return true if log size is valid else return false,
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public String checkLogMaxSizeAndFolder() throws ConfigException {
		StringBuffer errorMsg = new StringBuffer();

		if (StringUtils.isBlank(this.logDirectory)) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			errorMsg.append(GlobalLabelParameters.NO_LOG_DIRECTORY);
		}

		File dir = new File(this.getLogDirectory());
		if (!dir.isDirectory()) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			errorMsg.append(GlobalLabelParameters.INVALID_LOG_DIRECTORY);
		}

		if (StringUtils.isBlank(this.logFilename)) {
			this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME;
			errorMsg.append(GlobalLabelParameters.NO_LOG_FILENAME);
		}

		if (StringUtils.isBlank(this.logMaximumSize)) {
			this.logMaximumSize = GlobalLabelParameters.DEFAULT_LOG_SIZE;
		}

		return errorMsg.toString();
	}

	/**
	 * @param merchantID
	 *            -merchant id for merchant.
	 * @param prop
	 *            -prop value from properties file.
	 * @return property as merchant.
	 */
	private String getProperty(String merchantID, String prop) {
		return this.getProperty(merchantID, prop, null);
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
	private String getProperty(String merchantID, String prop, String defaultVal) {
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
		} else {
			val = val.trim();
		}

		return val;
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
	private int getIntegerProperty(String merchantID, String prop, int defaultVal) throws ConfigException {
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
			if (StringUtils.isBlank(logBoolean)) {
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

	public Logger getLogger() {
		return log;
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void checkKeyPassword() {
		if (StringUtils.isBlank(keyPass)) {
			setKeyPass(merchantID);
			Utility.log(log, GlobalLabelParameters.KEY_PASSWORD_EMPTY_NULL.concat(" :: "), merchantID, Level.ERROR);
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void checkMerchantKeyId() throws ConfigException {
		if (StringUtils.isBlank(merchantKeyId)) {
			throw new ConfigException(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void checkMerchantSecertKey() throws ConfigException {
		if (StringUtils.isBlank(merchantSecretKey)) {
			throw new ConfigException(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void checkKeyAlias() {
		if (StringUtils.isBlank(keyAlias)) {
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY, merchantID, Level.ERROR);
			setKeyAlias(merchantID);

		} else if (!keyAlias.equalsIgnoreCase(merchantID)) {
			setKeyAlias(merchantID);
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_MISMATCH, "", Level.ERROR);
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void checkKeyFile() throws ConfigException {
		setKeyFile(null);
		if (StringUtils.isBlank(keyFilename)) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_EMPTY, merchantID, Level.ERROR);
			if (merchantID != null) {
				setKeyFilename(merchantID);
			}
		}
		if (StringUtils.isBlank(keysDirectory)) {
			setKeysDirectory(GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH);
			Utility.log(log, GlobalLabelParameters.KEY_DIRECTORY_EMPTY, "", Level.INFO);
		}

		setKeyFile(new File(keysDirectory));

		if (!keyFile.isDirectory()) {
			setKeyFile(null);
			throw new ConfigException(GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND.concat(keysDirectory));
		}
		try {
			setKeyFile(new File(keysDirectory, keyFilename.concat(GlobalLabelParameters.P12_EXTENSION)));
			setKeyType(GlobalLabelParameters.P12_EXTENSION);
			if (!keyFile.exists()) {
				setKeyFile(null);
				throw new ConfigException(GlobalLabelParameters.KEY_FILE_NOT_FOUND.concat(keysDirectory).concat("/")
						.concat(keyFilename).concat(GlobalLabelParameters.P12_EXTENSION));
			}
		} catch (Exception e) {
			setKeyFile(null);
			throw new ConfigException(GlobalLabelParameters.KEY_FILE_NOT_FOUND);
		}

		if (!keyFile.canRead()) {
			setKeyFile(null);
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_READ.concat(keysDirectory.concat(keyFilename)));
		}
	}

	public String toString() {
		return "{ authenticationType : " + authenticationType + ", runEnvironment : " + runEnvironment
				+ ", keyFileName : " + keyFilename + ", enableLog : " + isLogEnable + ", logDirectory : " + logDirectory
				+ ", logMaximumSize : " + logMaximumSize + ", logFilename : " + logFilename + " }";
	}
}
