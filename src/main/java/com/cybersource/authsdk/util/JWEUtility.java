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
     * @param merchantConfig the merchant configuration containing JWE decryption details
     * @param jweBase64Data the base64 encoded JWE data to decrypt
     * @return the decrypted payload as a string
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws IOException if an I/O error occurs
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws ParseException if the JWE cannot be parsed
     * @throws JOSEException if a JOSE processing error occurs
     * @deprecated use {@link #decryptJWEUsingPrivateKey(PrivateKey, String)} instead.
     */
    @Deprecated
    public static String decryptJWEUsingPEM(MerchantConfig merchantConfig, String jweBase64Data) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, ParseException, JOSEException {
        Cache cache = new Cache();
        PrivateKey privateKey =  cache.getJWECachedPrivateKey(merchantConfig);
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
     * @param pemFilepath the path to the PEM file containing the private key
     * @return the private key read from the PEM file
     * @throws FileNotFoundException if the PEM file is not found
     * @throws IOException if an I/O error occurs while reading the file
     * @throws NoSuchAlgorithmException if the RSA algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
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
