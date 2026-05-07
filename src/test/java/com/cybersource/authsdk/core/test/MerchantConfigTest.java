package com.cybersource.authsdk.core.test;

import static org.junit.Assert.*;

import java.util.Properties;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MerchantConfigTest {


    @Mock
    MerchantConfig merchantConfig;

    private Properties props = new Properties();

    @Before
    public void setUp() throws ConfigException {

        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("requestJsonPath", "resources/request.json");

        props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        merchantConfig = new MerchantConfig(props);
    }


    @Test
    public void testValidateMerchantDetails() throws ConfigException {
        boolean result = merchantConfig.validateMerchantDetails("GET");
        assertTrue(result);
    }

    @Test
    public void testCheckRequestType() throws ConfigException {
        boolean result = merchantConfig.checkRequestType("GET");
        assertTrue(result);
    }

    @Test
    public void testCheckKeyPassword() throws ConfigException {
        boolean result = merchantConfig.checkKeyPassword();
        assertTrue(result);
    }

    @Test
    public void testCheckRunEnvironment() throws ConfigException {
        boolean result = merchantConfig.checkRunEvironment();
        assertTrue(result);
    }

    @Test
    public void testCheckMerchantKeyId() throws ConfigException {
        boolean result = merchantConfig.checkMerchantKeyId();
        assertTrue(result);
    }

    @Test
    public void testCheckMerchantSecretKey() throws ConfigException {
        boolean result = merchantConfig.checkMerchantSecretKey();
        assertTrue(result);
    }

    @Test
    public void testCheckKeyAlias() throws ConfigException {
        boolean result = merchantConfig.checkKeyAlias();
        assertTrue(result);
    }

    // ===== JWT Symmetric Key (SHARED_SECRET) Tests =====

    @Test
    public void testJwtDefaultKeyTypeIsP12() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("keyAlias", "testrest");
        jwtProps.setProperty("keyPass", "testrest");
        jwtProps.setProperty("keyFileName", "testrest");
        jwtProps.setProperty("keysDirectory", "src/main/resources");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        assertEquals("P12", jwtConfig.getJwtKeyType());
        assertFalse(jwtConfig.isSharedSecretKeyType());
    }

    @Test
    public void testJwtKeyTypeP12Explicit() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "P12");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("keyAlias", "testrest");
        jwtProps.setProperty("keyPass", "testrest");
        jwtProps.setProperty("keyFileName", "testrest");
        jwtProps.setProperty("keysDirectory", "src/main/resources");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        assertEquals("P12", jwtConfig.getJwtKeyType());
        assertFalse(jwtConfig.isSharedSecretKeyType());
    }

    @Test
    public void testJwtKeyTypeSharedSecret() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        assertEquals("SHARED_SECRET", jwtConfig.getJwtKeyType());
        assertTrue(jwtConfig.isSharedSecretKeyType());
        assertEquals("08c94330-f618-42a3-b09d-e1e43be5efda", jwtConfig.getMerchantKeyId());
        assertEquals("yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=", jwtConfig.getMerchantSecretKey());
    }

    @Test
    public void testJwtKeyTypeSharedSecretCaseInsensitive() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "shared_secret");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        assertTrue(jwtConfig.isSharedSecretKeyType());
    }

    @Test(expected = ConfigException.class)
    public void testJwtKeyTypeInvalid() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "INVALID");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");

        new MerchantConfig(jwtProps);
    }

    @Test(expected = ConfigException.class)
    public void testJwtSharedSecretMissingMerchantKeyId() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        new MerchantConfig(jwtProps);
    }

    @Test(expected = ConfigException.class)
    public void testJwtSharedSecretMissingSecretKey() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");

        new MerchantConfig(jwtProps);
    }

    @Test
    public void testJwtSharedSecretIgnoresP12Fields() throws ConfigException {
        // SHARED_SECRET should NOT require keysDirectory, keyAlias, keyPass, keyFileName
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        // Should succeed without any P12 related fields
        assertNotNull(jwtConfig);
    }

    @Test
    public void testValidateMerchantDetailsJwtSharedSecretGet() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        boolean result = jwtConfig.validateMerchantDetails("GET");
        assertTrue(result);
    }

    @Test
    public void testValidateMerchantDetailsJwtSharedSecretPost() throws ConfigException {
        Properties jwtProps = new Properties();
        jwtProps.setProperty("authenticationType", "jwt");
        jwtProps.setProperty("jwtKeyType", "SHARED_SECRET");
        jwtProps.setProperty("merchantID", "testrest");
        jwtProps.setProperty("runEnvironment", "apitest.cybersource.com");
        jwtProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        jwtProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig jwtConfig = new MerchantConfig(jwtProps);
        boolean result = jwtConfig.validateMerchantDetails("POST");
        assertTrue(result);
    }

}
