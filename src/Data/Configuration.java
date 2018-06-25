package Data;

import java.util.Properties;
/**
 * This class set's the default values(editable) for merchant details that are mandatory for the application to run.
 * 
 * #### Please do not the changes values on the left hand side of props.setProperty("left hand side", "editable"). ####
 */
public class Configuration {
	
	public static Properties setMerchantDetails() {

		Properties props = new Properties();

		/* HTTP_Signature and JWT Common Parameters. */
		props.setProperty("authenticationType", "jwt");
		props.setProperty("merchantID", "testrest");
		props.setProperty("runEnvironment", "CyberSource.Environment.SANDBOX");
		props.setProperty("requestJsonPath", "Resource/request.json");
		/* JWT Parameters. */
		props.setProperty("keyAlias", "testrest");
		props.setProperty("keyPassword", "testrest");
		/*
		 * P12 key path. Enter the folder path where the .p12 file is located.
		 */
		props.setProperty("keysDirectory", "Resource");
		/* HTTP Parameters. */
		props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
		props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");
		/* Logging to be enabled or not. */
		props.setProperty("enableLog", "true");
		props.setProperty("timeout", "1000");
		/* Log directory Path. */
		props.setProperty("logDirectory", "log");
		props.setProperty("logFilename", "cybs.log");
		/* Log file size in KB as '5k', in MB as '5M'. */
		props.setProperty("logMaximumSize", "5M");

		return props;
	}

}
