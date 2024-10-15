package com.cybersource.authsdk.util.mle;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import net.minidev.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.ZonedDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;



public class MLEUtility {
    public static JSONObject encodeRequest(MerchantConfig merchantConfig, String payload) throws MLEException {
        X509Certificate certificate = getCertificate(merchantConfig);
        Map<String, Object> customHeaders = new HashMap<>();
        customHeaders.put("iat", ZonedDateTime.now().toInstant().getEpochSecond());
        String encryptedPayload = encryptAttributeWithAlgo(payload, certificate, customHeaders);
        return createJsonObject(encryptedPayload);
    }

    private static X509Certificate getCertificate(MerchantConfig merchantConfig) throws MLEException {
        try {
            KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            merchantKeyStore.load(Files.newInputStream(Paths.get(merchantConfig.getKeysDirectory(), 
                    merchantConfig.getKeyFilename() + GlobalLabelParameters.P12_EXTENSION)), merchantConfig.getKeyPass().toCharArray());
            Enumeration<String> enumKeyStore = merchantKeyStore.aliases();
            while (enumKeyStore.hasMoreElements()) {
                String merchantKeyAlias = enumKeyStore.nextElement();
                if (merchantKeyAlias.contains("CyberSource_SJC_US")) {
                    Certificate certificate = merchantKeyStore.getCertificate(merchantKeyAlias);
                    return (X509Certificate) certificate;
                }
            }
        } catch (IOException e) {
            throw  new MLEException("Failed to load p12 in keystore from p12 file", e);
        } catch (CertificateException e) {
            throw new MLEException("Failed to load certificates in keystore", e);
        } catch (KeyStoreException e) {
            throw new MLEException("Failed to instantiate keystore", e);
        } catch (Exception e) {
            throw new MLEException("Some error occurred while encoding the request payload", e);
        }
        throw new MLEException("No certificate found for MLE");
    }

    private static String encryptAttributeWithAlgo(String content, X509Certificate x509Certificate, Map<String, Object> customHeaders) throws MLEException {
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
            throw new MLEException("Failed to encode payload", e);
        }
    }

    private static JSONObject createJsonObject(String jweToken) {
        JSONObject json = new JSONObject();
        json.put("encryptedRequest", jweToken);
        return json;
    }
}
