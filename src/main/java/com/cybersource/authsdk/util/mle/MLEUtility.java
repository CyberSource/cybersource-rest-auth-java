package com.cybersource.authsdk.util.mle;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class MLEUtility {
    private static final Logger logger = LogManager.getLogger(MLEUtility.class);

    public static boolean checkIsMLEForAPI(MerchantConfig merchantConfig, boolean isMLESupportedByCybsForApi, String operationId) {

        //isMLE for an api is false by default.
        //It takes into account if the end-user wants to use MLE for an MLE-enabled API
        boolean isMLEForAPI = false;

        //isMLEEnabledForApi checks if the API has MLE enabled.
        boolean isMLEEnabledForApi = isMLESupportedByCybsForApi && merchantConfig.isMLE();

        //Control the MLE only from map
        if (merchantConfig.getMapToControlMLEonAPI() != null &&
                merchantConfig.getMapToControlMLEonAPI().containsKey(operationId)) {
            if (merchantConfig.getMapToControlMLEonAPI().get(operationId)) {
                isMLEForAPI = isMLEEnabledForApi;
            }
            if (!merchantConfig.getMapToControlMLEonAPI().get(operationId)) {
                isMLEForAPI = false;
            }
        }

        return isMLEForAPI;
    }

    public static JSONObject encodeRequest(MerchantConfig merchantConfig, String payload) throws MLEException {
        if (payload == null || payload.equals("{}")) {
            return new JSONObject();
        }
        X509Certificate certificate = getCertificate(merchantConfig);
        Map<String, Object> customHeaders = new HashMap<>();
        customHeaders.put("iat", ZonedDateTime.now().toInstant().getEpochSecond());
        String encryptedPayload = encryptAttributeWithAlgo(payload, certificate, customHeaders);
        return createJsonObject(encryptedPayload);
    }

    private static X509Certificate getCertificate(MerchantConfig merchantConfig) throws MLEException {
        try {
            KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
//            merchantKeyStore.load(Files.newInputStream(Paths.get(merchantConfig.getKeysDirectory(),
//                    merchantConfig.getKeyFilename() + GlobalLabelParameters.P12_EXTENSION)), merchantConfig.getKeyPass().toCharArray());

            Cache cache = new Cache(merchantConfig);
            cache.setP12FileDetailsInCache();
            cache.retrieveP12DataFromCache();
            X509Certificate mleCert = cache.getMleCert();
            byte[] certBytes = mleCert.getEncoded();
            InputStream certStream = new ByteArrayInputStream(certBytes);

            merchantKeyStore.load(certStream, merchantConfig.getKeyPass().toCharArray());

            Enumeration<String> enumKeyStore = merchantKeyStore.aliases();
            while (enumKeyStore.hasMoreElements()) {
                String merchantKeyAlias = enumKeyStore.nextElement();
                if (merchantKeyAlias.contains(merchantConfig.getMleKeyAlias())) {
                    Certificate certificate = merchantKeyStore.getCertificate(merchantKeyAlias);
                    if (certificate instanceof X509Certificate) {
                        validateCertificateExpiry((X509Certificate) certificate);
                        return (X509Certificate) certificate;
                    }
                }
            }
        } catch (IOException e) {
            throw new MLEException("Certificate with alias " + merchantConfig.getMleKeyAlias() + " not found in " + merchantConfig.getKeyFilename() + ".p12", e);
        } catch (CertificateException e) {
            throw new MLEException("Failed to load certificates in keystore", e);
        } catch (KeyStoreException e) {
            throw new MLEException("Failed to instantiate keystore", e);
        } catch (Exception e) {
            throw new MLEException("Error occurred while loading certificate for MLE: " + e.getMessage(), e);
        }
        throw new MLEException("No certificate found for MLE");
    }

    private static String encryptAttributeWithAlgo(String content, X509Certificate x509Certificate, Map<String, Object> customHeaders) throws MLEException {
        String serialNumber = Utility.extractSerialNumber(x509Certificate);
        if (serialNumber == null) {
            throw new MLEException("No serial number found in certificate for MLE");
        }
        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .keyID(Utility.extractSerialNumber(x509Certificate))
                        .customParams(customHeaders)
                        .build(),
                new Payload(content));
        try {
            jweObject.encrypt(new RSAEncrypter((RSAPublicKey) x509Certificate.getPublicKey()));
            return jweObject.serialize();
        } catch (JOSEException e) {
            throw new MLEException("Failed to encrypt payload", e);
        }
    }

    private static JSONObject createJsonObject(String jweToken) {
        JSONObject json = new JSONObject();
        json.put("encryptedRequest", jweToken);
        return json;
    }

    private static void validateCertificateExpiry(X509Certificate certificate) throws MLEException {
        if (certificate.getNotAfter() == null) {
            throw new MLEException("Certificate is not valid");
        } else if (certificate.getNotAfter().before(new Date())) {
            throw new MLEException("Certificate required for MLE has been expired on : " + certificate.getNotAfter());
        } else {
            long timeToExpire = certificate.getNotAfter().getTime() - new Date().getTime();
            if (timeToExpire < GlobalLabelParameters.CERTIFICATE_EXPIRY_DATE_WARNING_DAYS * 24 * 60 * 60) {
                logger.warn("Certificate for MLE is expiring on : {}", certificate.getNotAfter());
            }
        }
    }
}
