package com.cybersource.apisdk.connection;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * 
 * 
 *
 */
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
	 * 
	 */
	default void setProxyConnection(MerchantConfig merchantConfig) {
		Logger logger = Log4j.getInstance(merchantConfig);
		String proxyAddress = merchantConfig.getProxyAddress(),
				proxyPort = String.valueOf(merchantConfig.getProxyPort());

		try {
			if (StringUtils.isBlank(proxyAddress)|| proxyAddress.isEmpty() || StringUtils.isBlank(proxyPort) || proxyPort.isEmpty()) {
				return;
			} else {
				if (StringUtils.isNotBlank(proxyAddress) && !proxyAddress.isEmpty() && StringUtils.isNotBlank(proxyPort) && !proxyPort.isEmpty()) {
					Utility.log(logger, GlobalLabelParameters.PROXY_HOST + " : " + proxyAddress, Level.INFO);
					Utility.log(logger, GlobalLabelParameters.PROXY_PORT + " : " + proxyPort, Level.INFO);
					Properties systemProperties = System.getProperties();
					systemProperties.setProperty(GlobalLabelParameters.PROXY_HOST, proxyAddress);
					systemProperties.setProperty(GlobalLabelParameters.PROXY_PORT, proxyPort);
				}
			}
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.PROXY_HOST_PORT_ERR, Level.FATAL);
			Utility.log(logger, e);
		}
	
		
	}
}
