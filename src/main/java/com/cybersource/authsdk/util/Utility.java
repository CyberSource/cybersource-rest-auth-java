package com.cybersource.authsdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import com.cybersource.authsdk.util.mle.MLEException;

public class Utility {
	private enum validationType {
		HTTP_SIGNATURE, JWT
	};
	
	
	private static final Logger logger = LogManager.getLogger(Utility.class);

	/**
	 * Checks whether the authentication type is rightly entered with no typo error.
	 * 
	 * @param authenticationType
	 *            HTTP or JWT
	 * @return true if valid authentication type else return false.
	 */
	public static boolean checkAuthenticationValidation(String authenticationType) {
		boolean status = false;

		if (authenticationType.equalsIgnoreCase(validationType.HTTP_SIGNATURE.toString())
				|| authenticationType.equalsIgnoreCase(validationType.JWT.toString())) {
			status = true;
		}
		return status;
	}

	/**
	 * 
	 * @param requestTarget
	 *            : request target from where we will retrieve Id
	 * @return id from request target.
	 */
	public static String retrieveGetIDFromRequestTarget(String requestTarget) {
		StringTokenizer str = new StringTokenizer(requestTarget, "/");
		String temp = null;
		while (str.hasMoreTokens()) {
			temp = str.nextToken();
			if (temp.matches("^[0-9]+$")) {
				return temp;
			}
		}
		return temp;
	}
	
	/**
     * Helper method to validate if a string represents a valid boolean value.
     * 
     * @param value the string to validate
     * @return true if the string is "true" or "false" (case-insensitive), false otherwise
     */
    public static boolean isValidBooleanString(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

	public static String extractSerialNumber(X509Certificate x509Certificate) {
		String serialNumber = null;
		String serialNumberPrefix = "SERIALNUMBER=";
		String principal = x509Certificate.getSubjectDN().getName().toUpperCase();
		int beg = principal.indexOf(serialNumberPrefix);
		if (beg >= 0) {
			int end = principal.indexOf(",", beg);
			if (end == -1) {
				end = principal.length();
			}
			serialNumber = principal.substring(beg + serialNumberPrefix.length(), end);
		} else {
			serialNumber = x509Certificate.getSerialNumber().toString();
		}
		return serialNumber;
	}
	
	/**
     * Validates that the given file path exists and is not empty.
     *
     * @param filePath The file path to validate.
     * @param pathType A description of the path type (e.g., "Input file").
     * @throws IOException If the file does not exist.
     * @throws IllegalArgumentException If the path is null or empty.
     */
    public static void validatePathAndFile(String filePath, String pathType) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error(pathType + " path cannot be null or empty");
            throw new IllegalArgumentException(pathType + " path cannot be null or empty");
        }

        // Normalize Windows-style paths that start with a slash before the drive letter
        String normalizedPath = filePath;
        if (File.separatorChar == '\\' && normalizedPath.matches("^/[A-Za-z]:.*")) {
            normalizedPath = normalizedPath.substring(1);
        }

        Path path = Paths.get(normalizedPath);
        if (!Files.exists(path)) {
            logger.error(pathType + " does not exist: " + path);
            throw new IOException(pathType + " does not exist: " + path);
        }
        if (!Files.isRegularFile(path)) {
        	logger.error(pathType + " does not have valid file: " + path);
            throw new IOException(pathType + " does not have valid file: " + path);
        }
        if (!Files.isReadable(path)) {
            logger.error(pathType + " is not readable: " + path);
            throw new IOException(pathType + " is not readable: " + path);
        }
    }
    
    /**
     * Loads X509 certificates from a PEM file.
     *
     * @param certFilePath The file path to the PEM certificate file.
     * @return The loaded X509Certificate(s) as a Collection.
     * @throws CertificateException If the certificate cannot be parsed or is invalid.
     * @throws IOException If the file cannot be read or does not exist.
     */
    public static Collection<X509Certificate> loadCertificatesFromPemFile(String certFilePath) throws CertificateException, IOException {
        try (FileInputStream inStream = new FileInputStream(certFilePath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<? extends java.security.cert.Certificate> certs = cf.generateCertificates(inStream);
            // Cast to X509Certificate
            List<X509Certificate> x509Certs = new ArrayList<>();
            for (java.security.cert.Certificate cert : certs) {
                x509Certs.add((X509Certificate) cert);
            }
            return x509Certs;
        }
    }
    
    /**
	 * Searches for a certificate that matches the given key alias from a collection of certificates.
	 * The search looks for the alias in the certificate's subject DN (Distinguished Name),
	 * specifically in the CN (Common Name) field.
	 * 
	 * @param certs Collection of X509 certificates to search through
	 * @param keyAlias The key alias to search for
	 * @return The matching X509Certificate, or null if no match is found
	 */
	public static X509Certificate searchCertificateByAlias(Collection<X509Certificate> certs, String keyAlias) {
		if (certs == null || keyAlias == null || keyAlias.isEmpty()) {
			return null;
		}
		
		for (X509Certificate cert : certs) {
			try {
				String subjectDN = cert.getSubjectDN().getName();
				
				// Parse the subject DN to find CN field
				String[] dnParts = subjectDN.split(",");
				for (String part : dnParts) {
					part = part.trim();
					if (part.toUpperCase().startsWith("CN=")) {
						String commonName = part.substring(3); // Remove "CN="
						if (keyAlias.equalsIgnoreCase(commonName)) {
							return cert;
						}
					}
				}
			} catch (Exception e) {
				logger.warn("Error parsing certificate subject DN: " + e.getMessage());
				// Continue with next certificate
			}		
		}
		
		return null;
	}
	
	/**
     * Validate the certificate expiry and log a warning if expired or expiring soon.
     *
     * @param certificate The X509Certificate to check.
     * @param keyAlias The alias of the key in the keystore.
     * @param mleCacheKeyIdentifier Identifier for the MLE cache key.
     * @throws MLEException if certificate validation fails.
     */
    public static void validateCertificateExpiry(X509Certificate certificate, String keyAlias, String mleCacheKeyIdentifier) throws MLEException {
        //Default Warn Message
		String warnMessageForNotHaveExpiryDate = "Certificate don't have expiry date.";
		String warnMessageForBeingExpiredCert = "Certificate with alias {} is going to expired on {} . Please update cert before that.";
		String warnMessageForExpiredCert = "Certificate with alias {} is expired as of {} . Please update cert before that.";
		
		//override log error message based on mle identifier
		if(GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_CONFIG_CERT.equals(mleCacheKeyIdentifier)) {
			warnMessageForNotHaveExpiryDate = "Certificate for Request MLE don't have expiry date from mleForRequestPublicCertPath in merchant config.";
			warnMessageForBeingExpiredCert = "Certificate for Request MLE with alias {} is going to expired on {} . Please update cert file in mleForRequestPublicCertPath in merchant config before that.";
			warnMessageForExpiredCert = "Certificate for Request MLE with alias {} is expired as of {} . Please update cert file in mleForRequestPublicCertPath in merchant config.";
		}
		if(GlobalLabelParameters.MLE_CACHE_KEY_IDENTIFIER_FOR_P12_CERT.equals(mleCacheKeyIdentifier)) {
			warnMessageForNotHaveExpiryDate = "Certificate for Request MLE don't have expiry date in p12 file.";
			warnMessageForBeingExpiredCert = "Certificate for Request MLE with alias {} is going to expired on {} . Please update p12 file before that.";
			warnMessageForExpiredCert = "Certificate for Request MLE with alias {} is expired as of {} . Please update p12 file.";
		}
		
		if (certificate.getNotAfter() == null) {
            //certificate do not have expire date
        	logger.warn(warnMessageForNotHaveExpiryDate);
        } else if (certificate.getNotAfter().before(new Date())) {
        	//certificate is already expired
        	logger.warn(warnMessageForExpiredCert, keyAlias, certificate.getNotAfter());
            //throw new MLEException("Certificate required for MLE has been expired on : " + certificate.getNotAfter());
        } else {
            long timeToExpire = certificate.getNotAfter().getTime() - new Date().getTime();
            if (timeToExpire < GlobalLabelParameters.CERTIFICATE_EXPIRY_DATE_WARNING_DAYS * 24 * 60 * 60) {
            	logger.warn(warnMessageForBeingExpiredCert,keyAlias,certificate.getNotAfter());
            }
        }
    }
    
    public static PrivateKey readPrivateKeyFromP12(String p12FilePath, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException 
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        
        try (FileInputStream fis = new FileInputStream(p12FilePath)) {
            keyStore.load(fis, password);
        }
        
        Enumeration<String> aliases = keyStore.aliases();
        
        if (!aliases.hasMoreElements()) {
            throw new RuntimeException("Keystore is empty - no aliases found");
        }
        
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            // First check if this alias contains a key entry (not just a certificate)
            if (keyStore.isKeyEntry(alias)) {
            	try {
                     PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
                     
                     // Additional null check - getKey() returns null if no private key found
                     if (privateKey != null) {
                         return privateKey;
                     }
                } catch (UnrecoverableKeyException e) {
                     // This could happen with wrong password or corrupted key
                     // System.err.println("Could not recover key for alias: " + alias + " - " + e.getMessage());
                     continue; // Try next alias
                }
            }
        }
        
        throw new RuntimeException("No private key found in the P12 file");
    }
    
    public static PrivateKey loadPrivateKeyFromPemFile(String keyFilePath, char[] password) throws Exception {
	    Security.addProvider(new BouncyCastleProvider());
	    try (FileReader keyReader = new FileReader(keyFilePath);
	        PEMParser pemParser = new PEMParser(keyReader)) {
	        Object object = pemParser.readObject();
	        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

	        if (object instanceof PEMKeyPair) {
	            // Unencrypted PKCS#1 key
	            return converter.getKeyPair((PEMKeyPair) object).getPrivate();
	        } else if (object instanceof PEMEncryptedKeyPair) {
	            // Encrypted PKCS#1 key
	            if (password == null) {
	                throw new IllegalArgumentException("Private key is password protected, but no password was provided.");
	            }
	            JcePEMDecryptorProviderBuilder decProvBuilder = new JcePEMDecryptorProviderBuilder();
	            PEMEncryptedKeyPair encKeyPair = (PEMEncryptedKeyPair) object;
	            PEMKeyPair decryptedKeyPair = encKeyPair.decryptKeyPair(decProvBuilder.build(password));
	            return converter.getKeyPair(decryptedKeyPair).getPrivate();
	        } else if (object instanceof PrivateKeyInfo) {
	            // Unencrypted PKCS#8 key
	            return converter.getPrivateKey((PrivateKeyInfo) object);
	        } else if (object instanceof PKCS8EncryptedPrivateKeyInfo) {
	            // Encrypted PKCS#8 key
	            if (password == null) {
	                throw new IllegalArgumentException("Private key is password protected, but no password was provided.");
	            }
	            try {
	                PKCS8EncryptedPrivateKeyInfo encPrivKeyInfo = (PKCS8EncryptedPrivateKeyInfo) object;
	                JceOpenSSLPKCS8DecryptorProviderBuilder decryptorBuilder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
	                PrivateKeyInfo pkInfo = encPrivKeyInfo.decryptPrivateKeyInfo(decryptorBuilder.build(password));
	                return converter.getPrivateKey(pkInfo);
	            } catch (PKCSException e) {
	                logger.error("Failed to decrypt PKCS#8 private key - incorrect password provided");
	                throw new IllegalArgumentException("Password is incorrect for the encrypted private key. Error:" + e.getMessage(), e);
	            }
	        } else {
	            throw new IllegalArgumentException("Unsupported PEM object: " + object.getClass());
	        }
	    }
	}
    
}
