package com.cybersource.authsdk.util;

import com.cybersource.authsdk.cache.Cache;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;


public class JWEUtility {

    public static String decryptJWEUsingPrivateKey(PrivateKey privateKey, String jweBase64Data) throws ParseException, JOSEException {
        JWEDecrypter jweDecrypter = new RSADecrypter(privateKey);
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(jweBase64Data);
        encryptedJWT.decrypt(jweDecrypter);
        return encryptedJWT.getPayload().toString();
    }

    /**
     * This method has been marked as Deprecated and will be removed in coming releases.
     *
     * @deprecated use {@link #decryptJWEUsingPrivateKey(PrivateKey, String)} instead.
     */
    @Deprecated
    public static String decryptJWEUsingPEM(MerchantConfig merchantConfig, String jweBase64Data) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, ParseException, JOSEException {
        Cache cache = new Cache(merchantConfig);
        PrivateKey privateKey =  cache.getJWECachedPrivateKey();
        JWEDecrypter jweDecrypter = new RSADecrypter(privateKey);
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(jweBase64Data);
        encryptedJWT.decrypt(jweDecrypter);
        return encryptedJWT.getPayload().toString();
    }

    /**
     * This method has been marked as Deprecated.
     * It is expected for the end-user to read the private key
     * from the PEM file / any other source and pass it to the SDK
     * using the method {@link #decryptJWEUsingPrivateKey(PrivateKey, String)}
     *
     * @deprecated
     */
    @Deprecated
    public static PrivateKey readPemFileToGetPrivateKey(String pemFilepath)
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		PemReader pemReader = new PemReader(new FileReader(pemFilepath));
		PemObject pemObject = pemReader.readPemObject();
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
        PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, ASN1Sequence.getInstance(pemObject.getContent()));
        byte[] pkcs8Encoded = privateKeyInfo.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey =  keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
    
}
