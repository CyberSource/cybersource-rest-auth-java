package com.cybersource.authsdk.cache;

import java.security.PrivateKey;

import org.bouncycastle.util.io.pem.PemObject;


/**
 * POJO class for caching PEM Private Key, lastModified KeyFile for jwe decryption
 */
public class CachedJWEPrivateKey {
    
    private PrivateKey privateKey;

	private long lastModifiedTimeStamp;
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

    public long getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(long lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }
}
