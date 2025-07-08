package com.cybersource.authsdk.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.util.mle.MLEUtility;

public class Utility {
	private enum validationType {
		HTTP_SIGNATURE, JWT
	};
	
	
	private static final Logger logger = LogManager.getLogger(Utility.class);

	/**
	 * Checks whether the authentication type is rightly entered with no typo
	 * error.
	 * 
	 * @param authenticationType
	 *            : HTTP or JWT
	 * @return -true if valid authentication type else return false.
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
     * @param path     The file path to validate.
     * @param pathType A description of the path type (e.g., "Input file").
     * @throws IOException              If the file does not exist.
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
}
