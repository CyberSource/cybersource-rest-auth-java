package com.cybersource.authsdk.cache;

import com.cybersource.authsdk.util.GlobalLabelParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.interfaces.RSAPublicKey;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

public class CacheForPublicKeys {
    private static Logger logger = LogManager.getLogger(CacheForPublicKeys.class);

    public static ConcurrentHashMap<String, RSAPublicKey> publicKeyCache = new ConcurrentHashMap<>();

    public CacheForPublicKeys() {}

    public void addPublicKeyToCache(String runEnvironment, String keyId, RSAPublicKey publicKey) {
        String cacheKey = GlobalLabelParameters.PUBLIC_KEY_CACHE_IDENTIFIER + "_" + runEnvironment + "_" + keyId;
        publicKeyCache.put(cacheKey, publicKey);
    }

    public RSAPublicKey getPublicKeyFromCache(String runEnvironment, String keyId) {
        String cacheKey = GlobalLabelParameters.PUBLIC_KEY_CACHE_IDENTIFIER + "_" + runEnvironment + "_" + keyId;

        if (publicKeyCache.isEmpty() || !publicKeyCache.containsKey(cacheKey)) {
            throw new NullPointerException("Public key not found in cache for [" + runEnvironment + ", " + keyId + "]");
        }

        return publicKeyCache.get(cacheKey);
    }
}
