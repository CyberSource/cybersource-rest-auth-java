package com.cybersource.authsdk.cache;

/**
 * POJO class for caching MLE KID and lastModified file timestamp
 */
public class CachedMLEKId {

    private String kid;
    private long lastModifiedTimeStamp;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public long getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(long lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }
}

