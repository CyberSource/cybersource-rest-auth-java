package com.cybersource.authsdk.core.test;

import java.util.Properties;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.test.resources.UtilityHelpers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.core.TokenGeneratorFactory;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationTest {

    @InjectMocks
    private Authorization authorization;

    @Mock
    private MerchantConfig merchantConfig;

    @Mock
    private TokenGeneratorFactory tokenGeneratorFactory;

    @Mock
    private TokenGenerator tokenGenerator;


    private Properties props = new Properties();

//    @Before
//    public void setUp() throws ConfigException {
//        props.setProperty("authenticationType", "jwt");
//        props.setProperty("merchantID", "testrest");
//        props.setProperty("runEnvironment", "apitest.cybersource.com");
//        props.setProperty("keyAlias", "testrest");
//        props.setProperty("keyPass", "testrest");
//        props.setProperty("keyFileName", "testrest");
//        props.setProperty("keysDirectory", "src/main/resources");
//
//
//        merchantConfig = new MerchantConfig(props);
//    }

    private String requestJsonData = "{\r\n"
            + "  \"clientReferenceInformation\": {\r\n"
            + "    \"code\": \"TC50171_3\"\r\n"
            + "  },\r\n"
            + "  \"processingInformation\": {\r\n"
            + "    \"commerceIndicator\": \"internet\"\r\n"
            + "  },\r\n"
            + "  \"aggregatorInformation\": {\r\n"
            + "    \"subMerchant\": {\r\n"
            + "      \"cardAcceptorID\": \"1234567890\",\r\n"
            + "      \"country\": \"US\",\r\n"
            + "      \"phoneNumber\": \"650-432-0000\",\r\n"
            + "      \"address1\": \"900 Metro Center\",\r\n"
            + "      \"postalCode\": \"94404-2775\",\r\n"
            + "      \"locality\": \"Foster City\",\r\n"
            + "      \"name\": \"Visa Inc\",\r\n"
            + "      \"administrativeArea\": \"CA\",\r\n"
            + "      \"region\": \"PEN\",\r\n"
            + "      \"email\": \"test@cybs.com\"\r\n"
            + "    },\r\n"
            + "    \"name\": \"V-Internatio\",\r\n"
            + "    \"aggregatorID\": \"123456789\"\r\n"
            + "  },\r\n"
            + "  \"orderInformation\": {\r\n"
            + "    \"billTo\": {\r\n"
            + "      \"country\": \"US\",\r\n"
            + "      \"lastName\": \"VDP\",\r\n"
            + "      \"address2\": \"Address 2\",\r\n"
            + "      \"address1\": \"201 S. Division St.\",\r\n"
            + "      \"postalCode\": \"48104-2201\",\r\n"
            + "      \"locality\": \"Ann Arbor\",\r\n"
            + "      \"administrativeArea\": \"MI\",\r\n"
            + "      \"firstName\": \"RTS\",\r\n"
            + "      \"phoneNumber\": \"999999999\",\r\n"
            + "      \"district\": \"MI\",\r\n"
            + "      \"buildingNumber\": \"123\",\r\n"
            + "      \"company\": \"Visa\",\r\n"
            + "      \"email\": \"test@cybs.com\"\r\n"
            + "    },\r\n"
            + "    \"amountDetails\": {\r\n"
            + "      \"totalAmount\": \"102.21\",\r\n"
            + "      \"currency\": \"USD\"\r\n"
            + "    }\r\n"
            + "  },\r\n"
            + "  \"paymentInformation\": {\r\n"
            + "    \"card\": {\r\n"
            + "      \"expirationYear\": \"2031\",\r\n"
            + "      \"number\": \"5555555555554444\",\r\n"
            + "      \"securityCode\": \"123\",\r\n"
            + "      \"expirationMonth\": \"12\",\r\n"
            + "      \"type\": \"002\"\r\n"
            + "    }\r\n"
            + "  }\r\n"
            + "}";

    @Test
    public void getTokenTestForJwt() throws Exception {
        Properties props = new Properties();

        // HTTP_Signature = http_signature and JWT = jwt
        props.setProperty("authenticationType", "jwt");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("requestJsonPath", "src/main/resources/request.json");

        // MetaKey Parameters
        props.setProperty("portfolioID", "");
        props.setProperty("useMetaKey", "false");

        // JWT Parameters
        props.setProperty("keyAlias", "testrest");
        props.setProperty("keyPass", "testrest");
        props.setProperty("keyFileName", "testrest");

        // P12 key path. Enter the folder path where the .p12 file is located.

        props.setProperty("keysDirectory", "src/main/resources");


        merchantConfig = new MerchantConfig(props);
        merchantConfig.validateMerchantDetails("POST");

        authorization = new Authorization();
        String token = authorization.getToken(merchantConfig, "POST", requestJsonData, "/v2/payments", "2023-10-10T10:10:10Z", false);

        Assert.assertNotNull(token);
    }

    @Test(expected = ConfigException.class)
    public void getTokenTestForFail() throws Exception {
        Properties props = new Properties();

        // HTTP_Signature = http_signature and JWT = jwt
        props.setProperty("authenticationType", "jwt");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("requestJsonPath", "src/main/resources/request.json");

        // MetaKey Parameters
        props.setProperty("portfolioID", "");
        props.setProperty("useMetaKey", "false");

        // JWT Parameters
        props.setProperty("keyAlias", "testrest");
        props.setProperty("keyPass", "testrest");
        props.setProperty("keyFileName", "testrest");

        // P12 key path. Enter the folder path where the .p12 file is located.

        props.setProperty("keysDirectory", "src/main/resources");


        merchantConfig = new MerchantConfig(props);
        merchantConfig.validateMerchantDetails("POST");

        authorization = new Authorization();
        // "abc" is not a valid HTTP verb — should throw ConfigException
        authorization.getToken(merchantConfig, "abc", requestJsonData, "/v2/payments", "2023-10-10T10:10:10Z",false);
    }

    @Test
    public void getTokenTestForHttp() throws Exception {
        Properties props = new Properties();
        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig merchantConfig = new MerchantConfig(props);
        Authorization authorization = new Authorization();
        String token = authorization.getToken(merchantConfig, "GET", requestJsonData, "/v2/payments", "2023-10-10T10:10:10Z",false);

        Assert.assertNotNull(token);
    }

    // ===== JWT SHARED_SECRET End-to-End Tests =====

    @Test
    public void getTokenTestForJwtSharedSecretPost() throws Exception {
        Properties props = new Properties();
        props.setProperty("authenticationType", "jwt");
        props.setProperty("jwtKeyType", "SHARED_SECRET");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig merchantConfig = new MerchantConfig(props);
        merchantConfig.validateMerchantDetails("POST");

        Authorization authorization = new Authorization();
        String token = authorization.getToken(merchantConfig, "POST", requestJsonData, "/v2/payments", "2023-10-10T10:10:10Z", false);

        Assert.assertNotNull(token);
    }

    @Test
    public void getTokenTestForJwtSharedSecretGet() throws Exception {
        Properties props = new Properties();
        props.setProperty("authenticationType", "jwt");
        props.setProperty("jwtKeyType", "SHARED_SECRET");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        MerchantConfig merchantConfig = new MerchantConfig(props);
        merchantConfig.validateMerchantDetails("GET");

        Authorization authorization = new Authorization();
        String token = authorization.getToken(merchantConfig, "GET", "", "/v2/payments", "2023-10-10T10:10:10Z", false);

        Assert.assertNotNull(token);
    }

}