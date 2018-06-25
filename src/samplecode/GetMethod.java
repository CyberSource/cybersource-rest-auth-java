package samplecode;

import java.util.Properties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.ConfigException;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;
import com.cybersource.authentication.util.Utility;



public class GetMethod {
	/* HTTP 5246387105766473203529 JWT 5155671723556493003525 */
	private ApiController apiController;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String responseCode, responseMessage;
	private Logger logger;

	/**/
	private String requestType = "GET";
	/* Enter the unique Get ID from command line argument. */
	private String getID = "5265502011846829204101";
	/* Give the url path to where the data needs to be authenticated. */
	private String url = GlobalLabelParameters.HttpUriPrefix;

	/* Give the path of cbys properties file which needs to be read. */
	private String cybsPath = "Resource/cybs.properties";
	/**/
	private String requestTarget = "/pts/v2/payments";
	private String authenticationType;

	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		new GetMethod(args);
	}

	/**
	 * 
	 * @param args
	 *            : Command line argument which contains GetID ( HTTP & JWT )
	 * @return
	 * @throws Exception
	 */
	public GetMethod(String[] args) throws Exception {

		/* Read the cbys.properites file. */
		if (cybsPath == null || this.cybsPath.isEmpty()) {
			System.err.println("Cybs Path is Missing/Null for GET Operation.");
			return;
		}
			merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
			merchantConfig = new MerchantConfig(merchantProp);
			merchantConfig.setRequestType(requestType);
		
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		/* Value read from the command line argument. */
		try {
			/* For testing is the arguments (GETID) entered is null or not.*/
			int i = args[0].length();
			getID = args[0];
		} catch (Exception e) {
			Utility.logException(logger,
					"No value present in the argument for GETID, proceeding with default value : " + getID, Level.INFO);
			System.err.println("No value present in the argument for GETID, proceeding with default value : " + getID);
			return;
		}
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL with respect to GETID. */
		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget() + "/" + getID;
		/* Set the URL. */
		merchantConfig.setURL(url);
		/* Begin the Get process. */
		process();
	}

	/**
	 * 
	 * @throws ConfigException
	 * @throws Exception
	 */
	private void process() throws ConfigException {
		System.out.println(authenticationType.toUpperCase() + " Request");
		apiController = new ApiController();
		/* Calling APISDK, com.cybersouce.api.controller. */
		apiController.payment(merchantConfig, requestType, getID, logger);
		/* Display response message and Headers in console. */
		responseCode = merchantConfig.getResponseCode();
		responseMessage = merchantConfig.getResponseMessage();
		try {
			if (!responseCode.isEmpty() && !responseMessage.isEmpty() && responseMessage != null
					&& responseCode != null) {
				new GenerateGetHeaders(getID);
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + responseCode);
				System.out.println(" V-C-Corealation ID  : " + merchantConfig.getV_c_CorelationID());
				System.out.println(" Response Message    : " + responseMessage);
			}
		} catch (Exception e) {
			return;
		}
	}
}
