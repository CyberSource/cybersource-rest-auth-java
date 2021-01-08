package com.cybersource.authsdk.oauth;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

public class OAuthToken implements TokenGenerator {
    private MerchantConfig merchantConfig;
    private String refreshToken;
    private String accessToken;
    private Logger logger;
    private Cache cache;

    /**
     * @param merchantConfig
     *            - list of parameters required for JWT token.
     */
    public OAuthToken(MerchantConfig merchantConfig) {
        this.merchantConfig = merchantConfig;
    }

    @Override
    public String getToken() {
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            accessToken = null;
        }
        return accessToken;
    }

    /**
     *
     * @return the passed Access Token.
     * @throws Exception -
     *             if some exception will occur.
     */
    public String getAccessToken() throws Exception {

        try {
            if (merchantConfig != null ) {
                /* Caching Access and Refresh Token. */

                /* Retrieving Access Token. */

                accessToken = merchantConfig.getAccessToken();
                refreshToken = merchantConfig.getRefreshToken();

            } else {
                throw new ConfigException("merchant config is null");
            }
        } catch (Exception e) {
            Utility.log(logger, GlobalLabelParameters.RETRIEVE_ACCESS_TOKEN_FAILED, "", Level.FATAL);
            Utility.log(logger, e);
            accessToken = null;
        }
        return accessToken;
    }

    /**
     * @throws ConfigException
     *             - if some value is missing or wrong for merchant.
     */
    private void setUpCache() throws ConfigException {
        cache.setAccessToken(accessToken);
        cache.setRefreshToken(refreshToken);
    }

}
