package com.cybersource.authsdk.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PrettyPrintingMap;
import com.cybersource.authsdk.util.Utility;

public class MerchantConfig {
	private static Logger logger = LogManager.getLogger(MerchantConfig.class);

	private Properties props;

	private Map<String, String> defaultHeaders;

	/* Authentication related parameters */

	/***
	 * Type of authentication mechanism. Can be `http_signature` or `jwt`
	 */
	private String authenticationType;

	/***
	 * HTTP verb for the request. Required for signature and JWT generation
	 */

	/* Network related parameters */

	/***
	 * The host (specified by name) serving the API
	 */
	private String requestHost;

	/***
	 * Data to be sent in the request
	 */

	/* Common parameters for HTTP and JWT. */

	/***
	 * CyberSource merchant ID
	 */
	private String merchantID;

	/***
	 * Environment to run against
	 */
	private String runEnvironment;

	/***
	 * Optional Intermediate Environment to run against
	 */
	private String intermediateHost;

	/**
	 * Default DeveloperId to be sent with API call
	 */
	private String defaultDeveloperId;

	/***
	 * ID to capture developer partner identification
	 * <p>
	 * CURRENTLY NOT SENT AS HEADER
	 */
	private String solutionId;

	/* Telemetry related parameters */

	/***
	 * Flag to check if retry should be attempted on failure
	 */
	private boolean retryEnabled;

	/***
	 * Delay to wait in between retries
	 */
	private long retryDelay;

	/* MetaKey related parameters */

	/***
	 * Flag to check if MetaKey authentication should be used
	 */
	private boolean useMetaKey;

	/***
	 * CyberSource portfolio ID
	 */
	private String portfolioID;

	/* HTTP Merchant Config Parameters */

	/***
	 * Key ID obtained from Key Management on CyberSource Business Center
	 */
	private String merchantKeyId;

	/***
	 * Corresponding Shared Secret for the above Key ID
	 */
	private String merchantSecretKey;

	/* JWT Authentication related parameters */

	/***
	 * Directory where .p12 certificate is stored
	 */
	private String keysDirectory;

	/***
	 * Friendly name alias for the certificate
	 */
	private String keyAlias;

	/***
	 * Password for the certificate
	 */
	private String keyPass;

	/***
	 * Name of the certificate file
	 */
	private String keyFilename;

	/***
	 * Certificate file object
	 */
	private File keyFile;

	/* OAuth2.0 Authentication related parameters */

	/***
	 * JWT created for OAuth2.0
	 */
	private String accessToken;

	/***
	 * Newly created JWT token for initial request or if refresh token expired, else
	 * the same refresh token as in the request
	 */
	private String refreshToken;

	/***
	 * The client ID that identifies the client application registered in the
	 * CyberSource Business Center
	 */
	private String clientId;

	/***
	 * The client secret received when the client application was registered in the
	 * CyberSource Business Center
	 */
	private String clientSecret;

	/***
	 * Flag to indicate if client certificate should be used to authenticate against
	 * the environment
	 */
	private boolean enableClientCert;

	/***
	 * Client certificate file
	 */
	private String clientCertFile;

	/***
	 * Path to client certificate file
	 */
	private String clientCertDirectory;

	/***
	 * Path to client JWE pem file directory
	 */

	private String pemFileDirectory;

	/***
	 * Password to access the client certificate file
	 */
	private String clientCertPassword;

	/* Proxy server related parameters */

	/***
	 * Flag to indicate if proxy authentication is required
	 */
	private boolean useProxy = false;

	/***
	 * Proxy host address
	 */
	private String proxyAddress;

	/***
	 * Proxy host port
	 */
	private int proxyPort;

	/***
	 * Username to authenticate against proxy host
	 */
	private String proxyUser;

	/***
	 * Password to authenticate against proxy host
	 */
	private String proxyPassword;

	/***
	 * User Defined Connection Timeout
	 */
	private int userDefinedConnectionTimeout;

	/***
	 * User Defined Read Timeout
	 */
	private int userDefinedReadTimeout;

	/***
	 * User Defined Write Timeout
	 */
	private int userDefinedWriteTimeout;

	/***
	 * User Defined Keep Alive Duration
	 */
	private int userDefinedKeepAliveDuration;

	/***
	 * User Defined Max Idle Connections
	 */
	private int userDefinedMaxIdleConnections;

	/***
	 * Flag to indicate if merchant details have been set properly
	 */
	private boolean merchantDetailsSet = false;

	/***
	 *  MLE Feature
	 */
	private boolean useMLEGlobally =false;

	private boolean enableRequestMLEForOptionalApisGlobally= false;

	private boolean disableRequestMLEForMandatoryApisGlobally =false;


	private Map<String, Boolean> mapToControlMLEonAPI;

	private String mleKeyAlias;

	private String mleForRequestPublicCertPath;

	public MerchantConfig() throws ConfigException {

	}

	public MerchantConfig(Properties _props) throws ConfigException {
		if (_props != null) {
			this.setProps(_props);
			setMerchantDetails();
		}
	}

	public MerchantConfig(Properties _props, Map<String, String> defaultHeaders) throws ConfigException {
		if (_props != null) {
			this.setProps(_props);
			setMerchantDetails();
			setDefaultHeaders(defaultHeaders);
		}
	}

	/**
	 * Validates merchant configuration details based on request type.
	 *
	 * @param requestTypeData the HTTP request type to validate
	 * @return true or false as per validation
	 * @throws ConfigException if some value is missing for merchant.
	 */
	public boolean validateMerchantDetails(String requestTypeData ) throws ConfigException {
		boolean isRequestType = checkRequestType(requestTypeData);

		boolean isMerchantId = true;
		boolean isCheckMerchantSecretKey = false;
		boolean isCheckMerchantKeyId = false;
		boolean isGetKeyAlias = false;
		boolean isCheckKeyPassword = false;
		boolean isCheckKeyFile = false;
		boolean isCheckLogMaxSize = true;
		boolean isCheckRunEnvironment;
		boolean isMerchantDetailStatus = false;

		checkAuthenticationType();

		if (getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isMerchantId = checkMerchantId();
			isCheckMerchantSecretKey = checkMerchantSecretKey();
			isCheckMerchantKeyId = checkMerchantKeyId();
		} else if (getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			/*
			 * JWE is not supported as an authentication mechanism
			 */
//			 || authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWE)) {
			isMerchantId = checkMerchantId();
			isGetKeyAlias = checkKeyAlias();
			isCheckKeyPassword = checkKeyPassword();
			isCheckKeyFile = checkKeyFile();
		}

		isCheckRunEnvironment = checkRunEvironment();

		if (this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isMerchantDetailStatus = isMerchantId && isCheckMerchantSecretKey && isCheckMerchantKeyId
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType && this.isMerchantDetailsSet();
		} else if (this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			isMerchantDetailStatus = isMerchantId && isGetKeyAlias && isCheckKeyPassword && isCheckKeyFile
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType && this.isMerchantDetailsSet();
		} else if (this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.OAUTH)
				|| this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.MUTUALAUTH)) {
			isMerchantDetailStatus = isCheckLogMaxSize && isCheckRunEnvironment && isRequestType;
		}

		Map<String, Object> configurationData = new HashMap<>();
		configurationData.put("AuthenticationType", this.getAuthenticationType());
		configurationData.put("RunEnvironment", this.getRunEnvironment());

		logger.info("Configuration :\n{}", new PrettyPrintingMap<String, Object>(configurationData));

		return isMerchantDetailStatus;
	}

	/**
	 * Validates if the provided request type is supported.
	 *
	 * @param requestTypeData the HTTP request type to validate (GET, POST, PUT, DELETE, PATCH)
	 * @return true if valid request type else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkRequestType(String requestTypeData) throws ConfigException {
//		String requestTypeCheck = this.getRequestType();
		if (requestTypeData.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| requestTypeData.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestTypeData.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestTypeData.equalsIgnoreCase(GlobalLabelParameters.DELETE)
				|| requestTypeData.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			return true;
		} else {
			logger.error("ConfigException : " + GlobalLabelParameters.INVALID_REQUEST_TYPE + " : " + requestTypeData);
			throw new ConfigException(GlobalLabelParameters.INVALID_REQUEST_TYPE);
		}
	}

	/**
	 * @return true if valid password else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyPassword() throws ConfigException {
		if (this.getKeyPass() == null || this.getKeyPass().isEmpty()) {
			this.setKeyPass(this.getMerchantID());
			logger.error(GlobalLabelParameters.KEY_PASSWORD_EMPTY_NULL);
		}
		return true;
	}

	/**
	 * @return true if valid run environment else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkRunEvironment() throws ConfigException {
		boolean runEnvironment = false;

		if (this.runEnvironment == null || this.runEnvironment.isEmpty()) {
			logger.error("ConfigException : " + GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
			throw new ConfigException(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
		} else if (GlobalLabelParameters.OLD_RUN_ENVIRONMENT_CONSTANTS.contains(this.runEnvironment.toUpperCase())) {
			throw new ConfigException(GlobalLabelParameters.DEPRECATED_ENVIRONMENT);
		}  else {
			runEnvironment = true;
		}
		return runEnvironment;
	}

	/**
	 * @return true if merchant id is not null or empty else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantKeyId() throws ConfigException {
		if (this.getMerchantKeyId() == null || this.getMerchantKeyId().isEmpty()) {
			logger.error("ConfigException : " + GlobalLabelParameters.MERCHANT_KEYID_MISSING);
			throw new ConfigException(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
		}
		return true;
	}

	/**
	 * @return true if secretKey is not null or empty else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantSecretKey() throws ConfigException {
		if (this.getMerchantSecretKey() == null || this.getMerchantSecretKey().isEmpty()) {
			logger.error("ConfigException : " + GlobalLabelParameters.MERCHANTSECRET_KEY_MISSING);
			throw new ConfigException(GlobalLabelParameters.MERCHANTSECRET_KEY_MISSING);
		}
		return true;
	}

	/**
	 * @return true if keyAlias is not null or empty else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyAlias() throws ConfigException {
		if (this.getKeyAlias() == null || this.getKeyAlias().isEmpty()) {
			logger.error(GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY);
			this.setKeyAlias(this.getMerchantID());
		} else if (!this.useMetaKey && !this.getKeyAlias().equalsIgnoreCase(this.getMerchantID())) {
			this.setKeyAlias(this.getMerchantID());
			logger.error(GlobalLabelParameters.KEY_ALIAS_MISMATCH);
		}
		return true;
	}

	private void setMerchantDetails() throws ConfigException {
		this.setAuthenticationType(this.getProps().getProperty("authenticationType"));
		this.setUseMetaKey(Boolean.valueOf(this.getProps().getOrDefault("useMetaKey", false).toString()));
		this.setEnableClientCert(Boolean.valueOf(this.getProps().getOrDefault("enableClientCert", false).toString()));
		if (null != this.getProps().getProperty("jwePEMFileDirectory")) {
			this.setPemFileDirectory((String) this.getProps().getProperty("jwePEMFileDirectory"));
		}
		if (this.isUseMetaKey()) {
			if(this.getProps().getProperty("portfolioID") == null || StringUtils.isEmpty(this.getProps().getProperty("portfolioID")))
			{
				logger.error("ConfigException : Portfolio ID is mandatory");
				throw new ConfigException("Portfolio ID is mandatory");
			}
			this.setPortfolioID(this.getProps().getProperty("portfolioID"));
		}

		if (this.isEnableClientCert()) {
			if (this.getProps().getProperty("clientCertFile") == null || StringUtils.isEmpty(this.getProps().getProperty("clientCertFile"))) {
				logger.error("ConfigException : Client Certificate is mandatory");
				throw new ConfigException("Client Cert is mandatory");
			}
			this.setClientCertFile(this.getProps().getProperty("clientCertFile"));

			if (this.getProps().getProperty("clientCertPassword") == null || StringUtils.isEmpty(this.getProps().getProperty("clientCertPassword"))) {
				logger.error("ConfigException : Client Certificate Password is mandatory");
				throw new ConfigException("Client Cert Password is mandatory");
			}
			this.setClientCertPassword(this.getProps().getProperty("clientCertPassword"));

			if (this.getProps().getProperty("clientCertDirectory") == null || StringUtils.isEmpty(this.getProps().getProperty("clientCertDirectory"))) {
				logger.error("ConfigException : Client Certificate Directory is mandatory");
				throw new ConfigException("Client Cert Directory is mandatory");
			}
			this.setClientCertDirectory(this.getProps().getProperty("clientCertDirectory"));
		}

		if (this.getAuthenticationType() != null
				&& this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			/* JWT Authentication */
			if (this.getProps().getProperty("keysDirectory") == null) {
				this.setKeysDirectory(GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH);
			}
			this.setKeysDirectory(this.getProps().getProperty("keysDirectory").trim());

			if (this.getProps().getProperty("merchantID") == null) {
				logger.error("ConfigException : Merchant ID is mandatory");
				throw new ConfigException("Merchant Id is mandatory");
			}
			this.setMerchantID(this.getProps().getProperty("merchantID").trim());

			if (this.getProps().getProperty("keyAlias") == null) {
				logger.error(GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY);
				this.setKeyAlias(this.getMerchantID());
			}
			this.setKeyAlias(this.getProps().getProperty("keyAlias").trim());

			if (this.getProps().getProperty("keyPass") == null) {
				this.setKeyPass(this.getMerchantID());
			}
			this.setKeyPass(this.getProps().getProperty("keyPass"));

			if (this.getProps().getProperty("keyFileName") == null) {
				this.setKeyFilename(this.getMerchantID());
			}
			this.setKeyFilename(this.getProps().getProperty("keyFileName").trim());
		} else if (this.getAuthenticationType() != null
				&& this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			/* HTTP Signature Authentication */
			if (this.getProps().getProperty("merchantID") == null) {
				logger.error("ConfigException : Merchant ID is mandatory");
				throw new ConfigException("Merchant Id is mandatory");
			}
			this.setMerchantID(this.getProps().getProperty("merchantID").trim());

			if (this.getProps().getProperty("merchantKeyId") == null) {
				logger.error("ConfigException : Merchant Key ID is mandatory");
				throw new ConfigException("Merchant KeyId is mandatory");
			}
			this.setMerchantKeyId(this.getProps().getProperty("merchantKeyId").trim());

			if (this.getProps().getProperty("merchantsecretKey") == null) {
				logger.error("ConfigException : Merchant Secret Key is mandatory");
				throw new ConfigException("Merchant SecretKey is mandatory");
			}
			this.setMerchantSecretKey(this.getProps().getProperty("merchantsecretKey").trim());
		} else if (this.getAuthenticationType() != null
				&& this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.OAUTH)) {
			/* OAuth Authentication */
			if (this.getProps().getProperty("accessToken") == null || StringUtils.isEmpty(this.getProps().getProperty("accessToken"))) {
				logger.error("ConfigException : Access Token is mandatory for OAuth");
				throw new ConfigException("Access Token is mandatory for OAuth");
			}
			this.setAccessToken(this.getProps().getProperty("accessToken"));

			if (this.getProps().getProperty("refreshToken") == null || StringUtils.isEmpty(this.getProps().getProperty("refreshToken"))) {
				logger.error("ConfigException : Refresh Token is mandatory for OAuth");
				throw new ConfigException("Refresh Token is mandatory for OAuth");
			}
			this.setRefreshToken(this.getProps().getProperty("refreshToken"));
		} else if (this.getAuthenticationType() != null
				&& this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.MUTUALAUTH)) {
			/* Mutual Auth Authentication */
			if (this.getProps().getProperty("clientId") == null || StringUtils.isEmpty(this.getProps().getProperty("clientId"))) {
				logger.error("ConfigException : Client ID is mandatory to generate OAuth");
				throw new ConfigException("Client Id is mandatory to generate OAuth");
			}
			this.setClientId(this.getProps().getProperty("clientId"));

			if (this.getProps().getProperty("clientSecret") == null || StringUtils.isEmpty(this.getProps().getProperty("clientSecret"))) {
				logger.error("ConfigException : Client Secret is mandatory to generate OAuth");
				throw new ConfigException("Client Secret is mandatory to generate OAuth");
			}
			this.setClientSecret(this.getProps().getProperty("clientSecret"));
		}

		this.setUseProxy(Boolean.valueOf(this.getProps().getOrDefault("useProxy", false).toString()));

		if (this.isUseProxyEnabled()) {
			this.setProxyAddress(this.getProps().getProperty("proxyAddress"));
			if (this.getProps().getProperty("proxyPort") != null) {
				this.setProxyPort(Integer.parseInt(this.getProps().getProperty("proxyPort")));
			} else {
				logger.error("ConfigException : Proxy Port is necessary for using Proxy");
				throw new ConfigException("Proxy Port is necessary for using Proxy");
			}

			this.setProxyUser(this.getProps().getProperty("proxyUser"));
			this.setProxyPassword(this.getProps().getProperty("proxyPassword"));
		}

		this.setSolutionId(this.getProps().getProperty("solutionId"));

		/* Run Environment. */
		if (this.getProps().getProperty("runEnvironment") == null || StringUtils.isEmpty(this.getProps().getProperty("runEnvironment"))) {
			logger.error("ConfigException : Run Environment is mandatory");
			throw new ConfigException("Run Environment is mandatory");
		} else {
			this.setRunEnvironment(this.getProps().getProperty("runEnvironment").trim());
		}

		/* Intermediate Host Environment. */
		if (StringUtils.isNotBlank(this.getProps().getProperty("intermediateHost"))) {
			this.setIntermediateHost(this.getProps().getProperty("intermediateHost").trim());
		}

		/* Set Default DeveloperId */
		if (StringUtils.isNotBlank(this.getProps().getProperty("defaultDeveloperId"))) {
			this.setDefaultDeveloperId(this.getProps().getProperty("defaultDeveloperId").trim());
		}

		int tempValue;

		tempValue = Integer.parseInt(this.getProps().getProperty("userDefinedConnectionTimeout", GlobalLabelParameters.DEFAULT_MAX_CONNECT_TIMEOUT_IN_SECONDS));
		if(tempValue <= Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_CONNECT_TIMEOUT_IN_SECONDS)) {
			logger.warn("Provided Connection Timeout Value is Incorrect, Defaulting to " + GlobalLabelParameters.DEFAULT_MAX_CONNECT_TIMEOUT_IN_SECONDS);
			tempValue = Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_CONNECT_TIMEOUT_IN_SECONDS);
		}
		this.setUserDefinedConnectionTimeout(tempValue);

		tempValue = Integer.parseInt(this.getProps().getProperty("userDefinedReadTimeout", GlobalLabelParameters.DEFAULT_MAX_READ_TIMEOUT_IN_SECONDS));
		if(tempValue <= Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_READ_TIMEOUT_IN_SECONDS)) {
			logger.warn("Provided Read Timeout Value is Incorrect, Defaulting to " + GlobalLabelParameters.DEFAULT_MAX_READ_TIMEOUT_IN_SECONDS);
			tempValue = Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_READ_TIMEOUT_IN_SECONDS);
		}
		this.setUserDefinedReadTimeout(tempValue);

		tempValue = Integer.parseInt(this.getProps().getProperty("userDefinedWriteTimeout", GlobalLabelParameters.DEFAULT_MAX_WRITE_TIMEOUT_IN_SECONDS));
		if(tempValue <= Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_WRITE_TIMEOUT_IN_SECONDS)) {
			logger.warn("Provided Write Timeout Value is Incorrect, Defaulting to " + GlobalLabelParameters.DEFAULT_MAX_WRITE_TIMEOUT_IN_SECONDS);
			tempValue = Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_WRITE_TIMEOUT_IN_SECONDS);
		}
		this.setUserDefinedWriteTimeout(tempValue);

		tempValue = Integer.parseInt(this.getProps().getProperty("userDefinedKeepAliveDuration", GlobalLabelParameters.DEFAULT_MAX_KEEP_ALIVE_DURATION_IN_SECONDS));
		if(tempValue <= Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_KEEP_ALIVE_DURATION_IN_SECONDS)) {
			logger.warn("Provided Keep Alive Duration Value is Incorrect, Defaulting to " + GlobalLabelParameters.DEFAULT_MAX_KEEP_ALIVE_DURATION_IN_SECONDS);
			tempValue = Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_KEEP_ALIVE_DURATION_IN_SECONDS);
		}
		this.setUserDefinedKeepAliveDuration(tempValue);

		tempValue = Integer.parseInt(this.getProps().getProperty("userDefinedMaxIdleConnections", GlobalLabelParameters.DEFAULT_MAX_IDLE_CONNECTIONS));
		if (tempValue <= Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_IDLE_CONNECTIONS)) {
			logger.warn("Provided Max Idle Connections Value is Incorrect, Defaulting to " + GlobalLabelParameters.DEFAULT_MAX_IDLE_CONNECTIONS);
			tempValue = Integer.parseInt(GlobalLabelParameters.DEFAULT_MAX_IDLE_CONNECTIONS);
		}
		this.setUserDefinedMaxIdleConnections(tempValue);

		try {
			if (GlobalLabelParameters.OLD_RUN_ENVIRONMENT_CONSTANTS.contains(this.runEnvironment.toUpperCase())) {
				throw new ConfigException(GlobalLabelParameters.DEPRECATED_ENVIRONMENT);
			}

			this.requestHost = this.runEnvironment;
		} catch (Exception e) {
			logger.info(GlobalLabelParameters.BEGIN_TRANSACTION);
			logger.error(GlobalLabelParameters.RUN_ENV_NULL_EMPTY);
			this.setMerchantDetailsSet(false);
		}

		/* Retry logic */
		String retryEnabledValue = this.getProps().getProperty("retryEnabled");
		if (retryEnabledValue != null && !retryEnabledValue.isEmpty()) {
			this.setRetryEnabled(Boolean.valueOf(retryEnabledValue));
		} else {
			this.setRetryEnabled(false);
		}
		//MLE Params
		boolean enableRequestMLEForOptionalApisGloballyValue = Boolean.parseBoolean(this.getProps().getProperty("enableRequestMLEForOptionalApisGlobally"));
		boolean useMLEGloballyValue = Boolean.parseBoolean(this.getProps().getProperty("useMLEGlobally"));

		boolean useMLE = false;

		useMLE = enableRequestMLEForOptionalApisGloballyValue || useMLEGloballyValue;

		this.setEnableRequestMLEForOptionalApisGlobally(useMLE);

		boolean disableRequestMLEForMandatoryApisGloballyValue = Boolean.parseBoolean(this.getProps().getProperty("disableRequestMLEForMandatoryApisGlobally"));
		this.setDisableRequestMLEForMandatoryApisGlobally(disableRequestMLEForMandatoryApisGloballyValue);

		if(null != this.getProps().getProperty("mleKeyAlias") && !StringUtils.isEmpty(this.getProps().getProperty("mleKeyAlias").trim())) {
			this.setMleKeyAlias((String)this.getProps().getProperty("mleKeyAlias").trim());
		}else {
			this.setMleKeyAlias(GlobalLabelParameters.DEFAULT_MLE_ALIAS_FOR_CERT);
		}
		if (null != this.getProps().getProperty("mleForRequestPublicCertPath") && !StringUtils.isEmpty(this.getProps().getProperty("mleForRequestPublicCertPath").trim()) ) {
			this.setMleForRequestPublicCertPath(this.getProps().getProperty("mleForRequestPublicCertPath").trim());
		}

		if (this.getProps().containsKey("mapToControlMLEonAPI")) {
			try {
				this.setMapToControlMLEonAPI((Map<String, Boolean>) this.getProps().get("mapToControlMLEonAPI"));
			} catch (ClassCastException e) {
				logger.warn("Unable to initialise MLE control Map from config");
			}
		}
		validateMLEConfiguration();

		this.setRetryDelay(Long.valueOf(this.getProps().getProperty("retryDelay", "0")));
		this.setMerchantDetailsSet(true);
	}

	/**
	 * @return true if authentication type is valid else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	private void checkAuthenticationType() throws ConfigException {
		/* Authentication Type. */
		if (this.getAuthenticationType() == null || this.getAuthenticationType().isEmpty()) {
			logger.info(GlobalLabelParameters.BEGIN_TRANSACTION);
			logger.error(GlobalLabelParameters.AUTH_NULL_EMPTY);
			throw new ConfigException(GlobalLabelParameters.AUTH_NULL_EMPTY);
		}
		if (!(this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)
				|| this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)
				/*
				 * JWE is not supported as an authentication mechanism ||
				 * this.authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWE)
				 */
				|| this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.OAUTH)
				|| this.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.MUTUALAUTH))) {
			logger.info(GlobalLabelParameters.BEGIN_TRANSACTION);
			logger.error(GlobalLabelParameters.AUTH_ERROR);
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
	}

	private void validateMLEConfiguration() throws ConfigException {

		// Fetch raw property values
		String enableRequestMLEForOptionalApisGloballyStr = this.getProps().getProperty("enableRequestMLEForOptionalApisGlobally");
		String useMLEGloballyStr = this.getProps().getProperty("useMLEGlobally");

		// Only validate if both are set (not null and not empty)
		if (StringUtils.isNotBlank(enableRequestMLEForOptionalApisGloballyStr) && StringUtils.isNotBlank(useMLEGloballyStr)) {
			if (Boolean.parseBoolean(enableRequestMLEForOptionalApisGloballyStr) != Boolean.parseBoolean(useMLEGloballyStr)) {
				logger.error("Both 'enableRequestMLEForOptionalApisGlobally' and 'useMLEGlobally' are set but have different values.");
				throw new ConfigException("Conflicting values for 'enableRequestMLEForOptionalApisGlobally' and 'useMLEGlobally'. They must be the same.");
			}
		}

		//verify the input path for mle Cert should be correct else throw error in both case mle=true/false
		if(this.mleForRequestPublicCertPath !=null && !StringUtils.isEmpty(this.mleForRequestPublicCertPath))
		{
			try {
				Utility.validatePathAndFile(this.mleForRequestPublicCertPath, "mleForRequestPublicCertPath");
			} catch (IOException e) {
				throw new ConfigException(e.getMessage());
			}
		}

		boolean mleConfigured = this.enableRequestMLEForOptionalApisGlobally;

		if (this.mapToControlMLEonAPI != null && !this.mapToControlMLEonAPI.isEmpty()) {
			for (boolean value : this.mapToControlMLEonAPI.values()) {
				if (value) {
					mleConfigured = true;
					break;
				}
			}
		}
		//if MLE=true then check for auth Type
		if (mleConfigured && !GlobalLabelParameters.JWT.equalsIgnoreCase(this.authenticationType)) {
			logger.error("Request MLE is only supported in JWT auth type");
			throw new ConfigException("Request MLE is only supported in JWT auth type");
		}

		//check the p12 file and set the keyFile to get the mleCert in case of JWT auth Type
		if(GlobalLabelParameters.JWT.equalsIgnoreCase(this.authenticationType)) {
			checkKeyFile();
		}
	}

	/**
	 * @return true if merchant id or not null or return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantId() throws ConfigException {
		boolean check = true;
		if (this.getMerchantID() == null || this.getMerchantID().isEmpty()) {
			logger.info(GlobalLabelParameters.BEGIN_TRANSACTION);
			logger.error(GlobalLabelParameters.MERCHANT_NULL_EMPTY);
			check = false;
			throw new ConfigException(GlobalLabelParameters.MERCHANT_NULL_EMPTY);
		}
		return check;

	}

	/**
	 * @return true if key file is available else return false.
	 * @throws ConfigException if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyFile() throws ConfigException {
		File newFile;

		this.setKeyFile(null);

		if (this.getKeyFilename() == null || this.getKeyFilename().isEmpty()) {
			logger.error(GlobalLabelParameters.KEY_FILE_EMPTY);
			if (this.getMerchantID() != null) {
				this.setKeyFilename(this.getMerchantID());
			}
		}
		if (this.getKeysDirectory() == null || this.getKeysDirectory().isEmpty()) {
			this.setKeysDirectory(GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH);
			logger.error(GlobalLabelParameters.KEY_DIRECTORY_EMPTY);
		}

		newFile = new File(this.keysDirectory);

		if (!newFile.isDirectory()) {
			logger.error(GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND + " : " + this.keysDirectory);
			return false;
		}
		try {
			newFile = new File(this.getKeysDirectory(),
					this.getKeyFilename().concat(GlobalLabelParameters.P12_EXTENSION));
			if (!newFile.exists()) {
				logger.error(GlobalLabelParameters.KEY_FILE_NOT_FOUND + " : "
						+ this.keysDirectory.concat("/").concat(this.getKeyFilename())
						+ GlobalLabelParameters.P12_EXTENSION);
				return false;
			}

			logger.info(GlobalLabelParameters.KEY_FILE + " : "
					+ this.keysDirectory.concat("/").concat(this.getKeyFilename())
					+ GlobalLabelParameters.P12_EXTENSION);
		} catch (Exception e) {
			return false;
		}
		boolean isRead = newFile.canRead();

		if (isRead) {
			this.setKeyFile(newFile);
			return true;
		} else {
			logger.info(GlobalLabelParameters.FILE_NOT_READ + " : " + this.keysDirectory.concat(this.getKeyFilename()));
			return false;
		}
	}

	/*
	 * Getters & Setters
	 */

	public Properties getProps() {
		return props;
	}

	private void setProps(Properties props) {
		this.props = props;
	}

	public String getMerchantID() {
		return this.merchantID;
	}

	private void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getAuthenticationType() {
		return this.authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getRequestHost() {
		return requestHost;
	}

	private void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public boolean isRetryEnabled() {
		return retryEnabled;
	}

	private void setRetryEnabled(boolean retryEnabled) {
		this.retryEnabled = retryEnabled;
	}

	public long getRetryDelay() {
		return retryDelay;
	}

	private void setRetryDelay(long retryDelay) {
		this.retryDelay = retryDelay;
	}

	public boolean isUseMetaKey() {
		return useMetaKey;
	}

	private void setUseMetaKey(boolean useMetaKey) {
		this.useMetaKey = useMetaKey;
	}

	public String getPortfolioID() {
		return this.portfolioID;
	}

	private void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getKeysDirectory() {
		return this.keysDirectory;
	}

	private void setKeysDirectory(String keysDirectory) {
		this.keysDirectory = keysDirectory;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	private void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyPass() {
		return this.keyPass;
	}

	private void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public String getKeyFilename() {
		return this.keyFilename;
	}

	private void setKeyFilename(String keyFilename) {
		this.keyFilename = keyFilename;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean isUseProxyEnabled() {
		return useProxy;
	}

	private void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public String getProxyAddress() {
		return proxyAddress;
	}

	private void setProxyAddress(String proxyAddress) {
		this.proxyAddress = proxyAddress;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	private void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return this.proxyUser;
	}

	private void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPassword() {
		return this.proxyPassword != null ? this.proxyPassword : "";
	}

	private void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public String getRunEnvironment() {
		return runEnvironment;
	}

	private void setRunEnvironment(String runEnvironment) {
		this.runEnvironment = runEnvironment;
	}

	public String getIntermediateHost() {
		return intermediateHost;
	}

	private void setIntermediateHost(String intermediateHost) {
		this.intermediateHost = intermediateHost;
	}

	public String getDefaultDeveloperId() {
		return defaultDeveloperId;
	}

	private void setDefaultDeveloperId(String defaultDeveloperId) {
		this.defaultDeveloperId = defaultDeveloperId;
	}

	public File getKeyFile() {
		return keyFile;
	}

	private void setKeyFile(File keyFile) {
		this.keyFile = keyFile;
	}

	public boolean isEnableClientCert() {
		return this.enableClientCert;
	}

	private void setEnableClientCert(boolean enableClientCert) {
		this.enableClientCert = enableClientCert;
	}

	public String getClientId() {
		return this.clientId;
	}

	private void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	private void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientCertFile() {
		return this.clientCertFile;
	}

	private void setClientCertFile(String clientCertFile) {
		this.clientCertFile = clientCertFile;
	}

	public String getClientCertDirectory() {
		return this.clientCertDirectory;
	}

	private void setClientCertDirectory(String clientCertDirectory) {
		this.clientCertDirectory = clientCertDirectory;
	}

	public String getClientCertPassword() {
		return this.clientCertPassword;
	}

	private void setClientCertPassword(String clientCertPassword) {
		this.clientCertPassword = clientCertPassword;
	}

	public boolean isMerchantDetailsSet() {
		return this.merchantDetailsSet;
	}

	private void setMerchantDetailsSet(boolean merchantDetailsSet) {
		this.merchantDetailsSet = merchantDetailsSet;
	}

	public String getMerchantKeyId() {
		return merchantKeyId;
	}

	private void setMerchantKeyId(String merchantKeyId) {
		this.merchantKeyId = merchantKeyId;
	}

	public String getMerchantSecretKey() {
		return merchantSecretKey;
	}

	private void setMerchantSecretKey(String merchantsecretKey) {
		this.merchantSecretKey = merchantsecretKey;
	}

	public String getSolutionId() {
		return solutionId;
	}

	private void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public int getUserDefinedConnectionTimeout() {
		return userDefinedConnectionTimeout;
	}

	private void setUserDefinedConnectionTimeout(int timeout) {
		this.userDefinedConnectionTimeout = timeout;
	}

	public int getUserDefinedReadTimeout() {
		return userDefinedReadTimeout;
	}

	private void setUserDefinedReadTimeout(int timeout) {
		this.userDefinedReadTimeout = timeout;
	}

	public int getUserDefinedWriteTimeout() {
		return userDefinedWriteTimeout;
	}

	private void setUserDefinedWriteTimeout(int timeout) {
		this.userDefinedWriteTimeout = timeout;
	}

	public int getUserDefinedKeepAliveDuration() {
		return userDefinedKeepAliveDuration;
	}

	private void setUserDefinedKeepAliveDuration(int duration) {
		this.userDefinedKeepAliveDuration = duration;
	}

	public int getUserDefinedMaxIdleConnections() {
		return userDefinedMaxIdleConnections;
	}

	public void setUserDefinedMaxIdleConnections(int userDefinedMaxIdleConnections) {
		this.userDefinedMaxIdleConnections = userDefinedMaxIdleConnections;
	}

	public Map<String, String> getDefaultHeaders() {
		return defaultHeaders;
	}

	private void setDefaultHeaders(Map<String, String> defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
	}

	public String getPemFileDirectory() {
		return pemFileDirectory;
	}

	public void setPemFileDirectory(String pemFileDirectory) {
		this.pemFileDirectory = pemFileDirectory;
	}

	public boolean isEnableRequestMLEForOptionalApisGlobally() {
		return enableRequestMLEForOptionalApisGlobally;
	}

	private void setEnableRequestMLEForOptionalApisGlobally(boolean enableRequestMLEForOptionalApisGlobally) {
		this.enableRequestMLEForOptionalApisGlobally = enableRequestMLEForOptionalApisGlobally;
	}

	public boolean isDisableRequestMLEForMandatoryApisGlobally() {
		return disableRequestMLEForMandatoryApisGlobally;
	}

	private void setDisableRequestMLEForMandatoryApisGlobally(boolean disableRequestMLEForMandatoryApisGlobally) {
		this.disableRequestMLEForMandatoryApisGlobally = disableRequestMLEForMandatoryApisGlobally;
	}

	public Map<String, Boolean> getMapToControlMLEonAPI() {
		return mapToControlMLEonAPI;
	}

	private void setMapToControlMLEonAPI(Map<String, Boolean> mapToControlMLEonAPI) {
		this.mapToControlMLEonAPI = mapToControlMLEonAPI;
	}

	public String getMleKeyAlias() {
		return mleKeyAlias;
	}

	private void setMleKeyAlias(String mleKeyAlias) {
		this.mleKeyAlias = mleKeyAlias;
	}

	public String getMleForRequestPublicCertPath() {
		return mleForRequestPublicCertPath;
	}

	public void setMleForRequestPublicCertPath(String mleForRequestPublicCertPath) {
		this.mleForRequestPublicCertPath = mleForRequestPublicCertPath;
	}
}
