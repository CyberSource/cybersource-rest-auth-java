package com.cybersource.authsdk.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/* This method returns value for parameter Signature which is then passed to Signature header
 * paramter 'Signature' is calucated based on below key values and then signed with SECRET KEY -
 * host: Sandbox (apitest.cybersource.com) or Production (api.cybersource.com) hostname
 * date: "HTTP-date" format as defined by RFC7231.
 * (request-target): Should be in format of httpMethod: path
                     Example: "post /pts/v2/payments"
 * Digest: Only needed for POST calls.
 * v-c-merchant-id: set value to Cybersource Merchant ID
                     This ID can be found on EBC portal */
public class SignatureGenerator {

	private MerchantConfig merchantConfigSigParam;
	private String signatureParameterBase64Encoded;
	private String space = GlobalLabelParameters.SPACE;
	private String httpMethod;
	private String merchantSecertKey;
	private String merchantId;

	public SignatureGenerator(MerchantConfig merchantConfig, String merchantSecertKey, String merchantId,
			String httpMethod, String getID) throws InvalidKeyException, NoSuchAlgorithmException {
		this.merchantConfigSigParam = merchantConfig;
		this.httpMethod = httpMethod;
		this.merchantSecertKey = merchantSecertKey;
		this.merchantId = merchantId;
	}
	
	public String signatureGeneration() throws NoSuchAlgorithmException, InvalidKeyException {

		StringBuilder signatureString = new StringBuilder();
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.HOST.toLowerCase());
		signatureString.append(": ");
		signatureString.append(merchantConfigSigParam.getRequestHost());
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.DATE.toLowerCase());
		signatureString.append(": ");
		signatureString.append(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT"))));
		signatureString.append('\n');
		signatureString.append("(request-target)");
		signatureString.append(": ");

		String requestTarget = null;
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.GET);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.POST);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.PUT);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.DELETE);
		}
		signatureString.append(requestTarget);
		signatureString.append('\n');

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			signatureString.append(GlobalLabelParameters.DIGEST.toLowerCase());
			signatureString.append(": ");
			signatureString.append(new PayloadDigest(merchantConfigSigParam).getDigest());
			signatureString.append('\n');
		}

		signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
		signatureString.append(": ");
		signatureString.append(merchantId);
		signatureString.delete(0, 1);
		String signatureStr = signatureString.toString();

		/*
		 * Signature string generated from above parameters is Signed with
		 * SecretKey hased with SHA256 and base64 encoded. Secret Key is Base64
		 * decoded before signing
		 */
		
		SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantSecertKey), "HmacSHA256");
		Mac aKeyId = Mac.getInstance("HmacSHA256");
		aKeyId.init(secretKey);
		aKeyId.update(signatureStr.getBytes());
		byte[] aHeaders = aKeyId.doFinal();
		signatureParameterBase64Encoded = Base64.getEncoder().encodeToString(aHeaders);
		secretKey = null;
		
		
		return signatureParameterBase64Encoded;
		
	}

	/**
	 * 
	 * @param requestType
	 * @return
	 */
	public String getRequestTarget(String requestType) {
		String requestTarget;
		switch (requestType) {
		case GlobalLabelParameters.POST:
			requestTarget = GlobalLabelParameters.POST.toLowerCase() + space
					+ merchantConfigSigParam.getRequestTarget();
			break;
		case GlobalLabelParameters.GET:
			requestTarget = GlobalLabelParameters.GET.toLowerCase() + space + merchantConfigSigParam.getRequestTarget();
			break;
		case GlobalLabelParameters.PUT:
			requestTarget = GlobalLabelParameters.PUT.toLowerCase() + space + merchantConfigSigParam.getRequestTarget();
			break;
		case GlobalLabelParameters.DELETE:
			requestTarget = GlobalLabelParameters.DELETE.toLowerCase() + space
					+ merchantConfigSigParam.getRequestTarget();
			break;
		default:
			requestTarget = null;
			break;
		}
		return requestTarget;
	}
}
