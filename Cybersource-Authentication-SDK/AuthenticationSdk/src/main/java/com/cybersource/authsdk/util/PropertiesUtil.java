package com.cybersource.authsdk.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 *
 *
 */
public final class PropertiesUtil {
	

	private PropertiesUtil(){
		
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getJSONInput(String fileName) throws IOException {
		InputStream jsonIn = getPropertyFile(fileName);
		byte[] jsonBytes = new byte[jsonIn.available()];
		jsonIn.read(jsonBytes);
		return new String(jsonBytes);
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	private static  InputStream getPropertyFile(String fileName) throws FileNotFoundException {
				return new FileInputStream(fileName);
	}


	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Properties getMerchantProperties( ) throws IOException {
		/* Give the path of cbys properties file which needs to be read. */
		String cybsPath = "src/main/resources/cybs.properties";
		InputStream merchantInput = getPropertyFile(cybsPath);
		Properties merchantProperties = new Properties();
		merchantProperties.load(merchantInput);
		return merchantProperties;
	}

	
}
