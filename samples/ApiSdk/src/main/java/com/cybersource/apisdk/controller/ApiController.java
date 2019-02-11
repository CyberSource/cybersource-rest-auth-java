package com.cybersource.apisdk.controller;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.apisdk.service.PaymentRequestService;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will make call as per request type.
 */
public class ApiController {

	private Logger logger;

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentGet(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestContext);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPost(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestContext);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPut(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestContext);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentDelete(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestContext);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	public Response paymentPatch(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		return processPayment(merchantConfig, requestContext);
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains information for merchant.
	 * @param requestContext
	 *            - contains request details
	 * @throws ConfigException
	 *             - contains information for merchant.
	 * @throws ClientException
	 *             - throws custom client exception.
	 */
	private Response processPayment(MerchantConfig merchantConfig, RequestContext requestContext)
			throws ConfigException, ClientException {
		Response responseObj = null;
		try {
			logger = merchantConfig.getLogger();
			Utility.log(logger, GlobalLabelParameters.BEGIN_TRANSACTION, "", Level.INFO);
			if (validateRequestDetails(requestContext.getRequestMethodType(), requestContext.getRequestJsonPath(), logger)) {
				Utility.log(logger, GlobalLabelParameters.USING_REQUEST_TARGET, requestContext.getRequestTarget(),
						Level.INFO);
				Utility.log(logger, GlobalLabelParameters.AUTENTICATION_TYPE, merchantConfig.getAuthenticationType(),
						Level.INFO);
				Utility.log(logger, GlobalLabelParameters.REQUEST_TYPE, " : ".concat(requestContext.getRequestMethodType()),
						Level.INFO);
				Utility.log(logger, GlobalLabelParameters.MERCH_CONF_LABEL, " ".concat(merchantConfig.toString()),
						Level.INFO);
				PaymentRequestService paymentRequest = new PaymentRequestService(merchantConfig, requestContext);
				responseObj = paymentRequest.processPaymentRequest();
				return responseObj;
			} else {
				Utility.log(logger, GlobalLabelParameters.REQUEST_VALIDATION_ERROR, "", Level.INFO);
				throw new ConfigException(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);
			}
		} finally {
			Utility.log(logger, GlobalLabelParameters.END_TRANSACTION, "", Level.INFO);
			Utility.log(logger, " ", "", Level.OFF);
		}
	}
	
	/**
     * @return true if valid method type else return false.
     * @throws ConfigException - if some value is missing or wrong for merchant.
     */
    private boolean isRequestMethodTypeValid(String httpMethod) throws ConfigException {
        if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
                || httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)
                || httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
                || httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)
                || httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
            return true;
        }
        return false;
    }

    /**
     * @param jsonPath -path for request json.
     * @return true if path is valid else return false.
     * @throws ConfigException - if some value is missing or wrong for merchant.
     */
    private boolean validateRequestDetails(String requestMethodType, String jsonPath, Logger logger) throws ConfigException {
		boolean check = true;

		if (!isRequestMethodTypeValid(requestMethodType)) {
			return false;
		}

		if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			return check;
		}

		if (StringUtils.isBlank(jsonPath)) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_JSON_MIS_NULL, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_MIS_NULL);
		}

		if (StringUtils.isNotBlank(jsonPath)
				&& jsonPath.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED)) {
			return check;
		}
		File f = new File(jsonPath);
		if (!f.exists()) {
			Utility.log(logger, GlobalLabelParameters.REQUEST_JSON_ERROR, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_ERROR);

		}
		if (!f.canRead()) {
			Utility.log(logger, GlobalLabelParameters.FILE_NOT_READ, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_READ);
		}
		return check;
    }
}
