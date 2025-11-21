package com.cybersource.authsdk.cache;

/**
 * POJO class for caching MLE KID, lastModified file timestamp, and CyberSource P12 validation status
 */
public class CachedMLEKId {

    private String kid;
    private long lastModifiedTimeStamp;
    private boolean isCyberSourceP12;

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

    public boolean isCyberSourceP12() {
        return isCyberSourceP12;
    }

    public void setCyberSourceP12(boolean cyberSourceP12) {
        isCyberSourceP12 = cyberSourceP12;
    }
}
