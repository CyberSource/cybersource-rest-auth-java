package com.cybersource.authsdk.util.jwt;

import com.cybersource.authsdk.util.jwt.exceptions.InvalidJwkException;
import com.cybersource.authsdk.util.jwt.exceptions.InvalidJwtException;
import com.cybersource.authsdk.util.jwt.exceptions.JwtSignatureValidationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class JWTUtility {
    public static SignedJWT parse(String jwtValue) throws InvalidJwtException {
        try {
            return SignedJWT.parse(jwtValue);
        } catch (ParseException e) {
            throw new InvalidJwtException("Malformed JWT cannot be parsed", e);
        }
    }

    public static boolean verifyJwt(SignedJWT signedJWT, RSAPublicKey publicKey)
            throws JwtSignatureValidationException {

        if (publicKey == null) {
            throw new IllegalArgumentException("No public key found");
        }

        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        try {
            return signedJWT.verify(verifier);
        } catch (JOSEException e) {
            throw new JwtSignatureValidationException("Cannot verify JWT validity", e);
        }
    }

    public static RSAPublicKey getRSAPublicKeyFromJwk(String jwkJsonString) throws ParseException, JOSEException, InvalidJwkException {
        JWK jwk = JWK.parse(jwkJsonString);
        if (jwk instanceof RSAKey) {
            RSAKey rsaKey = (RSAKey) jwk;
            return rsaKey.toRSAPublicKey();
        } else {
            throw new InvalidJwkException("JWK Algorithm mismatch. Expected algorithm : RSA");
        }
    }
}
