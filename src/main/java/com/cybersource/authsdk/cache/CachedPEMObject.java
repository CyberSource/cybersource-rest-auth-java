package com.cybersource.authsdk.cache;

import org.bouncycastle.util.io.pem.PemObject;


/**
 * POJO class for caching PEMObject, lastModified KeyFile
 */
public class CachedPEMObject {
    private PemObject pemObject;

    private long lastModifiedTimeStamp;

    public CachedPEMObject(PemObject pemObject, long lastModifiedTimeStamp) {
        this.pemObject = pemObject;
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }

    public PemObject getPemObject() {
        return pemObject;
    }

    public void setPemObject(PemObject pemObject) {
        this.pemObject = pemObject;
    }

    public long getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(long lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }
}
