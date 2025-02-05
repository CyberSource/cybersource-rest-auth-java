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

}
