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
	
    private static String cybsPath;
	private PropertiesUtil(){
		
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getJsonInput(String fileName) throws IOException {
		if(fileName.equalsIgnoreCase(GlobalLabelParameters.POST_OBJECT_METHOD_REQUEST_PATH)){
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
	 * @return
	 * @throws FileNotFoundException
	 */
	private static  InputStream getPropertyFile(String fileName) throws FileNotFoundException {
				return new FileInputStream(fileName);
	}


	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Properties getMerchantProperties() throws IOException {
		/* Give the path of cbys properties file which needs to be read. */
		if (runningFromIntelliJ()) {
			cybsPath = "./resources/cybs.properties";
		} else {
			cybsPath = "src/main/resources/cybs.properties";
		}
		InputStream merchantInput = getPropertyFile(cybsPath);
		Properties merchantProperties = new Properties();
		merchantProperties.load(merchantInput);
		return merchantProperties;
	}

	public static boolean runningFromIntelliJ() {
		String classPath = System.getProperty("java.class.path");
		return classPath.contains("idea_rt.jar");
	}

	
}
