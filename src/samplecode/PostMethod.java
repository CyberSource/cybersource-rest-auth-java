package samplecode;

import com.cybersource.api.controller.ApiController;
import com.cybersource.authentication.core.MerchantConfig;
import com.cybersource.authentication.util.GlobalLabelParameters;
import com.cybersource.authentication.util.PropertiesUtil;

import java.util.Properties;
/**
 * Depending on the authentication type HTTP or JWT , this class performs the
 * POST operation on the test url 'apitest.cybersource.com'.
 * 
 */
public class PostMethod {
	private ApiController apiController;
	private String authenticationType;
	private Properties merchantProp;
	private MerchantConfig merchantConfig;
	private String responseCode;
	/**/
	private String requestType = "POST";
	/* Give the url path to where the data needs to be authenticated. */
	private String url = GlobalLabelParameters.HttpUriPrefix;
	/* Give the path of cbys properties file, which needs to be read. */
	private String cybsPath = "Resource/cybs.properties";
	/* request json path */
	private String requestJsonPath = "Resource/request.json";
	/* Request Target. */
	private String requestTarget = "/pts/v2/payments";
	/* This method initiates or begins the process. */
	public static void main(String[] args) throws Exception {
		/**
		 * 
		 */
		new PostMethod();
	}
	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public PostMethod() throws Exception {
		apiController = new ApiController();
		/* Read the cbys.properites file */
		if (cybsPath == null || this.cybsPath.isEmpty()) {
			System.err.println("Cybs Path is Missing/Null for POST Operation.");
			return;
		}
		merchantProp = PropertiesUtil.getMerchantProperties(cybsPath);
		merchantConfig = new MerchantConfig(merchantProp);
		/* Set Request Type. */
		merchantConfig.setRequestType(requestType);
		/* Extract Authentication Type from cybs.properties */
		authenticationType = merchantConfig.getAuthenticationType().trim();
		merchantConfig.setCybsPath(cybsPath);
		/* Set request data */
		merchantConfig.setRequestJsonPath(requestJsonPath);
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
