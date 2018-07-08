package samplecode;

import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.PropertiesUtil;

import java.util.Properties;
/**
 * 
 * 
 *
 */
public class GetMethod {
	private ApiController apiController;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String responseCode, responseMessage,authenticationType;
	/**
	 * REQUEST TARGET /  UNIQUE GET ID
	 * [Editable]
	 */
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest&download=true&reportDate=20180523";
	/**
	 *  REQUEST-TYPE. 
	 *  [Non-Editable]
	 * */
	private String requestType = "GET";                                                                                                                                    
	private String url;
	/* This method initiates or begins the process. */
	public static void main(String args[]) throws Exception {
		new GetMethod();
	}
	/**
	 * 
	 * @throws Exception : Throws runtime exceptions.
	 */
	public GetMethod() throws Exception {
		
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		/* Set Request Type into the merchant config object.*/
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		merchantConfig.setRequestTarget(requestTarget);
		/* Construct the URL.*/
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL. */
		merchantConfig.setURL(url);
		/* Begin the Get process. */
		process();
	}
	/**
	 * 
	 */
	private void process() {
		System.out.println(authenticationType.toUpperCase() + " Request");
		apiController = new ApiController();
		/* Calling APISDK, com.cybersouce.api.controller. */
		apiController.paymentGet(merchantConfig);
		/* Display response message and Headers in console. */
		responseCode = merchantConfig.getResponseCode();
		responseMessage = merchantConfig.getResponseMessage();
		try {
			if (!responseCode.isEmpty() && !responseMessage.isEmpty() && responseMessage != null
					&& responseCode != null) {
				new GenerateGetHeaders(requestTarget);
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
