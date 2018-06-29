package samplecode;



import Data.Configuration;
import Data.PayloadData;
import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.log.Log4j;
import com.cybersource.authentication.util.GlobalLabelParameters;
import org.apache.logging.log4j.Logger;

import java.util.Properties;




/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * POST operation on the test url 'apitest.cybersource.com'.
 * 
 */
public class PostObjectMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String responseCode;
	private Logger logger;
	/**/
	private String requestType = "POST";
	/* Give the url path to where the data needs to be authenticated. */
	private String url = GlobalLabelParameters.HttpUriPrefix;
	private String requestData = PayloadData.readData();
	private String requestTarget = "/pts/v2/payments";

	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		/**
		 * 
		 */
		new PostObjectMethod();
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public PostObjectMethod() throws Exception {
		apiController = new ApiController();
		/* Read Merchant details.*/
		merchantProp = Configuration.setMerchantDetails();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		/* Set request data */
		merchantConfig.setRequestData(requestData);
		/* Set request target */
		merchantConfig.setRequestTarget(requestTarget);
		/* Set URL path w.r.t Post operation. */

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setURL(url);

		/* Begin Post process. */
		process();
	}

	/**
	 * Calls the APISDK for processing the payment request.
	 * 
	 * @throws Exception
	 */
	private void process() throws Exception {
		System.out.println(authenticationType.toUpperCase() + " Request");
		/* Initializing the logging object. */
		logger = Log4j.getInstance(merchantConfig);
		logger.info(GlobalLabelParameters.BEGIN_TRANSCATION);
		/* Call payment method of Api Controller class */
		apiController.paymentPost(merchantConfig);
		/* Display Response Message from the server and Headers. */
		responseCode = merchantConfig.getResponseCode();
		try {
			if (!responseCode.isEmpty() && !merchantConfig.getResponseMessage().isEmpty()
					&& merchantConfig.getResponseMessage() != null && responseCode != null) {
				new PostGenerateHeaders(merchantConfig);
				System.out.println(" URL                 : " + url);
				System.out.println(" Response Code       : " + merchantConfig.getResponseCode());
				System.out.println(" v-c-Co-relation ID  : " + merchantConfig.getV_c_CorelationID());
				System.out.println(" Response Message    : " + merchantConfig.getResponseMessage());
			}
		} catch (Exception e) {
			return;
		}
	}
}
