package com.cybersource.authsdk.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * 
 *
 *
 */
public final class PropertiesUtil {
	public static String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private static String cybsPath;

	private PropertiesUtil() {

	}

	/**
	 * 
	 * @param fileName
	 *            -name of the request json file.
	 * @return bytes for json file.
	 * @throws IOException-
	 *             if some IO operation failed.
	 */
	public static String getJsonInput(String fileName) throws IOException {
		if (fileName.equalsIgnoreCase(GlobalLabelParameters.POST_OBJECT_METHOD_REQUEST_PATH)) {
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
	 * @throws FileNotFoundException-if
	 *             file is not found.
	 */
	private static InputStream getPropertyFile(String fileName) throws FileNotFoundException {
		return new FileInputStream(fileName);
	}

	/**
	 * 
	 * @return merchant properties.
	 * @throws IOException-if
	 *             some IO operation failed.
	 */
	public static Properties getMerchantProperties() throws IOException {
		cybsPath = "src/main/resources/cybs.properties";
		InputStream merchantInput = getPropertyFile(cybsPath);
		Properties merchantProperties = new Properties();
		merchantProperties.load(merchantInput);
		return merchantProperties;
	}

}
