package com.cybersource.authsdk.test.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UtilityHelpers {
	private static String cybsPath;

	/**
	 * @param fileName - name of the file
	 * @return stream for the file.
	 * @throws FileNotFoundException if file is not found.
	 */
	private static InputStream getPropertyFile(String fileName) throws FileNotFoundException {
		return new FileInputStream(fileName);
	}

	/**
	 * 
	 * @return merchant properties.
	 * @throws IOException if some I/O operation failed.
	 */
	public static Properties getMerchantProperties() throws IOException {
		cybsPath = "src/main/resources/cybs.properties";
		InputStream merchantInput = getPropertyFile(cybsPath);
		Properties merchantProperties = new Properties();
		merchantProperties.load(merchantInput);
		return merchantProperties;
	}
}
