package samplecode;

import java.util.Properties;
import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.PropertiesUtil;
import Data.PayloadData;
/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the given working domain.
 */
public class PutMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	/**
	 * REQUEST-TARGET
	 * [Editable]
	 */
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
	/**
	 * JSON-PATH
	 * [Editable]
	 */
	private String requestJsonPath = "Resource/TRRReport.json";
	/**
	 *  REQUEST-TYPE. 
	 *  [Non-Editable]
	 * */
	private String requestType = "PUT";
	private String url;
	/* Request Data.*/
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
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		/* Set Json Path, request target, request data into merchant config object.*/
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestData);
		/* Construct the URL with respect to PUTID. */
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		/* Set the URL.*/
		merchantConfig.setURL(url);
		/* Begin PUT process.*/
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
		apiController.paymentPut(merchantConfig);
		/* Display Response Message from the server. */
		new PutGenerateHeaders(merchantConfig);
		System.out.println(" URL                : " + url);
		System.out.println(" Response Code      : " + merchantConfig.getResponseCode());
		System.out.println(" V-C-Corealation ID : " + merchantConfig.getV_c_CorelationID());
		System.out.println(" Response Message   : " + merchantConfig.getResponseMessage());
	}}
