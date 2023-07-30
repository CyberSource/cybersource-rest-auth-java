package com.cybersource.authsdk.util;

import com.cybersource.authsdk.core.MerchantConfig;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;


public class JWEUtility {

    private static PemObject loadPEMObjectFromFile(String path) throws IOException {
        String pemString = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.ISO_8859_1);
        PemReader pemReader = new PemReader(new StringReader(pemString));
        return pemReader.readPemObject();
    }

    public static String decryptJWEUsingPEM(MerchantConfig merchantConfig, String jweBase64Data) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, ParseException, JOSEException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey;
        PemObject pemObject = loadPEMObjectFromFile(merchantConfig.getPemFileDirectory());
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
        PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, ASN1Sequence.getInstance(pemObject.getContent()));
        byte[] pkcs8Encoded = privateKeyInfo.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Encoded);
        privateKey =  keyFactory.generatePrivate(keySpec);
        JWEDecrypter jweDecrypter = new RSADecrypter(privateKey);
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(jweBase64Data);
        encryptedJWT.decrypt(jweDecrypter);
        return encryptedJWT.getPayload().toString();
    }
}
