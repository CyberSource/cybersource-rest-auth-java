package com.cybersource.authsdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
	
	

	private PropertiesUtil() {

	}

	/**
	 * 
	 * @param fileName
	 *            -name of the request json file.
	 * @return bytes for json file.
	 * @throws IOException
	 *             - if some IO operation failed.
	 */
	public static String getJsonInput(String fileName) throws IOException {
		if (fileName.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED)) {
			return null;
		}
		InputStream jsonIn = getPropertyFile(fileName);
		byte[] jsonBytes = new byte[jsonIn.available()];
		jsonIn.read(jsonBytes);
		return new String(jsonBytes);
	}

	/**
	 * 
	 * @param fileName
	 *            -name of the file
	 * @return stream for the file.
	 * @throws FileNotFoundException
	 *             - if file is not found.
	 */
	private static InputStream getPropertyFile(String fileName) throws FileNotFoundException {
		return new FileInputStream(fileName);
	}

	/**
	 * 
	 * @return merchant properties.
	 * @throws IOException
	 *             - if some IO operation failed.
	 */
	public static Properties getMerchantProperties() throws IOException {
		ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
		File file = new File(classLoader.getResource("cybs.properties").getFile());
		InputStream merchantInput = getPropertyFile(file.getAbsolutePath());
		Properties merchantProperties = new Properties();
		merchantProperties.load(merchantInput);
		return merchantProperties;
	}
}
