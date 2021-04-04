package com.cybersource.apisdk.connection;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public interface Connection {

	/**
	 * Depending on the URL, connection is opened to the API server.
	 */
	void openConnection();

	/**
	 * After successfully operation, connection is nullified.
	 */
	void endConnection();

	/**
	 * @param merchantConfig
	 */
	default void setProxyConnection(MerchantConfig merchantConfig) {
		Logger logger = LogManager.getLogger(this.getClass());
		
		String proxyAddress = merchantConfig.getProxyAddress();
		String proxyPort = String.valueOf(merchantConfig.getProxyPort());

		try {
			if (StringUtils.isBlank(proxyAddress) || StringUtils.isBlank(proxyPort)) {
				return;
			} else {
				if (StringUtils.isNotBlank(proxyAddress) && StringUtils.isNotBlank(proxyPort)) {
					logger.info(GlobalLabelParameters.PROXY_HOST + " : " + proxyAddress);
					logger.info(GlobalLabelParameters.PROXY_PORT + " : " + proxyPort);
					Properties systemProperties = System.getProperties();
					systemProperties.setProperty(GlobalLabelParameters.PROXY_HOST, proxyAddress);
					systemProperties.setProperty(GlobalLabelParameters.PROXY_PORT, proxyPort);
				}
			}
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.PROXY_HOST_PORT_ERR);
			logger.error(e);
		}
	}
}
