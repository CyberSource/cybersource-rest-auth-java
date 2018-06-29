package samplecode;

import Data.PayloadData;
import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;

import java.util.Properties;
/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * PUT operation on the test url 'apitest.cybersource.com'.
 */
public class PutMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	/* Request Type is always PUT*/
	private String requestType = "PUT";
	/* Give the url path to where the data needs to be authenticated.*/
	private String url = GlobalLabelParameters.HttpUriPrefix;
	/* Give the path of cbys properties file, which needs to be read.*/
	private String cybsPath = "Resource/cybs.properties";
	/* Request Target.*/
	private String requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
	/* Request json path */
	private String requestJsonPath = "Resource/TRRReport.json";
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
		/* Read the cbys.properites file. */
		if (cybsPath == null || this.cybsPath.isEmpty()) {
			System.err.println("Cybs Path is Missing/Null");
			return;
		}
		merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		/* Set Json Path, request target, request data into merchant config object.*/
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestData);
		/* Construct the URL with respect to PUTID. */
		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
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
		apiController.paymentPut(merchantConfig);
		/* Display Response Message from the server. */
		new PutGenerateHeaders(merchantConfig);
		System.out.println(" URL                : " + url);
		System.out.println(" Response Code      : " + merchantConfig.getResponseCode());
		System.out.println(" V-C-Corealation ID : " + merchantConfig.getV_c_CorelationID());
		System.out.println(" Response Message   : " + merchantConfig.getResponseMessage());
	}}
