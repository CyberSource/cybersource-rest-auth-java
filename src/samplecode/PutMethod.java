package samplecode;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;

import Data.Configuration;
import Data.PayloadData;


/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the test url 'apitest.cybersource.com'.
 */
public class PutMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private Logger logger;
	/**/
	private String requestType = "PUT";
	/* Enter the unique Get ID from command line argument. */
	private String putID = "5265502011846829204101";
	/* Give the url path to where the data needs to be authenticated. */
	private String url = GlobalLabelParameters.HttpUriPrefix;

	/* Give the path of cbys properties file, which needs to be read. */
	private String cybsPath = "Resource/cybs.properties";
	private String requestTarget = "/pts/v2/payments";

	/*request json path*/
	private String requestJsonPath    = "Resources/request.json";
	private String requestData = PayloadData.readData();

	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		/**
		 * 
		 */
		new PutMethod(args);
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public PutMethod(String[] args) throws Exception {
		apiController = new ApiController();
		/* Read the cbys.properites file */
		if (cybsPath == null || cybsPath.isEmpty()) {
			merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
			merchantConfig = new MerchantConfig(merchantProp);
		} else {
			/* properties based merchant config. */
			merchantProp = Configuration.setMerchantDetails();
			merchantConfig = new MerchantConfig(merchantProp);
		}
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();

		try {

			putID = args[0];
		} catch (Exception e) {
			System.err.println("No value present in the argument, taking defuault value");
		}
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestData);
		/* Construct the URL with respect to PUTID. */
		url = url + merchantConfig.getURL() + merchantConfig.getRequestTarget() + "/" + putID;
		/* Set the URL. */
		merchantConfig.setURL(url);
		/* Begin PUT process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws Exception
	 */
	private void process() throws Exception {
		System.out.println(authenticationType.toUpperCase() + " Request");
		/* Call payment method of Api Controller class */
		apiController.payment(merchantConfig, requestType, putID, logger);
		/* Display Response Message from the server. */

		new PutGenerateHeaders(merchantConfig);
		System.out.println(" URL                : " + url);
		System.out.println(" Response Code      : " + merchantConfig.getResponseCode());
		System.out.println(" V-C-Corealation ID : " + merchantConfig.getV_c_CorelationID());
		System.out.println(" Response Message   : " + merchantConfig.getResponseMessage());
	}
}
