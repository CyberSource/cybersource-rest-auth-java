package com.cybersource.authsdk.util.test;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.mle.MLEException;
import com.cybersource.authsdk.util.mle.MLEUtility;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.security.auth.x500.X500Principal;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MLEUtilityTest {
    @Test
    public void preservesRequestContentTypeAndPayload() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        X509Certificate certificate = mock(X509Certificate.class);
        when(certificate.getPublicKey()).thenReturn(keyPair.getPublic());
        when(certificate.getSerialNumber()).thenReturn(new BigInteger("12345"));
        when(certificate.getSubjectDN()).thenReturn(new X500Principal("CN=MLE,SERIALNUMBER=12345"));
        MerchantConfig config = mock(MerchantConfig.class);
        Cache cache = mock(Cache.class);
        when(cache.getRequestMLECertFromCache(config)).thenReturn(certificate);
        Field cacheField = MLEUtility.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        Object originalCache = cacheField.get(null);
        try {
            cacheField.set(null, cache);
            for (String contentType : new String[] {
                    "application/json", "application/hal+json", "application/json; charset=utf-8"}) {
                JSONObject envelope = (JSONObject) MLEUtility.encryptSerializedRequestPayload(
                    config, Collections.singletonMap("amount", "10"), "{\"amount\":\"10\"}", contentType);
                assertEncryptedPayload(envelope, contentType, keyPair);
            }
                JSONObject envelope = (JSONObject) MLEUtility.encryptSerializedRequestPayload(
                    config, Collections.emptyMap(), "{}", "application/json");
                JWEObject emptyRequest = JWEObject.parse((String) envelope.get("encryptedRequest"));
                Assert.assertEquals("application/json", emptyRequest.getHeader().getContentType());
                emptyRequest.decrypt(new RSADecrypter(keyPair.getPrivate()));
                Assert.assertEquals("{}", emptyRequest.getPayload().toString());
        } finally {
            cacheField.set(null, originalCache);
        }
    }

    @Test(expected = MLEException.class)
    public void rejectsBlankContentType() throws Exception {
        MLEUtility.encryptSerializedRequestPayload(null, Collections.singletonMap("amount", "10"), "{}", " ");
    }

    @Test
    public void encryptsSerializedPayloadVerbatim() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        X509Certificate certificate = mock(X509Certificate.class);
        when(certificate.getPublicKey()).thenReturn(keyPair.getPublic());
        when(certificate.getSerialNumber()).thenReturn(new BigInteger("12345"));
        when(certificate.getSubjectDN()).thenReturn(new X500Principal("CN=MLE,SERIALNUMBER=12345"));
        MerchantConfig config = mock(MerchantConfig.class);
        Cache cache = mock(Cache.class);
        when(cache.getRequestMLECertFromCache(config)).thenReturn(certificate);
        Field cacheField = MLEUtility.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        Object originalCache = cacheField.get(null);
        String serializedPayload = "{ \"amount\" : 10 }";
        try {
            cacheField.set(null, cache);
            JSONObject envelope = (JSONObject) MLEUtility.encryptSerializedRequestPayload(
                    config, Collections.singletonMap("amount", "ignored"), serializedPayload, "application/json");
            JWEObject jwe = JWEObject.parse((String) envelope.get("encryptedRequest"));
            Assert.assertEquals("application/json", jwe.getHeader().getContentType());
            jwe.decrypt(new RSADecrypter(keyPair.getPrivate()));
            Assert.assertEquals(serializedPayload, jwe.getPayload().toString());
        } finally {
            cacheField.set(null, originalCache);
        }
    }

    @Test
    public void leavesAbsentPayloadUnchanged() throws Exception {
        Assert.assertNull(MLEUtility.encryptSerializedRequestPayload(null, null, null, "application/json"));
    }

    private void assertEncryptedPayload(JSONObject envelope, String contentType, KeyPair keyPair) throws Exception {
        JWEObject jwe = JWEObject.parse((String) envelope.get("encryptedRequest"));
        Assert.assertEquals(contentType, jwe.getHeader().getContentType());
        Assert.assertEquals("12345", jwe.getHeader().getKeyID());
        Assert.assertNotNull(jwe.getHeader().getCustomParam("iat"));
        jwe.decrypt(new RSADecrypter(keyPair.getPrivate()));
        Assert.assertEquals("{\"amount\":\"10\"}", jwe.getPayload().toString());
    }
}