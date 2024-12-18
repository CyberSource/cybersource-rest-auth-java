package com.cybersource.authsdk.oauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class OAuthToken implements TokenGenerator {
    private static Logger logger = LogManager.getLogger(OAuthToken.class);
    
	private MerchantConfig merchantConfig;
	private String refreshToken;
	private String accessToken;

	/**
	 * @param merchantConfig - list of parameters required for JWT token.
	 */
	public OAuthToken(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
	}

	@Override
	public String getToken() {
		try {
			this.accessToken = getAccessToken();
		} catch (Exception e) {
			logger.error("Error retrieving access token\n{}", e);
			this.accessToken = null;
		}
		return this.accessToken;
	}

	@Override
	public String getToken(String requestData) {
		return " ";
	}

	/**
	 *
	 * @return the passed Access Token.
	 * @throws Exception - if some exception will occur.
	 */
	public String getAccessToken() throws Exception {

		try {
			if (this.merchantConfig != null) {
				this.accessToken = this.merchantConfig.getAccessToken();
				this.refreshToken = this.merchantConfig.getRefreshToken();

			} else {
				throw new ConfigException("merchant config is null");
			}
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.RETRIEVE_ACCESS_TOKEN_FAILED);
			logger.error("Exception : {}", e);
			this.accessToken = null;
		}
		return this.accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
